package design.pattern.composite;

import design.pattern.composite.Company;

import java.util.ArrayList;
import java.util.List;

public class Department implements Company {
    List<Company> list = new ArrayList<>();

    @Override
    public void add(Company a1) {
        list.add(a1);
    }

    @Override
    public int getAge() {
        return list.stream().mapToInt(Company::getAge).sum();
    }
}
