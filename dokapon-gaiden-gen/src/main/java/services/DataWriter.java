package services;

import characters.Letter;
import entities.CodePatch;
import entities.NameTable;
import entities.PointerData;
import entities.PointerTable;
import lz.tables.CompressedDataPointer;
import lz.tables.CompressedDataTable;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataWriter {

    public static void saveData(String romOutput, byte[] data) {
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(romOutput);
            stream.write(data);
            stream.close();
        } catch (IOException ex) {
            Logger.getLogger(DataWriter.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException ex) {
                    Logger.getLogger(DataWriter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public static byte[] writeCodePatches(List<CodePatch> patchList, byte[] data, boolean debug) {
        System.out.println("Write Patches (debug="+debug+")");
        for (CodePatch cp:patchList) {
            if (cp.isDebug()==debug)
                //if (cp.getCode().length()!=5)
                cp.writePatch(data);
        }
        return data;
    }
    
    public static void generateKanaList() throws IOException {
        String s = "D:\\git\\dokapon-gaiden\\notes\\japanese-01.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(s))) {
            String line;
            int start = Integer.parseInt("0001",16);
            while ((line = br.readLine()) != null) {
                line = line.trim();
                for (char c:line.toCharArray()) {
                    System.out.println(Integer.toHexString(start)+"="+c);
                    start += Integer.parseInt("0100",16);
                }
            }
        }
        
        /*int value = Integer.parseInt("2500",16);
        char c = 'あ';
        for (int i=0;i<100;i++) {
            c = (char) (c+i);
            System.out.println(Integer.toHexString(value)+"="+c);
            value+=Integer.parseInt("100",16);
        }*/
    }
    
    public static void generateKanaJson() throws IOException {
        String s = "D:\\git\\dokapon-gaiden\\notes\\japanese-char-00.txt";
        String json = "\t{\n" +
                "      \"value\":\"%s\",\n" +
                "      \"code\":\"%s\"\n" +
                "    },\n";
        try (BufferedReader br = new BufferedReader(new FileReader(s))) {
            String line;
            
            while ((line = br.readLine()) != null) {
                
                //line = line.trim();
                String[] split = line.split("=");
                    String value = split[0].toUpperCase();
                    System.out.printf(json,split[1],value);
                
            }
        }
        
        
    }

    public static byte[] writeEnglish(PointerTable table, byte[] data) {
        System.out.println("Write English for table "+table.getId());
        for (PointerData p : table.getDataEng()) {

            //int offset = p.getOffset();
            
            /*if ((offset != Integer.parseInt("385c0",16)
            && (offset != Integer.parseInt("385c0",16)
            ))) {
                continue;
            }*/
            
            int offsetMenuData = p.getOffsetMenuData();
            int value = p.getValue();
            int offsetData = p.getOffsetData();
            String[] menuData = p.getMenuData();

            if (menuData==null) {
                if (!table.getKeepOldPointerValues()) {
                    for (Integer offset : p.getOffsets()) {
                        data[offset] = (byte) (value % 256);
                        data[offset + 1] = (byte) (value / 256);
                    }
                }
                for (String s : p.getData()) {
                    String[] bytes = s.split("(?<=\\G..)");
                    for (String b:bytes) {
                        int a = Integer.parseInt(b, 16);
                        data[offsetData++] = (byte) a;
                    }
                    /*int a = Integer.parseInt(s.substring(0, 2), 16);
                    int b = Integer.parseInt(s.substring(2, 4), 16);
                    data[offsetData++] = (byte) a;
                    data[offsetData++] = (byte) b;*/
                }
            } else {

                //System.out.println();
                //if (offset == Integer.parseInt("207f1",16)) {

                for (Integer offset : p.getOffsets()) {
                    data[offset] = (byte) (value % 256);
                    data[offset + 1] = (byte) (value / 256);
                }
                
                //}
                for (String s : p.getData()) {
                    int a = Integer.parseInt(s.substring(0, 2), 16);
                    int b = Integer.parseInt(s.substring(2, 4), 16);
                    data[offsetData++] = (byte) a;
                    data[offsetData++] = (byte) b;
                }
                int menuByte = 0;
                for (String s : menuData) {
                    int a = Integer.parseInt(s.substring(0, 2), 16);
                    data[offsetMenuData+(menuByte++)] = (byte) a;
                }
            }
            System.out.println("Pointer : "+p);
        }
        System.out.println("Last pointer : "+table.getDataEng().get(table.getDataEng().size()-1));
        return data;
    }

    public static byte[] writeTable(CompressedDataTable table, byte[] data) {
        System.out.println("Write CompressedDataTable");
        for (CompressedDataPointer p : table.getNewPointers()) {

            int offset = p.getOffset();
            int value = p.getValue();
            int offsetData = p.getOffsetData();

            data[offset] = (byte) (value % 256);
            data[offset + 1] = (byte) (value / 256);
                
            for (byte b : p.getData()) {
                data[offsetData++] = b;
            }
        }
        return data;
    }

    public static void writeNames(List<NameTable> names, Map<String, List<Letter>> syllables, byte[] data) {
        for (NameTable table:names) {
            int offset = table.getOffset();
            int shift = table.getShift();
            int length = table.getLength();
            for (String name:table.getNames()) {
                //System.out.printf("%s : %s\n", Integer.toHexString(offset), name);
                int count = 0;
                for (Letter letter : syllables.get(name)) {
                    byte[] codeBytes = letter.getCodeBytes();
                    for (byte b:codeBytes) {
                        data[offset+(count++)]=b;
                    }
                }
                while (count<length*2) {
                    data[offset+(count++)]=0;
                }
                offset+=shift;
            }
        }
    }
}
