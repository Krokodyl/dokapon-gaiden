package services;

import entities.Dictionnary;
import entities.PointerData;
import entities.PointerRange;
import entities.PointerTable;
import enums.CharType;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

import static services.Constants.*;
import static services.DataReader.*;
import static services.Utils.*;

public class TablePrinter {


    public void generateTranslationFile2(PointerTable table, byte[] data, Dictionnary japanese, String output) throws FileNotFoundException, UnsupportedEncodingException {

        PrintWriter writer = new PrintWriter(output, "UTF-8");
        for (PointerData p : table.getDataJap()) {

            //int offset = p.getOffset();
            int value = p.getValue();
            int offsetData = p.getOffsetData();
            byte[] bytes = hexStringToByteArray(p.getData());

            String jpn = getJapaneseFromBytes(bytes, japanese);
            String eng = "";

            for (Integer offset : p.getOffsets()) {
                writer.println(TRANSLATION_KEY_OFFSET+"="+toHexString(offset, 6));
            }
            writer.println(TRANSLATION_KEY_VALUE+"="+toHexString(value, 4));
            writer.println(TRANSLATION_KEY_OFFSETDATA+"="+toHexString(offsetData, 6));

            if (p.getMenuData()!=null) {
                byte[] mbytes = hexStringToByteArray(p.getMenuData());
                writer.println(TRANSLATION_KEY_MENUDATA+"="+bytesToHex(mbytes));
            }
            
            writer.println(TRANSLATION_KEY_DATA+"="+bytesToHex(bytes));
            writer.println(TRANSLATION_KEY_JPN +"="+jpn);
            writer.println(TRANSLATION_KEY_ENG+"="+eng);
            writer.println();
            writer.flush();
        }


    }
    
    public void generateTranslationFile(PointerTable table, byte[] data, Dictionnary japanese, String output) throws FileNotFoundException, UnsupportedEncodingException {
        
        List<PointerRange> ranges = table.getRanges();
        int rangeId = 1;
        for (PointerRange range : ranges) {
            PrintWriter writer = new PrintWriter(output.replace("range", Integer.toString(rangeId++)), "UTF-8");
            int start = range.getStart();
            int end = range.getEnd();
            int shift = range.getShift();
            for (int offset=start;offset<=end;offset=offset+2) {
                int byte1 = data[offset] & 0xFF;
                int byte2 = data[offset+1] & 0xFF;
                int value = byte2 * 256 + byte1;
                byte[] bytes = readUntilEndOfLine(data, value + shift);
                if (table.isStopAtNextPointer()) {
                    bytes = readUntil(data, value + shift, END_OF_LINE_00_CHARACTER_BYTE);
                    if (offset+2<end) {
                        int byte3 = data[offset+2] & 0xFF;
                        int byte4 = data[offset+3] & 0xFF;
                        int stopOffset = byte4 * 256 + byte3;
                        bytes = readUntilOffset(data, value + shift, stopOffset + shift);
                    }
                }

                String jpn = getJapaneseFromBytes(bytes, japanese);
                String eng = "";
                
                        
                writer.println(TRANSLATION_KEY_OFFSET+"="+toHexString(offset, 6));
                writer.println(TRANSLATION_KEY_VALUE+"="+toHexString(value, 4));
                writer.println(TRANSLATION_KEY_OFFSETDATA+"="+toHexString(value + shift, 6));
                
                writer.println(TRANSLATION_KEY_DATA+"="+bytesToHex(bytes));
                writer.println(TRANSLATION_KEY_JPN +"="+jpn);
                writer.println(TRANSLATION_KEY_ENG+"="+eng);
                writer.println();
            }
            writer.close();
        }
    }

