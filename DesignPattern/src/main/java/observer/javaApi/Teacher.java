package observer.javaApi;

import java.util.Observable;
import java.util.Observer;

/**
 * @author SuccessZhang
 */
public class Teacher implements Observer {
    @Override
    public void update(Observable o, Object arg) {
        Student student = (Student) o;
        Question question = (Question) arg;
        System.out.println(student.getName() + "问" + question.getContent());
        System.out.println("老师回答" + question.getContent().replace("?", "") + "2");
    }
}
