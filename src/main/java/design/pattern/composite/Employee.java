package design.pattern.composite;

public class Employee implements Company{
    int age;
    public Employee(int i) {
        this.age=i;
    }

    public void add(Company a1) {

    }

    @Override
    public int getAge() {
        return age;
    }
}
