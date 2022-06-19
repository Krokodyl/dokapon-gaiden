package lz;

import services.DokaponGaiden;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LzDecompressor {

    int offset = 0;
    ByteArrayOutputStream compressedData = new ByteArrayOutputStream();
    ByteArrayOutputStream decompressedData = new ByteArrayOutputStream();
    
    final static int REPEAT_MASK = 0x80;
    final static int REPEAT_LENGTH_MASK = 0x7C;
    final static int COPY_MASK = 0x7F;

    public void decompressData(byte[] input, String start) {
        decompressData(input, Integer.parseInt(start,16));
    }
    
    public void decompressData(byte[] input, int start) {
        decompressedData = new ByteArrayOutputStream();
        compressedData = new ByteArrayOutputStream();
        int index = start;
        this.offset = start;
        boolean end = false;
        while (!end) {
            byte cursor = input[index++];
            compressedData.write(cursor);
            if (cursor==0x00) {
                end = true;
            } else {
                if ((cursor & REPEAT_MASK) == REPEAT_MASK) {
                    byte b = input[index++];
                    compressedData.write(b);
                    int shift = getShift(cursor, b);
                    int length = ((cursor & REPEAT_LENGTH_MASK)>>2)+1;
                    byte[] output = decompressedData.toByteArray();
                    int repeatStart = output.length-shift-1;
                    if (repeatStart<0) repeatStart=0;
                    int repeatIndex = repeatStart;
                    while (length>=0) {
                        byte data = output[repeatIndex++];
                        if (repeatIndex>output.length-1) repeatIndex=repeatStart;
                        decompressedData.write(data);
                        length--;
                    }
                } else {
                    int copyLenth = cursor & COPY_MASK;
                    while (copyLenth>0) {
                        byte data = input[index++];
                        compressedData.write(data);
                        decompressedData.write(data);
                        copyLenth--;
                    }
                }
            }
        }
    }
    
    public byte[] getCompressedData() {
        return compressedData.toByteArray();
    }
    
    public byte[] getDecompressedData(){
        return decompressedData.toByteArray();
    }
    
    public int getCompressedDataSize(){
        return getCompressedData().length;
    }
    
    public int getDecompressedDataSize(){
        return getDecompressedData().length;
    }
    
    public void printStats() {
        int compressedDataSize = getCompressedDataSize();
        int decompressedDataSize = getDecompressedDataSize();
        System.out.printf("Decompressed from %s to %s\n", Integer.toHexString(offset), Integer.toHexString(offset+ compressedDataSize));
        System.out.printf("Compressed size : %s (%s)\n",compressedDataSize, Integer.toHexString(compressedDataSize));
        System.out.printf("Decompressed size : %s (%s)\n", decompressedDataSize, Integer.toHexString(decompressedDataSize));
    }

    public int getShift(byte left, byte right) {
        int shift = right & 0xFF;
        shift += (left & 0x03) * (0x100);
        return shift;
    }
   
}
