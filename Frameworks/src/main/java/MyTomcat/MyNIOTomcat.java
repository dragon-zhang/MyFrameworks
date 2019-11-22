package MyTomcat;

import MyTomcat.http.MyRequest;
import MyTomcat.http.MyResponse;
import MyTomcat.servlet.BaseServlet;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

/**
 * @author SuccessZhang
 */
public enum MyNIOTomcat {

    //枚举单例
    INSTANCE;

    /**
     * 端口
     */
    private static final int PORT = 10001;

    /**
     * 用来储存servlet信息
     */
    private static Map<String, Object> servletMapping = new HashMap<>();

    public static Map<String, Object> getServletMapping() {
        return servletMapping;
    }

    /**
     * 初始化
     */
    @SuppressWarnings("unchecked")
    private void init() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        String base = MyNIOTomcat.class.getResource("/").getPath().replace("classes", "resources");
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
    private void startup() throws Exception {
        System.out.println("MyNIOTomcat has startup!");
        InetSocketAddress server = new InetSocketAddress(PORT);
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //将channel设置为非阻塞的形式
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(server);
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        ByteBuffer buffer = ByteBuffer.allocate(2048);
        while (true) {
            int select = selector.select();
            if (select == 0) {
                continue;
            }
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                if (key.isAcceptable()) {
                    ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
                    SocketChannel clientChannel = serverChannel.accept();
                    clientChannel.configureBlocking(false);
                    //状态read时才会去读取数据
                    clientChannel.register(selector, SelectionKey.OP_READ);
                }
                if (key.isReadable()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    channel.read(buffer);
                    MyRequest request = new MyRequest(new ByteArrayInputStream(buffer.array()));
                    buffer.clear();
                    MyResponse response = new MyResponse(channel);
                    BaseServlet servlet = (BaseServlet) MyNIOTomcat.getServletMapping().get(request.getUrl().replace("/", ""));
                    if (servlet == null) {
                        channel.write(ByteBuffer.wrap(response.response(request.getProtocol(), "没有指定的servlet")));
                        channel.close();
                    } else {
                        servlet.service(request, response);
                    }
                }
                iterator.remove();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        INSTANCE.init();
        INSTANCE.startup();
    }
}
