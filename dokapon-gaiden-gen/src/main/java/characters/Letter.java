package characters;

import java.util.Objects;

public class Letter {
    
    String value;
    String filename;
    String code;
    int width;

    public Letter(String value, String filename) {
        this.value = value;
        this.filename = filename;
    }

    public Letter(String value) {
        this.value = value;
    }

    public Letter(char value, String filename) {
        this.value = ""+value;
        this.filename = filename;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public byte[] getCodeBytes() {
        byte[] data = new byte[2];
        int a = Integer.parseInt(code.substring(0, 2), 16);
        int b = Integer.parseInt(code.substring(2, 4), 16);
        data[0] = (byte) a;
        data[1] = (byte) b;
        return data;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Letter letter = (Letter) o;
        return Objects.equals(value, letter.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Letter{" +
                "value=" + value +
                ", filename='" + filename + '\'' +
                ", code='" + code + '\'' +
                ", width=" + width +
                '}';
    }
}
