
public class Notification {
    private final User recipient;
    private final LostFoundReport report;
    private final String text;

    public Notification(User recipient, LostFoundReport report, String text) {
        this.recipient = recipient;
        this.report = report;
        this.text = text;
    }

    public User getRecipient() { return recipient; }
    public LostFoundReport getReport() { return report; }

    @Override
    public String toString() { return "To " + recipient.getName() + ": " + text; }
}
