package dev.hytalemodding.events;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.hud.HudManager;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.hytalemodding.ui.PlayerHud;
import dev.hytalemodding.ui.PlayerHudManager;

import java.util.concurrent.CompletableFuture;

public class ExampleEvent {
    public static void onPlayerReady(PlayerReadyEvent event) {
        Player player = event.getPlayer();
        player.sendMessage(Message.raw("Welcome " + player.getDisplayName()));

        Ref<EntityStore> ref = player.getReference();
        if (ref == null)
            return;

        PlayerRef playerRef = ref.getStore().getComponent(ref, PlayerRef.getComponentType());
        if (playerRef == null)
            return;

        CompletableFuture.runAsync(() -> {
            HudManager hudManager = player.getHudManager();

            if (hudManager.getCustomHud() != null)
                return;


            var playerHud = new PlayerHud(playerRef);
            hudManager.setCustomHud(playerRef, playerHud);

            PlayerHudManager.getInstance().addPlayerHud(playerRef.getUuid(), playerHud);
        });
    }
}