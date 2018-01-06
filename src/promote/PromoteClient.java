package promote;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by liKun on 2018/1/4 0004.
 */
public class PromoteClient {
    ChatType chatType=ChatType.getChatType();
    public static void main(String[] args){
        PromoteClient client=new PromoteClient();
        client.start();
    }
    public void start(){
        Socket socket;
        ExecutorService executorService= Executors.newFixedThreadPool(10);
        try {
            socket=new Socket("localhost",9999);
            BufferedReader br=new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
            PrintWriter pw=new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"utf-8"),true);
            pw.println(chatType.getCONNECT_TEST());
            while(true){
                String read=br.readLine();
                if(read!=null){
                    String[] reads=read.split(":");
                    if(reads.length>0){
                        String typeIdentifier=reads[0];
                        if(typeIdentifier.equals(chatType.getLOGIN_NAME_EXIT())){
                            System.out.println("该用户已存在，请重新输入");
                            sendMsgHandler(pw,"请输入用户名",chatType.getLOGIN_NAME());
                        }else if(typeIdentifier.equals(chatType.getLOGIN_SUCCESS())){
                            System.out.println("登录成功！可以聊天了。");
                            System.out.println("如果发布广播消息在消息前加‘public@’；如果私信则私信名@");
                            sendMsgHandler(pw,"请输入要发送的消息：",null);
                        }else if(typeIdentifier.equals(chatType.getSEND_SUCCESS())){
                            System.out.println("消息发送成功！");
                            sendMsgHandler(pw,"请输入要发送的消息：",null);
                        }else if(typeIdentifier.equals(chatType.getCONNECT_SUCCESS())){
                            //链接成功
                            System.out.println("成功链接到服务器");
                            sendMsgHandler(pw,"请输入用户名",chatType.getLOGIN_NAME());
                        }
                    }
                }

            }
        } catch (IOException e) {
            System.out.println("获取链接失败");
        }
    }

    public void sendMsgHandler(PrintWriter pw,String promptMsg,String sendStart){
        System.out.println(promptMsg);
        Scanner scanner=new Scanner(System.in);
        String username=scanner.nextLine();
        pw.println(sendStart+":"+username);
    }

    public void chatMsgHanler(PrintWriter pw){
        Scanner scanner=new Scanner(System.in);
        String chatMsg=scanner.nextLine();
        if(chatMsg.trim()==""){
            System.out.println("请输入有效的消息");
            chatMsgHanler(pw);
        }else{
            String[] arr=chatMsg.trim().split("@");
            if(arr[0].equals("")){
                System.out.println("请输入要发送消息类型或者发送人");
            }else{
                if(arr[0].equals("public")){
                    pw.println(chatType.getPUBLIC_CHAT()+":"+arr[1]);
                }else{
                    pw.println(chatType.getPRIVARE_CHAT()+"："+arr[0]+":"+arr[1]);
                }
            }
        }

    }


    class ServerHandler implements Runnable{
        BufferedReader br;
        PrintWriter pw;
        public ServerHandler(BufferedReader br, PrintWriter pw) {
            this.br = br;
            this.pw = pw;
        }
        @Override
        public void run() {
            while(true){
                try {
                    String msg=br.readLine();
                    System.out.println("server:"+msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
