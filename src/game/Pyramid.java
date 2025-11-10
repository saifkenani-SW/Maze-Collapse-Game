package game;

import basicStructure.Location;

import java.util.Objects;

public class Pyramid {
    private Location location;
    private int numberOfKey = 0;

    public Pyramid(Location location, int numberOfKey) {
        this.location = location;
        this.numberOfKey=numberOfKey;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean hasKey() {
        return numberOfKey>0;
    }

    public int getNumberOfKey() {
        return numberOfKey;
    }

    public void setNumberOfKey(int numberOfKey) {
        this.numberOfKey = numberOfKey;
    }

    public Pyramid clone() {
        return new Pyramid(location.clone(), numberOfKey);
    }

    @Override
    public String toString() {
        return "Pyramid{" +
                "location=" + location +
                ", hasKey=" + hasKey() +
                ", numberOfKey=" + numberOfKey +
                '}';
    }
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Pyramid obj)) return false;

        return Objects.equals(this.location, obj.location)
                && this.hasKey() == obj.hasKey();
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, hasKey(),numberOfKey);
    }

}
