package me.lkp111138.plugin.util;

import me.lkp111138.plugin.quest.Quest;
import org.bukkit.entity.Player;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.UUID;

public class Util {
    /**
     * Gets the number of digits in a decimal.
     * @param i the number
     * @return ceil(log10(i))
     */
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

    /**
     * Converts uuid to byte array for database storage.
     * @param uuid the uuid
     * @return the uuid in a byte array
     */
    public static byte[] getBytesFromUUID(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());

        return bb.array();
    }

    /**
     * Convers a byte array back into an uuid
     * @param bytes the bytes to be converte back
     * @return the uuid converted from the byte array
     */
    public static UUID getUUIDFromBytes(byte[] bytes) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        long high = byteBuffer.getLong();
        long low = byteBuffer.getLong();

        return new UUID(high, low);
    }

    /**
     * Properly capitalizes a string
     * @param s the string
     * @return the capitalized string
     */
    public static String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    /**
     * Sets a player's walk speed while respecting constraints (0-1)
     * @param player the player
     * @param speed the speed
     */
    public static void setWalkSpeed(Player player, float speed) {
        player.setWalkSpeed(Math.max(0, Math.min(1, speed)));
    }

    /**
     * Calculates stats based on base and multiplier
     * @param base base value
     * @param roll rolled multiplier
     * @return The proper value for the stat
     */
    public static int properValueForStats(int base, int roll) {
        int value = (int) (base * roll / 100.0 + 0.5);
        if (value == 0) {
            return (int) Math.signum(base);
        }
        return value;
    }

    /**
     * Converts a number to string, ready for lores
     * @param n the number
     * @param color apply colour?
     * @return The string ready for lore
     */
    public static String nToString(int n, boolean color) {
        if (n >= 0) {
            return (color ? "\u00a7a" : "") + "+" + n;
        } else {
            return (color ? "\u00a7c" : "") + n;
        }
    }

    /**
     * Converts a number to string, ready for lores
     * @param n the number
     * @return The string ready for lore
     */
    public static String nToString(int n) {
        return nToString(n, false);
    }

    public static String questionmarksForQuery(int fieldCount, int rowCount) {
        // (?,?,?),(?,?,?)
        StringBuilder rowBuilder = new StringBuilder("(");
        for (int i = 0; i < fieldCount; i++) {
            rowBuilder.append("?,");
        }
        rowBuilder.setLength(rowBuilder.length() - 1);
        rowBuilder.append(")");
        String row = rowBuilder.toString();
        rowBuilder.setLength(0);
        for (int i = 0; i < rowCount; i++) {
            rowBuilder.append(row).append(",");
        }
        rowBuilder.setLength(rowBuilder.length() - 1);
        return rowBuilder.toString();
    }

    public static void sendQuestDialog(Player player, List<String> dialog) {
        String prefix = Quest.dialogPrefix.replaceAll("\\$\\{total}", String.valueOf(dialog.size()));
        for (int i = 0; i < dialog.size(); i++) {
            String s = dialog.get(i);
            // todo send with delay
            String localPrefix = prefix.replaceAll("\\$\\{index}", String.valueOf(i + 1));
            player.sendMessage(localPrefix + s);
        }
    }
}