    public void generateReferenceFile(PointerTable table,String name, String output) throws IOException {
        PrintWriter writer = new PrintWriter(output, "UTF-8");
        System.out.println("generateReferenceFile table from translation File : "+name);
        BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(Translator.class.getClassLoader().getResourceAsStream(name)), StandardCharsets.UTF_8));
        PointerData p = new PointerData();
        String line = br.readLine();
        while (line != null) {
            if (line.contains(Constants.TRANSLATION_KEY_VALUE_SEPARATOR)) {
                String[] split = line.split(Constants.TRANSLATION_KEY_VALUE_SEPARATOR);
                if (split.length>0) {
                    if (split[0].equals(Constants.TRANSLATION_KEY_OFFSET)) {
                        p.addOffset(Integer.parseInt(split[1], 16));
                    }
                    if (split[0].equals(Constants.TRANSLATION_KEY_OFFSETDATA)) {
                        p.setOffsetData(Integer.parseInt(split[1], 16));
                    }
                    if (split[0].equals(Constants.TRANSLATION_KEY_MENUDATA)) {
                        if (split.length>1) p.setMenuData(split[1].split(" "));
                    }
                    if (split[0].equals(Constants.TRANSLATION_KEY_VALUE)) {
                        p.setValue(Integer.parseInt(split[1], 16));
                    }
                    if (split[0].equals(Constants.TRANSLATION_KEY_DATA)) {
                        if (split.length>1) p.setData(split[1].split(" "));
                    }

                    //System.out.println(TRANSLATION_KEY_JPN +"="+jpn);
                    //System.out.println(TRANSLATION_KEY_ENG+"="+eng);
                    /*String[] splitJpn = jpn.split("(?<=\\G.{8})");
                    String[] splitEng = eng.split("(?<=\\G.{8})");
                    for (int k=0;k<splitJpn.length;k++) {
                        writer.println(splitEng[k]+"="+splitJpn[k]);
                    }*/
                    
                }
            } else {
                if (p.getData().length>0) {
                    table.addPointerDataJap(p);
                    p = new PointerData();
                }
            }
            line = br.readLine();
        }


        
        
        writer.close();
    }

    public void generateMenuTable(byte[] data, Dictionnary japanese) {
        int i = Integer.parseInt("2028D",16);
        int count = 0;
        while (i <= Integer.parseInt("209DF",16)) {
            byte cursor = data[i];
            if (cursor == 2 && data[i+5]==0) {
                int a = (data[i+6] & 0xFF);
                int b = (data[i+7] & 0xFF);
                int offsetData = Integer.parseInt("18000",16) + (b * 256 + a);
                byte[] bytes = readUntilEndOfLine(data, offsetData);
                count++;
                if (bytes[0]==MODE_F0_BYTE || bytes[0]==MODE_F1_BYTE) {

                System.out.println(TRANSLATION_KEY_OFFSET+"="+toHexString(i,5));
                System.out.println(TRANSLATION_KEY_VALUE+"="+toHexString(b)+toHexString(a));
                System.out.println(TRANSLATION_KEY_OFFSETDATA+"="+toHexString(offsetData,5));
                System.out.println(TRANSLATION_KEY_MENUDATA+"="
                        +toHexString(data[i])+" "
                        +toHexString(data[i+1])+" "
                        +toHexString(data[i+2])+" "
                        +toHexString(data[i+3])+" "
                        +toHexString(data[i+4])+" "
                        +toHexString(data[i+5])+" "
                        );

                String jpn = getJapaneseFromBytes(bytes, japanese);
                String eng = "";

                System.out.println(TRANSLATION_KEY_DATA+"="+bytesToHex(bytes));
                System.out.println(TRANSLATION_KEY_JPN +"="+jpn);
                System.out.println(TRANSLATION_KEY_ENG+"=");
                System.out.println();


                    //System.out.println(toHexString(i,5)+"  "+toHexString(a)+" "+toHexString(b)+"  "+toHexString(offsetData,5));
                    //System.out.println(Arrays.toString(strings));
                }
            }
            i++;
            /*int a = (data[i] & 0xFF);
            int b = (data[i + 1] & 0xFF);
            int offsetData = Integer.parseInt("18000",16) + (b * 256 + a);*/
            //String[] strings = DataReader.readPointerData(offsetData, data);

        }
        System.out.println(count);
    }

    public String getJapaneseFromBytes(byte[] data, Dictionnary japanese) {
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
                res += "{"+code+"}"+"{"+param+"}";
            }
            /*if (code.equals("0D80")) {
                a = data[k++];
                b = data[k++];
                byte c = data[k++];
                String param = Utils.toHexString(a)+Utils.toHexString(b)+Utils.toHexString(c);
                res += "{"+code+"}"+"{"+param+"}";
            } else if (code.equals("0180")) {
                a = data[k++];
                b = data[k++];
                String param = Utils.toHexString(a)+Utils.toHexString(b);
                res += "{"+code+"}"+"{"+param+"}";
            }*/
            else {
                String s = japanese.getJapanese(code);
                if (s==null) s="{"+code+"}";
                res += s;
            }
            
        }
        return res;
    }
}
