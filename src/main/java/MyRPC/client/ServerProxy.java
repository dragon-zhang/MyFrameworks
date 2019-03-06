package MyRPC.client;

import MyRPC.message.RPCMessage;
import com.alibaba.fastjson.JSON;
import com.example.disperse.service.TestService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @author SuccessZhang
 */
public enum ServerProxy implements InvocationHandler {

    //枚举单例
    INSTANCE;

    private static String host;

    private static int port;

    @SuppressWarnings("unchecked")
    public static <T> T getServer(Class<T> type, String host, int port) {
        ServerProxy.host = host;
        ServerProxy.port = port;
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[]{type}, INSTANCE);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String result = null;
        RPCMessage message = new RPCMessage();
        message.setClassName(method.getDeclaringClass().getName());
        message.setMethodName(method.getName());
        message.setParams(args);
        try {
            Socket socket = new Socket(host, port);
            //发送请求
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(JSON.toJSONString(message).getBytes());
            outputStream.flush();
            socket.shutdownOutput();
            //返回请求
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String msg;
            while ((msg = reader.readLine()) != null) {
                sb.append(msg);
            }
            result = sb.toString();
            socket.shutdownInput();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        TestService testService = ServerProxy.getServer(TestService.class, "127.0.0.1", 9999);
        System.out.println(testService.hello("client"));
    }
}
