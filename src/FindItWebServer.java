import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Upgraded Java HTTP Web Server for FindIt: BatStateU-Alangilan Lost & Found.
 * Features:
 *  - Interactive Leaflet.js Campus Map with ~100m GPS proximity radar
 *  - Photo attachments wired to LostFoundReport.setPhotoPath(...)
 *  - Split-pane Owner-Finder messenger, toast alerts & unread notification counter
 */
public class FindItWebServer {
    private static final FindItSystem system = new FindItSystem();
    private static User currentUser = null;

    private static Owner demoOwner;
    private static Finder demoFinder;
    private static Admin demoAdmin;

    public static void main(String[] args) throws IOException {
        seedDemoData();

        int port = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/", FindItWebServer::handleHtmlPage);
        server.createContext("/photos/", FindItWebServer::handlePhoto);
        server.createContext("/api/state", FindItWebServer::handleState);
        server.createContext("/api/login", FindItWebServer::handleLogin);
        server.createContext("/api/register", FindItWebServer::handleRegister);
        server.createContext("/api/logout", FindItWebServer::handleLogout);
        server.createContext("/api/report", FindItWebServer::handleCreateReport);
        server.createContext("/api/resolve", FindItWebServer::handleResolveReport);
        server.createContext("/api/flag", FindItWebServer::handleFlagReport);
        server.createContext("/api/remove", FindItWebServer::handleRemoveReport);
        server.createContext("/api/location", FindItWebServer::handleLocationUpdate);
        server.createContext("/api/message", FindItWebServer::handleSendMessage);

        server.setExecutor(null);
        server.start();
        System.out.println("====================================================");
        System.out.println("FindIt Website (Upgraded UI) is running!");
        System.out.println("Open in your browser: http://localhost:" + port);
        System.out.println("====================================================");
    }

