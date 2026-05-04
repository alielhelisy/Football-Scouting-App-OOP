package Project;

public class Scout extends Person {
    private final String scoutCode;

    public Scout(String name, String scoutCode) {
        super(name);
        this.scoutCode = scoutCode;
    }

    public String getScoutCode() {
        return scoutCode;
    }

    @Override
    public int dailyQuota() {
        return 10;
    }
}