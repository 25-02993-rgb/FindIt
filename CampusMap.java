import java.util.ArrayList;

/** Campus map: landmark list plus reports pinned on it. */
public class CampusMap {
    private ArrayList<Landmark> landmarks = new ArrayList<>();
    private ArrayList<LostFoundReport> pins = new ArrayList<>();

    public CampusMap() {
        addLandmark(new Landmark("Main Gate", 13.7833, 121.0728));
        addLandmark(new Landmark("Library", 13.7838, 121.0733));
        addLandmark(new Landmark("Canteen", 13.7840, 121.0730));
        addLandmark(new Landmark("Gymnasium", 13.7835, 121.0738));
        addLandmark(new Landmark("Engineering Building", 13.7842, 121.0736));
    }

    public void addLandmark(Landmark landmark) {
        landmarks.add(landmark);
    }

    public Landmark findLandmark(String name) {
        for (Landmark landmark : landmarks)
            if (landmark.getName().equalsIgnoreCase(name)) return landmark;
        return null;
    }

    public void addPin(LostFoundReport report) { pins.add(report); }
    public void removePin(LostFoundReport report) { pins.remove(report); }

    /** Open reports within radiusMeters of a point. */
    public ArrayList<LostFoundReport> reportsNear(double lat, double lon, double radiusMeters) {
        ArrayList<LostFoundReport> result = new ArrayList<>();
        for (LostFoundReport report : pins) {
            if (!report.isResolved() && !report.isFlagged()
                    && report.distanceTo(lat, lon) <= radiusMeters)
                result.add(report);
        }
        return result;
    }

    public ArrayList<Landmark> getLandmarks() { return landmarks; }
    public ArrayList<LostFoundReport> getPins() { return pins; }
}
