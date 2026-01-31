package dev.hytalemodding.ui;

import com.hypixel.hytale.server.core.universe.PlayerRef;

import java.util.HashMap;
import java.util.UUID;

public class PlayerHudManager {
    private static PlayerHudManager INSTANCE = new PlayerHudManager();

    final private HashMap<UUID, PlayerHud> playerHuds = new HashMap<>();

    public static PlayerHudManager getInstance() {
        return INSTANCE;
    }

    public void addPlayerHud(UUID playerId, PlayerHud hud) {
        playerHuds.put(playerId, hud);
    }

    public PlayerHud getPlayerHud(PlayerRef playerRef) {
        return playerHuds.get(playerRef.getUuid());
    }
}
