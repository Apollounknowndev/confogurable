package dev.worldgen.confogurable;

import dev.worldgen.confogurable.debug.FogDebugCommand;
import dev.worldgen.confogurable.resource.FogModifierManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.packs.PackType;

public class ConfogurableFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> FogDebugCommand.register(dispatcher));
        ClientTickEvents.END_CLIENT_TICK.register(ConfogurableCommon::tick);
    }
}
