package dev.worldgen.confogurable.util;

import dev.worldgen.confogurable.resource.FogModifierEntry;
import dev.worldgen.confogurable.resource.FogModifierManager;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;

import java.util.Optional;

public class DarkSkyManager {
    private static float darkSky = 0f;
    private static float prevDarkSky = 0f;

    public static void tick(LocalPlayer clientPlayerEntity, ResourceKey<Biome> biome) {
        Optional<FogModifierEntry> optionalFogModifierEntry = FogModifierManager.getFogModifier(biome, clientPlayerEntity.clientLevel.dimension(), clientPlayerEntity.getOnPos(), TimeFixer.fix(clientPlayerEntity.clientLevel.getDayTime()));
        prevDarkSky = darkSky;
        darkSky = Mth.clampedLerp(prevDarkSky, optionalFogModifierEntry.isPresent() ? 0f : 1f, 0.1f);
    }

    public static float getDarkSky(float tickDelta) {
        return Mth.clampedLerp(prevDarkSky, darkSky, tickDelta);
    }
}
