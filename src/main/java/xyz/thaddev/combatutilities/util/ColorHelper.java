package xyz.thaddev.combatutilities.util;

import java.awt.*;

public class ColorHelper {
    public static String black = "\u00A70";
    public static String dark_blue = "\u00A71";
    public static String dark_green = "\u00A72";
    public static String dark_aqua = "\u00A73";
    public static String dark_red = "\u00A74";
    public static String dark_purple = "\u00A75";
    public static String gold = "\u00A76";
    public static String gray = "\u00A77";
    public static String dark_gray = "\u00A78";
    public static String blue = "\u00A79";
    public static String green = "\u00A7a";
    public static String aqua = "\u00A7b";
    public static String red = "\u00A7c";
    public static String light_purple = "\u00A7d";
    public static String yellow = "\u00A7e";
    public static String white = "\u00A7f";

    public static String getColorFromCode(String color) {
        //shut up it works dont complain
        return switch (color) {
            case "%$black" -> black;
            case "%$dark_blue" -> dark_blue;
            case "%$dark_green" -> dark_green;
            case "%$dark_aqua" -> dark_aqua;
            case "%$dark_red" -> dark_red;
            case "%$dark_purple" -> dark_purple;
            case "%$gold" -> gold;
            case "%$gray" -> gray;
            case "%$dark_gray" -> dark_gray;
            case "%$blue" -> blue;
            case "%$green" -> green;
            case "%$aqua" -> aqua;
            case "%$red" -> red;
            case "%$light_purple" -> light_purple;
            case "%$yellow" -> yellow;
            default -> white;
        };
    }

    public static String from(String fromText) {
        String[] colors = fromText.split(" ");
        StringBuilder builder = new StringBuilder();
        for (String color : colors) {
            if (color.startsWith("%$")) {
                builder.append(getColorFromCode(color));
            } else {
                builder.append(color).append(" ");
            }
        }
        return builder.toString().trim();
    }

    public static int rgbToInteger(int red, int green, int blue, int alpha) {
        return (alpha & 0xFF) << 24 | (red & 0xFF) << 16 | (green & 0xFF) << 8 | (blue & 0xFF);
    }

    public static int rgbToInteger(int red, int green, int blue) {
        return rgbToInteger(red, green, blue, 255);
    }

    public static Color integerToColor(int color) {
        return new Color(color);
    }
}
