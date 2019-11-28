package tcp.client;

import java.net.Socket;

public class Client {
    private String hostname;
    private int port;
    private String username;
    private WriteThread wt;

    public Client(String hostname,int port,String username){
        this.hostname=hostname;
        this.port=port;
        this.username=username;
    }

   public void listenToServer(MessageHandler caller){
        try{
            Socket socket=new Socket(hostname,port);
            ReadThread rt=new ReadThread(socket,this);
            rt.registerCallBack(caller);
            rt.start();
            wt=new WriteThread(socket,this);
            sendMessage("connected");
            //wt.start();
            rt.join();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void sendMessage(String text){
        wt.sendMessage(new Message(text,this.username));
        wt.sendMessage(new Message(text,this.username));
    }

    public void disconnect(){
        wt.setStop();
    }

    public String getUsername() {
        return username;
    }
}
