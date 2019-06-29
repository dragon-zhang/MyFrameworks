package MySpringBoot;

import MySpringBoot.annotation.ScanPackage;
import MySpringBoot.initializer.MySpringBootApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

/**
 * @author SuccessZhang
 */
@ScanPackage("MySpringBoot")
@EnableRetry
@SpringBootApplication
public class MyApplication {

    public static void main(String[] args) {
        MySpringBootApplication.run(MyApplication.class, args);
    }

}
