package dev.worldgen.confogurable.fog;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.InclusiveRange;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import java.util.List;


public record FogCondition(List<ResourceKey<Biome>> biomes, List<ResourceKey<Level>> dimensions, InclusiveRange<Integer> elevation, InclusiveRange<Integer> time) {
    public static final Codec<FogCondition> CODEC = RecordCodecBuilder.create(instance -> instance.group(ResourceKey.codec(Registries.BIOME).listOf().fieldOf("biomes").forGetter(FogCondition::biomes), ResourceKey.codec(Registries.DIMENSION).listOf().fieldOf("dimensions").forGetter(FogCondition::dimensions), InclusiveRange.codec(Codec.INT).fieldOf("elevation").forGetter(FogCondition::elevation), InclusiveRange.codec(Codec.INT).fieldOf("time").forGetter(FogCondition::time)).apply(instance, FogCondition::new));

    public String toString() {
        StringBuilder result = new StringBuilder("{\n  biomes:");

        if (biomes.isEmpty()) result.append("[");
        for (int i = 0; i < biomes.size(); i++) {
            if (i == 0) result.append("[");
            else result.append(",");
            result.append(biomes.get(i).location());
        }
        result.append("],\n  dimensions:");

        if (dimensions.isEmpty()) result.append("[");
        for (int i = 0; i < dimensions.size(); i++) {
            if (i == 0) result.append("[");
            else result.append(",");
            result.append(dimensions.get(i).location());
        }
        result.append("],\n  elevation:{").append(elevation.minInclusive()).append(",").append(elevation.maxInclusive()).append("},\n  time:{").append(time.minInclusive()).append(",").append(time.maxInclusive()).append("}\n}");
        return result.toString();
    }

    public boolean evaluate(ResourceKey<Biome> biome, ResourceKey<Level> dimension, BlockPos position, int time) {
        if (!biomes.contains(biome) && !biomes.isEmpty()) return false;
        if (!dimensions.contains(dimension) && !dimensions.isEmpty()) return false;
        if (!time().isValueInRange(time)) return false;
        return elevation().isValueInRange(position.getY());
    }
}
