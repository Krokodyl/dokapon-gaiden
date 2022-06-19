import org.junit.Test;
import services.Decompressor;

import static org.junit.Assert.assertEquals;

public class DecompressorTest {

    @Test
    public void testShift0() {
        // Arrange
        byte a = (byte) Integer.parseInt("11110000",2);
        byte b = (byte) Integer.parseInt("00000000",2);

        // Act
        int shift = Decompressor.getShift(a, b);

        // Assert
        assertEquals(shift, 0);
    }

    @Test
    public void testShift16() {
        // Arrange
        byte a = (byte) Integer.parseInt("11110000",2);
        byte b = (byte) Integer.parseInt("00010000",2);

        // Act
        int shift = Decompressor.getShift(a, b);

        // Assert
        assertEquals(shift, 16);
    }

    @Test
    public void testShift16768() {
        // Arrange
        byte a = (byte) Integer.parseInt("11110011",2);
        byte b = (byte) Integer.parseInt("00010000",2);

        // Act
        int shift = Decompressor.getShift(a, b);

        // Assert
        assertEquals(shift, 16+768);
    }
    
}
