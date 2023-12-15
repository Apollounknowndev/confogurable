package dev.worldgen.confogurable.events;

import dev.worldgen.confogurable.ConfogurableCommon;
import dev.worldgen.confogurable.debug.FogDebugHud;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ConfogurableCommon.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ConfogurableModEvents {

    @SubscribeEvent
    public static void registerDebugGuiOverlay(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("debug", ((gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
            FogDebugHud.render(gui, guiGraphics, partialTick);
        }));
    }
}
