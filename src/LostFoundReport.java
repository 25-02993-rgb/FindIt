import java.time.LocalDate;

/** Lost or found item report for the FindIt system. */
public class LostFoundReport {
    public enum Type { LOST, FOUND }

    private static int nextId = 1;

    private final int id;
    private String title;
    private String category;
    private String description;
    private String photoPath;
    private LocalDate date;
    private String landmark;
    private double latitude;
    private double longitude;
    private Type type;
    private User poster;
    private boolean resolved;
    private boolean flagged;

    public LostFoundReport(String title, String category, String description,
                           double latitude, double longitude, Type type, User poster) {
        this.id = nextId++;
        this.title = title;
        this.category = category;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.poster = poster;
        this.date = LocalDate.now();
    }

    /** Haversine distance in meters. */
    public double distanceTo(double lat, double lon) {
        final double R = 6371000;
        double dLat = Math.toRadians(lat - latitude);
        double dLon = Math.toRadians(lon - longitude);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(lat))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public String getPhotoPath() { return photoPath; }
    public void setPhotoPath(String photoPath) { this.photoPath = photoPath; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public String getLandmark() { return landmark; }
    public void setLandmark(String landmark) { this.landmark = landmark; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public Type getType() { return type; }
    public User getPoster() { return poster; }
    public boolean isResolved() { return resolved; }
    public void setResolved(boolean resolved) { this.resolved = resolved; }
    public boolean isFlagged() { return flagged; }
    public void setFlagged(boolean flagged) { this.flagged = flagged; }

    @Override
    public String toString() {
        return "#" + id + " [" + type + "] " + title + " (" + category + ") on " + date
                + (landmark != null ? " near " + landmark : "")
                + (resolved ? " - RESOLVED" : "") + (flagged ? " - FLAGGED" : "");
    }
}
