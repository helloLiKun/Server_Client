package primary;

import java.io.*;
import java.net.Socket;

/**
 * Created by liKun on 2018/1/4 0004.
 */
public class PrimaryClient {
    public static void main(String[] args){
        Socket socket = null;
        try {
            socket=new Socket("localhost",9999);
            InputStream is=socket.getInputStream();
            OutputStream os=socket.getOutputStream();
            BufferedReader br=new BufferedReader(new InputStreamReader(is,"utf-8"));
            PrintWriter pw=new PrintWriter(new OutputStreamWriter(os,"utf-8"),true);
            pw.println("你好服务端");
            System.out.println("server:"+br.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(socket!=null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }


}
