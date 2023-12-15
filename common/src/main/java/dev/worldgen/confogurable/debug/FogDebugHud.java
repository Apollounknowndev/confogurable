package dev.worldgen.confogurable.debug;


import dev.worldgen.confogurable.ConfogurableCommon;
import dev.worldgen.confogurable.resource.FogModifierEntry;
import dev.worldgen.confogurable.resource.FogModifierManager;
import dev.worldgen.confogurable.util.FogColorManager;
import dev.worldgen.confogurable.util.FogDistanceManager;
import dev.worldgen.confogurable.util.TimeFixer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class FogDebugHud {
    private static boolean enabled = false;

    public static void render(Gui inGameHud, GuiGraphics context, float tickDelta) {
        if (enabled && Minecraft.renderNames() && !Minecraft.getInstance().options.renderDebug) {
            LocalPlayer clientPlayerEntity = Minecraft.getInstance().player;
            if (clientPlayerEntity != null) {
                Optional<ResourceKey<Biome>> registryKey = clientPlayerEntity.level().getBiome(clientPlayerEntity.getOnPos()).unwrapKey();
                if (registryKey.isPresent()) {
                    Optional<FogModifierEntry> optionalFogModifierEntry = FogModifierManager.getFogModifier(registryKey.get(), clientPlayerEntity.clientLevel.dimension(), clientPlayerEntity.getOnPos(), TimeFixer.fix(clientPlayerEntity.clientLevel.getDayTime()));

                    Font textRenderer = inGameHud.getFont();
                    String debugText = "(No active fog config)";

                    if (optionalFogModifierEntry.isPresent()) {
                        ResourceLocation id = optionalFogModifierEntry.get().id();
                        debugText = id.toString();
                    }


                    context.drawString(textRenderer, Component.literal(debugText).getVisualOrderText(), context.guiWidth() - textRenderer.width(debugText) - 1, 1, 0xFFFFFF);

                    String fogStartText = "" + Math.round(FogDistanceManager.getFogStart(tickDelta));
                    String fogEndText = "" + Math.round(FogDistanceManager.getFogEnd(tickDelta));
                    context.drawString(textRenderer, Component.literal(fogStartText).getVisualOrderText(), context.guiWidth() - textRenderer.width(fogStartText) - 21, textRenderer.lineHeight + 2, 0xFFFFFF);
                    context.drawString(textRenderer, Component.literal(fogEndText).getVisualOrderText(), context.guiWidth() - textRenderer.width(fogEndText) - 21, (textRenderer.lineHeight + 1) * 2 + 1, 0xFFFFFF);

                    String timeOfDay = "" + TimeFixer.fix(clientPlayerEntity.clientLevel.getDayTime());
                    context.drawString(textRenderer, Component.literal(timeOfDay).getVisualOrderText(), context.guiWidth() - textRenderer.width(timeOfDay) - 1, 30, 0xFFFFFF);

                    Vec3 unpackedColor = FogColorManager.getFogColor(tickDelta);
                    int backgroundColor = argbAsInt(128, 0, 0, 0);
                    int color = argbAsInt(255, (int) (unpackedColor.x * 255), (int) (unpackedColor.y * 255), (int) (unpackedColor.z * 255));
                    context.pose().pushPose();
                    context.pose().translate((float) (context.guiWidth() - 20), (float) (textRenderer.lineHeight + 2), 0f);
                    context.pose().scale(1.0f, 1.0f, 1.0f);
                    context.fill(0, 0, 18, 18, backgroundColor);
                    context.fill(1, 1, 17, 17, color);
                    context.pose().popPose();
                }
            }
        }
    }

    public static int argbAsInt(int a, int r, int g, int b) {
        return a << 24 | r << 16 | g << 8 | b;
    }

    public static void enabled(boolean enable) {
        enabled = enable;
    }
}
