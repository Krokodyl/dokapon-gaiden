package lz;

public class DataSection {
    
    int startOffset;
    int endOffset;
    
    DataSection left = null;
    DataSection right = null;
    
    boolean compressed = false;

    public DataSection(int i, int e) {
        startOffset = i;
        endOffset = e;
    }

    public boolean isCompressed() {
        return compressed;
    }

    public void setCompressed(boolean compressed) {
        this.compressed = compressed;
    }

    public int getStartOffset() {
        return startOffset;
    }

    public void setStartOffset(int startOffset) {
        this.startOffset = startOffset;
    }

    public int getEndOffset() {
        return endOffset;
    }

    public void setEndOffset(int endOffset) {
        this.endOffset = endOffset;
    }
}
