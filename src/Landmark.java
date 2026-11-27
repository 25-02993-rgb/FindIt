/**
 * Campus landmark selector (proposal: "Interactive Campus Map").
 * Supplements GPS pins, especially indoors.
 * NOTE: coordinates are PLACEHOLDERS - replace with real pins from Google Maps.
 */
public class Landmark {
    private final String name;
    private final double latitude;
    private final double longitude;

    public Landmark(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() { return name; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }

    @Override
    public String toString() { return name; }
}
