package me.lkp111138.plugin;

import java.lang.reflect.Field;

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
}
