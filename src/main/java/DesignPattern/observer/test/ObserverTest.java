package DesignPattern.observer.test;

import DesignPattern.observer.guavaApi.GuavaEvent;
import DesignPattern.observer.javaApi.Question;
import DesignPattern.observer.javaApi.Student;
import DesignPattern.observer.javaApi.Teacher;
import com.google.common.eventbus.EventBus;

/**
 * @author SuccessZhang
 * 业务场景：课堂上学生问老师问题，老师回答问题
 * 优点：
 * 观察者和被观察者之间建立了一个抽象的耦合
 * 观察者模式支持广播通信
 * 缺点：
 * 有过多的细节依赖、提高时间消耗及程序的复杂度
 */
public class ObserverTest {
    public static void main(String[] args) {
        //java原生api
        Student student = new Student();
        student.addObserver(new Teacher());
        student.askQuestion(new Question(student, "1+1=?"));
        //guava api
        EventBus eventBus = new EventBus();
        GuavaEvent guavaEvent = new GuavaEvent();
        eventBus.register(guavaEvent);
        eventBus.post("Tom");
    }
}
