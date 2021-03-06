package MyRPC.server.thread;

import MyRPC.message.RPCMessage;
import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @author SuccessZhang
 */
@AllArgsConstructor
public class ServerHandler implements Runnable {

    private Object service;

    private Socket socket;

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String msg;
            while ((msg = reader.readLine()) != null) {
                sb.append(msg);
            }
            socket.shutdownInput();
            RPCMessage request = JSON.parseObject(sb.toString(), RPCMessage.class);
            Object[] args = request.getParams();
            Class<?>[] types = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                types[i] = args[i].getClass();
            }
            Class<?> clazz = Class.forName(request.getClassName());
            if (clazz.isAssignableFrom(service.getClass())) {
                //发布的接口有实现类
                Method method = clazz.getMethod(request.getMethodName(), types);
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(method.invoke(service, args).toString().getBytes());
                outputStream.flush();
                socket.shutdownOutput();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
