package observer.javaApi;

/**
 * @author SuccessZhang
 */
public class Question {

    private Student fromStudent;

    private String content;

    public Question(Student fromStudent, String content) {
        this.fromStudent = fromStudent;
        this.content = content;
    }

    public Student getFromStudent() {
        return fromStudent;
    }

    public String getContent() {
        return content;
    }
}
