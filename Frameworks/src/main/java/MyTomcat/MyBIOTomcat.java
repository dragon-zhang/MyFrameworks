package MyTomcat;

import MyTomcat.thread.SocketHandler;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author SuccessZhang
 */
public enum MyBIOTomcat implements ThreadFactory, RejectedExecutionHandler {

    //枚举单例
    INSTANCE;

    /**
     * 端口
     */
    private static final int PORT = 10000;

    /**
     * 用来储存servlet信息
     */
    private static Map<String, Object> servletMapping = new HashMap<>();

    public static Map<String, Object> getServletMapping() {
        return servletMapping;
    }

    private ExecutorService executorService = new ThreadPoolExecutor(6,
            15,
            30000L,
            TimeUnit.SECONDS,
            //能够接收请求产生的消息队列即可，设置过大会浪费内存
            new LinkedBlockingQueue<>(800),
            this, this);

    @Override
    public Thread newThread(Runnable r) {
        //实际cpu调用的线程
        Thread thread = new Thread(r);
        //设置优先级
        thread.setPriority(Thread.NORM_PRIORITY);
        //创建的线程默认为非守护线程，这里设置成守护线程
        thread.setDaemon(true);
        return thread;
    }

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        //线程池没有被关闭
        if (executor.isShutdown()) {
            throw new RejectedExecutionException("ThreadPoolExecutor is shutdown");
        } else {
            //任务数量超过最大值（队列长度 + maximumPoolSize）
            throw new RuntimeException("the number of tasks exceeds the maximum");
        }
    }

    /**
     * 初始化
     */
    @SuppressWarnings("unchecked")
    private void init() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        String base = MyBIOTomcat.class.getResource("/").getPath().replace("classes", "resources");
        SAXReader reader = new SAXReader();
        //通过read方法将文件转化为dom对象
        Document document = null;
        try {
            document = reader.read(new File(base + "servlet.xml"));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        if (document == null) {
            throw new RuntimeException("transform document failed !");
        }
        //获取根节点<mapper>
        Element root = document.getRootElement();
        //获取子节点列表
        List<Element> elements = root.elements();
        for (Element element : elements) {
            Element servletName = element.element("servlet-name");
            Element servletClass = element.element("servlet-class");
            servletMapping.put(servletName.getText(), Class.forName(servletClass.getText()).newInstance());
        }
    }

    /**
     * 运行
     */
    private void startup() throws IOException {
        System.out.println("MyBIOTomcat has startup!");
        ServerSocket serverSocket = new ServerSocket(PORT);
        while (true) {
            Socket socket = serverSocket.accept();
            executorService.execute(new SocketHandler(socket));
        }
    }

    public static void main(String[] args) throws IllegalAccessException, InstantiationException, ClassNotFoundException, IOException {
        INSTANCE.init();
        INSTANCE.startup();
    }
}
