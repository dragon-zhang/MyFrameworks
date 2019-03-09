package MySpringBoot;

import MySpringBoot.annotation.ScanPackage;
import MySpringBoot.initializer.MySpringBootApplication;

/**
 * @author SuccessZhang
 */
@ScanPackage("MySpringBoot")
public class MyApplication {

    public static void main(String[] args) {
        MySpringBootApplication.run(MyApplication.class, args);
    }

}
