package promote;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by liKun on 2018/1/4 0004.
 */
public class PromoteServer{
    ChatType chatType=ChatType.getChatType();
    Socket socket;
    //存储所有用户的输出流
    Map<String,PrintWriter> users=new HashMap<>();
    //支持100并发的线程池
    ExecutorService executorService= Executors.newFixedThreadPool(100);

    public PromoteServer() {
    }

    public static void main(String[] args) {
        PromoteServer server = new PromoteServer();
        server.start();
    }

    public void start(){
        ServerSocket server=null;
        try {
            server=new ServerSocket(9999);
            System.out.println("服务器成功启动，等待链接......");
            while(true){
                socket=server.accept();
                ClientHandler clientHandler=new ClientHandler(socket);
                executorService.execute(clientHandler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(server!=null){
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public  class ClientHandler extends PublicParent implements Runnable{

        public ClientHandler(Socket socket) {
            super(socket);
        }

        @Override
        public void run() {
            handlerClient();
        }

        public void handlerClient(){
            while(true){
                try {
                    String msg=br.readLine();
                    if(msg!=null){
                        String[] arr=msg.split(":");
                        if(arr.length>0){
                            String typeIdentifier=arr[0];
                            if(typeIdentifier.equals(chatType.getLOGIN_NAME())){
                                handlerLoginName(arr[1],pw);
                            }else if(typeIdentifier.equals(chatType.getEXITE_SYSTEM())){
                                handlerExit(arr[1]);
                            }else if(typeIdentifier.equals(chatType.getPRIVARE_CHAT())){
                                handlerPrivateChat(arr,pw);
                            }else if(typeIdentifier.equals(chatType.getPUBLIC_CHAT())){
                                handlerPublicChat(arr,pw);
                            }else if(msg.equals(chatType.getCONNECT_TEST())){
                                pw.println(chatType.getCONNECT_SUCCESS());
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void handlerLoginName(String loginName,PrintWriter pw){
            for(Map.Entry<String,PrintWriter> entry:users.entrySet()){
                if(entry.getKey().equals(loginName)){
                    pw.println(chatType.getLOGIN_NAME_EXIT());
                    return;
                }
            }
            for(Map.Entry<String,PrintWriter> entry:users.entrySet()){
                entry.getValue().println(loginName+"上线了！");
            }
            users.put(loginName,pw);
            pw.println(chatType.getLOGIN_SUCCESS());
        }

        public void handlerExit(String exitName){
            users.get(exitName).println(chatType.getEXITE_SUCCESS());
            users.remove(exitName);
            for(Map.Entry<String,PrintWriter> entry:users.entrySet()){
                entry.getValue().println(exitName+"下线了");
            }
        }

        public void handlerPrivateChat(String[] arr,PrintWriter pw){
            String str=assembleMsg(arr,3);
            users.get(arr[2]).println(arr[1]+"私信你："+str);
            users.get(arr[1]).println("私信"+arr[2]+"成功！");
        }

        public void handlerPublicChat(String[] arr,PrintWriter pw){
            String str=assembleMsg(arr,2);
            for (Map.Entry<String, PrintWriter> entry : users.entrySet()) {
                if(entry.getKey().equals(arr[1])){
                    users.get(arr[1]).println("广播消息成功！");
                }else{
                    entry.getValue().println(arr[1]+"广播消息："+str);
                }
            }
        }

        public String assembleMsg(String[] arr,int begin){
            String str="";
            for(int i=begin;i<arr.length;i++){
                str=str+arr[i];
            }
            return str;
        }
    }

    public class PublicParent{
        BufferedReader br;
        PrintWriter pw;
        public PublicParent(Socket socket) {
            try {
                this.br = new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
                this.pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"utf-8"),true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
