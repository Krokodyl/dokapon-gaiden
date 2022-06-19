package services;

import entities.FontColor;
import entities.Palette;
import entities.Palette2bpp;
import entities.Palette4bpp;
import lz.LzCompressor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ImageReader {

    BufferedImage image;

    byte[] imageData;

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    public ImageReader() {

    }

    public byte[] getBytes() {
        return outputStream.toByteArray();
    }

    public void generateSpriteTitleScreen() throws IOException {
        generateSpriteDataFromImage(
                "src/main/resources/sprites/title-screen.png",
                "src/main/resources/gen/sprite-uncompressed.data",
                new Palette4bpp("/palettes/palette-title-screen.png"),
                4
        );
        String uncomp = "src/main/resources/gen/sprite-uncompressed.data";
        String outputFile = "src/main/resources/data/110020.data";
        Compressor compressor = new Compressor(uncomp);
        compressor.compressData();
        DataWriter.saveData(outputFile, compressor.getCompressedData());
        //CompressedSpriteManager compressedSpriteManager = new CompressedSpriteManager(null);
        //compressedSpriteManager.compressCopyFile(uncomp, Header.LATIN_SPRITES_HEADER, outputFile);
        //compressedSpriteManager.decompressFile(outputFile, "src/main/resources/data/decomp-1B8000.data");
    }

    public void generateSpriteRulesScreen() throws IOException {
        generateSpriteDataFromImage(
                "src/main/resources/sprites/rules-screen.png",
                "src/main/resources/gen/sprite-uncompressed.data",
                new Palette4bpp("/palettes/palette-rules-screen.png"),
                4
        );
        String uncomp = "src/main/resources/gen/sprite-uncompressed.data";
        String outputFile = "src/main/resources/data/28014.data";
        LzCompressor compressor = new LzCompressor();
        byte[] compressed = compressor.compress(Files.readAllBytes(new File(uncomp).toPath()));
        DataWriter.saveData(outputFile, compressed);
        //CompressedSpriteManager compressedSpriteManager = new CompressedSpriteManager(null);
        //compressedSpriteManager.compressCopyFile(uncomp, Header.LATIN_SPRITES_HEADER, outputFile);
        //compressedSpriteManager.decompressFile(outputFile, "src/main/resources/data/decomp-1B8000.data");
    }

    public void generateRulesScreenTilesMap() throws IOException {
        String uncomp = "src/main/resources/tiles-maps/rules-screen-01.data";
        String outputFile = "src/main/resources/data/2ACC4.data";
        LzCompressor compressor = new LzCompressor();
        byte[] compressed = compressor.compress(Files.readAllBytes(new File(uncomp).toPath()));
        DataWriter.saveData(outputFile, compressed);
        uncomp = "src/main/resources/tiles-maps/rules-screen-02.data";
        outputFile = "src/main/resources/data/2AF15.data";
        compressor = new LzCompressor();
        compressed = compressor.compress(Files.readAllBytes(new File(uncomp).toPath()));
        DataWriter.saveData(outputFile, compressed);
        uncomp = "src/main/resources/tiles-maps/rules-screen-03.data";
        outputFile = "src/main/resources/data/2B0D1.data";
        compressor = new LzCompressor();
        compressed = compressor.compress(Files.readAllBytes(new File(uncomp).toPath()));
        DataWriter.saveData(outputFile, compressed);
    }

    public void generateTitleScreenTilesMap() throws IOException {
        String uncomp = "src/main/resources/tiles-maps/title-screen.data";
        String outputFile = "src/main/resources/data/118050.data";
        Compressor compressor = new Compressor(uncomp);
        compressor.compressData();
        DataWriter.saveData(outputFile, compressor.getCompressedData());
    }

    private static String generateSpriteDataFromImage(String image, String output, Palette palette, int bpp) throws IOException {
        System.out.println("Generating Sprite Data from image "+image);
        ImageReader fontImageReader = new ImageReader();
        String s = "";
        if (bpp==2) s = fontImageReader.loadFontImage2bpp(image, palette);
        else s = fontImageReader.loadFontImage4bpp(image, palette);
        byte[] bytes = fontImageReader.getBytes();

        try (FileOutputStream fos = new FileOutputStream(output)) {
            fos.write(bytes);
            fos.close();
            //There is no more need for this line since you had created the instance of "fos" inside the try. And this will automatically close the OutputStream
        }

        return s;
    }

    public byte[] loadFontImage2bppSquelched(File file, Palette palette, FontColor fontColor) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //StringBuffer sb = new StringBuffer();
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {

        }
        int width = 0;
        boolean stop = false;
        int tileX = 0, tileY = 0, x = 0, y = 0;
        while (tileY++<image.getHeight()/8) {
            tileX=0;
            while (tileX++<image.getWidth()/8) {
                y=0;
                while (y++<8) {
                    x=0;
                    int encodedLine = 0;
                    while (x++<8) {
                        int pixelX = (tileX - 1) * 8 + (x - 1);
                        int rgb = image.getRGB(pixelX, ((tileY - 1) * 8 + (y - 1)));
                        if (rgb==0) {
                            stop = true;
                            break;
                        }
                        String color = Utils.getColorAsHex(rgb).toLowerCase();
                        FontColor pixelColor = palette.getFontColor(color);
                        int mask = pixelColor.getMask();
                        mask = mask >> (x-1);
                        encodedLine = encodedLine | mask;
                        if (fontColor==pixelColor) {
                            if (width< pixelX) {
                                width = pixelX;
                            }
                        }
                    }
                    if (stop) break;
                    int leftByte = encodedLine >> 8;
                    int rightByte = encodedLine & 0x00FF;
                    byteArrayOutputStream.write(leftByte);
                    //outputStream.write(rightByte);
                    //String hex = Utils.toHexString(leftByte, 2);
                    //sb.append(hex.replaceAll(" ",""));
                    //System.out.print(Utils.bytesToHex(byteArrayOutputStream.toByteArray()));
                }
                if (stop) break;
                //System.out.println();
            }
            if (stop) break;
        }
        byte[] squelch = squelch(byteArrayOutputStream.toByteArray(), width + 2);
        //System.out.println(Utils.bytesToHex(squelch));
        return squelch;
        //return sb.toString();
    }
    
    private byte[] squelch(byte[] bytes, int width) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        stream.write(width);
        stream.write(0);
        for (int k=0;k<8;k++) {
            stream.write(bytes[k+8]);
            stream.write(bytes[k]);
        }
        for (int k=0;k<7;k++) {
            stream.write(bytes[16+k+8]);
            stream.write(bytes[16+k]);
        }
        return stream.toByteArray();
    }

    public String loadFontImage2bpp(String file, Palette palette) {
        StringBuffer sb = new StringBuffer();
        try {
            image = ImageIO.read(new File(file));
        } catch (IOException e) {

        }
        boolean stop = false;
        int tileX = 0, tileY = 0, x = 0, y = 0;
        while (tileY++<image.getHeight()/8) {
            tileX=0;
            while (tileX++<image.getWidth()/8) {
                y=0;
                while (y++<8) {
                    x=0;
                    int encodedLine = 0;
                    while (x++<8) {
                        int rgb = image.getRGB(((tileX - 1) * 8 + (x - 1)), ((tileY - 1) * 8 + (y - 1)));
                        if (rgb==0) {
                            stop = true;
                            break;
                        }
                        String color = Utils.getColorAsHex(rgb).toLowerCase();
                        FontColor fontColor = palette.getFontColor(color);
                        int mask = fontColor.getMask();
                        mask = mask >> (x-1);
                        encodedLine = encodedLine | mask;
                    }
                    if (stop) break;
                    int leftByte = encodedLine >> 8;
                    int rightByte = encodedLine & 0x00FF;
                    outputStream.write(leftByte);
                    outputStream.write(rightByte);
                    String hex = Utils.toHexString(leftByte, 2)+" "+Utils.toHexString(rightByte,2);
                    sb.append(hex.replaceAll(" ",""));
                    //System.out.print(hex+" ");
                }
                if (stop) break;
                //System.out.println();
            }
            if (stop) break;
        }
        return sb.toString();
    }

    public String loadFontImage4bpp(String file, Palette palette) {
        StringBuffer sb = new StringBuffer();
        byte[] output = new byte[0];
        int indexOutput = 0;
        try {
            image = ImageIO.read(new File(file));
            output = new byte[image.getHeight()*image.getWidth()/2];
        } catch (IOException e) {

        }
        boolean stop = false;
        int tileX = 0, tileY = 0, x = 0, y = 0;
        while (tileY++<image.getHeight()/8) {
            tileX=0;
            while (tileX++<image.getWidth()/8) {
                y=0;
                while (y++<8) {
                    x=0;
                    long encodedLine = 0;
                    while (x++<8) {
                        int rgb = image.getRGB(((tileX - 1) * 8 + (x - 1)), ((tileY - 1) * 8 + (y - 1)));
                        String color = Utils.getColorAsHex(rgb).toLowerCase();
                        if (rgb==0) {
                            stop = true;
                            break;
                        }
                        FontColor fontColor = palette.getFontColor(color);
                        long mask = fontColor.getLongMask();
                        mask = mask >> (x-1);
                        encodedLine = encodedLine | mask;
                    }
                    if (stop) break;
                    //int leftByte = encodedLine >> 8;
                    //int rightByte = encodedLine & 0x00FF;
                    long byte1 = encodedLine >> 24;
                    long byte2 = (encodedLine >> 16) & 0x00FF;
                    long byte3 = (encodedLine >> 8) & 0x00FF;
                    long byte4 = (encodedLine) & 0x00FF;

                    output[indexOutput] = (byte) ((byte1) & 0xFF);
                    output[indexOutput+1] = (byte) ((byte2) & 0xFF);
                    output[indexOutput+16] = (byte) ((byte3) & 0xFF);
                    output[indexOutput+17] = (byte) ((byte4) & 0xFF);
                    indexOutput += 2;
                }
                indexOutput += 16;
                if (stop) break;
            }
            if (stop) break;
        }
        int k = 0;
        for (byte b:output) {
            //if (k++%16==0) System.out.println();
            String s = Utils.toHexString(b) + " ";
            sb.append(s);
            outputStream.write(b);
            //System.out.print(s);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        
        String s = "*!?:.+-()[]\\/_#$%0123456789~";
        int start = Integer.parseInt("4000",16);
        String pa = "" +
                "    {\n" +
                "      \"value\":\"%s\",\n" +
                "      \"code\":\"%s\"\n" +
                "    },\n";
        for (char c:s.toCharArray()) {
            System.out.printf(pa,c,Integer.toHexString(start));
            start += Integer.parseInt("100",16);
        }
        
        Map<String, BufferedImage> imageMap = new HashMap<>();
        Path path = null;
        try {
            path = Paths.get("D:\\git\\dokapon-gaiden\\dokapon-gaiden-gen\\src\\main\\resources\\images\\tmp");
            Files.list(path).forEach(
                    file -> {
                        if (file.toFile().isFile()) {
                            try {

                                BufferedImage image = ImageIO.read(file.toFile());
                                BufferedImage out = new BufferedImage(16, image.getHeight(), BufferedImage.TYPE_INT_ARGB);
                                int key = 0;
                                Color white = new Color(255, 255, 255, 255);
                                Color black = new Color(0, 0, 0, 255);
                                for (int x = 0; x < out.getWidth(); x++) {
                                    for (int y = 0; y < out.getHeight(); y++) {
                                        if (x>=8) out.setRGB(x,y,black.getRGB());
                                        else {
                                            int in = image.getRGB(x, y);
                                            if (in==white.getRGB()) out.setRGB(x,y,black.getRGB());
                                            else out.setRGB(x,y,white.getRGB());
                                        }
                                    }
                                }
                                ImageIO.write(out, "png", file.toFile());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeLatin(byte[] data) {
        int start = Integer.parseInt("58160",16);
        final int[] offset = {start};
        Path path = null;
        try {
            path = Paths.get(ClassLoader.getSystemResource("images/latin").toURI());
            Files.list(path).sorted().forEach(
                    file -> {
                        if (file.toFile().isFile()) {
                            try {
                                byte[] bytes = loadFontImage2bppSquelched(file.toFile(), new Palette2bpp("/palettes/palette-font.png"), FontColor.MAP_2BPP_COLOR_02);
                                for (byte b:bytes) {
                                    data[offset[0]++] = b;
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
            );
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        //imageReader.loadFontImage2bppSquelched("src/main/resources/images/latin/char-000.png", new Palette2bpp("/palettes/palette-latin.png"), FontColor.MAP_2BPP_COLOR_02);

    }
}
