package MyJedis;

import java.io.IOException;
import java.net.Socket;

/**
 * @author SuccessZhang
 */
public class MyJedis {

    private static Socket socket;

    public static void init(String host, int port) {
        try {
            socket = new Socket(host, port);
        } catch (IOException e) {
            System.out.println("init socket failed !");
            e.printStackTrace();
        }
    }

    public static String set(String key, String value) {
        StringBuilder sb = new StringBuilder();
        sb.append("*3\r\n");
        sb.append("$3\r\n");
        sb.append("set\r\n");
        sb.append("$").append(key.getBytes().length).append("\r\n");
        sb.append(key).append("\r\n");
        sb.append("$").append(value.getBytes().length).append("\r\n");
        sb.append(value).append("\r\n");
        try {
            socket.getOutputStream().write(sb.toString().getBytes());
            byte[] response = new byte[2048];
            socket.getInputStream().read(response);
            return new String(response).trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "set (" + key + "," + value + ") failed !";
    }

    public static String get(String key) {
        StringBuilder sb = new StringBuilder();
        sb.append("*2\r\n");
        sb.append("$3\r\n");
        sb.append("get\r\n");
        sb.append("$").append(key.getBytes().length).append("\r\n");
        sb.append(key).append("\r\n");
        try {
            socket.getOutputStream().write(sb.toString().getBytes());
            byte[] response = new byte[2048];
            socket.getInputStream().read(response);
            String res = new String(response);
            return res.substring(res.indexOf("\n")).trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "get " + key + " failed !";
    }

    public static String del(String key) {
        StringBuilder sb = new StringBuilder();
        sb.append("*2\r\n");
        sb.append("$3\r\n");
        sb.append("del\r\n");
        sb.append("$").append(key.getBytes().length).append("\r\n");
        sb.append(key).append("\r\n");
        try {
            socket.getOutputStream().write(sb.toString().getBytes());
            byte[] response = new byte[2048];
            socket.getInputStream().read(response);
            String res = new String(response);
            return res.substring(res.indexOf("\n")).trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "del " + key + " failed !";
    }

    public static void expire(String key, int seconds) {
        StringBuilder sb = new StringBuilder();
        sb.append("*3\r\n");
        sb.append("$6\r\n");
        sb.append("expire\r\n");
        sb.append("$").append(key.getBytes().length).append("\r\n");
        sb.append(key).append("\r\n");
        String str = String.valueOf(seconds);
        sb.append("$").append(str.getBytes().length).append("\r\n");
        sb.append(str).append("\r\n");
        try {
            socket.getOutputStream().write(sb.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void expireAt(String key, long unixTime) {
        StringBuilder sb = new StringBuilder();
        sb.append("*3\r\n");
        sb.append("$8\r\n");
        sb.append("expireat\r\n");
        sb.append("$").append(key.getBytes().length).append("\r\n");
        sb.append(key).append("\r\n");
        String str = String.valueOf(unixTime);
        sb.append("$").append(str.getBytes().length).append("\r\n");
        sb.append(str).append("\r\n");
        try {
            socket.getOutputStream().write(sb.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Boolean exists(String key) {
        StringBuilder sb = new StringBuilder();
        sb.append("*2\r\n");
        sb.append("$6\r\n");
        sb.append("exists\r\n");
        sb.append("$").append(key.getBytes().length).append("\r\n");
        sb.append(key).append("\r\n");
        try {
            socket.getOutputStream().write(sb.toString().getBytes());
            byte[] response = new byte[2048];
            socket.getInputStream().read(response);
            String res = new String(response);
            return res.contains("1");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
