package dev.worldgen.confogurable.events;

import dev.worldgen.confogurable.ConfogurableCommon;
import dev.worldgen.confogurable.debug.FogDebugCommand;
import dev.worldgen.confogurable.debug.FogDebugHud;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ConfogurableCommon.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ConfogurableForgeEvents {

    @SubscribeEvent
    public static void registerCommands(RegisterClientCommandsEvent event) {
        FogDebugCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void tick(TickEvent.ClientTickEvent event) {
        ConfogurableCommon.tick(Minecraft.getInstance());
    }
}
