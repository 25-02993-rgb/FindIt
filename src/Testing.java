/** Checks the features of the FindIt lost-and-found system. */
public class Testing {
    public static void run() {
        FindItSystem system = new FindItSystem();
        String domain = FindItSystem.CAMPUS_DOMAIN;

        Owner owner = new Owner("Ana", "ana" + domain, "pw");
        Finder finder = new Finder("Ben", "ben" + domain, "pw");
        Admin admin = new Admin("Mod", "mod" + domain, "pw");
        User outsider = new User("Eve", "eve@gmail.com", "pw");

        check("Campus emails can register",
                system.register(owner) && system.register(finder) && system.register(admin));
        check("Non-campus email rejected", !system.register(outsider));
        check("Login works", system.login("ana" + domain, "pw") == owner);

        LostFoundReport lost = owner.reportLost("Black wallet", "Wallet",
                "black leather wallet with ID", 13.7850, 121.0736);
        lost.setLandmark("Library");
        system.submitReport(lost);

        LostFoundReport found = system.submitAtLandmark(finder, LostFoundReport.Type.FOUND,
                "Found wallet", "Wallet", "black wallet on a table", "Library");
        check("Landmark report is pinned",
                found != null && system.getMap().getPins().size() == 2);

        system.updateLocation(owner, 13.7850, 121.0737);
        check("Owner notified of nearby found wallet",
                system.notificationsFor(owner).size() == 1);
        system.updateLocation(owner, 13.7850, 121.0737);
        check("No duplicate notification",
                system.notificationsFor(owner).size() == 1);

        system.updateLocation(finder, 13.7900, 121.0800);
        check("Far-away finder not notified", system.notificationsFor(finder).isEmpty());

        system.sendMessage(owner, finder, found, "Hi, I think that's my wallet.");
        system.sendMessage(finder, owner, found, "What ID is inside?");
        check("Conversation stored", system.conversation(found, owner, finder).size() == 2);

        system.flagReport(lost);
        check("Flagged report in moderation queue", system.moderationQueue().size() == 1);
        check("Only poster/admin can resolve",
                !system.markResolved(owner, found) && system.markResolved(finder, found));
        check("Resolved report leaves the map search",
                system.getMap().reportsNear(13.7850, 121.0736, 100).isEmpty());
    }

    private static void check(String name, boolean ok) {
        System.out.println((ok ? "PASS  " : "FAIL  ") + name);
    }
}
