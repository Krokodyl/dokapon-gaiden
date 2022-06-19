package services;

import characters.LatinChar;

import java.util.ArrayList;
import java.util.List;

public class LatinLoader {

    private List<LatinChar> latinChars = new ArrayList<>();

    public List<LatinChar> getLatinChars() {
        return latinChars;
    }

    public void loadLatin() {
        System.out.println("Load Latin");
        latinChars.addAll(JsonLoader.loadLatin());
    }

}
