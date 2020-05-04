package me.lkp111138.plugin.util;

import org.bukkit.entity.Player;

import java.nio.ByteBuffer;
import java.util.UUID;

public class Util {
    public static int digitCount(int i) {
        if (i >= 1000000000) {
            return 10;
        }
        if (i >= 100000000) {
            return 9;
        }
        if (i >= 10000000) {
            return 8;
        }
        if (i >= 1000000) {
            return 7;
        }
        if (i >= 100000) {
            return 6;
        }
        if (i >= 10000) {
            return 5;
        }
        if (i >= 1000) {
            return 4;
        }
        if (i >= 100) {
            return 3;
        }
        if (i >= 10) {
            return 2;
        }
        return 1;
    }

    public static byte[] getBytesFromUUID(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());

        return bb.array();
    }

    public static UUID getUUIDFromBytes(byte[] bytes) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        long high = byteBuffer.getLong();
        long low = byteBuffer.getLong();

        return new UUID(high, low);
    }

    public static String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    public static void setWalkSpeed(Player player, float speed) {
        player.setWalkSpeed(Math.max(0, Math.min(1, speed)));
    }

    public static int properValueForStats(int base, int roll) {
        int value = (int) (base * roll / 100.0 + 0.5);
        if (value == 0) {
            return (int) Math.signum(base);
        }
        return value;
    }



    public static String nToString(int n, boolean color) {
        if (n >= 0) {
            return (color ? "\u00a7a" : "") + "+" + n;
        } else {
            return (color ? "\u00a7c" : "") + n;
        }
    }

    public static String nToString(int n) {
        return nToString(n, false);
    }
}
