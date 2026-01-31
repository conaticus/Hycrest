package dev.hytalemodding.events;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.EventTitleUtil;
import dev.hytalemodding.NationBase.NationBase;
import dev.hytalemodding.NationBase.NationBaseManager;
import dev.hytalemodding.ui.PlayerHud;
import dev.hytalemodding.ui.PlayerHudManager;
import dev.hytalemodding.util.ChunkVector;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.awt.*;
import java.util.HashMap;
import java.util.UUID;

public class PlayerMoveTickingSystem extends EntityTickingSystem<EntityStore> {
    public HashMap<UUID, Vector3i> playerLastPositions = new HashMap<>();

    @Override
    public void tick(float v, int i, @NonNullDecl ArchetypeChunk<EntityStore> archetypeChunk, @NonNullDecl Store<EntityStore> store, @NonNullDecl CommandBuffer<EntityStore> commandBuffer) {
        Ref<EntityStore> ref = archetypeChunk.getReferenceTo(i);
        if (!ref.isValid())
            return;

        PlayerRef playerRef = store.getComponent(ref, PlayerRef.getComponentType());
        if (playerRef == null || !playerRef.isValid())
            return;

        Vector3i currentPosition = playerRef.getTransform().getPosition().toVector3i();

        playerLastPositions.compute(playerRef.getUuid(), (uuid, lastPosition) -> {
            if (currentPosition.equals(lastPosition))
                return lastPosition;

            if (lastPosition != null)
                this.onPlayerMoveBlock(playerRef, lastPosition, currentPosition);

            return currentPosition;
        });
    }

    private void onPlayerMoveBlock(PlayerRef playerRef, Vector3i lastPosition, Vector3i position) {
        NationBaseManager baseManager = NationBaseManager.getInstance();
        NationBase base = baseManager.getBaseAt(ChunkVector.fromPosition(position));

        PlayerHud playerHud = PlayerHudManager.getInstance().getPlayerHud(playerRef);
        if (playerHud == null)
            return;

        if (base == null && playerHud.currentBase != null) {
            playerHud.unsetBase();
            return;
        }

        if (base == null || base.equals(playerHud.currentBase))
            return;

        playerHud.setBase(base);
        EventTitleUtil.showEventTitleToPlayer(playerRef, Message.raw(base.name), Message.raw("You are entering a nation base, proceed with caution").color(Color.orange), true, null, 5, 1, 1);
    }

    @NullableDecl
    @Override
    public Query<EntityStore> getQuery() {
        return PlayerRef.getComponentType();
    }
}
