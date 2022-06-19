package lz;

import com.google.common.primitives.Bytes;

public class RepeatCommand extends Command{
    
    int shift;
    int length;

    public RepeatCommand(int shift, int length) {
        this.shift = shift;
        this.length = length;
    }

    public byte[] getBytes() {
        byte[] b = new byte[2];
        byte command = (byte) 0x80;
        byte l = (byte) ((byte)(length-2 & 0xFF) << 2);
        byte l2 = (byte) (shift-1 >> 8);
        b[0] = (byte) (command | l | l2);
        b[1] = (byte) (shift-1 & 0xFF);
        return b;
    }

    public int getShift() {
        return shift;
    }

    public void setShift(int shift) {
        this.shift = shift;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return "RepeatCommand{" +
                "shift=" + shift +
                ", length=" + length +
                '}';
    }
}
