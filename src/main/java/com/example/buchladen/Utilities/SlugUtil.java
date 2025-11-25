package com.example.buchladen.Utilities;

import java.text.Normalizer;

public class SlugUtil {

    public static String toSlug(String input) {
        if(input == null) return "";

        String value = input
                .replace("Ä", "Ae").replace("ä", "ae")
                .replace("Ö", "Oe").replace("ö", "oe")
                .replace("Ü", "Ue").replace("ü", "ue")
                .replace("ß", "ss");

        value = Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{InCOMBINING_DIACRITICAL_MARKS}+", "");

        value = value.toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|$", "");

        return value;
    }
}
