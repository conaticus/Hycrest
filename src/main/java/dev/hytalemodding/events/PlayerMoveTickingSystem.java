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
import dev.hytalemodding.NationBase.NationBase;
import dev.hytalemodding.NationBase.NationBaseManager;
import dev.hytalemodding.util.ChunkVector;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

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
        if (playerRef == null)
            return;

        Vector3i currentPosition = playerRef.getTransform().getPosition().toVector3i();

        playerLastPositions.compute(playerRef.getUuid(), (uuid, lastPosition) -> {
            if (currentPosition.equals(lastPosition))
                return lastPosition;

            this.onPlayerMoveBlock(playerRef, lastPosition, currentPosition);

            return currentPosition;
        });
    }

    private void onPlayerMoveBlock(PlayerRef playerRef, Vector3i lastPosition, Vector3i position) {
        var base = NationBaseManager.getInstance().getBaseAt(ChunkVector.fromPosition(position));
        if (base == null)
            return;

        System.out.println(base.name);
    }

    @NullableDecl
    @Override
    public Query<EntityStore> getQuery() {
        return PlayerRef.getComponentType();
    }
}
