package observer.javaApi;

import java.util.Observable;

/**
 * @author SuccessZhang
 */
public class Student extends Observable {

    public String getName() {
        return "小明";
    }

    public void askQuestion(Question question) {
        setChanged();
        notifyObservers(question);
    }
}
