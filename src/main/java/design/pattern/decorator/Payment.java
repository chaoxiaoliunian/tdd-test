package design.pattern.decorator;

import java.util.ArrayList;
import java.util.List;

public interface Payment {
    List<Integer> money = new ArrayList<>();

    void pay(int i);

    int query();
}
