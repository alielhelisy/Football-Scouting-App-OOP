package Project;

public class Admin extends Person {
    public Admin(String name) {
        super(name);
    }

    @Override
    public int dailyQuota() {
        return 100;
    }
}
