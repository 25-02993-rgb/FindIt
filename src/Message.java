import java.time.LocalDateTime;

/** In-app message between an item owner and a finder. */
public class Message {
    private final User sender;
    private final User receiver;
    private final LostFoundReport report;
    private final String text;
    private final LocalDateTime sentAt = LocalDateTime.now();

    public Message(User sender, User receiver, LostFoundReport report, String text) {
        this.sender = sender;
        this.receiver = receiver;
        this.report = report;
        this.text = text;
    }

    public User getSender() { return sender; }
    public User getReceiver() { return receiver; }
    public LostFoundReport getReport() { return report; }

    @Override
    public String toString() { return sender.getName() + " wrote: " + text; }
}
