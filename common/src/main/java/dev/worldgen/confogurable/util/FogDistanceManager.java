package dev.worldgen.confogurable.util;

import dev.worldgen.confogurable.fog.FogModifier;
import dev.worldgen.confogurable.resource.FogModifierEntry;
import dev.worldgen.confogurable.resource.FogModifierManager;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;

import java.util.Optional;

public class FogDistanceManager {
    private static final float defaultValue = Float.MAX_VALUE - 1;
    private static float vanillaFogStart = defaultValue;
    private static float fogStart = defaultValue;
    private static float prevFogStart = defaultValue;
    private static float vanillaFogEnd = defaultValue;
    private static float fogEnd = defaultValue;
    private static float prevFogEnd = defaultValue;

    public static void tick(LocalPlayer clientPlayerEntity, ResourceKey<Biome> biome) {
        float newFogStart = vanillaFogStart;
        float newFogEnd = vanillaFogEnd;
        Optional<FogModifierEntry> optionalFogModifierEntry = FogModifierManager.getFogModifier(biome, clientPlayerEntity.clientLevel.dimension(), clientPlayerEntity.getOnPos(), TimeFixer.fix(clientPlayerEntity.clientLevel.getDayTime()));
        if (optionalFogModifierEntry.isPresent()) {
            FogModifier fogModifier = optionalFogModifierEntry.get().fogModifier();

            newFogStart = fogModifier.fogStart().apply(vanillaFogStart);
            newFogEnd = fogModifier.fogEnd().apply(vanillaFogEnd);
        }
        if (fogStart != defaultValue && fogEnd != defaultValue) {
            prevFogStart = fogStart;
            fogStart = Mth.clampedLerp(prevFogStart, newFogStart, 0.1f);
            prevFogEnd = fogEnd;
            fogEnd = Mth.clampedLerp(prevFogEnd, newFogEnd, 0.1f);
        }
    }

    public static void passVanillaFogStart(float vanillaFogStart) {
        FogDistanceManager.vanillaFogStart = vanillaFogStart;
        if (fogStart == defaultValue) fogStart = vanillaFogStart;
        if (prevFogStart == defaultValue) prevFogStart = vanillaFogStart;
    }

    public static void passVanillaFogEnd(float vanillaFogEnd) {
        FogDistanceManager.vanillaFogEnd = vanillaFogEnd;
        if (fogEnd == defaultValue) fogEnd = vanillaFogEnd;
        if (prevFogEnd == defaultValue) prevFogEnd = vanillaFogEnd;
    }

    public static float getFogStart(float tickDelta) {
        return Mth.clampedLerp(prevFogStart, fogStart, tickDelta);
    }

    public static float getFogEnd(float tickDelta) {
        return Mth.clampedLerp(prevFogEnd, fogEnd, tickDelta);
    }
}
