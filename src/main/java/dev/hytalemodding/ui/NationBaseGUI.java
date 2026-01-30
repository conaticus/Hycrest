package dev.hytalemodding.ui;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.server.core.asset.util.ColorParseUtil;
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.hytalemodding.NationBase.NationBase;
import dev.hytalemodding.NationBase.NationBaseManager;
import dev.hytalemodding.util.ChunkVector;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.concurrent.CompletableFuture;

public class NationBaseGUI extends InteractiveCustomUIPage<NationBaseGUI.Data> {
    private CompletableFuture<MapAsset> mapAsset = null;

    final private int chunkX;
    final private int chunkZ;

    private NationBase nationBase;

    public NationBaseGUI(@Nonnull PlayerRef playerRef, int chunkX, int chunkZ, @Nonnull CustomPageLifetime lifetime) {
        super(playerRef, lifetime, Data.CODEC);

        this.chunkX = chunkX;
        this.chunkZ = chunkZ;

        this.nationBase = new NationBase(playerRef.getUuid(), new ChunkVector(chunkX, chunkZ));
    }

    private CompletableFuture<MapAsset> generateMapAsset(UICommandBuilder uiCommandBuilder) {
        CompletableFuture<MapAsset> mapAsset = MapAsset.generate(playerRef, chunkX - 8, chunkZ - 8, chunkX + 8, chunkZ + 8);

        if (mapAsset == null)
            return null;

        mapAsset.thenAccept(asset -> {
            if (asset == null) return;

            MapAsset.sendToPlayer(this.playerRef.getPacketHandler(), asset);

            uiCommandBuilder.set("#ChunkCards.Background", "UI/Custom/HycrestAssets/Map.png");
            this.sendUpdate();
        });

        return mapAsset;
    }

    @Override
    public void build(@NonNullDecl Ref<EntityStore> ref, @NonNullDecl UICommandBuilder uiCommandBuilder, @NonNullDecl UIEventBuilder uiEventBuilder, @NonNullDecl Store<EntityStore> store) {
        uiCommandBuilder.append("NationBaseGUI/NationBaseGUI.ui");

        if (this.mapAsset == null) {
            this.mapAsset = this.generateMapAsset(uiCommandBuilder);

            if (this.mapAsset == null)
                return;
        }

        for (int z = 0; z <= 8*2; z++) {
            uiCommandBuilder.appendInline("#ChunkCards", "Group { LayoutMode: Left; Anchor: (Bottom: 0); }");

            for (int x = 0; x <= 8*2; x++) {
                uiCommandBuilder.append("#ChunkCards[" + z  + "]", "Pages/Buuz135_SimpleClaims_ChunkEntry.ui");

                if ((z - 8) == 0 && (x - 8) == 0) {
                    uiCommandBuilder.set("#ChunkCards[" + z + "][" + x + "].Text", "+");
                }

                var chunkLocation = new ChunkVector(chunkX + x - 8, chunkZ + z - 8);
                boolean isChunkSelected = nationBase.hasChunkAt(chunkLocation);

                Color cardColor = new Color(0, 0, 0, 25); // 20 Opacity

                if (isChunkSelected) {
                    cardColor = new Color(175, 0, 200, 128); // 50% Opacity
                }

                uiCommandBuilder.set("#ChunkCards[" + z + "][" + x + "].Background.Color", ColorParseUtil.colorToHexAlpha(cardColor));
                uiCommandBuilder.set("#ChunkCards[" + z + "][" + x + "].OutlineColor", ColorParseUtil.colorToHexAlpha(cardColor));
                uiCommandBuilder.set("#ChunkCards[" + z + "][" + x + "].OutlineSize", 1);

                uiEventBuilder.addEventBinding(CustomUIEventBindingType.Activating, "#ChunkCards[" + z + "][" + x + "]", EventData.of("Action", "LeftClicking:" + chunkLocation.x + ":" + chunkLocation.z));
                uiEventBuilder.addEventBinding(CustomUIEventBindingType.Activating, "#ConfirmButton", EventData.of("Action", "ConfirmButton"));
                uiEventBuilder.addEventBinding(CustomUIEventBindingType.ValueChanged, "#BaseNameInput", EventData.of("@BaseNameInput", "#BaseNameInput.Value"));

//                var tooltip = MessageHelper.multiLine()
//                        .append(Message.raw("Owner: ").bold(true).color(hytaleGold))
//                        .append(Message.raw("bloopkey1")).nl()
//                        .append(Message.raw("Description: ").bold(true).color(hytaleGold))
//                        .append(Message.raw("bloopkey2"));
//
//                    tooltip = tooltip.nl().nl().append(Message.raw("*Right Click to Unclaim*").bold(true).color(Color.RED.darker().darker()));
//
//                uiCommandBuilder.set("#ChunkCards[" + z + "][" + x + "].TooltipTextSpans", tooltip.build());
            }
        }
    }

    @Override
    public void handleDataEvent(@Nonnull Ref<EntityStore> ref, @Nonnull Store<EntityStore> store, Data data) {
        super.handleDataEvent(ref, store, data);

        if (data.value != null) {
            this.nationBase.name = data.value;
            this.sendUpdate();
            return;
        }

        String[] actions = data.action.split(":");

        String button = actions[0];

        if (button.equals("ConfirmButton")) {
            NationBaseManager.getInstance().addNationBase(nationBase);

            this.close();
            return;
        }

        int x = Integer.parseInt(actions[1]);
        int z = Integer.parseInt(actions[2]);

        if (button.equals("LeftClicking")) {
            this.nationBase.setCenterChunk(new ChunkVector(x, z));
        }

        UICommandBuilder commandBuilder = new UICommandBuilder();
        UIEventBuilder eventBuilder = new UIEventBuilder();

        this.build(ref, commandBuilder, eventBuilder, store);
        this.sendUpdate(commandBuilder, eventBuilder, true);
    }

    public static class Data {
        static final String KEY_ACTION = "Action";

        public static final BuilderCodec<Data> CODEC = BuilderCodec.builder(Data.class, Data::new)
                .addField(new KeyedCodec<>(KEY_ACTION, Codec.STRING), (searchGuiData, s) -> searchGuiData.action = s, searchGuiData -> searchGuiData.action)
                .addField(new KeyedCodec<>("@BaseNameInput", Codec.STRING), (data, value) -> data.value = value, data -> data.value)
                .build();

        private String action;
        private String value;
    }
}
