package proxy;

import java.util.HashMap;
import java.util.Map;

/**
 * @author SuccessZhang
 */
public class Matchmaker {
    private Map<String, Person> persons = new HashMap<>();

    public void addSeeker(Person person) {
        persons.put(person.getName(), person);
    }

    public void makeMatch() {
        for (String name : persons.keySet()) {
            System.out.println("我帮" + name + "物色对象");
            persons.get(name).findLove();
            System.out.println("物色成功，双方同意交往");
            System.out.println(name + "给我钱");
        }
    }
}
