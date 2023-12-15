package dev.worldgen.confogurable.util;

public class TimeFixer {
    public static int fix(long time) {
        return ((int) time) % 24000;
    }
}
