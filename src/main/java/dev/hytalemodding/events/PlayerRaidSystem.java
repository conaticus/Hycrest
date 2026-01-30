package dev.hytalemodding.events;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.ecs.DamageBlockEvent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import javax.annotation.Nonnull;

public class PlayerRaidSystem extends EntityEventSystem<EntityStore, DamageBlockEvent> {
    public PlayerRaidSystem() {
        super(DamageBlockEvent.class);
    }

    @Override
    public void handle(int i, @Nonnull ArchetypeChunk<EntityStore> archetypeChunk, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer, @Nonnull DamageBlockEvent damageEvent) {
        Ref<EntityStore> ref = archetypeChunk.getReferenceTo(i);
        Player player = store.getComponent(ref, Player.getComponentType());

        player.sendMessage(Message.raw("you cannot beak"));
        damageEvent.setDamage(1 / 100f); // requires 100 hits
//        damageEvent.setCancelled(true);
    }

    @NullableDecl
    @Override
    public Query<EntityStore> getQuery() {
        return Query.and(
            Player.getComponentType()
        );
    }
}
