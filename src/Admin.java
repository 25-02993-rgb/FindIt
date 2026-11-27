import java.util.ArrayList;

/** Moderator who reviews suspicious or inappropriate item reports. */
public class Admin extends User {
    public Admin(String name, String email, String password) {
        super(name, email, password);
    }

    public void flag(LostFoundReport report) { report.setFlagged(true); }

    public void remove(ArrayList<LostFoundReport> allReports, LostFoundReport report) {
        allReports.remove(report);
    }

    public ArrayList<LostFoundReport> reviewQueue(ArrayList<LostFoundReport> allReports) {
        ArrayList<LostFoundReport> queue = new ArrayList<>();
        for (LostFoundReport report : allReports)
            if (report.isFlagged()) queue.add(report);
        return queue;
    }
}
