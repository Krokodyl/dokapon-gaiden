package entities;

import characters.JapaneseChar;
import enums.CharType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static services.Utils.toHexString;

public class Dictionnary {

    Map<String, String> japaneseDictionnary = new HashMap<>();

    public Dictionnary(List<JapaneseChar> chars) {
        for (JapaneseChar c:chars) {
            japaneseDictionnary.put(c.getCode(), c.getValue());
        }
    }
    
    public String getJapanese(String value) {
        return japaneseDictionnary.get(value);
    }

}
