package tcp.client;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class WriteThread {

    private Socket socket;
    private PrintWriter writer;
    private Client client;
    private boolean isStop=false;
    private Gson gson;

    WriteThread(Socket socket,Client client){
        this.socket=socket;
        this.client=client;
        this.gson=new Gson();
        try {
            OutputStream out=socket.getOutputStream();
            this.writer=new PrintWriter(out,true);

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setStop(){
        isStop=true;
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void sendMessage(Message message){
        this.writer.println(gson.toJson(message));
    }
}
