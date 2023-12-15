package dev.worldgen.confogurable;

import dev.worldgen.confogurable.util.DarkSkyManager;
import dev.worldgen.confogurable.util.FogColorManager;
import dev.worldgen.confogurable.util.FogDistanceManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;


public class ConfogurableCommon {
    public static final String MOD_ID = "confogurable";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void tick(Minecraft minecraftClient) {
        LocalPlayer clientPlayerEntity = minecraftClient.player;
        if (minecraftClient.level != null && minecraftClient.player != null) {
            Optional<ResourceKey<Biome>> registryKey = clientPlayerEntity.level().getBiome(clientPlayerEntity.getOnPos()).unwrapKey();
            if (registryKey.isPresent()) {
                FogColorManager.tick(clientPlayerEntity, registryKey.get());
                FogDistanceManager.tick(clientPlayerEntity, registryKey.get());
                DarkSkyManager.tick(clientPlayerEntity, registryKey.get());
            }
        }
    }
}