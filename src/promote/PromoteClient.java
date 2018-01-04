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
    public static void main(String[] args){
        PromoteClient client=new PromoteClient();
        client.start();

    }
    public void start(){
        Socket socket;
        String username;
        String read;
        ExecutorService executorService= Executors.newFixedThreadPool(10);
        try {
            socket=new Socket("localhost",9999);
            BufferedReader br=new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
            PrintWriter pw=new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"utf-8"),true);
            Scanner scanner=new Scanner(System.in);
            System.out.println("请输入用户名：");
            username=scanner.nextLine();
            pw.println("%username%:"+username);
            System.out.println(br.readLine());
            while(true){
                String msg="";
                read=br.readLine();
                if(read.equals("%name%is%exit%")){
                    System.out.println("用户名已经存在！");
                    System.out.println("请重新输入：");
                    msg="%username%:";
                }else if(read.equals("%login%success%")){
                    System.out.println("登录成功！");
                    executorService.execute(new ServerHandler(br,pw));
                }else{
                    msg="%"+username+"%said:"+read;
                }
                msg=msg+scanner.nextLine();
                pw.println(msg);
                String[] strs=msg.split(":");
                if(strs!=null && strs.length>0){
                    if(strs[0].equals("")){
                        username=strs[1];
                    }
                }
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
