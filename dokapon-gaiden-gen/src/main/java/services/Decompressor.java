package services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Decompressor {

    private int start;
    private byte[] input;
    
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    int repeatMask = 0x80;
    int repeatLengthMask = 0x7C;
    int copyMask = 0x7F;
    
    static boolean verbose = true;

    public Decompressor() {
    }
    
    public Decompressor(byte[] data, int start) {
        this.start = start;
        this.input = data;
    }

    public Decompressor(byte[] data, String start) {
        this.start = Integer.parseInt(start,16);
        this.input = data;
    }
    
    public byte[] decompress(byte[] data) {
        this.start = 0;
        this.input = data;
        decompressData();
        return outputStream.toByteArray();
    }
    
    public void decompressData() {
        int index = start;
        boolean end = false;
        while (!end) {
            byte cursor = input[index++];
            if (cursor==0x00) {
                end = true;
            } else {
                if ((cursor & repeatMask) == repeatMask) {
                    int shift = getShift(cursor, input[index++]);
                    int length = ((cursor & repeatLengthMask)>>2)+1;
                    byte[] output = outputStream.toByteArray();
                    int repeatStart = output.length-shift-1;
                    if (repeatStart<0) repeatStart=0;
                    int repeatIndex = repeatStart;
                    while (length>=0) {
                        byte data = output[repeatIndex++];
                        if (repeatIndex>output.length-1) repeatIndex=repeatStart;
                        outputStream.write(data);
                        length--;
                    }
                } else {
                    int copyLenth = cursor & copyMask;
                    while (copyLenth>0) {
                        byte data = input[index++];
                        outputStream.write(data);
                        copyLenth--;
                    }
                }
            }
        }
        System.out.printf("Decompressed from %s to %s\n", Integer.toHexString(start), Integer.toHexString(index));
        System.out.printf("Compressed size : %s (%s)\n",(index-start), Integer.toHexString((index-start)));
        System.out.printf("Decompressed size : %s (%s)\n", (outputStream.toByteArray().length), Integer.toHexString(outputStream.toByteArray().length));
    }
    
    public byte[] getDecompressedData(){
        return outputStream.toByteArray();
    }

    public void printOutput() throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter("D:/git/dokapon-gaiden/test/output.txt", "UTF-8");

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
    
    public static int getShift(byte left, byte right) {
        int shift = right & 0xFF;
        shift += (left & 0x03) * (0x100);
        return shift;
    }
    
    
}