    private static void seedDemoData() {
        demoOwner = new Owner("Juan Dela Cruz", "juan.delacruz@g.batstate-u.edu.ph", "pass123");
        demoOwner.getKeywords().addFromText("wallet black leather id student");

        demoFinder = new Finder("Maria Santos", "maria.santos@g.batstate-u.edu.ph", "pass123");
        demoAdmin = new Admin("Campus Moderator", "admin.alangilan@g.batstate-u.edu.ph", "admin123");

        system.register(demoOwner);
        system.register(demoFinder);
        system.register(demoAdmin);

        LostFoundReport r1 = system.submitAtLandmark(
                demoOwner, LostFoundReport.Type.LOST,
                "Black Leather Wallet & BatStateU ID", "Wallet / ID",
                "Lost near the tables outside Canteen around 11:00 AM. Contains student ID and ₱200.", "Canteen"
        );
        if (r1 != null) r1.setPhotoPath("/photos/wallet_black.jpg");

        LostFoundReport r2 = system.submitAtLandmark(
                demoFinder, LostFoundReport.Type.FOUND,
                "Found Black Wallet with Student ID", "Wallet / ID",
                "Picked up a black leather wallet on a study bench near Library entrance.", "Library"
        );
        if (r2 != null) r2.setPhotoPath("/photos/wallet_black.jpg");

        LostFoundReport r3 = system.submitAtLandmark(
                demoFinder, LostFoundReport.Type.FOUND,
                "Blue Aquaflask 24oz Tumbler", "Personal Item",
                "Left behind at the lower bleachers inside Gymnasium after PE class.", "Gymnasium"
        );
        if (r3 != null) r3.setPhotoPath("/photos/aquaflask_blue.jpg");


        // ---- More sample reports (photos are real files in webui/photos/) ----
        seed(demoOwner,  LostFoundReport.Type.LOST,  "Black iPhone 11 with Clear Case", "Electronics / Phone",
             "Lost after class. Cracked corner on the clear case, lock screen is a blue wallpaper. Has a small sticker on the back.", "Library", "phone_iphone.jpg", 1);
        seed(demoFinder, LostFoundReport.Type.FOUND, "White Wireless Earbuds in Charging Case", "Electronics / Phone",
             "Found on a table near the food stalls. Case has a small scratch on the lid.", "Canteen", "earbuds_white.jpg", 0);
        seed(demoOwner,  LostFoundReport.Type.LOST,  "Keys with Red Lanyard and Mini Flashlight", "Keys",
             "Three keys on a red lanyard with a small flashlight keychain. Last seen in the engineering lab hallway.", "Engineering Building", "keys_lanyard.jpg", 2);
        seed(demoFinder, LostFoundReport.Type.FOUND, "Motorcycle Key with Keychain", "Keys",
             "Found on the ground near the guardhouse. Single motorcycle key with a rubber keychain.", "Main Gate", "motorcycle_key.jpg", 0);
        seed(demoOwner,  LostFoundReport.Type.LOST,  "Gray Backpack with Black Straps", "Bag / School Materials",
             "Left after PE class. Has a laptop sleeve inside and a keychain on the zipper.", "Gymnasium", "backpack_gray.jpg", 3);
        seed(demoFinder, LostFoundReport.Type.FOUND, "Casio fx-991EX Scientific Calculator", "Bag / School Materials",
             "Found on a desk after an exam. Name is written on the back cover, ask the finder to verify.", "Engineering Building", "calculator_casio.jpg", 1);
        seed(demoOwner,  LostFoundReport.Type.LOST,  "Prescription Eyeglasses in Black Case", "Personal Item",
             "Thin black frame glasses in a hard black case. Probably left on a study table.", "Library", "eyeglasses.jpg", 2);
        seed(demoFinder, LostFoundReport.Type.FOUND, "Black Foldable Umbrella", "Personal Item",
             "Found near the entrance during the rain. Plain black with a wooden-style handle.", "Main Gate", "umbrella_black.jpg", 1);
        seed(demoOwner,  LostFoundReport.Type.LOST,  "Notebook and Pencil Case", "Bag / School Materials",
             "Blue spiral notebook with class notes and a zippered pencil case with pens and a ruler.", "Engineering Building", "notebook_pencilcase.jpg", 4);
        seed(demoFinder, LostFoundReport.Type.FOUND, "Power Bank with USB Charging Cable", "Electronics / Phone",
             "Found plugged into an outlet near the study area. White power bank with a short cable attached.", "Library", "powerbank.jpg", 0);
        seed(demoOwner,  LostFoundReport.Type.LOST,  "Brown Cardholder with ATM Card", "Wallet / ID",
             "Small brown leather cardholder with an ATM card and a school library card inside.", "Canteen", "cardholder_brown.jpg", 2);
        seed(demoFinder, LostFoundReport.Type.FOUND, "Green Tumbler with Stickers", "Personal Item",
             "Green insulated tumbler with a few stickers. Found on a canteen table after lunch.", "Canteen", "tumbler_green.jpg", 1);
        seed(demoOwner,  LostFoundReport.Type.LOST,  "Silver Wristwatch", "Personal Item",
             "Silver analog wristwatch with a metal strap. Possibly slipped off in the gym area.", "Gymnasium", "wristwatch.jpg", 3);
        seed(demoFinder, LostFoundReport.Type.FOUND, "Student ID with Blue Lace", "Wallet / ID",
             "Student ID on a blue lanyard, found near the gate. Owner can describe the photo/name to claim.", "Main Gate", "id_lace.jpg", 0);

        system.sendMessage(demoFinder, demoOwner, r2, "Hi Juan! I found a black wallet near the Library. Does it have your BatStateU ID?");
        system.sendMessage(demoOwner, demoFinder, r2, "Yes! My student ID is inside. Can we meet at the Main Gate guardhouse?");

        Landmark lib = system.getMap().findLandmark("Library");
        if (lib != null) {
            system.updateLocation(demoOwner, lib.getLatitude(), lib.getLongitude());
        }
    }


    /** Adds a seeded report whose photo is a real file at webui/photos/<photoFile>. */
    private static void seed(User poster, LostFoundReport.Type type, String title, String category,
                             String description, String landmark, String photoFile, int daysAgo) {
        LostFoundReport r = system.submitAtLandmark(poster, type, title, category, description, landmark);
        if (r == null) return;
        r.setPhotoPath("/photos/" + photoFile);
        r.setDate(java.time.LocalDate.now().minusDays(daysAgo));
    }

