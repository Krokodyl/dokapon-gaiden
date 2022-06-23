package services;

import characters.JapaneseChar;
import characters.LatinChar;
import characters.LatinSprite;
import characters.SpriteLocation;
import entities.*;
import enums.CharSide;
import enums.CharType;
import enums.PointerRangeType;
import lz.tables.CompressedDataTable;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static enums.CharType.MODE_F0;
import static services.Utils.bytesToHex;

public class JsonLoader {

    static String file = "config.json";

    private static String loadJson() {
        InputStream is =
                JsonLoader.class.getClassLoader().getResourceAsStream(file);
        String jsonTxt = null;
        try {
            jsonTxt = IOUtils.toString( is );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonTxt;
    }

    public static Config loadConfig() {

        Config config = new Config();

        JSONObject json = new JSONObject(loadJson());

        JSONObject c = json.getJSONObject("config");
        config.setRomInput(c.getString("rom-input"));
        config.setRomOutput(c.getString("rom-output"));
        config.setBpsPatchOutput(c.getString("bps-patch-output"));
        config.setFileDicoJap(c.getString("file.jap"));
        config.setFileDicoLatin(c.getString("file.latin"));
        config.setFileDicoNames(c.getString("file.names"));

        return config;
    }

    public static List<CodePatch> loadCodePatches() {
        List<CodePatch> codePatches = new ArrayList<>();

        JSONObject json = new JSONObject(loadJson());

        JSONArray array = json.getJSONArray("code-patches");
        for (Object o : array) {
            JSONObject next = (JSONObject) o;
            String code = next.getString("code");
            int offset = Integer.parseInt(next.getString("offset"), 16);
            if (next.has("file") && next.getBoolean("file")) {
                try {
                    System.out.println("Loading code patch "+"src/main/resources/data/" + next.getString("offset") + ".data");
                    byte[] bytes = Files.readAllBytes(new File("src/main/resources/data/" + next.getString("offset") + ".data").toPath());
                    code = bytesToHex(bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                code = next.getString("code");
            }
            if (code.contains("$")) {
                code = code.replaceAll("\\$0","90");
                code = code.replaceAll("\\$1","92");
            }
            CodePatch codePatch = new CodePatch(code, offset);
            if (next.has("debug")) codePatch.setDebug(next.getBoolean("debug"));

            codePatches.add(codePatch);
        }
        return codePatches;
    }

    public static List<JapaneseChar> loadJapanese() {
        List<JapaneseChar> chars = new ArrayList<>();
        JSONObject json = new JSONObject(loadJson());

        JSONArray array = json.getJSONArray("japanese");
        for (Object o : array) {
            JSONObject next = (JSONObject) o;
            JapaneseChar c = new JapaneseChar();
            String value = next.getString("value");
            if (next.has("code")) {
                c.setCode(next.getString("code"));
            }
            c.setValue(value);
            if (next.has("type")) {
                String type = next.getString("type");
                c.setType(CharType.valueOf(type));
            } else {
                c.setType(MODE_F0);
            }
            chars.add(c);
        }
        return chars;
    }
    
    public static PointerRange readPointerRange(JSONObject pointerObject) {
        PointerRange range = new PointerRange(
                Integer.parseInt(pointerObject.getString("start"),16),
                Integer.parseInt(pointerObject.getString("end"),16),
                Integer.parseInt(pointerObject.getString("shift"),16));
        if (pointerObject.has("type")) {
            String type = pointerObject.getString("type");
            if (type.equals("COUNTER")) {
                range.setType(PointerRangeType.COUNTER);
            }
            if (type.equals("MENU")) {
                range.setType(PointerRangeType.MENU);
            }
            if (type.equals("FIXED_LENGTH")) {
                range.setType(PointerRangeType.FIXED_LENGTH);
            }
        }
        return range;
    }

    public static List<CompressedDataTable> loadCompressedTables() {
        List<CompressedDataTable> list = new ArrayList<>();
        JSONObject json = new JSONObject(loadJson());
        JSONArray array = json.getJSONArray("compressed-data-tables");
        for (Object o : array) {
            JSONObject next = (JSONObject) o;
            CompressedDataTable table = new CompressedDataTable();

            table.setNewDataStart(Integer.parseInt(next.getString("new-data-start"),16));
            table.setNewDataShift(Integer.parseInt(next.getString("new-data-shift"),16));
            if (next.has("new-start")){
                table.setNewStart(Integer.parseInt(next.getString("new-start"),16));
            }
            JSONArray pointersArray = next.getJSONArray("pointers");
            for (Object value : pointersArray) {
                JSONObject pointerObject = (JSONObject) value;
                PointerRange range = readPointerRange(pointerObject);
                if (!next.has("skip") || !next.getBoolean("skip")) {
                    list.add(table);
                }
                table.addRange(range);
            }
            
            
        }
        return list;
    }

    public static List<PointerTable> loadTables() {
        List<PointerTable> tables = new ArrayList<>();

        JSONObject json = new JSONObject(loadJson());

        JSONArray array = json.getJSONArray("tables");
        for (Object o : array) {
            JSONObject next = (JSONObject) o;
            int id = next.getInt("id");
            PointerTable table = new PointerTable(id);
            table.setNewDataStart(Integer.parseInt(next.getString("new-data-start"),16));
            table.setNewDataShift(Integer.parseInt(next.getString("new-data-shift"),16));
            JSONArray pointersArray = next.getJSONArray("pointers");
            for (Object value : pointersArray) {
                JSONObject pointerObject = (JSONObject) value;
                PointerRange range = readPointerRange(pointerObject);
                if (range.getType()==PointerRangeType.FIXED_LENGTH) {
                    table.setStopAtNextPointer(true);
                    table.setKeepOldPointerValues(false);
                }
                /*if (pointerObject.has("options")) {
                    JSONObject options = (JSONObject)pointerObject.get("options");
                    Map<PointerOption, Object> map = getOptions(options);
                    range.setOptions(map);
                    if (options.has("target-options")) {
                        JSONObject targetOptions = (JSONObject)options.get("target-options");
                        Map<PointerOption, Object> mapTarget = getOptions(targetOptions);
                        range.setTargetOptions(mapTarget);
                    }
                }*/
                table.addPointerRange(range);
            }
            /*if (next.has("menu")) {
                table.setMenu(next.getBoolean("menu"));
            }*/
            if (next.has("counter")) {
                table.setCounter(next.getBoolean("counter"));
            }
            if (next.has("even-length")) {
                table.setEvenLength(next.getBoolean("even-length"));
            }
            if (next.has("overflow")) {
                JSONObject overflow = next.getJSONObject("overflow");
                Overflow ow = new Overflow();
                ow.setLimit(overflow.getInt("limit"));
                ow.setDataStart(overflow.getInt("data-start"));
                ow.setDataShift(overflow.getInt("data-shift"));
                table.setOverflow(ow);
            }
            if (!next.has("skip") || !next.getBoolean("skip")) {
                tables.add(table);
            }
        }
        return tables;
    }

    public static List<LatinChar> loadLatin() {
        List<LatinChar> latinChars = new ArrayList<>();

        JSONObject json = new JSONObject(loadJson());

        JSONArray array = json.getJSONArray("latin");
        for (Object o : array) {
            JSONObject next = (JSONObject) o;
            LatinChar c = new LatinChar();
            String value = next.getString("value");
            if (next.has("code")) {
                c.setCode(next.getString("code"));
            }
            c.setValue(value);
            if (next.has("sprite")) {
                JSONObject sprite = next.getJSONObject("sprite");
                if (sprite.has("image")) {
                    c.setSprite(new LatinSprite(sprite.getString("image")));
                } else {
                    c.setSprite(new LatinSprite(sprite.getString("image-top"), sprite.getString("image-bot")));
                }
            }
            if (next.has("location")) {
                JSONObject location = next.getJSONObject("location");
                c.setSpriteLocation(new SpriteLocation(Integer.parseInt(location.getString("offset"), 16), CharSide.valueOf(location.getString("side"))));
            }
            latinChars.add(c);
        }
        return latinChars;
    }

    public static List<String> loadTranslationFiles() {

        List<String> files = new ArrayList<>();
        JSONObject json = new JSONObject(loadJson());

        JSONArray c = json.getJSONArray("translations-files");
        for (Object o : c) {
            String next = (String) o;
            files.add(next);
        }

        return files;
    }

    public static List<NameTable> loadNames() {
        List<NameTable> tables = new ArrayList<>();
        JSONObject json = new JSONObject(loadJson());

        JSONArray array = json.getJSONArray("names");
        for (Object o : array) {
            JSONObject next = (JSONObject) o;
            NameTable table = new NameTable();
            String offset = next.getString("offset");
            String shift = next.getString("shift");
            String length = next.getString("length");
            table.setOffset(Integer.parseInt(offset,16));
            table.setShift(Integer.parseInt(shift,16));
            table.setLength(Integer.parseInt(length,16));
            JSONArray values = next.getJSONArray("values");
            String[] strings = values.toList().stream().map(Object::toString).toArray(String[]::new);
            table.setNames(strings);
            tables.add(table);
        }
        return tables;
    }
}
