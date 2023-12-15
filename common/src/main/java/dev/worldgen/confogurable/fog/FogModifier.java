package dev.worldgen.confogurable.fog;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record FogModifier(int priority, FogCondition condition, int color, int skyColor, FogDistanceModifier fogStart, FogDistanceModifier fogEnd) {
    public static final Codec<FogModifier> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.INT.fieldOf("priority").forGetter(FogModifier::priority),
        FogCondition.CODEC.fieldOf("condition").forGetter(FogModifier::condition),
        Codec.INT.fieldOf("color").forGetter(FogModifier::color),
        Codec.INT.fieldOf("sky_color").forGetter(FogModifier::skyColor),
        FogDistanceModifier.CODEC.fieldOf("fog_start").forGetter(FogModifier::fogStart),
        FogDistanceModifier.CODEC.fieldOf("fog_end").forGetter(FogModifier::fogEnd)
    ).apply(instance, FogModifier::new));
}
