package lz;

import com.google.common.primitives.Bytes;
import services.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LzCompressor {
    
    // input
    byte[] data;
    
    final int WINDOW_SIZE = 127;
    final int BUFFER_SIZE = 32;
    final int BUFFER_MIN_SIZE = 3;
    
    
    
    
    public byte[] compress(byte[] data) throws IOException {
        List<Command> commandList = new ArrayList<>();
        int bufferStart=0, bufferEnd=BUFFER_SIZE-1;
        int windowStart = 0;

        byte[] bytes = new byte[1];
        bytes[0] = data[bufferStart];
        commandList.add(new WriteCommand(bytes));
        bufferStart++;
        
        bufferEnd = bufferStart+BUFFER_SIZE;
        windowStart = bufferEnd-1-WINDOW_SIZE;
        if (windowStart<0) windowStart=0;
        int windowEnd = windowStart + WINDOW_SIZE;
        if (windowEnd>=bufferEnd-1) windowEnd=bufferEnd-1;
        byte[] window = getWindow(data, windowStart, windowEnd);
        
        while (bufferStart<data.length){
            boolean found = false;
            boolean endSearch = false;
            int index = -1;
            while (!found && !endSearch) {
                byte[] buffer = getBuffer(data, bufferStart, bufferEnd);
                windowStart = bufferEnd - 1 - WINDOW_SIZE;
                if (windowStart < 0) windowStart = 0;
                windowEnd = windowStart + WINDOW_SIZE;
                if (windowEnd >= bufferEnd - 1) windowEnd = bufferEnd - 1;
                window = getWindow(data, windowStart, windowEnd);
                index = Bytes.indexOf(window, buffer);
                if (index < 0) {
                    bufferEnd--;
                    if (bufferEnd - bufferStart < BUFFER_MIN_SIZE) endSearch = true;
                } else found = true;
            }
            if (!found) {
                bytes = new byte[1];
                bytes[0] = data[bufferStart];
                Command command = commandList.get(commandList.size()-1);
                boolean append = false;
                if (command instanceof WriteCommand) {
                    WriteCommand writeCommand = (WriteCommand) command;
                    if (writeCommand.getData().length<126) writeCommand.appendData(bytes);
                    else commandList.add(new WriteCommand(bytes));
                }
                else commandList.add(new WriteCommand(bytes));
                bufferStart++;
            } else {
                commandList.add(new RepeatCommand(bufferStart - (windowStart + index), bufferEnd - bufferStart));
                bufferStart = bufferEnd;
            }
            bufferEnd = bufferStart + BUFFER_SIZE;
            if (bufferEnd>data.length) bufferEnd=data.length;
        }
        commandList.add(new EndCommand());
        for (Command command : commandList) {
            //System.out.println(command);
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for (Command command : commandList) {
            byte[] commandBytes = command.getBytes();
            //System.out.println(Utils.bytesToHex(commandBytes));
            out.write(commandBytes);
            out.flush();
        }
        System.out.printf("LzCompress %s bytes (%s)\n", data.length, Integer.toHexString(data.length));
        System.out.printf("Compressed size : %s (%s)\n",out.toByteArray().length, Integer.toHexString(out.toByteArray().length));
        return out.toByteArray();

    }

    private byte[] getBuffer(byte[] data, int bufferStart, int bufferEnd) {
        return Arrays.copyOfRange(data, bufferStart, bufferEnd);
    }

    private byte[] getWindow(byte[] data, int windowStart, int windowEnd) {
        return Arrays.copyOfRange(data, windowStart, windowEnd);
    }

    public static void main(String[] args) throws IOException {
        byte[] data = new byte[0];
        try {
            data = Files.readAllBytes(new File("src/main/resources/gen/uncompressed.data").toPath());
        } catch (IOException ex) {
            Logger.getLogger(DokaponGaiden.class.getName()).log(Level.SEVERE, null, ex);
        }
        LzCompressor compressor = new LzCompressor();
        byte[] compressed = compressor.compress(data);
        DataWriter.saveData("D:\\git\\dokapon-gaiden\\dokapon-gaiden-gen\\src\\main\\resources\\gen\\lz\\"+"comp.data",compressed);
        
        Decompressor decompressor = new Decompressor();
        byte[] decompressed = decompressor.decompress(compressed);
        DataWriter.saveData("src/main/resources/gen/lz/decomp.data", decompressed);
        /*Decompressor decompressor = new Decompressor(data, "D63E7");
        decompressor.decompressData();
        decompressor.printOutput();
        DataWriter.saveData("src/main/resources/gen/uncompressed.data", decompressor.getDecompressedData());*/
        //Compressor compressor = new Compressor(decompressor.getDecompressedData());
        //compressor.compressData();
        //DataWriter.saveData("D:\\git\\dokapon-gaiden\\dokapon-gaiden-gen\\src\\main\\resources\\data\\"+"118050.data",compressor.getCompressedData());
        //compressor.printOutput();

    }
    
}
