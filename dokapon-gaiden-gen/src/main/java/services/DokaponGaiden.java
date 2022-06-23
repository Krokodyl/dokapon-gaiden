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
        decompressor.decompressData(data, "d399e");
        decompressor.printStats();
        DataWriter.saveData("src/main/resources/gen/d399e.data", decompressor.getDecompressedData());
        //Decompressor decompressor = new Decompressor(data, "28014");
        //decompressor.decompressData();
        //decompressor.printOutput();
        
        LatinLoader latinLoader = new LatinLoader();
        latinLoader.loadLatin();
        Translator translator = new Translator(latinLoader);

        ImageReader imageReader = new ImageReader();
        List<NameTable> names = JsonLoader.loadNames();
        Map<String, List<Letter>> syllables = imageReader.generateSyllables(names, latinLoader);
        DataWriter.writeNames(names, syllables, data);
        imageReader.writeLatin(data);
        
        imageReader.generateSpriteRulesScreen();
        imageReader.generateRulesScreenTilesMap();
        imageReader.generateSpriteInputScreen();
        imageReader.generateInputScreenTilesMap();
        imageReader.generateSpriteCharacterSelectScreen();
        imageReader.generateCharacterSelectScreenTilesMap();
        
        //imageReader.generateSpriteTitleScreen();
        //imageReader.generateTitleScreenTilesMap();
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

        for (PointerTable table:subTables) {
            System.out.println(String.format("---------------- Table %s ---------------------",table.getId()));
            //new TablePrinter().generateTranslationFile2(table, data, japanese, "src/main/resources/gen/SubTable "+table.getId()+".txt");
            System.out.println("--------------------------------------");
        }

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
