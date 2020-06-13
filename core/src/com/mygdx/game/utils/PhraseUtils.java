package com.mygdx.game.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class PhraseUtils {

    private static HashMap<PhraseType, ArrayList<String>> phrases = new HashMap<>();

    public static void load(JSONObject json) {
        for (String key : json.keySet()) {
            JSONArray array = json.optJSONArray(key);
            PhraseType type = null;
            switch (key) {
                case "not_found":
                    type = PhraseType.NOT_FOUND;
                    break;
                case "found":
                    type = PhraseType.FOUND;
                    break;
            }
            ArrayList<String> p = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                p.add(array.optString(i));
            }
            phrases.put(type, p);
        }
    }

    public static String getRandomPhrase(PhraseType type) {
        ArrayList<String> list = phrases.get(type);
        Random random = new Random();
        return list.get(random.nextInt(list.size()));
    }

    public enum PhraseType {
        NOT_FOUND("not_found"), FOUND("found");
        private String name;

        PhraseType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