    /** Serves real photo files from webui/photos/. */
    private static void handlePhoto(HttpExchange ex) throws IOException {
        String name = ex.getRequestURI().getPath().substring("/photos/".length());
        if (!name.matches("[A-Za-z0-9_\\-]+\\.(jpg|jpeg|png|webp)")) {
            sendText(ex, 404, "text/plain; charset=UTF-8", "Not found");
            return;
        }
        Path file = findWebRoot().resolve("photos").resolve(name);
        if (!Files.isRegularFile(file)) {
            sendText(ex, 404, "text/plain; charset=UTF-8", "Photo not found: " + name);
            return;
        }
        String lower = name.toLowerCase();
        String type = lower.endsWith(".png") ? "image/png" : lower.endsWith(".webp") ? "image/webp" : "image/jpeg";
        byte[] bytes = Files.readAllBytes(file);
        ex.getResponseHeaders().set("Content-Type", type);
        ex.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = ex.getResponseBody()) { os.write(bytes); }
    }

    /** Demo-only item photo drawn as inline SVG (no files needed). kind: wallet | bottle */
    private static String makeSampleSvgPhoto(String label, String bg, String fg) {
        return makeDemoPhoto(label.toLowerCase().contains("aqua") ? "bottle" : "wallet", label, bg, fg);
    }

    private static String makeDemoPhoto(String kind, String label, String bg, String fg) {
        String art;
        if (kind.equals("bottle")) {
            art = "<rect x='140' y='28' width='40' height='16' rx='4' fill='#e2e8f0'/>"
                + "<rect x='132' y='42' width='56' height='108' rx='16' fill='#2563eb'/>"
                + "<rect x='132' y='70' width='56' height='26' fill='#1e40af'/>"
                + "<rect x='142' y='50' width='8' height='90' rx='4' fill='#fff' opacity='0.25'/>";
        } else {
            art = "<rect x='86' y='52' width='148' height='96' rx='12' fill='#1c1917'/>"
                + "<rect x='86' y='52' width='148' height='28' rx='12' fill='#292524'/>"
                + "<rect x='196' y='84' width='38' height='30' rx='8' fill='#44403c'/>"
                + "<circle cx='212' cy='99' r='5' fill='#eab308'/>"
                + "<rect x='104' y='36' width='72' height='46' rx='5' fill='#fff' transform='rotate(-6 140 59)'/>"
                + "<rect x='110' y='42' width='20' height='24' rx='3' fill='#990000' transform='rotate(-6 140 59)'/>"
                + "<rect x='136' y='44' width='34' height='5' rx='2' fill='#94a3b8' transform='rotate(-6 140 59)'/>"
                + "<rect x='136' y='54' width='26' height='5' rx='2' fill='#cbd5e1' transform='rotate(-6 140 59)'/>"
                + "<rect x='86' y='52' width='148' height='96' rx='12' fill='none' stroke='#57534e' stroke-width='2' stroke-dasharray='5 4' transform='translate(0 0)' opacity='0.0'/>";
        }
        String svg = "<svg xmlns='http://www.w3.org/2000/svg' width='320' height='200' viewBox='0 0 320 200'>"
                + "<rect width='320' height='200' fill='" + bg + "'/>"
                + "<ellipse cx='160' cy='158' rx='80' ry='9' fill='#000' opacity='0.25'/>"
                + art
                + "<text x='160' y='182' text-anchor='middle' fill='" + fg + "' "
                + "font-family='Segoe UI, Arial, sans-serif' font-size='13' font-weight='bold'>"
                + label.replace("&", "&amp;") + " (demo photo)</text></svg>";
        return "data:image/svg+xml;base64,"
                + java.util.Base64.getEncoder().encodeToString(svg.getBytes(StandardCharsets.UTF_8));
    }

    // ==================== WEB PAGE HANDLER ====================
    private static void handleHtmlPage(HttpExchange ex) throws IOException {
        String requestPath = ex.getRequestURI().getPath();
        if (!requestPath.equals("/") && !requestPath.equals("/index.html")
                && !requestPath.equals("/style.css") && !requestPath.equals("/styles.css")
                && !requestPath.equals("/app.js")) {
            sendText(ex, 404, "text/plain; charset=UTF-8", "Not found");
            return;
        }

        String fileName;
        String contentType;
        if (requestPath.equals("/style.css") || requestPath.equals("/styles.css")) {
            fileName = "style.css";
            contentType = "text/css; charset=UTF-8";
        } else if (requestPath.equals("/app.js")) {
            fileName = "app.js";
            contentType = "application/javascript; charset=UTF-8";
        } else {
            fileName = "index.html";
            contentType = "text/html; charset=UTF-8";
        }

        Path webRoot = findWebRoot();
        Path file = webRoot.resolve(fileName);
        if (!Files.isRegularFile(file)) {
            sendText(ex, 404, "text/plain; charset=UTF-8",
                    "Missing web file: " + file.toAbsolutePath());
            return;
        }
        sendText(ex, 200, contentType, Files.readString(file, StandardCharsets.UTF_8));
    }

    private static Path findWebRoot() {
        Path cwd = Paths.get("").toAbsolutePath();
        Path[] candidates = {
                cwd.resolve("webui"),
                cwd.resolve("..").resolve("webui"),
                cwd.resolve("..").resolve("..").resolve("webui")
        };
        for (Path candidate : candidates) {
            if (Files.isDirectory(candidate)) return candidate.normalize();
        }
        return cwd.resolve("webui");
    }

    // ==================== API HANDLERS ====================
    private static void handleState(HttpExchange ex) throws IOException {
        StringBuilder json = new StringBuilder("{");

        if (currentUser == null) {
            json.append("\"user\":null,");
        } else {
            String role = (currentUser instanceof Admin) ? "Admin"
                    : (currentUser instanceof Owner) ? "Owner" : "Finder";
            json.append("\"user\":{")
                .append("\"id\":").append(currentUser.getId()).append(",")
                .append("\"name\":\"").append(esc(currentUser.getName())).append("\",")
                .append("\"email\":\"").append(esc(currentUser.getEmail())).append("\",")
                .append("\"role\":\"").append(role).append("\"},");
        }

        // Landmarks
        json.append("\"landmarks\":[");
        ArrayList<Landmark> lms = system.getMap().getLandmarks();
        for (int i = 0; i < lms.size(); i++) {
            Landmark lm = lms.get(i);
            if (i > 0) json.append(",");
            json.append("{\"name\":\"").append(esc(lm.getName())).append("\",")
                .append("\"lat\":").append(lm.getLatitude()).append(",")
                .append("\"lon\":").append(lm.getLongitude()).append("}");
        }
        json.append("],");

        // Reports (including photoPath)
        json.append("\"reports\":[");
        ArrayList<LostFoundReport> reps = system.getReports();
        for (int i = 0; i < reps.size(); i++) {
            LostFoundReport r = reps.get(i);
            if (i > 0) json.append(",");
            json.append("{\"id\":").append(r.getId()).append(",")
                .append("\"type\":\"").append(r.getType()).append("\",")
                .append("\"title\":\"").append(esc(r.getTitle())).append("\",")
                .append("\"category\":\"").append(esc(r.getCategory())).append("\",")
                .append("\"description\":\"").append(esc(r.getDescription())).append("\",")
                .append("\"photoPath\":\"").append(esc(r.getPhotoPath() != null ? r.getPhotoPath() : "")).append("\",")
                .append("\"landmark\":\"").append(esc(r.getLandmark() != null ? r.getLandmark() : "Campus")).append("\",")
                .append("\"lat\":").append(r.getLatitude()).append(",")
                .append("\"lon\":").append(r.getLongitude()).append(",")
                .append("\"date\":\"").append(r.getDate()).append("\",")
                .append("\"posterId\":").append(r.getPoster().getId()).append(",")
                .append("\"posterName\":\"").append(esc(r.getPoster().getName())).append("\",")
                .append("\"posterEmail\":\"").append(esc(r.getPoster().getEmail())).append("\",")
                .append("\"resolved\":").append(r.isResolved()).append(",")
                .append("\"flagged\":").append(r.isFlagged()).append("}");
        }
        json.append("],");

        // Notifications
        json.append("\"notifications\":[");
        if (currentUser != null) {
            ArrayList<Notification> notifs = system.notificationsFor(currentUser);
            for (int i = 0; i < notifs.size(); i++) {
                if (i > 0) json.append(",");
                json.append("{\"reportId\":").append(notifs.get(i).getReport().getId()).append(",")
                    .append("\"text\":\"").append(esc(notifs.get(i).toString())).append("\"}");
            }
        }
        json.append("],");

        // Messages
        json.append("\"messages\":[");
        if (currentUser != null) {
            boolean first = true;
            for (LostFoundReport r : reps) {
                User other = (r.getPoster() == currentUser)
                        ? ((currentUser == demoOwner) ? demoFinder : demoOwner)
                        : r.getPoster();
                for (Message m : system.conversation(r, currentUser, other)) {
                    if (!first) json.append(",");
                    first = false;
                    boolean mine = m.getSender() == currentUser;
                    String rawText = m.toString().replace(m.getSender().getName() + " wrote: ", "");
                    json.append("{\"reportId\":").append(r.getId()).append(",")
                        .append("\"sender\":\"").append(esc(m.getSender().getName())).append("\",")
                        .append("\"mine\":").append(mine).append(",")
                        .append("\"text\":\"").append(esc(rawText)).append("\"}");
                }
            }
        }
        json.append("]}");

        sendJson(ex, 200, json.toString());
    }

    private static void handleLogin(HttpExchange ex) throws IOException {
        Map<String, String> params = parseForm(ex);
        User u = system.login(params.getOrDefault("email", ""), params.getOrDefault("password", ""));
        if (u == null) {
            sendJson(ex, 401, "{\"error\":\"Invalid credentials or unregistered email.\"}");
            return;
        }
        currentUser = u;
        sendJson(ex, 200, "{\"ok\":true}");
    }

    private static void handleRegister(HttpExchange ex) throws IOException {
        Map<String, String> params = parseForm(ex);
        String name = params.getOrDefault("name", "").trim();
        String email = params.getOrDefault("email", "").trim();
        String password = params.getOrDefault("password", "");
        String role = params.getOrDefault("role", "Owner");
        String keywords = params.getOrDefault("keywords", "");

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            sendJson(ex, 400, "{\"error\":\"All fields are required.\"}");
            return;
        }

        User u;
        if ("Admin".equalsIgnoreCase(role)) {
            u = new Admin(name, email, password);
        } else if ("Finder".equalsIgnoreCase(role)) {
            u = new Finder(name, email, password);
        } else {
            Owner o = new Owner(name, email, password);
            o.getKeywords().addFromText(keywords);
            u = o;
        }

        if (!system.register(u)) {
            sendJson(ex, 400, "{\"error\":\"Must use a unique @g.batstate-u.edu.ph email.\"}");
            return;
        }
        currentUser = u;
        sendJson(ex, 200, "{\"ok\":true}");
    }

    private static void handleLogout(HttpExchange ex) throws IOException {
        currentUser = null;
        sendJson(ex, 200, "{\"ok\":true}");
    }

    private static void handleCreateReport(HttpExchange ex) throws IOException {
        if (currentUser == null) { sendJson(ex, 401, "{\"error\":\"Login required\"}"); return; }
        Map<String, String> params = parseForm(ex);
        LostFoundReport.Type type = "FOUND".equalsIgnoreCase(params.get("type"))
                ? LostFoundReport.Type.FOUND : LostFoundReport.Type.LOST;
        String title = params.getOrDefault("title", "").trim();
        String category = params.getOrDefault("category", "Other");
        String landmark = params.getOrDefault("landmark", "Main Gate");
        String description = params.getOrDefault("description", "").trim();
        String photoPath = params.getOrDefault("photoPath", "").trim();

        LostFoundReport created = system.submitAtLandmark(currentUser, type, title, category, description, landmark);
        if (created != null) {
            if (!photoPath.isEmpty()) {
                created.setPhotoPath(photoPath);
            }
            if (currentUser instanceof Owner && type == LostFoundReport.Type.LOST) {
                ((Owner) currentUser).getKeywords().addFromText(title + " " + description);
            }
        }
        sendJson(ex, 200, "{\"ok\":true}");
    }

    private static void handleResolveReport(HttpExchange ex) throws IOException {
        if (currentUser == null) { sendJson(ex, 401, "{\"error\":\"Login required\"}"); return; }
        int id = Integer.parseInt(parseForm(ex).getOrDefault("id", "-1"));
        LostFoundReport r = findReportById(id);
        if (r != null && system.markResolved(currentUser, r)) {
            sendJson(ex, 200, "{\"ok\":true}");
        } else {
            sendJson(ex, 403, "{\"error\":\"Only the original poster or an Admin can resolve this report.\"}");
        }
    }

    private static void handleFlagReport(HttpExchange ex) throws IOException {
        int id = Integer.parseInt(parseForm(ex).getOrDefault("id", "-1"));
        LostFoundReport r = findReportById(id);
        if (r != null) system.flagReport(r);
        sendJson(ex, 200, "{\"ok\":true}");
    }

    private static void handleRemoveReport(HttpExchange ex) throws IOException {
        if (!(currentUser instanceof Admin)) {
            sendJson(ex, 403, "{\"error\":\"Admin access required.\"}");
            return;
        }
        int id = Integer.parseInt(parseForm(ex).getOrDefault("id", "-1"));
        LostFoundReport r = findReportById(id);
        if (r != null) system.removeReport((Admin) currentUser, r);
        sendJson(ex, 200, "{\"ok\":true}");
    }

    private static void handleLocationUpdate(HttpExchange ex) throws IOException {
        if (currentUser == null) { sendJson(ex, 401, "{\"error\":\"Login required\"}"); return; }
        String lmName = parseForm(ex).getOrDefault("landmark", "");
        Landmark lm = system.getMap().findLandmark(lmName);
        int before = system.notificationsFor(currentUser).size();
        if (lm != null) {
            system.updateLocation(currentUser, lm.getLatitude(), lm.getLongitude());
        }
        int added = system.notificationsFor(currentUser).size() - before;
        sendJson(ex, 200, "{\"ok\":true,\"newAlerts\":" + added + "}");
    }

    private static void handleSendMessage(HttpExchange ex) throws IOException {
        if (currentUser == null) { sendJson(ex, 401, "{\"error\":\"Login required\"}"); return; }
        Map<String, String> params = parseForm(ex);
        int id = Integer.parseInt(params.getOrDefault("reportId", "-1"));
        String text = params.getOrDefault("text", "").trim();
        LostFoundReport r = findReportById(id);
        if (r != null && !text.isEmpty()) {
            User recipient = (r.getPoster() == currentUser)
                    ? ((currentUser == demoOwner) ? demoFinder : demoOwner)
                    : r.getPoster();
            system.sendMessage(currentUser, recipient, r, text);
        }
        sendJson(ex, 200, "{\"ok\":true}");
    }

    private static LostFoundReport findReportById(int id) {
        for (LostFoundReport r : system.getReports()) {
            if (r.getId() == id) return r;
        }
        return null;
    }

    // ==================== HTTP UTILITIES ====================
    private static void sendText(HttpExchange ex, int status, String contentType, String text) throws IOException {
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", contentType);
        ex.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = ex.getResponseBody()) { os.write(bytes); }
    }

    private static Map<String, String> parseForm(HttpExchange ex) throws IOException {
        InputStream is = ex.getRequestBody();
        String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        Map<String, String> map = new HashMap<>();
        for (String pair : body.split("&")) {
            String[] kv = pair.split("=", 2);
            if (kv.length == 2) {
                map.put(URLDecoder.decode(kv[0], StandardCharsets.UTF_8),
                        URLDecoder.decode(kv[1], StandardCharsets.UTF_8));
            }
        }
        return map;
    }

    private static void sendJson(HttpExchange ex, int status, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        ex.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = ex.getResponseBody()) { os.write(bytes); }
    }

    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\n", "\\n").replace("\r", "");
    }
}

