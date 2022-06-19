package lz;

import com.google.common.primitives.Bytes;
import services.Utils;

import java.util.Arrays;

public class WriteCommand extends Command {
    
    byte[] data;

    public WriteCommand(byte[] data) {
        this.data = data;
    }
    
    public void appendData(byte[] data) {
        this.data = Bytes.concat(this.data, data);
    }
    
    public byte[] getBytes() {
        byte[] b = new byte[1];
        b[0] = (byte) (data.length & 0x7F);
        return Bytes.concat(b, data);
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "WriteCommand{" +
                "length="+ data.length + " " +
                "data=" + Utils.bytesToHex(data) +
                '}';
    }
}
