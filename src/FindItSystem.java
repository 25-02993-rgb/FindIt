import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/** Central controller for the FindIt lost-and-found system. */
public class FindItSystem {
    public static final String CAMPUS_DOMAIN = "@g.batstate-u.edu.ph";
    public static final double NEARBY_RADIUS_METERS = 100;

    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<LostFoundReport> reports = new ArrayList<>();
    private ArrayList<Message> messages = new ArrayList<>();
    private ArrayList<Notification> notifications = new ArrayList<>();
    private HashMap<Integer, double[]> locations = new HashMap<>();
    private HashSet<String> alreadyNotified = new HashSet<>();
    private CampusMap map = new CampusMap();

    public boolean register(User u) {
        if (!u.getEmail().toLowerCase().endsWith(CAMPUS_DOMAIN)) return false;
        for (User existing : users)
            if (existing.getEmail().equalsIgnoreCase(u.getEmail())) return false;
        users.add(u);
        return true;
    }

    public User login(String email, String password) {
        for (User u : users)
            if (u.login(email, password)) return u;
        return null;
    }

    public void submitReport(LostFoundReport report) {
        reports.add(report);
        map.addPin(report);
    }

    public LostFoundReport submitAtLandmark(User poster, LostFoundReport.Type type, String title,
                                             String category, String description, String landmarkName) {
        Landmark landmark = map.findLandmark(landmarkName);
        if (landmark == null) return null;
        LostFoundReport report = new LostFoundReport(title, category, description,
                landmark.getLatitude(), landmark.getLongitude(), type, poster);
        report.setLandmark(landmark.getName());
        submitReport(report);
        return report;
    }

    public void updateLocation(User u, double lat, double lon) {
        locations.put(u.getId(), new double[]{lat, lon});
        checkNearby(u);
    }

    public ArrayList<Notification> checkNearby(User u) {
        ArrayList<Notification> created = new ArrayList<>();
        double[] loc = locations.get(u.getId());
        if (loc == null) return created;

        for (LostFoundReport report : map.reportsNear(loc[0], loc[1], NEARBY_RADIUS_METERS)) {
            String key = u.getId() + "-" + report.getId();
            if (report.getPoster() == u || alreadyNotified.contains(key) || !isRelevant(u, report)) continue;

            Notification notification = new Notification(u, report,
                    "A " + report.getType() + " report is within ~"
                            + (int) NEARBY_RADIUS_METERS + " m: " + report.getTitle());
            notifications.add(notification);
            created.add(notification);
            alreadyNotified.add(key);
        }
        return created;
    }

    private boolean isRelevant(User u, LostFoundReport report) {
        if (u instanceof Owner) {
            return report.getType() == LostFoundReport.Type.FOUND
                    && ((Owner) u).getKeywords()
                            .matchScore(report.getTitle() + " " + report.getDescription()) > 0;
        }
        return report.getType() == LostFoundReport.Type.LOST;
    }

    public Message sendMessage(User from, User to, LostFoundReport report, String text) {
        if (!users.contains(from) || !users.contains(to)) return null;
        Message message = new Message(from, to, report, text);
        messages.add(message);
        return message;
    }

    public ArrayList<Message> conversation(LostFoundReport report, User a, User b) {
        ArrayList<Message> result = new ArrayList<>();
        for (Message message : messages) {
            boolean pair = (message.getSender() == a && message.getReceiver() == b)
                    || (message.getSender() == b && message.getReceiver() == a);
            if (message.getReport() == report && pair) result.add(message);
        }
        return result;
    }

    public boolean markResolved(User by, LostFoundReport report) {
        if (report.getPoster() != by && !(by instanceof Admin)) return false;
        report.setResolved(true);
        return true;
    }

    public void flagReport(LostFoundReport report) { report.setFlagged(true); }

    public void removeReport(Admin admin, LostFoundReport report) {
        reports.remove(report);
        map.removePin(report);
    }

    public ArrayList<LostFoundReport> moderationQueue() {
        ArrayList<LostFoundReport> queue = new ArrayList<>();
        for (LostFoundReport report : reports)
            if (report.isFlagged()) queue.add(report);
        return queue;
    }

    public ArrayList<Notification> notificationsFor(User u) {
        ArrayList<Notification> result = new ArrayList<>();
        for (Notification notification : notifications)
            if (notification.getRecipient() == u) result.add(notification);
        return result;
    }

    public CampusMap getMap() { return map; }
    public ArrayList<LostFoundReport> getReports() { return reports; }
}
