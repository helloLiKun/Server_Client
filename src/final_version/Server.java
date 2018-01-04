package final_version;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * 服务器端应用程序
 * Created by liKun on 2018/1/4 0004.
 */
public class Server {
    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    // 服务端Socket
    private ServerSocket serverSocket;
    // 所有客户端输出流,key为用户的昵称，value为该用户的输出流
    private Map<String,PrintWriter> allOut;
    // 线程池
    private ExecutorService threadPool;
    /**
     * 构造方法，用于初始化
     */
    public Server() {
        try {
            serverSocket = new ServerSocket(8088);
            allOut = new HashMap<String,PrintWriter>();
            threadPool = Executors.newFixedThreadPool(40);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 服务端开启方法
     */
    public void start() {
        try {
            //循环监听客户端的连接
            while(true){
                System.out.println("等待客户端连接...");
                // 监听客户端的连接
                Socket socket = serverSocket.accept();
                System.out.println("客户端已连接!");

                //启动一个线程来完成针对该客户端的交互
                ClientHandler handler = new ClientHandler(socket);
                threadPool.execute(handler);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 将输出流存入共享集合，与下面两个方法互斥，保证同步安全
     * @param out
     */
    private synchronized void addOut(String nickName,PrintWriter out){
        allOut.put(nickName,out);
    }
    /**
     * 将给定输出流从共享集合删除
     * @param
     */
    private synchronized void removeOut(String nickName){
        allOut.remove(nickName);
    }
    /**
     * 将消息转发给所有客户端
     * @param message
     */
    private synchronized void sendMessage(String message){
        for(PrintWriter o : allOut.values()){
            o.println(message);
        }
    }
    /**
     * 将消息发送给指定昵称的客户端
     * @param nickName
     * @param message
     */
    private synchronized void sendMessageToOne(String nickName,String message){
        PrintWriter out = allOut.get(nickName);
        if(out!=null){
            out.println(message);
        }
    }
    /**
     * 线程体，用于并发处理不同客户端的交互
     */
    private class ClientHandler implements Runnable {
        // 该线程用于处理的客户端
        private Socket socket;
        // 开客户端的昵称
        private String nickName;
        public ClientHandler(Socket socket) {
            this.socket = socket;
        }
        @Override
        public void run() {
            PrintWriter pw = null;
            try {
                //将客户端的输出流存入共享集合，以便广播消息
                OutputStream out = socket.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(out,"UTF-8");
                pw = new PrintWriter(osw,true);
                /*
                 * 将用户信息存入共享集合
                 * 需要同步
                 */
                //先获取该用户昵称
                nickName = getNickName();
                addOut(nickName,pw);
                Thread.sleep(100);
                /*
                 * 通知所有用户该用户已上线
                 */
                sendMessage(nickName+"上线了");

                InputStream in = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(in, "UTF-8");
                BufferedReader br = new BufferedReader(isr);

                String message = null;
                // 循环读取客户端发送的信息
                while ((message = br.readLine())!=null) {
                    //首先查看是不是私聊
                    if(message.startsWith("\\")){
                        /*
                         * 私聊格式：\昵称:内容
                         */
                        //找到:的位置
                        int index = message.indexOf(":");
                        if(index>=0){
                            //截取昵称
                            String name = message.substring(1,index);
                            //截取内容
                            String info = message.substring(index+1,message.length());
                            //拼接内容
                            info = nickName+"对你说:"+info;
                            //发送私聊信息给指定用户
                            sendMessageToOne(name, info);
                            //发送完私聊后就不在广播了。
                            continue;
                        }
                    }
                    /*
                     * 遍历所有输出流，将该客户端发送的信息转发给所有客户端
                     * 需要同步
                     */
                    sendMessage(nickName+"说:"+message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                /*
                 * 当客户端断线，要将输出流从共享集合中删除
                 * 需要同步
                 */
                removeOut(nickName);
                /*
                 * 通知所有用户该用户已下线
                 */
                sendMessage(nickName+"下线了");
                System.out.println("当前在线人数:"+allOut.size());
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        /**
         * 获取该用户的昵称
         * @return
         */
        private String getNickName()throws Exception{
            try {
                //获取该用户的输出流
                OutputStream out = socket.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(out,"UTF-8");
                PrintWriter pw = new PrintWriter(osw,true);
                //获取该用户的输入流
                InputStream in = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(in, "UTF-8");
                BufferedReader br = new BufferedReader(isr);
                //读取客户端发送过来的昵称
                String nickName = br.readLine();
                while(true){
                    //若昵称为空发送失败代码
                    if(nickName.trim().equals("")){
                        pw.println("FAIL");
                    }
                    //若昵称已经存在发送失败代码
                    if(allOut.containsKey(nickName)){
                        pw.println("FAIL");
                        //若成功，发送成功代码，并返回昵称
                    }else{
                        pw.println("OK");
                        return nickName;
                    }
                    //若改昵称被占用，等待用户再次输入昵称
                    nickName = br.readLine();
                }
            } catch (Exception e) {
                throw e;
            }
        }
    }
}
