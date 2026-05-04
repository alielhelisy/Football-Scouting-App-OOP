package Project;

public abstract class Person {
    protected String name;

    public Person(String name) { 
        this.name = name; 
    }

    public String getName() { 
        return name; 
    }

    public abstract int dailyQuota();
}
