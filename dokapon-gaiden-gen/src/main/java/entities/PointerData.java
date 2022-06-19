package entities;

import java.util.Objects;

public class PointerData {

    private int value;
    private int offset;
    private String[] data;
    private int offsetData;
    private PointerData oldPointer;
    private String[] menuData;
    private int offsetMenuData;

    public void setValue(int value) {
        if (this.value>0) System.out.println(this.value+"  ->  "+value);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }

    public void setData(String[] data) {
        this.data = data;
    }

    public String[] getData() {
        return data;
    }

    public void setOffsetData(int offsetData) {

        this.offsetData = offsetData;
    }

    public int getOffsetData() {
        return offsetData;
    }

    public void setOldPointer(PointerData oldPointer) {
        this.oldPointer = oldPointer;
    }

    public PointerData getOldPointer() {
        return oldPointer;
    }

    public void setMenuData(String[] menuData) {
        this.menuData = menuData;
    }

    public String[] getMenuData() {
        return menuData;
    }

    public void setOffsetMenuData(int offsetMenuData) {
        this.offsetMenuData = offsetMenuData;
    }

    public int getOffsetMenuData() {
        return offsetMenuData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PointerData that = (PointerData) o;
        return offset == that.offset;
    }

    @Override
    public int hashCode() {
        return Objects.hash(offset);
    }

    @Override
    public String toString() {
        String s = "";
        for (String a:data) s = s+a+" ";
        String t = "";
        if (menuData!=null)
        for (String a:menuData) t = t+a+" ";
        return "Pointer{" + "offset=" + Integer.toHexString(offset) + ", value=" + Integer.toHexString(value) + ", offsetdata="+Integer.toHexString(offsetData)+ ", offsetMenudata="+Integer.toHexString(offsetMenuData)+", menudata=" + t + ", data=" + s + '}';
    }
    
    
}
