import java.util.ArrayList;

/** A campus user who found an item and submitted a found report. */
public class Finder extends User {
    private ArrayList<LostFoundReport> myFoundReports = new ArrayList<>();

    public Finder(String name, String email, String password) {
        super(name, email, password);
    }

    public LostFoundReport reportFound(String title, String category, String description,
                                       double lat, double lon) {
        LostFoundReport report = new LostFoundReport(title, category, description, lat, lon,
                LostFoundReport.Type.FOUND, this);
        myFoundReports.add(report);
        return report;
    }

    public void markResolved(LostFoundReport report) {
        if (myFoundReports.contains(report)) report.setResolved(true);
    }

    public ArrayList<LostFoundReport> getMyFoundReports() { return myFoundReports; }
}
