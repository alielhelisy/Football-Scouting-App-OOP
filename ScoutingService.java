package Project;

import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ScoutingService {
    private RatingStrategy ratingStrategy = new SimpleAverageStrategy();

    public void setRatingStrategy(RatingStrategy s) {
        if (s != null) this.ratingStrategy = s;
    }

    private static final String DATA_FILE = "scouting_data.dat";
    private final Map<Integer, Player> players = new LinkedHashMap<>();
    private final List<Review> reviews = new ArrayList<>();
    private int nextPlayerId = 1;
    private int nextReviewId = 1;

    public ScoutingService() {
        loadFromDisk();
    }

    public Player addPlayerWithReview(Position pos, String name, int stars, String comment, String scoutName) {
        Player p = new Player(nextPlayerId++, name, pos);
        players.put(p.getId(), p);
        addReviewInternal(p.getId(), scoutName, stars, comment);
        recomputePlayerStats(p.getId());
        saveToDisk();
        return p;
    }

    public Review addReview(int playerId, String scoutName, int stars, String comment) {
        Review r = addReviewInternal(playerId, scoutName, stars, comment);
        recomputePlayerStats(playerId);
        saveToDisk();
        return r;
    }

    public Review addReviewByPlayerName(String playerName, String scoutName, int stars, String comment) {
        Integer id = findPlayerIdByName(playerName);
        if (id == null) return null;
        return addReview(id, scoutName, stars, comment);
    }

    private Review addReviewInternal(int playerId, String scoutName, int stars, String comment) {
        if (stars < 1 || stars > 5) throw new DataFormatRuntimeException("Stars must be between 1 and 5");
        Review r = new Review(nextReviewId++, playerId, scoutName, LocalDate.now(), stars, comment);
        reviews.add(r);
        return r;
    }

    public List<Player> listPlayersByPosition(Position pos) {
        return players.values().stream()
                .filter(p -> p.getPosition() == pos)
                .collect(Collectors.toList());
    }

    public List<Review> getReviewsForPlayer(int playerId) {
        return reviews.stream().filter(r -> r.getPlayerId() == playerId).collect(Collectors.toList());
    }

    public DefaultTableModel tableModelFor(Position pos) {
        String[] cols = {"ID", "Name", "Last Review"};
        List<Player> list = listPlayersByPosition(pos);
        Object[][] data = new Object[list.size()][cols.length];

        for (int i = 0; i < list.size(); i++) {
            Player p = list.get(i);
            data[i][0] = p.getId();
            data[i][1] = p.getName();
            data[i][2] = (p.getLastReview() == null) ? "" : p.getLastReview();
        }
        return new DefaultTableModel(data, cols) {
            private static final long serialVersionUID = 1L;
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
    }

    protected void recomputePlayerStats(int playerId) {
        List<Review> rs = getReviewsForPlayer(playerId);
        if (rs.isEmpty()) return;
        int[] arr = rs.stream().mapToInt(Review::getStars).toArray();
        double avg = ratingStrategy.compute(arr);
        Review last = rs.get(rs.size() - 1);
        Player p = players.get(playerId);
        if (p != null) {
            p.setAvgStars(avg);
            p.setLastReview(last.getText());
        }
    }

    public List<String> listPlayerNames() {
        List<String> out = new ArrayList<>();
        for (Player p : players.values()) out.add(p.getName());
        Collections.sort(out, String.CASE_INSENSITIVE_ORDER);
        return out;
    }

    public Integer findPlayerIdByName(String name) {
        if (name == null) return null;
        String target = name.trim();
        for (Player p : players.values()) {
            if (p.getName().equalsIgnoreCase(target)) return p.getId();
        }
        return null;
    }

    public Review getLatestReviewForPlayer(int playerId) {
        Review last = null;
        for (Review r : reviews) {
            if (r.getPlayerId() == playerId) last = r;
        }
        return last;
    }

    public boolean updateLastReviewText(int playerId, String newText) {
        Review last = getLatestReviewForPlayer(playerId);
        if (last == null) return false;
        last.setText(newText);
        recomputePlayerStats(playerId);
        saveToDisk();
        return true;
    }

    @SuppressWarnings("unchecked")
    private void loadFromDisk() {
        java.io.File f = new java.io.File(DATA_FILE);
        if (!f.exists()) return;
        try (java.io.ObjectInputStream in = new java.io.ObjectInputStream(new java.io.FileInputStream(f))) {
            Object objPlayers = in.readObject();
            Object objReviews = in.readObject();
            Object objNextIds  = in.readObject();
            players.clear();
            players.putAll((Map<Integer, Player>) objPlayers);
            reviews.clear();
            reviews.addAll((List<Review>) objReviews);
            int[] ids = (int[]) objNextIds;
            nextPlayerId = ids[0];
            nextReviewId = ids[1];
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void saveToDisk() {
        try (java.io.ObjectOutputStream out = new java.io.ObjectOutputStream(new java.io.FileOutputStream(DATA_FILE))) {
            out.writeObject(new LinkedHashMap<>(players));
            out.writeObject(new ArrayList<>(reviews));
            out.writeObject(new int[]{nextPlayerId, nextReviewId});
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<Player> searchPlayersByPositions(Set<Position> positions) {
        if (positions == null || positions.isEmpty()) return Collections.emptyList();
        List<Player> out = new ArrayList<>();
        for (Player p : players.values()) {
            if (positions.contains(p.getPosition())) out.add(p);
        }
        return out;
    }

    private Review latestReview(int playerId) {
        Review last = null;
        for (Review r : reviews) {
            if (r.getPlayerId() == playerId) last = r;
        }
        return last;
    }

    public DefaultTableModel searchTableModel(Set<Position> positions) {
        String[] cols = {"ID", "Name", "Position", "Stars", "Last Review", "Scout"};
        List<Player> list = searchPlayersByPositions(positions);
        Object[][] data = new Object[list.size()][cols.length];
        for (int i = 0; i < list.size(); i++) {
            Player p = list.get(i);
            Review last = latestReview(p.getId());
            data[i][0] = p.getId();
            data[i][1] = p.getName();
            data[i][2] = p.getPosition().display();
            data[i][3] = String.format("%.1f ★", p.getAvgStars());
            data[i][4] = (last == null) ? "" : last.getText();
            data[i][5] = (last == null) ? "" : last.getScoutName();
        }
        return new DefaultTableModel(data, cols) {
            private static final long serialVersionUID = 1L;
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
    }

    public boolean deletePlayer(int playerId) {
        Player removed = players.remove(playerId);
        if (removed == null) return false;
        reviews.removeIf(r -> r.getPlayerId() == playerId);
        saveToDisk();
        return true;
    }

    public boolean updatePlayer(int playerId, String newName, Position newPos) {
        Player p = players.get(playerId);
        if (p == null) return false;
        if (newName != null && !newName.trim().isEmpty()) p.setName(newName.trim());
        if (newPos != null) p.setPosition(newPos);
        recomputePlayerStats(playerId);
        saveToDisk();
        return true;
    }
}