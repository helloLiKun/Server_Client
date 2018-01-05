package promote;

import java.io.*;
import java.lang.reflect.Field;
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



    }
    public void start(){
        Socket socket;
        String username;
        ExecutorService executorService= Executors.newFixedThreadPool(10);
        try {
            socket=new Socket("localhost",9999);
            BufferedReader br=new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
            PrintWriter pw=new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"utf-8"),true);
            while(true){
                String read=br.readLine();
                if(read!=null){
                    String[] reads=read.split(":");
                    if(reads.length>0){
                        String typeIdentifier=reads[0];
                        if(typeIdentifier.equals(chatType.getLOGIN_NAME_EXIT())){

                        }else if(typeIdentifier.equals(chatType.getPASSWORD_ERROR())){

                        }else if(typeIdentifier.equals(chatType.getLOGIN_SUCCESS())){

                        }else if(typeIdentifier.equals(chatType.getSEND_SUCCESS())){

                        }
                    }
                }


//                if(read.equals(chatType.getLOGIN_NAME())){
//                    System.out.println("用户名已经存在！");
//                    System.out.println("请重新输入：");
//                    msg=chatType.getLOGIN_NAME();
//                }else if(read.equals(chatType.getLOGIN_SUCCESS())){
//                    System.out.println("登录成功！");
//                    executorService.execute(new ServerHandler(br,pw));
//                }else{
//                }
//                pw.println(msg);
//                String[] strs=msg.split(":");
//                if(strs!=null && strs.length>0){
//                    if(strs[0].equals("")){
//                        username=strs[1];
//                    }
//                }
            }
        } catch (IOException e) {
            System.out.println("获取链接失败");
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
