package services;

import com.google.common.primitives.Bytes;
import lz.DataSection;

import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;

public class Compressor {

    private final byte[] input;

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    static boolean verbose = true;

    public Compressor(byte[] input) {
        this.input = input;
    }

    public Compressor(String input) throws IOException {
        this.input = Files.readAllBytes(new File(input).toPath());
    }

    public void compressData(byte[] data) {
        int window = 32;
        DataSection section = new DataSection(0, data.length);
        
    }
    
    public void compressSection(DataSection section, byte[] data, int windowSize) {
        int searchFrom = section.getStartOffset()-127;
        if (searchFrom<0) searchFrom=0;
        byte[] pattern = Arrays.copyOfRange(data, section.getEndOffset()-windowSize, section.getEndOffset());
        byte[] target = Arrays.copyOfRange(data, searchFrom, searchFrom + 127 + windowSize - 1);
        int index = Bytes.indexOf(pattern, target);
        if (index>-1) {
            
        }
    }

    public void compressData() {
        int index = 0;
        while (index<input.length) {
            int length = input.length-index;
            if (length>127) length=127;
            int flagByte = 0x7F & (length & 0xFF);
            outputStream.write(flagByte);
            while (length>0) {
                outputStream.write(input[index++]);
                length--;
            }
        }
        outputStream.write(0);
    }
    
    public byte[] getCompressedData(){
        return outputStream.toByteArray();
    }

    public void printOutput() throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter("D:/git/dokapon-gaiden/test/compressed.txt", "UTF-8");

        byte[] bytes = outputStream.toByteArray();

        int offset = 0;
        for (int i=0;i<bytes.length;i++) {
            if (i%16==0) {
                if (verbose) System.out.println();
                writer.println();
                if (verbose) System.out.print(Utils.toHexString(offset,4)+"   ");
                offset+=16;
            }
            writer.print(Utils.toHexString(bytes[i] & 0xFF,2).toLowerCase());
            if (verbose) System.out.print(Utils.toHexString(bytes[i] & 0xFF,2)+" ");
        }
        writer.close();
    }
}
