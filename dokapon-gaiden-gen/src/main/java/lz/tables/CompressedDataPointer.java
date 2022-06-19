package lz.tables;

import services.Utils;

import java.util.Arrays;

public class CompressedDataPointer {
    
    private int value;
    private int offset;
    private int offsetData;
    private byte[] data;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getOffsetData() {
        return offsetData;
    }

    public void setOffsetData(int offsetData) {
        this.offsetData = offsetData;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CompressedDataPointer{" +
                "value=" + Integer.toHexString(value) +
                ", offset=" + Integer.toHexString(offset) +
                ", offsetData=" + Integer.toHexString(offsetData) +
                ", data.length=" + data.length +
                ", data=" + Utils.bytesToHex(data) +
                '}';
    }
}
