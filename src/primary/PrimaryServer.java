package primary;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by liKun on 2018/1/4 0004.
 */
public class PrimaryServer {
    public static void main(String[] args){
        ServerSocket server = null;
        try {
            server=new ServerSocket(9999);
            Socket socket=server.accept();
            PrintWriter pw=new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
            pw.println("你好，客户端");

            InputStream is=socket.getInputStream();
            BufferedReader br=new BufferedReader(new InputStreamReader(is,"UTF-8"));

            String message=br.readLine();
            System.out.println("client:"+message);



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
}
