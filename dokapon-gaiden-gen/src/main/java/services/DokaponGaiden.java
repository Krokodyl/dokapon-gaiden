package services;

import characters.JapaneseChar;
import characters.Letter;
import entities.*;
import lz.LzDecompressor;
import lz.tables.CompressedDataTable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DokaponGaiden {


    static byte[] data;
    static Config config;

    public static void main(String[] args) throws IOException {

        config = JsonLoader.loadConfig();
        
        try {
            data = Files.readAllBytes(new File(config.getRomInput()).toPath());
        } catch (IOException ex) {
            Logger.getLogger(DokaponGaiden.class.getName()).log(Level.SEVERE, null, ex);
        }

        LzDecompressor decompressor = new LzDecompressor();
        decompressor.decompressData(data, "cf8f8");
        decompressor.printStats();
        DataWriter.saveData("src/main/resources/gen/cf8f8.data", decompressor.getDecompressedData());
        //Decompressor decompressor = new Decompressor(data, "28014");
        //decompressor.decompressData();
        //decompressor.printOutput();
        
        LatinLoader latinLoader = new LatinLoader();
        latinLoader.loadLatin();
        Translator translator = new Translator(latinLoader);

        ImageReader imageReader = new ImageReader();
        ImageReader.extractMenus(data, Integer.parseInt("1352C",16), Integer.parseInt("1360C",16));
        ImageReader.extractMenus(data, Integer.parseInt("13612",16), Integer.parseInt("13631",16));
        ImageReader.extractMenus(data, Integer.parseInt("13642",16), Integer.parseInt("13801",16));
        ImageReader.extractMenus(data, Integer.parseInt("13804",16), Integer.parseInt("13833",16));
        ImageReader.extractMenus(data, Integer.parseInt("1383E",16), Integer.parseInt("1391D",16));
        ImageReader.extractMenus(data, Integer.parseInt("1395E",16), Integer.parseInt("13ABD",16));

        ImageReader.generateLatinMenus();
                
        List<NameTable> names = JsonLoader.loadNames();
        Map<String, List<Letter>> syllables = imageReader.generateSyllables(names, latinLoader);
        DataWriter.writeNames(names, syllables, data);
        imageReader.writeLatin(data);

        //imageReader.turnImageIntoTiles(10*8, "5A16","/sprites/title-screen-dokapon.png","title-screen-dokapon.png","title-screen-dokapon.data");
        //imageReader.turnImageIntoTiles(13*8, "9D3A", "/sprites/title-screen-gaiden.png","title-screen-gaiden.png","title-screen-gaiden.data");
        //imageReader.turnImageIntoTiles(00*8,"E03A","/sprites/title-screen-fiery.png","title-screen-fiery.png","title-screen-fiery.data");

        imageReader.generateSpritePasswordGenScreen();
        imageReader.generateSpriteMoveMenu();
        imageReader.generateSpriteEnd();
        imageReader.generateSpriteTitleScreen();
        imageReader.generateTitleScreenTilesMap();
        imageReader.generateSpriteMapSelectScreen();
        
        //imageReader.generateSpriteDuelCard();
        //imageReader.compressSpriteDuelCard();
        /*imageReader.generateSpriteCharacterNameScreen();
        imageReader.generateSpritePasswordScreen();
        imageReader.generatePasswordScreenTilesMap();
        
        imageReader.generateSpriteMoveMenu();

        imageReader.generateSpriteTitleScreen();
        
        imageReader.generateSpriteMenuSelection();
        imageReader.generateInputScreenTilesMap();
        imageReader.generateInputMoreScreenTilesMap();
        imageReader.generateSpriteCharacterSelectScreen();
        imageReader.generateCharacterSelectScreenTilesMap();
        imageReader.generateCharacterSelectAuditionScreenTilesMap();
        imageReader.generateSpriteRulesScreen();*/
        
        
        /*imageReader.generateTitleScreenTilesMap();
        imageReader.generateTitleScreenDokaponTilesMap();
        
        imageReader.generateSpriteRulesScreen();
        imageReader.generateRulesScreenTilesMap();
        imageReader.generateSpriteInputScreen();
        
        
        imageReader.generateCharacterSelectAuditionScreenTilesMap();
        imageReader.generatePlayerOrderScreenTilesMap();
        imageReader.generateSpriteMapSelectScreen();
        imageReader.generateMapScreenTilesMap();
        */
        
        
        //imageReader.loadFontImage2bppSquelched("src/main/resources/images/latin/char-000.png", new Palette2bpp("/palettes/palette-latin.png"), FontColor.MAP_2BPP_COLOR_02);
        //System.out.println(image2bpp);

        DataWriter.writeCodePatches(JsonLoader.loadCodePatches(), data, false);
        
        
        /*DataWriter.saveData("src/main/resources/gen/uncompressed.data", decompressor.getDecompressedData());
        Compressor compressor = new Compressor(decompressor.getDecompressedData());
        compressor.compressData();*/
        //DataWriter.saveData("D:\\git\\dokapon-gaiden\\dokapon-gaiden-gen\\src\\main\\resources\\data\\"+"118050.data",compressor.getCompressedData());
        //compressor.printOutput();
        
        /*decompressor = new Decompressor(compressor.getCompressedData(), "0");
        decompressor.decompressData();
        decompressor.printOutput();*/

        List<JapaneseChar> japaneseChars = JsonLoader.loadJapanese();
        Dictionnary japanese = new Dictionnary(japaneseChars);
        
        List<PointerTable> tables = JsonLoader.loadTables();
        
        for (String s:JsonLoader.loadTranslationFiles()) {
            try {
                translator.loadTranslationFile(s);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (CompressedDataTable table : JsonLoader.loadCompressedTables()) {
            CompressedDataTable compressedDataTable = DataReader.readTable(table, data);
            DataReader.generateNewTable(compressedDataTable);
            DataWriter.writeTable(compressedDataTable, data);
        }


        List<PointerTable> subTables = new ArrayList<>();
                
        for (PointerTable table:tables) {
            PointerTable subTable = DataReader.readTable(table, data);
            subTables.add(subTable);
        }

        for (PointerTable table:tables) {
            System.out.println(String.format("---------------- Table %s ---------------------",table.getId()));
            //new TablePrinter().generateTranslationFile2(table, data, japanese, "src/main/resources/gen/Table "+table.getId()+".txt");
            System.out.println("--------------------------------------");
        }

        PointerTable cleanTable = new PointerTable(200);
        Map<Integer, PointerData> subMap = new HashMap<>();
        for (PointerTable table:subTables) {
            for (PointerData pointerData : table.getDataJap()) {
                if (!subMap.containsKey(pointerData.getValue())) {
                    subMap.put(pointerData.getValue(), pointerData);
                    cleanTable.addPointerDataJap(pointerData);
                }
            }
        }
        
        System.out.println(String.format("---------------- Table %s ---------------------",cleanTable.getId()));
        //new TablePrinter().generateTranslationFile2(cleanTable, data, japanese, "src/main/resources/gen/SubTable "+cleanTable.getId()+".txt");
        System.out.println("--------------------------------------");

        for (PointerTable table:tables) {
            DataReader.generateEnglish(translator, table, data);
        }

        for (PointerTable table:tables) {
            DataWriter.writeEnglish(table, data);
        }

        
        
        //DataWriter.generateKanaJson();

        System.out.println("Saving rom-output...");
        DataWriter.saveData(config.getRomOutput(), data);
        System.out.println("Process complete");
    }
    
}
