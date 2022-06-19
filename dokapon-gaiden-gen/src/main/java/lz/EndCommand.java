package lz;

public class EndCommand extends Command{


    @Override
    public byte[] getBytes() {
        return new byte[1];
    }

    @Override
    public String toString() {
        return "EndCommand{}";
    }
}
