package dev.hytalemodding;

import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import dev.hytalemodding.commands.ExampleCommand;
import dev.hytalemodding.commands.OpenNationBaseGuiCommand;
import dev.hytalemodding.events.ExampleEvent;
import dev.hytalemodding.events.PlayerMoveTickingSystem;
import dev.hytalemodding.events.PlayerRaidSystem;

import javax.annotation.Nonnull;

public class ExamplePlugin extends JavaPlugin {

    public ExamplePlugin(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        this.getCommandRegistry().registerCommand(new ExampleCommand("example", "An example command"));
        this.getCommandRegistry().registerCommand(new OpenNationBaseGuiCommand());

        this.getEventRegistry().registerGlobal(PlayerReadyEvent.class, ExampleEvent::onPlayerReady);

        getEntityStoreRegistry().registerSystem(new PlayerRaidSystem());

        getEntityStoreRegistry().registerSystem(new PlayerMoveTickingSystem());
    }
}