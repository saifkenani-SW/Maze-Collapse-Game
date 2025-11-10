package basicStructure;

import java.util.Objects;

public class Location {
   private int row;
   private int column;

    public Location(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }
    public Location clone() {
        return new Location(row, column);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location other)) return false;
        return this.row == other.row && this.column == other.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }


}
