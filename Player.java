package Project;

public class Player implements java.io.Serializable {
    private static int totalCreated = 0;
    private static final long serialVersionUID = 1L;

    private final int id;
    private String name;
    private Position position;
    private double avgStars;
    private String lastReview;

    public Player(int id, String name, Position position) {
        if (name == null || name.isBlank()) {
            throw new DataFormatRuntimeException("Player name cannot be empty");
        }
        this.id = id;
        this.name = name.trim();
        this.position = position;
        totalCreated++;
    }

    public int getId() { 
        return id; 
    }

    public String getName() { 
        return name; 
    }

    public Position getPosition() { 
        return position; 
    }

    public double getAvgStars() { 
        return avgStars; 
    }

    public String getLastReview() { 
        return lastReview; 
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new DataFormatRuntimeException("Player name cannot be empty");
        }
        this.name = name.trim();
    }

    public void setPosition(Position position) { 
        this.position = position; 
    }

    public void setAvgStars(double avgStars) { 
        this.avgStars = avgStars; 
    }

    public void setLastReview(String lastReview) { 
        this.lastReview = lastReview; 
    }

    public static int getTotalCreated() { 
        return totalCreated; 
    }
}
