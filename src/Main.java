import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int choice;
        do {
            System.out.println("\n=== FindIt: BatStateU-Alangilan Lost & Found ===");
            System.out.println("1. About FindIt");
            System.out.println("2. Run feature tests");
            System.out.println("0. Exit");
            System.out.print("Choose: ");
            try { choice = Integer.parseInt(in.nextLine().trim()); }
            catch (NumberFormatException e) { choice = -1; }

            if (choice == 1) showAbout();
            else if (choice == 2) Testing.run();
        } while (choice != 0);
    }

    static void showAbout() {
        System.out.println("\n--- Problem ---");
        System.out.println("Students, faculty, and staff may lose IDs, wallets, keys, phones, and");
        System.out.println("school materials on campus. Reports are scattered across social media");
        System.out.println("and group chats, so they are hard to organize, search, and follow up.");
        System.out.println("\n--- Solution ---");
        System.out.println("FindIt is an Android app for the BatStateU-Alangilan community. Users");
        System.out.println("submit lost/found reports with photo, description, category, date, and");
        System.out.println("location, browse them on a campus map, get alerts for reports within");
        System.out.println("~100 m (subject to device permissions), and message each other to");
        System.out.println("arrange safe returns.");
        System.out.println("\n--- Features ---");
        System.out.println("* Campus-scoped user access");
        System.out.println("* Lost and found reports");
        System.out.println("* Interactive campus map with landmark selector");
        System.out.println("* Nearby report detection (~100 m)");
        System.out.println("* Notifications");
        System.out.println("* Owner-finder communication");
        System.out.println("* Report status and moderation");
        System.out.println("\n--- SDGs ---");
        System.out.println("SDG 11: Sustainable Cities and Communities");
        System.out.println("SDG 12: Responsible Consumption and Production");
        System.out.println("\nScope: personal belongings within BatStateU-Alangilan only. No other");
        System.out.println("campuses, pets, or missing-person reports. Recovery is not guaranteed.");
    }
}
