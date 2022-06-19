package lz.tables;

import entities.PointerData;
import entities.PointerRange;

import java.util.ArrayList;
import java.util.List;

public class CompressedDataTable {

    List<PointerRange> ranges = new ArrayList<>();
    List<CompressedDataPointer> pointers = new ArrayList<>();
    List<CompressedDataPointer> newPointers = new ArrayList<>();
    private int newStart;
    private int newDataStart;
    private int newDataShift;

    public void addRange(PointerRange r){
        ranges.add(r);
    }

    public List<PointerRange> getRanges() {
        return ranges;
    }

    public int getNewDataStart() {
        return newDataStart;
    }

    public void setNewDataStart(int newDataStart) {
        this.newDataStart = newDataStart;
    }

    public int getNewDataShift() {
        return newDataShift;
    }

    public void setNewDataShift(int newDataShift) {
        this.newDataShift = newDataShift;
    }

    public void addPointer(CompressedDataPointer p) {
        pointers.add(p);
    }

    public void addNewPointer(CompressedDataPointer p) {
        newPointers.add(p);
    }

    public List<CompressedDataPointer> getPointers() {
        return pointers;
    }

    public List<CompressedDataPointer> getNewPointers() {
        return newPointers;
    }

    public int getNewStart() {
        return newStart;
    }

    public void setNewStart(int newStart) {
        this.newStart = newStart;
    }
}
