package promote;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by liKun on 2018/1/4 0004.
 */
public class PromoteClient1 {
    public static void main(String[] args){
        Socket socket=null;
        String username=null;
        try {
            socket=new Socket("localhost",9999);
            BufferedReader br=new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
            PrintWriter pw=new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"utf-8"),true);
            Scanner scanner=new Scanner(System.in);
            String read=null;
            System.out.println("请输入用户名：");
            username=scanner.nextLine();
            pw.println(username);
            System.out.println(br.readLine());
            while(true){System.out.println(username+"：");
                String msg=scanner.nextLine();
                pw.println(msg);
                read=br.readLine();
                if(read!=null){
                    System.out.println("服务端："+read);
                }
            }
        } catch (IOException e) {
            System.out.println("获取链接失败");
        }
    }
}
