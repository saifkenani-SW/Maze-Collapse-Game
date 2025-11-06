package basicStructure;

public class Pyramid {
    private Location location;
    private boolean hasKey = false;
    private int numberOfKey = 0;

    public Pyramid(Location location, int numberOfKey) {
        this.location = location;
        setNumberOfKey(numberOfKey);
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean hasKey() {
        return hasKey;
    }

    public int getNumberOfKey() {
        return numberOfKey;
    }

    public void setNumberOfKey(int numberOfKey) {
        this.numberOfKey = numberOfKey;
        this.hasKey = numberOfKey > 0;
    }

    @Override
    public String toString() {
        return "Pyramid{" +
                "location=" + location +
                ", hasKey=" + hasKey +
                ", numberOfKey=" + numberOfKey +
                '}';
    }
}
