package com.emirkabal.dervish.utils;

import java.util.Random;

public class RANDOM {
    public static Object nextObject(Object[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }
}
