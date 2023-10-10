package com.apogames.help;

import java.util.Arrays;

public class Helper {

    public static byte[][] clone(byte[][] tileArray) {
        byte[][] clone = new byte[tileArray.length][tileArray[0].length];
        for (int y = 0; y < tileArray.length; y++) {
            System.arraycopy(tileArray[y], 0, clone[y], 0, tileArray[0].length);
        }
        return clone;
    }

    public static boolean equal(final byte[][] arr1, final byte[][] arr2) {
        if (arr1.length != arr2.length) {
            return false;
        }

        for (int i = 0; i < arr1.length; i++) {
            if (!Arrays.equals(arr1[i], arr2[i])) {
                return false;
            }
        }

        return true;
    }

    public static void print(final byte[][] array) {
        for (int y = 0; y < array.length; y++) {
            for (int x = 0; x < array[0].length; x++) {
                System.out.print(array[y][x]);
            }
            System.out.println();
        }
    }

}
