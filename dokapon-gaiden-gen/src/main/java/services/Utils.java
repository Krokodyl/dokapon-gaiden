package services;

public class Utils {

    public static String toHexString(byte value) {
        return toHexString(((int)value) & 0xFF, 2);
    }

    public static String toHexString(int value) {
        return toHexString(value & 0xFF, 2);
    }

    public static String toHexString(int value, int padding) {
        return String.format("%0"+padding+"x",value).toUpperCase();
    }

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 3];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 3] = HEX_ARRAY[v >>> 4];
            hexChars[j * 3 + 1] = HEX_ARRAY[v & 0x0F];
            hexChars[j * 3 + 2] = ' ';
        }
        return new String(hexChars);
    }

    public static String getColorAsHex(int rgb) {
        int blue = rgb & 0xff;
        int green = (rgb & 0xff00) >> 8;
        int red = (rgb & 0xff0000) >> 16;
        return toHexString(red,2)+toHexString(green,2)+toHexString(blue,2);
    }

    public static byte[] hexStringToByteArray(String[] s) {
        byte[] bytes = new byte[s.length];
        for (int i = 0; i < s.length; i++) {
            bytes[i] = (byte) (Integer.parseInt(s[i],16) & 0xFF);
        }
        return bytes;
    }

    public static String padLeft(String s,char c,int length) {
        while (s.length()<length) {
            s=c+s;
        }
        return s;
    }

    public static String[] squelch(String[] data) {
        String res = "";
        boolean space = false;
        for (String s:data) {
            res+=s;
            if (space) res+=" ";
            space = !space;
        }
        return res.trim().split(" ");
    }

    public static byte[] codeBytes(String code) {
        byte[] data = new byte[2];
        int a = Integer.parseInt(code.substring(0, 2), 16);
        int b = Integer.parseInt(code.substring(2, 4), 16);
        data[0] = (byte) a;
        data[1] = (byte) b;
        return data;
    }
}
