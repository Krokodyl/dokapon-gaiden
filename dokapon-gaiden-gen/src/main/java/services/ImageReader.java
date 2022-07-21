package services;

import characters.LatinChar;
import characters.Letter;
import entities.*;
import lz.LzCompressor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

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

    public void generateSpriteMoveMenu() throws IOException {
        generateSpriteDataFromImage(
                "src/main/resources/sprites/move-menu.png",
                "src/main/resources/gen/sprite-uncompressed.data",
                new Palette2bpp("/palettes/palette-font.png"),
                2
        );
        String uncomp = "src/main/resources/gen/sprite-uncompressed.data";
        String outputFile = "src/main/resources/data/A0373.data";
        Compressor compressor = new Compressor(uncomp);
        compressor.compressData();
        DataWriter.saveData(outputFile, compressor.getCompressedData());
        //CompressedSpriteManager compressedSpriteManager = new CompressedSpriteManager(null);
        //compressedSpriteManager.compressCopyFile(uncomp, Header.LATIN_SPRITES_HEADER, outputFile);
        //compressedSpriteManager.decompressFile(outputFile, "src/main/resources/data/decomp-1B8000.data");
    }

    public void generateSpriteEnd() throws IOException {
        generateSpriteDataFromImage(
                "src/main/resources/sprites/end.png",
                "src/main/resources/gen/sprite-uncompressed.data",
                new Palette4bpp("/palettes/palette-end.png"),
                4
        );
        String uncomp = "src/main/resources/gen/sprite-uncompressed.data";
        String outputFile = "src/main/resources/data/9ECD4.data";
        Compressor compressor = new Compressor(uncomp);
        compressor.compressData();
        DataWriter.saveData(outputFile, compressor.getCompressedData());
    }

    public void generateSpriteDuelCard() throws IOException {
        generateSpriteDataFromImage(
                "src/main/resources/sprites/duel-card.png",
                "src/main/resources/gen/cf8f8-en.data",
                new Palette4bpp("/palettes/palette-duel.png"),
                4
        );
    }

    public void compressSpriteDuelCard() throws IOException {
        String uncomp = "src/main/resources/gen/cf8f8-en.data";
        String outputFile = "src/main/resources/data/190054.data";
        Compressor compressor = new Compressor(uncomp);
        compressor.compressData();
        DataWriter.saveData(outputFile, compressor.getCompressedData());
    }

    public void generateSpriteMenuSelection() throws IOException {
        generateSpriteDataFromImage(
                "src/main/resources/sprites/menu-selection.png",
                "src/main/resources/gen/sprite-uncompressed.data",
                new Palette4bpp("/palettes/palette-menu-selection.png"),
                4
        );
        String uncomp = "src/main/resources/gen/sprite-uncompressed.data";
        String outputFile = "src/main/resources/data/A8018.data";
        Compressor compressor = new Compressor(uncomp);
        compressor.compressData();
        DataWriter.saveData(outputFile, compressor.getCompressedData());
    }

    public void generateSpriteCharacterNameScreen() throws IOException {
        generateSpriteDataFromImage(
                "src/main/resources/sprites/character-name.png",
                "src/main/resources/gen/sprite-uncompressed.data",
                new Palette4bpp("/palettes/palette-character-name.png"),
                4
        );
        String uncomp = "src/main/resources/gen/sprite-uncompressed.data";
        String outputFile = "src/main/resources/data/DC5A1.data";
        LzCompressor compressor = new LzCompressor();
        byte[] compressed = compressor.compress(Files.readAllBytes(new File(uncomp).toPath()));
        DataWriter.saveData(outputFile, compressed);
        //CompressedSpriteManager compressedSpriteManager = new CompressedSpriteManager(null);
        //compressedSpriteManager.compressCopyFile(uncomp, Header.LATIN_SPRITES_HEADER, outputFile);
        //compressedSpriteManager.decompressFile(outputFile, "src/main/resources/data/decomp-1B8000.data");
    }

    public void generateSpritePasswordScreen() throws IOException {
        generateSpriteDataFromImage(
                "src/main/resources/sprites/password.png",
                "src/main/resources/gen/sprite-uncompressed.data",
                new Palette4bpp("/palettes/palette-character-name.png"),
                4
        );
        String uncomp = "src/main/resources/gen/sprite-uncompressed.data";
        String outputFile = "src/main/resources/data/178020.data";
        LzCompressor compressor = new LzCompressor();
        byte[] compressed = compressor.compress(Files.readAllBytes(new File(uncomp).toPath()));
        DataWriter.saveData(outputFile, compressed);
        //CompressedSpriteManager compressedSpriteManager = new CompressedSpriteManager(null);
        //compressedSpriteManager.compressCopyFile(uncomp, Header.LATIN_SPRITES_HEADER, outputFile);
        //compressedSpriteManager.decompressFile(outputFile, "src/main/resources/data/decomp-1B8000.data");
    }

    public void generateSpritePasswordGenScreen() throws IOException {
        generateSpriteDataFromImage(
                "src/main/resources/sprites/password-gen.png",
                "src/main/resources/gen/sprite-uncompressed.data",
                new Palette4bpp("/palettes/palette-password-gen.png"),
                4
        );
        String uncomp = "src/main/resources/gen/sprite-uncompressed.data";
        String outputFile = "src/main/resources/data/d4529.data";
        LzCompressor compressor = new LzCompressor();
        byte[] compressed = compressor.compress(Files.readAllBytes(new File(uncomp).toPath()));
        DataWriter.saveData(outputFile, compressed);
    }

    public void generatePasswordScreenTilesMap() throws IOException {
        String uncomp = "src/main/resources/tiles-maps/password.data";
        String outputFile = "src/main/resources/data/2BBF6.data";
        LzCompressor compressor = new LzCompressor();
        byte[] compressed = compressor.compress(Files.readAllBytes(new File(uncomp).toPath()));
        DataWriter.saveData(outputFile, compressed);
    }

    public void generateSpriteCharacterSelectScreen() throws IOException {
        generateSpriteDataFromImage(
                "src/main/resources/sprites/character-select.png",
                "src/main/resources/gen/sprite-uncompressed.data",
                new Palette4bpp("/palettes/palette-character-select.png"),
                4
        );
        String uncomp = "src/main/resources/gen/sprite-uncompressed.data";
        String outputFile = "src/main/resources/data/D63E7.data";
        LzCompressor compressor = new LzCompressor();
        byte[] compressed = compressor.compress(Files.readAllBytes(new File(uncomp).toPath()));
        DataWriter.saveData(outputFile, compressed);
        //CompressedSpriteManager compressedSpriteManager = new CompressedSpriteManager(null);
        //compressedSpriteManager.compressCopyFile(uncomp, Header.LATIN_SPRITES_HEADER, outputFile);
        //compressedSpriteManager.decompressFile(outputFile, "src/main/resources/data/decomp-1B8000.data");
    }

    public void generateCharacterSelectScreenTilesMap() throws IOException {
        String uncomp = "src/main/resources/tiles-maps/character-select.data";
        String outputFile = "src/main/resources/data/D399E.data";
        LzCompressor compressor = new LzCompressor();
        byte[] compressed = compressor.compress(Files.readAllBytes(new File(uncomp).toPath()));
        DataWriter.saveData(outputFile, compressed);
    }

    public void generateCharacterSelectAuditionScreenTilesMap() throws IOException {
        String uncomp = "src/main/resources/tiles-maps/character-select-02.data";
        String outputFile = "src/main/resources/data/D43B5.data";
        LzCompressor compressor = new LzCompressor();
        byte[] compressed = compressor.compress(Files.readAllBytes(new File(uncomp).toPath()));
        DataWriter.saveData(outputFile, compressed);
    }
    
    public void generatePlayerOrderScreenTilesMap() throws IOException {
        String uncomp = "src/main/resources/tiles-maps/player-order.data";
        String outputFile = "src/main/resources/data/2B55F.data";
        LzCompressor compressor = new LzCompressor();
        byte[] compressed = compressor.compress(Files.readAllBytes(new File(uncomp).toPath()));
        DataWriter.saveData(outputFile, compressed);
    }

    public void generateSpriteMapSelectScreen() throws IOException {
        generateSpriteDataFromImage(
                "src/main/resources/sprites/map-select.png",
                "src/main/resources/gen/sprite-uncompressed.data",
                new Palette4bpp("/palettes/palette-map-select.png"),
                4
        );
        String uncomp = "src/main/resources/gen/sprite-uncompressed.data";
        String outputFile = "src/main/resources/data/d8022.data";
        LzCompressor compressor = new LzCompressor();
        byte[] compressed = compressor.compress(Files.readAllBytes(new File(uncomp).toPath()));
        DataWriter.saveData(outputFile, compressed);
    }

    public void generateMapScreenTilesMap() throws IOException {
        String uncomp = "src/main/resources/tiles-maps/map-select.data";
        String outputFile = "src/main/resources/data/D41Ed.data";
        LzCompressor compressor = new LzCompressor();
        byte[] compressed = compressor.compress(Files.readAllBytes(new File(uncomp).toPath()));
        DataWriter.saveData(outputFile, compressed);
    }

    public void generateSpriteInputScreen() throws IOException {
        generateSpriteDataFromImage(
                "src/main/resources/sprites/input-screen.png",
                "src/main/resources/gen/sprite-uncompressed.data",
                new Palette4bpp("/palettes/palette-input-screen.png"),
                4
        );
        String uncomp = "src/main/resources/gen/sprite-uncompressed.data";
        String outputFile = "src/main/resources/data/DC5A1.data";
        LzCompressor compressor = new LzCompressor();
        byte[] compressed = compressor.compress(Files.readAllBytes(new File(uncomp).toPath()));
        DataWriter.saveData(outputFile, compressed);
        //CompressedSpriteManager compressedSpriteManager = new CompressedSpriteManager(null);
        //compressedSpriteManager.compressCopyFile(uncomp, Header.LATIN_SPRITES_HEADER, outputFile);
        //compressedSpriteManager.decompressFile(outputFile, "src/main/resources/data/decomp-1B8000.data");
    }

    public void generateInputScreenTilesMap() throws IOException {
        String uncomp = "src/main/resources/tiles-maps/input-screen.data";
        String outputFile = "src/main/resources/data/D3C2C.data";
        LzCompressor compressor = new LzCompressor();
        byte[] compressed = compressor.compress(Files.readAllBytes(new File(uncomp).toPath()));
        DataWriter.saveData(outputFile, compressed);
    }
    
    public void generateInputMoreScreenTilesMap() throws IOException {
        String uncomp = "src/main/resources/tiles-maps/input-screen-more.data";
        String outputFile = "src/main/resources/data/D3F0A.data";
        LzCompressor compressor = new LzCompressor();
        byte[] compressed = compressor.compress(Files.readAllBytes(new File(uncomp).toPath()));
        DataWriter.saveData(outputFile, compressed);
    }

    public void generateTitleScreenDokaponTilesMap() throws IOException {
        String uncomp = "src/main/resources/tiles-maps/title-screen-dokapon.data";
        String outputFile = "src/main/resources/data/2B71E.data";
        LzCompressor compressor = new LzCompressor();
        byte[] compressed = compressor.compress(Files.readAllBytes(new File(uncomp).toPath()));
        DataWriter.saveData(outputFile, compressed);
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
        if (file.toString().contains("w8")) width = 8 + 2;
        if (file.toString().contains("w4")) width = 4 + 2;
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

    public int getWidth(File file, Palette palette, FontColor fontColor) {
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {

        }
        int width = 0;
        boolean stop = false;
        int tileX, tileY = 0, x, y;
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
                }
                if (stop) break;
            }
            if (stop) break;
        }
        if (file.toString().contains("w8")) width = 8 + 2;
        if (file.toString().contains("w4")) width = 4 + 2;
        return width + 2;
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
        int start = Integer.parseInt("58020",16);
        final int[] offset = {start};
        Path path = null;
        try {
            path = Paths.get(ClassLoader.getSystemResource("images/latin").toURI());
            Files.list(path).sorted().forEach(
                    file -> {
                        if (file.toFile().isFile()) {
                            try {
                                //System.out.printf("Writing Latin char %s at %s\n",file.toFile().getName(),Integer.toHexString(offset[0]));
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
            offset[0] = Integer.parseInt("58C00",16);
            path = Paths.get(ClassLoader.getSystemResource("images/syllable").toURI());
            Files.list(path).sorted().forEach(
                    file -> {
                        if (file.toFile().isFile()) {
                            try {
                                //System.out.printf("Writing Latin char %s at %s\n",file.toFile().getName(),Integer.toHexString(offset[0]));
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
    
    public Map<String, List<Letter>> generateSyllables(List<NameTable> tables, LatinLoader latinLoader) {
        Map<String, List<Letter>> mapWords = new HashMap<>();
        int code = Integer.parseInt("0B00",16);
        Map<String, Letter> letterMap = new HashMap<>();
        String name = "char-%s.png";
        try {
            for (int k = 0; k < 26; k++) {
                char value = (char) ('A' + k);
                String filename = String.format(name, Utils.padLeft("" + k, '0', 3));
                Letter l = new Letter(value, filename);
                Path path = Paths.get(ClassLoader.getSystemResource("images/latin/" + filename).toURI());
                int width = getWidth(path.toFile(), new Palette2bpp("/palettes/palette-font.png"), FontColor.MAP_2BPP_COLOR_02);
                l.setWidth(width);
                l.setCode(Utils.toHexString(code,4));

                letterMap.put(l.getValue(), l);
                int m = k + 26;
                value = (char) ('a' + k);
                filename = String.format(name, Utils.padLeft("" + m, '0', 3));
                l = new Letter(value, filename);
                path = Paths.get(ClassLoader.getSystemResource("images/latin/" + filename).toURI());
                width = getWidth(path.toFile(), new Palette2bpp("/palettes/palette-font.png"), FontColor.MAP_2BPP_COLOR_02);
                l.setCode(Utils.toHexString(code+Integer.parseInt("1A00",16),4));
                l.setWidth(width);
                letterMap.put(l.getValue(), l);
                code += Integer.parseInt("100",16);
            }
            char value = ' ';
            String filename = "char-150-w2.png";
            Letter l = new Letter(value, filename);
            int width = 2;
            l.setCode("0A00");
            l.setWidth(width);
            letterMap.put(l.getValue(), l);
        } catch (IOException | URISyntaxException e) {
            
        }
        /*for (Map.Entry<String, Letter> entry : letterMap.entrySet()) {
            System.out.println(entry);
        }*/
        List<Letter> syllables = new ArrayList<>();
        int width = 0;
        code = Integer.parseInt("6000",16);
        for (NameTable table:tables)
        for (String word:table.getNames()) {
            mapWords.put(word, new ArrayList<Letter>());
            String syllable = "";
            width = 0;
            for (char c:word.toCharArray()) {
                Letter letter = letterMap.get(""+c);
                if (letter!=null){
                    if (letter.getWidth() + width < 16) {
                        syllable += letter.getValue();
                        width += letter.getWidth();
                    } else {
                        Letter newSyllable = new Letter(syllable);
                        if (syllable.length()>1) {
                            newSyllable.setWidth(width);
                            newSyllable.setCode(Utils.toHexString(code, 4));
                            if (!syllables.contains(newSyllable)) {
                                syllables.add(newSyllable);
                                code += Integer.parseInt("100", 16);
                            } else {
                                for (Letter s : syllables) {
                                    if (s.equals(newSyllable)) newSyllable = s;
                                }
                            }
                        } else {
                            for (LatinChar latinChar : latinLoader.getLatinChars()) {
                                if (latinChar.getValue().equals(syllable)) {
                                    newSyllable = new Letter(syllable);
                                    newSyllable.setCode(latinChar.getCode());
                                }
                            }
                        }
                        mapWords.get(word).add(newSyllable);
                        width = letter.getWidth();
                        syllable = letter.getValue();
                    }
                }
            }
            if (!syllable.isEmpty()) {
                Letter newSyllable = new Letter(syllable);
                if (syllable.length()>1) {
                    newSyllable.setWidth(width);
                    newSyllable.setCode(Utils.toHexString(code, 4));
                    if (!syllables.contains(newSyllable)) {
                        syllables.add(newSyllable);
                        code += Integer.parseInt("100", 16);
                    } else {
                        for (Letter s : syllables) {
                            if (s.equals(newSyllable)) newSyllable = s;
                        }
                    }
                } else {
                    for (LatinChar latinChar : latinLoader.getLatinChars()) {
                        if (latinChar.getValue().equals(syllable)) {
                            newSyllable = new Letter(syllable);
                            newSyllable.setCode(latinChar.getCode());
                        }
                    }
                }
                mapWords.get(word).add(newSyllable);
            }
        }
        int n = 200;
        for (Letter syllable : syllables) {
            //System.out.println(syllable);
            if (syllable.getValue().length()>1){
                BufferedImage out = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = (Graphics2D) out.getGraphics();
                g.setColor(Color.BLACK);
                g.fillRect(0,0,16,16);

                int x = 0;
                for (char c : syllable.getValue().toCharArray()) {
                    try {
                        Letter letter = letterMap.get("" + c);
                        Path path = null;
                        path = Paths.get(ClassLoader.getSystemResource("images/latin/" + letter.getFilename()).toURI());
                        BufferedImage image = ImageIO.read(path.toFile());
                        BufferedImage subImage = getSubImage(image, letter.getWidth());
                        g.drawImage(subImage, x, 0, null);
                        x += letter.getWidth();
                    } catch (URISyntaxException | IOException e) {
                        e.printStackTrace();
                    }


                }
                try {
                    String folder = "D:\\git\\dokapon-gaiden\\dokapon-gaiden-gen\\src\\main\\resources\\images\\syllable";
                    ImageIO.write(out, "png", new File(folder + "/" + n + ".png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                /*String json = "{\n" +
                        "      \"value\":\"{SYL-%s}\",\n" +
                        "      \"code\":\"%s\"\n" +
                        "    },\n";
                System.out.printf(json, syllable.getValue(), syllable.getCode());*/
                n++;
            }
        }
        return mapWords;
    }
    
    public BufferedImage getSubImage(BufferedImage image, int width) {
        return image.getSubimage(0,0,width,image.getWidth());
    }
    
    public void turnImageIntoTiles(int tileX, String s, String file, String outputTiles, String outputMap) throws IOException {
        BufferedImage image = ImageIO.read(Objects.requireNonNull(getClass().getResource(file)));
        ByteArrayOutputStream outputMapBytes = new ByteArrayOutputStream();
        Map<Tile, String> tiles = new HashMap<>();
        BufferedImage out = new BufferedImage(8*16, 320, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) out.getGraphics();
        int code = Integer.parseInt(s,16);
        int tileY = 0;
        for (int y=0;y<image.getHeight();y=y+8) {
            for (int x=0;x<image.getWidth();x=x+8) {
                BufferedImage subimage = image.getSubimage(x, y, 8, 8);
                String folder = "D:\\git\\dokapon-gaiden\\dokapon-gaiden-gen\\src\\main\\resources\\gen\\test\\"+tileX+"-"+tileY+".png";
                ImageIO.write(subimage, "png", new File(folder ));
                Tile tile = new Tile(subimage);
                String hexCode = Utils.toHexString(code, 4);
                if (!tiles.containsKey(tile)) {
                    /*for (Map.Entry<Tile, String> entry : tiles.entrySet()) {
                        boolean b = compareImages(entry.getKey().image, tile.image);
                        System.out.println(b);
                    }*/

                    tiles.put(tile, hexCode);
                    g.drawImage(subimage,tileX,tileY, null);
                    tileX += 8;
                    if (tileX%(8*16)==0) {
                        tileX=0;tileY+=8;
                    }
                    outputMapBytes.write(Utils.codeBytes(hexCode));
                    code += Integer.parseInt("0100",16);
                } else {
                    hexCode = tiles.get(tile);
                    outputMapBytes.write(Utils.codeBytes(hexCode));
                }
            }
        }
        try {
            DataWriter.saveData("D:\\git\\dokapon-gaiden\\dokapon-gaiden-gen\\src\\main\\resources\\gen\\"+outputMap, outputMapBytes.toByteArray());
            String folder = "D:\\git\\dokapon-gaiden\\dokapon-gaiden-gen\\src\\main\\resources\\gen\\"+outputTiles;
            ImageIO.write(out, "png", new File(folder ));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    class Tile {
        
        BufferedImage image;
        int[] rgbData;

        public Tile(BufferedImage image) {
            this.image = image;
            rgbData = this.image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Tile tile = (Tile) o;
            return Arrays.equals(rgbData, tile.rgbData);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(rgbData);
        }
    }

    public static boolean compareImages(BufferedImage imgA, BufferedImage imgB) {
        // The images must be the same size.
        if (imgA.getWidth() != imgB.getWidth() || imgA.getHeight() != imgB.getHeight()) {
            return false;
        }

        int width  = imgA.getWidth();
        int height = imgA.getHeight();

        // Loop over every pixel.
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Compare the pixels for equality.
                if (imgA.getRGB(x, y) != imgB.getRGB(x, y)) {
                    return false;
                }
            }
        }

        return true;
    }
    
    public static void generateLatinMenus() {
        Map<String, String> letters = new HashMap<>();
        letters.put(" ", "00 00");
        int index = 1;
        for (char c='0';c<='9';c++) {
            letters.put(""+c, Utils.toHexString(index++)+" 00");
        }
        letters.put("A", Utils.toHexString(index++)+" 00");
        letters.put("B", Utils.toHexString(index++)+" 00");
        letters.put("C", Utils.toHexString(index++)+" 00");
        letters.put("D", Utils.toHexString(index++)+" 00");
        letters.put("E", Utils.toHexString(index++)+" 00");
        index = 16;
        for (char c='F';c<='U';c++) {
            letters.put(""+c, Utils.toHexString(index++)+" 00");
        }
        letters.put("V", Utils.toHexString(index++)+" 00");
        letters.put("W", Utils.toHexString(index++)+" 00");
        letters.put("X", Utils.toHexString(index++)+" 00");
        letters.put("Y", Utils.toHexString(index++)+" 00");
        letters.put("Z", Utils.toHexString(index++)+" 00");
        
        
        String[] menus = {
                "SWITCHA CARD",
                "NO    SWITCH",
                "REMOVEITEM  ",
                "GET   READY ",
                "SELECTTARGET",
                "SELECTACTION"
                
        };
        for (String menu:menus) {
            String[] lines = menu.split("(?<=\\G.{6})");
            System.out.printf("Menu text : %s ", menu);
            String line = "";
            for (char c:lines[0].toCharArray()) {
                line+=letters.get(""+c)+" ";
            }
            System.out.printf("0C 00 %s FF FF ", line.trim());
            line = "";
            for (char c:lines[1].toCharArray()) {
                line+=letters.get(""+c)+" ";
            }
            System.out.printf("0C 00 %s FF FF\n", line.trim());
        }
        
        Map<String, String> values = new HashMap<>();
        values.put("fire","E0 01 E1 01 00 00 ");
        values.put("ice","E2 01 E3 01 00 00 ");
        values.put("hit","E4 01 E5 01 00 00 ");
        values.put("shield", "E6 01 E7 01 E8 01 ");
        values.put("jewel", "F0 01 F1 01 F2 01 ");
        values.put("sword", "F3 01 F4 01 F5 01 ");
        values.put("armor", "F6 01 F7 01 F8 01 ");
        values.put("lvl-top", "E9 01 EA 01 ");
        values.put("lvl-bot", "F9 01 FA 01 ");
        values.put("1-top", "87 01 ");
        values.put("2-top", "88 01 ");
        values.put("3-top", "89 01 ");
        values.put("4-top", "8A 01 ");
        values.put("5-top", "8B 01 ");
        values.put("6-top", "8C 01 ");
        values.put("7-top", "8D 01 ");
        values.put("8-top", "8E 01 ");
        values.put("9-top", "8F 01 ");
        values.put("1-bot", "97 01 ");
        values.put("2-bot", "98 01 ");
        values.put("3-bot", "99 01 ");
        values.put("4-bot", "9A 01 ");
        values.put("5-bot", "9B 01 ");
        values.put("6-bot", "9C 01 ");
        values.put("7-bot", "9D 01 ");
        values.put("8-bot", "9E 01 ");
        values.put("9-bot", "9F 01 ");
        String[] types = {"hit", "fire", "ice"};
        String[] gear = {"jewel", "sword", "shield", "armor"};
        String all = "";
        for (String type:types) {
            for (int level=1;level<10;level++) {
                for (String g:gear) {
                    String top = "0C 00 " + values.get(type) + values.get("lvl-top") + values.get(level+"-top") + "FF FF ";
                    String bot = "0C 00 " + values.get(g) + values.get("lvl-bot") + values.get(level+"-bot") + "FF FF ";
                    /*System.out.println(type +" "+ g + " "+level);
                    System.out.println(top);
                    System.out.println(bot);*/
                    all+=top;
                    all+=bot;
                }
            }
        }
        //System.out.println("13C82");
        //System.out.println(all);
    }
    
    public static void extractMenus(byte[] data, int start, int end) throws IOException {
        BufferedImage sprites = ImageIO.read(Objects.requireNonNull(ImageReader.class.getResource("/sprites/move-menu.png")));
        int offset = start;
        while (offset<end) {
            if (data[offset]==Integer.parseInt("0C", 16) && data[offset+16]==Integer.parseInt("0C", 16)) {
                //System.out.println("extractMenus "+Integer.toHexString(offset));
                byte[] bytes = Arrays.copyOfRange(data, offset, offset + Integer.parseInt("20", 16));
                if (bytes[0]==Integer.parseInt("0C", 16) && bytes[16]==Integer.parseInt("0C", 16)) {
                    BufferedImage image = new BufferedImage(6*8, 16, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g = (Graphics2D) image.getGraphics();
                    g.setColor(Color.BLACK);
                    g.fillRect(0,0,image.getWidth(),image.getHeight());
                    for (int tileY=0;tileY<2;tileY++) {
                        for (int tileX=0;tileX<6;tileX++) {
                            byte a = (byte) (bytes[2+(tileY*16)+tileX*2] & 0xFF);
                            byte b = (byte) (bytes[2+1+(tileY*16)+tileX*2] & 0xFF);
                            BufferedImage subImage = menuSubImage(sprites, a, b);
                            g.drawImage(subImage,tileX*8,tileY*8, null);
                        }
                    }
                    String folder = "D:\\git\\dokapon-gaiden\\dokapon-gaiden-gen\\src\\main\\resources\\images\\menus";
                    ImageIO.write(image, "png", new File(folder + "/" + offset +" - "+Integer.toHexString(offset) + ".png"));
                    
                }
                offset+=32;
            } else {
                offset++;
            }
        }
    }
    
    public static BufferedImage menuSubImage(BufferedImage sprites, byte a, byte b) {
        int y = 0x80*(b & 0xFF) + 8*((a & 0xFF)/0x10);
        int x = 8*(0x0F & (a & 0xFF));
        return sprites.getSubimage(x, y, 8, 8);
    }
}
