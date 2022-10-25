package entity;

public class Student {
    private Long id;
    private String name;
    private String birthdate;
    private int credits;
    private int averageGrade;

    public Student() {
    }

    public Student(String name, String birthdate, int credits, int averageGrade) {
        this.name = name;
        this.birthdate = birthdate;
        this.credits = credits;
        this.averageGrade = averageGrade;
    }

    public Student(Long id, String name, String birthdate, int credits, int averageGrade) {
        this.id = id;
        this.name = name;
        this.birthdate = birthdate;
        this.credits = credits;
        this.averageGrade = averageGrade;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public int getAverageGrade() {
        return averageGrade;
    }

    public void setAverageGrade(int averageGrade) {
        this.averageGrade = averageGrade;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthdate=" + birthdate +
                ", credits=" + credits +
                ", avgGrade=" + averageGrade +
                '}';
    }
}
