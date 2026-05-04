package Project;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public final class Users {
    private static final Map<String, String> USERS = new HashMap<>();
    private static final Map<String, String> ROLES = new HashMap<>();

    private static final File DB_FILE =
            new File(System.getProperty("user.home"), ".scoutingapp_users.txt");

    private Users() {}

    static {
        loadFromDisk();
    }

    public static synchronized boolean checkLogin(String username, String password) {
        if (username == null || password == null) return false;
        String key = username.trim().toLowerCase(Locale.ROOT);
        String expected = USERS.get(key);
        return expected != null && expected.equals(password.trim());
    }

    public static synchronized String roleOf(String username) {
        if (username == null) return "VIEWER";
        String key = username.trim().toLowerCase(Locale.ROOT);
        String role = ROLES.get(key);
        return role != null ? role : "VIEWER";
    }

    public static synchronized boolean createAccount(String username, String password, String role) {
        if (!isValidUsername(username) || !isValidPassword(password) || !isValidRole(role)) {
            return false;
        }
        String key = username.trim().toLowerCase(Locale.ROOT);
        if (USERS.containsKey(key)) return false;
        USERS.put(key, password);
        ROLES.put(key, role.toUpperCase(Locale.ROOT).trim());
        saveToDisk();
        return true;
    }

    public static synchronized List<String> listUsers() {
        List<String> out = new ArrayList<>(USERS.keySet());
        out.sort(String::compareTo);
        return out;
    }

    private static synchronized void loadFromDisk() {
        USERS.clear();
        ROLES.clear();
        if (!DB_FILE.exists()) return;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(DB_FILE), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) continue;
                String[] parts = line.split("\t");
                if (parts.length < 3) continue;
                String user = parts[0].trim();
                String pass = parts[1];
                String role = parts[2].trim().toUpperCase(Locale.ROOT);
                String key  = user.toLowerCase(Locale.ROOT);
                USERS.put(key, pass);
                ROLES.put(key, role);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static synchronized void saveToDisk() {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(DB_FILE, false), StandardCharsets.UTF_8))) {
            bw.write("# username\tpassword\trole\n");
            for (String key : USERS.keySet()) {
                String pass = USERS.get(key);
                String role = ROLES.getOrDefault(key, "VIEWER");
                bw.write(key + "\t" + pass + "\t" + role + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean isValidUsername(String u) {
        if (u == null) return false;
        String s = u.trim();
        return !s.isEmpty() && s.length() <= 32 && s.matches("[A-Za-z0-9_.-]+");
    }

    private static boolean isValidPassword(String p) {
        return p != null && p.length() >= 6;
    }

    private static boolean isValidRole(String r) {
        if (r == null) return false;
        String up = r.toUpperCase(Locale.ROOT).trim();
        return up.equals("ADMIN") || up.equals("SCOUT") || up.equals("VIEWER");
    }

    public static synchronized String login(String username, String password) throws AuthException {
        if (username == null || password == null) {
            throw new AuthException("Username and password are required.");
        }
        final String u = username.trim();
        final String p = password;

        if (u.isEmpty()) {
            throw new AuthException("Username is required.");
        }
        if (p.length() < 6) {
            throw new AuthException("Password too short (min 6).");
        }

        if ("admin".equalsIgnoreCase(u) && "admin123".equals(p)) {
            return "ADMIN";
        }
        if ("scout".equalsIgnoreCase(u) && "scout123".equals(p)) {
            return "SCOUT";
        }

        if (checkLogin(u, p)) {
            String role = roleOf(u);
            return (role == null || role.isBlank()) ? "SCOUT" : role.toUpperCase();
        }

        throw new AuthException("Invalid username or password.");
    }
}