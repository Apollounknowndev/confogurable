package dev.worldgen.confogurable.util;

import dev.worldgen.confogurable.fog.FogModifier;
import dev.worldgen.confogurable.resource.FogModifierEntry;
import dev.worldgen.confogurable.resource.FogModifierManager;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class FogColorManager {
    private static Vec3 vanillaFog;
    private static Vec3 fog;
    private static Vec3 prevFog;
    private static Vec3 vanillaSky;
    private static Vec3 sky;
    private static Vec3 prevSky;

    public static void tick(LocalPlayer clientPlayerEntity, ResourceKey<Biome> biome) {
        Vec3 newFog = vanillaFog;
        Vec3 newSky = vanillaSky;
        Optional<FogModifierEntry> optionalFogModifierEntry = FogModifierManager.getFogModifier(biome, clientPlayerEntity.clientLevel.dimension(), clientPlayerEntity.getOnPos(), TimeFixer.fix(clientPlayerEntity.clientLevel.getDayTime()));
        if (optionalFogModifierEntry.isPresent()) {
            FogModifier fogModifier = optionalFogModifierEntry.get().fogModifier();

            newFog = Vec3.fromRGB24(fogModifier.color());
            newSky = Vec3.fromRGB24(fogModifier.skyColor());
        }
        if (fog != null && sky != null) {
            prevFog = fog;
            fog = lerp(prevFog, newFog, 0.1f);
            prevSky = sky;
            sky = lerp(prevSky, newSky, 0.1f);
        }
    }

    public static void passVanillaFog(Vec3 vanillaFog) {
        FogColorManager.vanillaFog = vanillaFog;
        if (fog == null) fog = vanillaFog;
        if (prevFog == null) prevFog = vanillaFog;
    }

    public static void passVanillaSky(Vec3 vanillaSky) {
        FogColorManager.vanillaSky = vanillaSky;
        if (sky == null) sky = vanillaSky;
        if (prevSky == null) prevSky = vanillaSky;
    }

    public static Vec3 getFogColor(float tickDelta) {
        return lerp(prevFog, fog, tickDelta);
    }

    public static Vec3 getSkyColor(float tickDelta) {
        return lerp(prevSky, sky, tickDelta);
    }

    public static Vec3 lerp(Vec3 start, Vec3 end, float delta) {
        double x = Mth.clampedLerp(start.x, end.x, delta);
        double y = Mth.clampedLerp(start.y, end.y, delta);
        double z = Mth.clampedLerp(start.z, end.z, delta);
        return new Vec3(x, y, z);
    }
}
