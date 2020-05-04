package me.lkp111138.plugin;

import net.minecraft.server.v1_12_R1.ChatMessageType;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
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

    public static Object getPrivateField(String fieldName, Class clazz, Object object) {
        Field field;
        Object o = null;

        try {
            field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            o = field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return o;
    }

    @SuppressWarnings("rawtypes")
    public static void sendActionBar(Player player, String message) {
        IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
        PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, ChatMessageType.GAME_INFO);
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(ppoc);
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
}
