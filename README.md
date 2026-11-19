# 1. Project Title
**FindIt: BatStateU-Alangilan Lost & Found System**

---

## 2. Project Description
FindIt is a campus-scoped lost-and-found reporting and recovery platform built specifically for the **Batangas State University – The National Engineering University (Alangilan Campus)** community. It centralizes lost and found item reports, pins them onto an interactive campus landmark map, triggers proximity alerts within ~100 meters using Haversine GPS calculations and keyword matching, and provides built-in messaging and moderation tools so owners and finders can safely coordinate item returns.

---

## 3. Problem Being Addressed
Students, faculty, and staff frequently misplace personal belongings such as student IDs, wallets, keys, phones, calculators, and school materials on campus. Currently, lost-and-found announcements are scattered across unofficial social media pages and group chats, making them difficult to organize, filter by location, verify, or follow up on once an item is recovered.

---

## 4. Project Objectives
* **Centralize Campus Reports:** Provide a structured platform exclusively for `@g.batstate-u.edu.ph` campus users to submit and browse `LOST` and `FOUND` item reports.
* **Improve Location Accuracy:** Allow users to pin reports to recognizable BatStateU-Alangilan landmarks (such as Main Gate, Library, Canteen, Gymnasium, and Engineering Building) alongside GPS coordinates.
* **Automate Relevant Alerts:** Notify item owners when a matching found report is submitted within a ~100-meter radius without sending duplicate alerts.
* **Facilitate Safe Recovery & Moderation:** Enable direct owner-finder communication and equip campus administrators with a moderation queue to flag or remove inappropriate posts.
* **Support Sustainable Development Goals (SDGs):** Promote **SDG 11** (*Sustainable Cities and Communities*) and **SDG 12** (*Responsible Consumption and Production*) by reducing unnecessary item replacement and waste on campus.

---

## 5. Target Users
* **Item Owners (`Owner`):** BatStateU-Alangilan students, faculty, or staff who lost a personal belonging and want to post a report or receive proximity/keyword alerts.
* **Item Finders (`Finder`):** Campus community members who discovered an unattended item and want to log its location and connect with its rightful owner.
* **Campus Administrators (`Admin`):** Authorized moderators who review flagged reports, maintain platform integrity, and remove spam or resolved/invalid entries.

---

## 6. Main Features
* **Campus-Scoped Authentication:** Restricts registration and login strictly to valid `@g.batstate-u.edu.ph` email addresses.
* **Lost & Found Report Management:** Supports creating, viewing, filtering, flagging, and resolving reports with item title, category, description, date, and landmark/GPS data.
* **Interactive Campus Map & Landmark Selector:** Displays active `LOST` and `FOUND` pins across key BatStateU-Alangilan landmarks (`CampusMap` and `Landmark`).
* **~100m Proximity & Keyword Matching:** Uses the Haversine distance formula (`LostFoundReport.distanceTo`) and keyword scoring (`Keywords`) to alert users when relevant items are nearby (`~100 m`).
* **In-App Owner–Finder Messaging:** Stores threaded conversations tied to specific item reports (`Message`) so users can arrange safe campus meetups.
* **Role-Based Resolution & Moderation Queue:** Ensures only the original poster or an `Admin` can mark a report as resolved, while `Admin` users manage flagged posts.
* **Automated Feature Test Suite:** Includes a built-in verification suite (`Testing.java`) that validates registration rules, proximity alerts, messaging, and moderation logic.

---

## 7. Technologies Used
* **Programming Language:** Java (JDK 17+ / Standard Edition)
* **Backend & Core Architecture:** Object-Oriented Java (`java.util`, `java.time`)
* **Web Frontend & Local Server:** Java Built-in HTTP Server (`com.sun.net.httpserver.HttpServer`), HTML5, CSS3, Vanilla JavaScript (Fetch API)
* **Console Interface:** Java `Scanner` CLI (`Main.java`)
* **Development & Version Control:** Visual Studio Code, Git, and GitHub

---

## 8. Team Members and Roles
| Name | Role | Responsibilities |
| :--- | :--- | :--- |
| **[Mark Dwayne Ocampo]** | Project Lead & Backend Developer | Designed `FindItSystem`, proximity/notification logic, and repository integration |
| **[Selina Josephine Ani]** | System Architect & Frontend developer  | Implemented `User`, `Owner`, `Finder`, `Admin`, `LostFoundReport`, and `Keywords` |
| **[Matt Aclan]** | Map, Messaging & Testing Developer | Implemented `CampusMap`, `Landmark`, `Message`, `Notification`, and `Testing` |
| **[Laarnie Bauan]** | Documentation Lead | Built `FindItWebServer` UI, UML diagrams, screenshots, and `README.md` |

---

