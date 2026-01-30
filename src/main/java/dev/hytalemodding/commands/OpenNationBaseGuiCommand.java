package dev.hytalemodding.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.pages.CustomUIPage;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.hytalemodding.ui.NationBaseGUI;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.concurrent.CompletableFuture;

public class OpenNationBaseGuiCommand extends AbstractPlayerCommand {
    public OpenNationBaseGuiCommand() {
        super("base", "Select a region to start building a base in.");
    }

    @Override
    protected void execute(@NonNullDecl CommandContext commandContext, @NonNullDecl Store<EntityStore> store, @NonNullDecl Ref<EntityStore> ref, @NonNullDecl PlayerRef playerRef, @NonNullDecl World world) {
        Player player = commandContext.senderAs(Player.class);

        var position = store.getComponent(playerRef.getReference(), TransformComponent.getComponentType());
        var chunkX = ChunkUtil.chunkCoordinate(position.getPosition().getX());
        var chunkZ = ChunkUtil.chunkCoordinate(position.getPosition().getZ());

        CompletableFuture.runAsync(() -> {
            CustomUIPage page = player.getPageManager().getCustomPage();;
            if (page != null)
                return;

            page = new NationBaseGUI(playerRef, chunkX, chunkZ, CustomPageLifetime.CanDismiss);
            player.getPageManager().openCustomPage(ref, store, page);
        });
    }
}
