package services;

import characters.JapaneseChar;
import characters.LatinChar;
import characters.SpecialChar;
import entities.Dictionnary;
import entities.PointerData;
import entities.PointerTable;
import entities.Translation;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static services.Constants.*;
import static services.Constants.specialCodes;

public class Translator {

    private List<Translation> translations = new ArrayList<>();
    private Map<String, String> references = new HashMap<>();

    private Map<String, SpecialChar> specialCharMap = new HashMap<>();

    private LatinLoader latinLoader;

    boolean replaceMissingTranslationWithValue = true;
    
    int missingTranslations = 0;

    public Translator(LatinLoader latinLoader) {
        this.latinLoader = latinLoader;
    }

    public void setSpecialCharMap(Map<String, SpecialChar> specialCharMap) {
        this.specialCharMap = specialCharMap;
    }

    public void loadTranslationFile(String name) throws IOException {
        System.out.println("Load Translation File : "+name);
        BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(Translator.class.getClassLoader().getResourceAsStream(name)), StandardCharsets.UTF_8));
        String line = br.readLine();
        Translation t = new Translation();
        String jpn = "";
        int translationCount = 0;
        int missingTranslations = 0;
        while (line != null) {
            if (line.contains(Constants.TRANSLATION_KEY_VALUE_SEPARATOR)) {
                String[] split = line.split(Constants.TRANSLATION_KEY_VALUE_SEPARATOR);
                if (split.length>0) {
                    if (split[0].equals(Constants.TRANSLATION_KEY_OFFSETDATA)) {
                        t.setOffsetData(Integer.parseInt(split[1], 16));
                    }
                    if (split[0].equals(Constants.TRANSLATION_KEY_OFFSET)) {
                        t.setOffset(Integer.parseInt(split[1], 16));
                    }
                    if (split[0].equals(Constants.TRANSLATION_KEY_MENUDATA)) {
                        if (split.length>1) t.setMenuData(split[1]);
                    }
                    if (split[0].equals(Constants.TRANSLATION_KEY_VALUE)) {
                        t.setValue(split[1]);
                    }
                    if (split[0].equals(Constants.TRANSLATION_KEY_ENG)) {
                        if (split.length>1) {
                            t.setTranslation(split[1]);
                        } else {
                            t.setTranslation("");
                        }
                    }
                    if (split[0].equals(Constants.TRANSLATION_KEY_JPN)) {
                        if (split.length>1) {
                            t.setJapanese(split[1]);
                            jpn = split[1];
                        }
                    }
                }
            } else {
                if (t.getTranslation() != null && !t.getTranslation().trim().isEmpty()) {
                    translations.add(t);
                    translationCount++;
                }
                else {
                    missingTranslations++;
                    System.out.println("MISSING TRANSLATIONS : "+Integer.toHexString(t.getOffset())+"  "+jpn);
                }
                t = new Translation();
            }
            line = br.readLine();
        }
        System.out.printf("Translations : %s\n",translationCount);
        System.out.printf("Missing translations : %s\n",missingTranslations);
    }

    public void loadReferenceFile(String name) throws IOException {
        System.out.println("Load Reference File : "+name);
        BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(Translator.class.getClassLoader().getResourceAsStream(name)), StandardCharsets.UTF_8));
        String line = br.readLine();
        
        while (line != null) {
            if (line.contains(Constants.TRANSLATION_KEY_VALUE_SEPARATOR)) {
                String[] split = line.split(Constants.TRANSLATION_KEY_VALUE_SEPARATOR);
                if (split.length>0) {
                    String key = split[0];
                    String value = split[1];
                    references.put(key,value);
                }
            }
            line = br.readLine();
        }
    }

    public String[] getTranslation(PointerData p, boolean evenLength) {
        return getTranslation(p, evenLength, true);
    }

    public String[] getTranslation(PointerData p, boolean evenLength, boolean prefixF0) {
        String jpn = "";
        for (Translation t : translations) {
            String translation = t.getTranslation();
            if (t.getOffsetData() == p.getOffsetData() && translation != null && !translation.isEmpty()) {
                for (Map.Entry<String, String> e : references.entrySet()) {
                    translation = translation.replaceAll(e.getKey(), e.getValue());
                }
                jpn = t.getJapanese();
                String eng = getCodesFromEnglish(translation);
                if (prefixF0) eng = Utils.toHexString(MODE_F0_BYTE) + " " + eng;
                return eng.split(" ");
            }
        }
        /*
        
         */
        
        /*
        Missing translation
         */
        if (replaceMissingTranslationWithValue) {
            missingTranslations++;
            String[] data = p.getData();
            String prefix = "";
            if (data[0].equals("03") && data[1].equals("80")) {
                String code = data[0]+data[1]+data[2]+data[3];
                if (!missingSpecialCodes.contains(code)) {
                    missingSpecialCodes.add(code);
                    System.out.println("SPECIAL CODE "+code);
                }
                switch (code) {
                    case "0380F694":{
                        prefix = "{0780}{0C80}{050301}{0180}{0A00}";
                    }break;
                    case "0380AAA8":{
                        prefix = "{0780}{0D80}{070506}{0180}{1400}";
                    }break;
                    case "0380B7A8":{
                        prefix = "{0780}{0C80}{070301}{0180}{0A00}";
                    }break;
                    case "038079FF":{
                        prefix = "{0D80}{070504}{0180}{1400}";
                    }break;
                    case "03805FFF":{
                        prefix = "{0780}{0D80}{070506}{0180}{1400}";
                    }break;
                    case "038024C5":{
                        prefix = ".....";
                    }break;
                }
            }
            jpn = getJapaneseWithoutSpecial(Utils.hexStringToByteArray(p.getData()));
            if (jpn.equals("{EL}")) {
                System.out.println("Keep original data for "+Utils.toHexString(p.getValue(),4).toUpperCase());
                return null;
            }
            String translation = Utils.toHexString(p.getValue(),4).toUpperCase();
            translation = prefix + translation + "{0180}{C000}{0780}{0A80}{EL}";
            String eng = getCodesFromEnglish(translation);
            return eng.split(" ");
        } else return null;
    }

    public String getJapaneseWithoutSpecial(byte[] data) {
        List<JapaneseChar> japaneseChars = JsonLoader.loadJapanese();
        Dictionnary japanese = new Dictionnary(japaneseChars);
        String res = "";
        int k=0;
        while (k<data.length)  {
            String code = "";
            byte a = data[k++];
            byte b = data[k++];
            code += Utils.toHexString(a)+Utils.toHexString(b);
            if (specialCodes.containsKey(code)) {
                int paramCount = specialCodes.get(code);
                String param = "";
                while (paramCount>0) {
                    b = data[k++];
                    param += Utils.toHexString(b);
                    paramCount--;
                }
                //res += "{"+code+"}"+"{"+param+"}";
            }
            else {
                String s = japanese.getJapanese(code);
                if (s!=null) res += s;
            }
        }
        return res;
    }
    
    public static List<String> missingSpecialCodes = new ArrayList<>();

    public String getEnglish(PointerData p) {
        for (Translation t : translations) {
            if (t.getOffsetData() == p.getOffsetData() && t.getTranslation() != null && !t.getTranslation().isEmpty()) {
                String menuData = t.getMenuData();
                String eng = t.getTranslation();
                return eng;
            }
        }
        return null;
    }

    public String[] getMenuData(PointerData p) {
        for (Translation t : translations) {
            if (t.getOffsetData() == p.getOffsetData() && t.getTranslation() != null && !t.getTranslation().isEmpty()) {
                String menuData = t.getMenuData();
                if (menuData == null) {
                    return null;
                }
                return menuData.trim().split(" ");
            }
        }
        return null;
    }

    public String getCodesFromEnglish(String eng) {
        String res = "";
        boolean skip = false;
        String skipped = "";
        boolean openQuote = true;
        //checkLineLength(eng.split("\\{NL\\}"));
        for (char c : eng.toCharArray()) {
            if (c == '{') {
                skip = true;
                skipped = "";
            }
            if (!skip) {
                LatinChar e = getLatinChar(c + "");
                if (e == null) {
                    if (c=='"' && openQuote) {
                        e = getLatinChar("{O-QUOTES}");
                        openQuote = false;
                    }
                    if (c=='"' && !openQuote) {
                        e = getLatinChar("{C-QUOTES}");
                        openQuote = true;
                    }
                }
                if (e == null) {
                    res = res += e.getCode() + " ";
                }
                res = res += e.getCode() + " ";

            }
            if (skip) {
                skipped += c;
            }
            if (c == '}') {
                skip = false;
                if (containsTranslation(skipped)) {
                    res += getLatinChar(skipped).getCode() + " ";
                } else {
                    res += skipped;
                }
            }
        }
        res = res.replace("{NL}", Constants.NEW_LINE_CHARACTER_HEXA+" ");
        res = res.replace("{EL}", Constants.END_OF_LINE_CHARACTER_HEXA+" ");
        res = res.replace("{", "");
        res = res.replace("}", " ");
        return res.trim();
    }

    private void checkLineLength(String[] lines) {
        for (String eng:lines) {
            String res = "";
            boolean skip = false;
            for (char c : eng.toCharArray()) {
                if (c == '{') {
                    skip = true;
                } else if (c == '}') {
                    skip = false;
                } else {
                    if (!skip) res += c;
                }
            }
            if (res.length()>LENGTH_DIALOG_LINE) {
                System.out.println("LINE TOO LONG "+res+" ("+res.length()+")");
                System.out.println(insertLineBreak(res));
            }
        }
    }

    public List<String> collectSpecialChars(String eng) {
        List<String> res = new ArrayList<>();
        boolean skip = false;
        String spe = "";
        for (char c : eng.toCharArray()) {
            if (c == '{') {
                skip = true;
                spe += c;
            } else if (c == '}') {
                skip = false;
                spe += c;
                if (!specialCharMap.containsKey(spe) && !res.contains(spe)) res.add(spe);
                spe = "";
            } else {
                if (skip) spe += c;
            }
        }
        return res;
    }

    public void showSpecialChars() {
        /*System.out.println("SPECIAL");
        Collections.sort(specials);
        for (String s:specials) System.out.println(s);*/
    }

    private String insertLineBreak(String line) {
        String[] split = line.split(" ");
        String res = "";
        String segment = "";
        for (String s:split) {
            if ((segment+" "+s).length()>LENGTH_DIALOG_LINE) {
                segment+="{NL}";
                res += segment;
                segment = s;
            } else {
                segment += " "+s;
            }
        }
        res+=segment;
        return res.trim()+"{EL}";
    }

    public String getJapanese(String codes, List<JapaneseChar> japaneseChars) {
        String[] split = codes.split(" ");
        String res = "";
        for (String s:split) {
            boolean found = false;
            for (JapaneseChar jc:japaneseChars) {
                if (jc.getCode().equals(s)) {
                    found = true;
                    res+=jc.getValue();
                }
            }
            if (!found) {
                res+="{"+s+"}";
            }
        }
        return res;
    }

    private LatinChar getLatinChar(String c) {
        for (LatinChar e : latinLoader.getLatinChars()) {
            if (e.getValue().equals(c)) {
                return e;
            }
        }
        return null;
    }

    private boolean containsTranslation(String s) {
        for (LatinChar e : latinLoader.getLatinChars()) {
            if (e.getValue().equals(s)) {
                return true;
            }
        }
        return false;
    }

    public void checkTranslations(byte[] data) {
        int count = 0;
        for (Translation t:translations) {
            int a = (data[t.getOffset()] & 0xFF);
            int b = (data[t.getOffset()+1] & 0xFF);
            String val = Integer.toHexString(a)+Integer.toHexString(b);
            if (!t.getValue().contains(val))
            {
                count++;
                System.out.println(Integer.toHexString(t.getOffset())+" "+t.getValue()+" "+val);
            }
        }
    }

    public String[] getTranslationPointer(PointerData p, PointerTable table) {
        String eng = getCodesFromEnglish(
                Utils.padLeft(Integer.toHexString(p.getValue()),'0',4)
                        +" "
                        +(Integer.toHexString(p.getOffsetData()))
                        +"{EL}");
        return eng.split(" ");

    }

    public void checkInGameLength(String english) {
        String[] lines = english.split("\\{NL\\}");
        for (String line:lines) {
            int length = 0;
            for (Map.Entry<String, SpecialChar> specialCharEntry : specialCharMap.entrySet()) {
                int matches = StringUtils.countMatches(line, specialCharEntry.getKey());
                length+=specialCharEntry.getValue().getInGameLength()*matches;
                line = line.replace(specialCharEntry.getKey(),"");
            }
            length+=line.length();
            if (length> LENGTH_DIALOG_LINE)
                System.out.println("LINE TOO LONG "+line+" ("+length+")");
        }
    }

    public int getInGameLength(String english) {
        String[] lines = english.split("\\{NL\\}");
        int length = 0;
        for (String line:lines) {
            for (Map.Entry<String, SpecialChar> specialCharEntry : specialCharMap.entrySet()) {
                int matches = StringUtils.countMatches(line, specialCharEntry.getKey());
                length+=specialCharEntry.getValue().getInGameLength()*matches;
                line = line.replace(specialCharEntry.getKey(),"");
            }
            length+=line.length();
        }
        return length;
    }

    public int checkDataLength(String english) {
        String line = english;
        int length = 0;
        for (Map.Entry<String, SpecialChar> specialCharEntry : specialCharMap.entrySet()) {
            int matches = StringUtils.countMatches(line, specialCharEntry.getKey());
            length+=specialCharEntry.getValue().getDataLength()*matches;
            line = line.replace(specialCharEntry.getKey(),"");
        }
        length+=line.length();
        return length;
    }

}
