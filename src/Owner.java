import java.util.ArrayList;

/** A campus user who has lost an item and searches for matching found reports. */
public class Owner extends User {
    private Keywords keywords = new Keywords();
    private ArrayList<LostFoundReport> myLostReports = new ArrayList<>();

    public Owner(String name, String email, String password) {
        super(name, email, password);
    }

    public LostFoundReport reportLost(String title, String category, String description,
                                      double lat, double lon) {
        LostFoundReport report = new LostFoundReport(title, category, description, lat, lon,
                LostFoundReport.Type.LOST, this);
        myLostReports.add(report);
        keywords.addFromText(title + " " + description);
        return report;
    }

    public ArrayList<LostFoundReport> findMatches(ArrayList<LostFoundReport> allReports,
                                                   double myLat, double myLon, double radiusMeters) {
        ArrayList<LostFoundReport> result = new ArrayList<>();
        for (LostFoundReport report : allReports) {
            if (report.getType() == LostFoundReport.Type.FOUND
                    && !report.isResolved()
                    && report.distanceTo(myLat, myLon) <= radiusMeters
                    && keywords.matchScore(report.getTitle() + " " + report.getDescription()) > 0) {
                result.add(report);
            }
        }
        return result;
    }

    public Keywords getKeywords() { return keywords; }
    public ArrayList<LostFoundReport> getMyLostReports() { return myLostReports; }
}
