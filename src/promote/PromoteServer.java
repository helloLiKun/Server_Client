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
    Map<String,PrintWriter> pws=new HashMap<>();
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
                handlerClient();
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

    public void handlerClient(){
        try {
            BufferedReader br=new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
            PrintWriter pw=new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"utf-8"),true);
            pw.println("成功链接到服务器");
            String msg=br.readLine();
            if(msg!=null){
                String[] arr=msg.split(":");
                if(arr.length>0){
                    String typeIdentifier=msg.split(":")[0];
                    if(typeIdentifier.equals(chatType.getLOGIN_NAME())){
                        Handler handler=new Handler(br,pw);
                        executorService.execute(handler);
                    }else if(typeIdentifier.equals(chatType.getLOGIN_PASSWORD())){

                    }else if(typeIdentifier.equals(chatType.getLOGIN_SUCCESS())){

                    }else if(typeIdentifier.equals(chatType.getPRIVARE_CHAT())){

                    }else if(typeIdentifier.equals(chatType.getPUBLIC_CHAT())){

                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handlerLogin(String msg){
        String[] contents=msg.split(":");
    }



    public class Handler implements Runnable{
        private BufferedReader br;
        private PrintWriter pw;
        public Handler() {
        }
        public Handler(BufferedReader br, PrintWriter pw) {
            this.br = br;
            this.pw = pw;
        }
        @Override
        public void run() {
            String name=null;
            try {
                while(true){
                    String msg=br.readLine().trim();
                    if(name==null && msg!=null){
                        String[] strs=msg.split(":");
                        if(strs!=null && strs.length>0){
                            if(strs[0].equals("%username%")){
                                name=strs[1];
                                for(Map.Entry<String,PrintWriter> entry:pws.entrySet()){
                                    if(entry.getKey().equals(name)){
                                        pw.println("%name%is%exit%");
                                        return;
                                    }
                                }
                                System.out.println(name+"上线了！");
                                pw.println("%login%success%");
                                pws.put(name,pw);

                            }
                        }
                    }else{
                        System.out.println(name+":"+msg);
                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
