package dev.hytalemodding.ui;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import dev.hytalemodding.NationBase.NationBase;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import javax.annotation.Nonnull;

public class PlayerHud extends CustomUIHud  {
    public PlayerHud(@Nonnull PlayerRef playerRef) { super(playerRef); }

    public NationBase currentBase = null;

    @Override
    protected void build(@NonNullDecl UICommandBuilder uiCommandBuilder) {
        uiCommandBuilder.append("PlayerHud/PlayerHud.ui");
    }

    public void unsetBase() {
        currentBase = null;

        UICommandBuilder uiCommandBuilder = new UICommandBuilder();
        uiCommandBuilder.set("#BaseLabel.TextSpans", Message.raw(""));
        this.update(false, uiCommandBuilder);
    }

    public void setBase(NationBase base) {
        currentBase = base;

        UICommandBuilder uiCommandBuilder = new UICommandBuilder();
        uiCommandBuilder.set("#BaseLabel.TextSpans", Message.raw(String.format("Current Base: %s", base.name)));
        this.update(false, uiCommandBuilder);
    }
}