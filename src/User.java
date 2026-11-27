/**
 * Base class for everyone who uses FindIt (BatStateU-Alangilan community).
 * Mirrors User.java in Resumatch.
 */
public class User {
    private static int nextId = 1;

    private final int id;
    private String name;
    private String email;      // campus email
    private String password;

    public User(String name, String email, String password) {
        this.id = nextId++;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public boolean login(String email, String password) {
        return this.email.equalsIgnoreCase(email) && this.password.equals(password);
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public void setName(String name) { this.name = name; }

    @Override
    public String toString() {
        return "[" + id + "] " + name + " <" + email + ">";
    }
}
