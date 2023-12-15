package dev.worldgen.confogurable.fog;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public record FogDistanceModifier(DistanceOperation operation, float value) {
    public static final Codec<FogDistanceModifier> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        DistanceOperation.CODEC.fieldOf("operation").forGetter(FogDistanceModifier::operation),
        Codec.FLOAT.fieldOf("value").forGetter(FogDistanceModifier::value)
    ).apply(instance, FogDistanceModifier::new));

    public String toString() {
        String operationIcon = "";
        switch (operation) {
            case SET -> operationIcon = "=";
            case MULTIPLY -> operationIcon = "*";
            case ADD -> {
                if (value >= 0) operationIcon = "+";
            }
            case SET_EXACT -> operationIcon = "@";
        }
        return operationIcon + value;
    }

    public float apply(float base) {
        return switch (operation) {
            case SET -> Minecraft.getInstance().gameRenderer.getRenderDistance() * value;
            case MULTIPLY -> base * value;
            case ADD -> base + value;
            case SET_EXACT -> value;
        };
    }

    private enum DistanceOperation implements StringRepresentable {
        SET("set"),
        MULTIPLY("multiply"),
        ADD("add"),
        SET_EXACT("set_exact");

        public static final StringRepresentable.EnumCodec<DistanceOperation> CODEC = StringRepresentable.fromEnum(DistanceOperation::values);

        private final String name;

        DistanceOperation(String name) {
            this.name = name;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }
    }
}
