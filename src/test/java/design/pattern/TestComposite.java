package design.pattern;

import design.pattern.composite.Company;
import design.pattern.composite.Department;
import design.pattern.composite.Employee;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestComposite {
    /**
     * 计算一个公司所有人的年龄
     */
    @Test
    public void testDepartment() {
        Department departmentA = new Department();
        Employee a1 = new Employee(30);
        Employee a2 = new Employee(50);
        departmentA.add(a1);
        departmentA.add(a2);

        Department departmentB = new Department();
        Employee b1 = new Employee(30);
        Employee b2 = new Employee(60);
        departmentB.add(b1);
        departmentB.add(b2);

        departmentB.add(departmentA);
        assertEquals(170, departmentB.getAge());
    }
}
