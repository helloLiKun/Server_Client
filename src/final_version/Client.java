package final_version;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
/**
 * 客户端应用程序
 * Created by liKun on 2018/1/4 0004.
 */
public class Client {
    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }

    //客户端Socket
    private Socket socket;
    /**
     * 构造方法，用于初始化
     */
    public Client(){
        try {
            socket = new Socket("localhost",8088);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 客户端工作方法
     */
    public void start(){
        try {
            //创建Scanner读取用户输入内容
            Scanner scanner = new Scanner(System.in);
            //首先输入昵称
            inputNickName(scanner);

            //将接收服务端信息的线程启动
            ServerHander handler = new ServerHander();
            Thread t = new Thread(handler);
            t.setDaemon(true);
            t.start();

            OutputStream out = socket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(out,"UTF-8");
            PrintWriter pw = new PrintWriter(osw,true);
            while(true){
                pw.println(scanner.nextLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            if(socket != null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 输入昵称
     */
    private void inputNickName(Scanner scanner)throws Exception{
        //定义昵称
        String nickName = null;
        //创建输出流
        PrintWriter pw = new PrintWriter(
                new OutputStreamWriter(
                        socket.getOutputStream(),"UTF-8")
                ,true);
        //创建输入流
        BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        socket.getInputStream(),"UTF-8")
        );
        /*
         * 循环以下操作
         * 输入用户名，并上传至服务器，等待服务器回应，若昵称可用就结束循环，否则通知用户后
         * 重新输入昵称
         */
        while(true){
            System.out.println("请输入昵称:");
            nickName = scanner.nextLine();
            if(nickName.trim().equals("")){
                System.out.println("昵称不能为空");
            }else{
                pw.println(nickName);
                String pass = br.readLine();
                if(pass!=null&&!pass.equals("OK")){
                    System.out.println("昵称已被占用，请更换。");
                }else{
                    System.out.println("你好!"+nickName+",开始聊天吧!");
                    break;
                }
            }
        }
    }

    /**
     * 该线程用于接收服务端发送过来的信息
     */
    private class ServerHander implements Runnable{
        @Override
        public void run() {
            try {
                InputStream in = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(in, "UTF-8");
                BufferedReader br = new BufferedReader(isr);
                while(true){
                    System.out.println(br.readLine());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
