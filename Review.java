package Project;

import java.time.LocalDate;

public class Review implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private final int id;
    private final int playerId;
    private final String scoutName;
    private final LocalDate date;
    private final int stars;
    private String text;

    public Review(int id, int playerId, String scoutName, LocalDate date, int stars, String text) {
        this.id = id;
        this.playerId = playerId;
        this.scoutName = scoutName;
        this.date = date;
        this.stars = stars;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public int getPlayerId() {
        return playerId;
    }

    public String getScoutName() {
        return scoutName;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getStars() {
        return stars;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}