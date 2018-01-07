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
    String username;
    ChatType chatType=ChatType.getChatType();
    ExecutorService executorService=Executors.newFixedThreadPool(10);
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
                ConsoleHandler consoleHandler=new ConsoleHandler(read,pw);
                executorService.execute(consoleHandler);
            }
        } catch (IOException e) {
            System.out.println("获取链接失败");
        }
    }

    public class ConsoleHandler implements Runnable{
        private String read;
        private PrintWriter pw;

        public ConsoleHandler(String read, PrintWriter pw) {
            this.read = read;
            this.pw = pw;
        }

        public void run(){
            if(read!=null){
                String[] reads=read.split(":");
                if(reads.length>0){
                    String typeIdentifier=reads[0];
                    if(typeIdentifier.equals(chatType.getLOGIN_NAME_EXIT())){
                        System.out.println("该用户已存在，请重新输入");
                        sendMsgHandler(pw,"请输入用户名",chatType.getLOGIN_NAME());
                    }else if(typeIdentifier.equals(chatType.getEXITE_SUCCESS())){
                        System.out.println("您已下线");
                        sendMsgHandler(pw,"请输入用户名",chatType.getLOGIN_NAME());
                    }else if(typeIdentifier.equals(chatType.getLOGIN_SUCCESS())){
                        System.out.println("登录成功！可以聊天了。");
                        System.out.println("如果发布广播消息直接输入发布的消息内容；如果私信则私信名@私信内容;如果下线输入EXIT..");
                        chatMsgHanler(pw);
                    }else if(typeIdentifier.equals(chatType.getCONNECT_SUCCESS())){
                        //链接成功
                        System.out.println("成功链接到服务器");
                        sendMsgHandler(pw,"请输入用户名",chatType.getLOGIN_NAME());
                    }else{
                        System.out.println(read);
                        chatMsgHanler(pw);
                    }
                }
            }
        }
    }

    public void sendMsgHandler(PrintWriter pw,String promptMsg,String sendStart){
        System.out.println(promptMsg);
        Scanner scanner=new Scanner(System.in);
        username=scanner.nextLine();
        pw.println(sendStart+":"+username);
    }

    public void chatMsgHanler(PrintWriter pw){
        System.out.println("请输入聊天消息：");
        Scanner scanner=new Scanner(System.in);
        String chatMsg=scanner.nextLine();
        if(chatMsg.trim()==""){
            System.out.println("请输入有效的消息");
            chatMsgHanler(pw);
        }else{
            String[] arr=chatMsg.trim().split("@");
            if(arr[0].equals("")){
                System.out.println("请输入要发送的内容");
            }else if(arr[0].equals("EXIT..")){
                pw.println(chatType.getEXITE_SYSTEM()+":"+username);
            }else if(arr.length==1){
                pw.println(chatType.getPUBLIC_CHAT()+":"+username+":"+arr[0]);
            }else{
                pw.println(chatType.getPRIVARE_CHAT()+":"+username+":"+arr[0]+":"+arr[1]);
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