## 9. OOP Concepts Demonstrated
* **Encapsulation:** Class attributes (such as user credentials, report status flags, coordinates, and notification history) are marked `private` and accessed/modified through controlled getter, setter, and domain methods (`markResolved`, `flagReport`, `updateLocation`).
* **Inheritance:** `Owner`, `Finder`, and `Admin` extend the base `User` superclass, inheriting shared identity fields (`id`, `name`, `email`, `password`) and authentication behavior (`login`).
* **Polymorphism:** `FindItSystem` stores all user types in a unified `ArrayList<User>` and dynamically checks behavior at runtime using `instanceof` (e.g., tailoring `isRelevant()` alerts for `Owner` vs. `Finder` and authorizing `Admin` actions).
* **Abstraction & Modularity:** Complex operations—such as Haversine GPS distance calculation (`LostFoundReport`), keyword matching (`Keywords`), and spatial landmark lookups (`CampusMap`)—are encapsulated behind clean, single-purpose methods.
* **Composition & Aggregation:** `FindItSystem` coordinates collections of `User`, `LostFoundReport`, `Message`, `Notification`, and `CampusMap` objects, while `Owner` composes a `Keywords` instance and `CampusMap` manages `Landmark` pins.

---

## 10. Instructions for Running the Program

### Prerequisites
* **Java Development Kit (JDK 17 or higher)** installed and configured in your system `PATH`.
* **Git** (optional, for cloning the repository).

### Option A: Running the Website Interface (Recommended)
1. Clone or download this repository and open the project folder in **VS Code** or a terminal.
2. Compile all Java source files inside `src/`:
   ```bash
   javac src/*.java
   ```
3. Start the built-in FindIt web server:
   ```bash
   java -cp src FindItWebServer
   ```
4. Open your web browser and navigate to:
   ```text
   http://localhost:8080
   ```
5. Log in using one of the pre-loaded campus demo accounts or register a new `@g.batstate-u.edu.ph` account:
   * **Owner:** `juan.delacruz@g.batstate-u.edu.ph` / `pass123`
   * **Finder:** `maria.santos@g.batstate-u.edu.ph` / `pass123`
   * **Admin:** `admin.alangilan@g.batstate-u.edu.ph` / `admin123`

### Option B: Running the Console Menu & Automated Feature Tests
1. Compile the source files:
   ```bash
   javac src/*.java
   ```
2. Run `Main`:
   ```bash
   java -cp src Main
   ```
3. Enter `1` to view the project overview or `2` to run the automated `Testing` suite.

---

## 11. Sample Screenshots
> *(Place your captured screenshots inside the `screenshots/` folder and update the filenames below.)*

* **Campus Login & Registration Screen**
  ![Login Screen](screenshots/login.png)
* **Lost & Found Reports Feed**
  ![Reports Feed](screenshots/feed.png)
* **Interactive Campus Map & ~100m Proximity Alerts**
  ![Campus Map](screenshots/campus-map.png)
* **Owner–Finder Messaging & Admin Moderation**
  ![Messages and Moderation](screenshots/messages-admin.png)
* **Automated Feature Tests Output (`Testing.java`)**
  ![Console Tests](screenshots/console-tests.png)

---

## 12. Known Limitations
* **In-Memory Data Storage:** Reports, user accounts, messages, and notifications are stored in memory (`ArrayList` / `HashMap`) during runtime and reset when the server restarts.
* **Placeholder Landmark Coordinates:** Landmark coordinates in `CampusMap.java` use representative BatStateU-Alangilan coordinates and simulated GPS check-ins rather than live mobile hardware GPS.
* **Campus & Item Scope:** Strictly limited to personal belongings within BatStateU-Alangilan; does not cover other campuses, pets, or missing-person reports, and item recovery is not guaranteed.
* **Photo Upload Handling:** `LostFoundReport` supports a `photoPath` field, though the web prototype focuses on text descriptions and landmark pins rather than binary file uploads.

---

## 13. Future Improvements
* **Persistent Database Integration:** Connect `FindItSystem` to MySQL, PostgreSQL, or SQLite so accounts, reports, and chat histories persist across restarts.
* **Image Upload & Preview:** Allow users to upload and display photos of lost or found items directly in the report feed.
* **Real-Time Browser Notifications & WebSockets:** Push instant proximity alerts and live chat updates without manual page refreshes.
* **Google Maps / Leaflet Integration:** Replace the schematic campus canvas with an interactive tile map using verified BatStateU-Alangilan building coordinates.
* **Email / OTP Verification:** Verify `@g.batstate-u.edu.ph` ownership via one-time passcode upon registration.

---

## 14. Project Contributors
* **[Member 1 Name]** – [@github-username1](https://github.com/github-username1)
* **[Member 2 Name]** – [@github-username2](https://github.com/github-username2)
* **[Member 3 Name]** – [@github-username3](https://github.com/github-username3)
* **[Member 4 Name]** – [@github-username4](https://github.com/github-username4)
