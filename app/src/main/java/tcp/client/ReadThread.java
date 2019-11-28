package tcp.client;

import android.telecom.Call;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ReadThread extends Thread {

    private Socket socket;
    private Client client;
    private Gson gson;
    private BufferedReader reader;
    private MessageHandler callback;
    ReadThread(Socket socket,Client client){
        this.socket=socket;
        this.client=client;
        this.gson=new Gson();
        try{
           InputStream input= socket.getInputStream();
           reader=new BufferedReader(new InputStreamReader(input));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void registerCallBack(MessageHandler callback){
        this.callback=callback;
    }

    @Override
    public void run() {

        while (true){
            try{
                Message messageFromServer =gson.fromJson(reader.readLine(),Message.class);
                System.out.println(messageFromServer.toString());
                callback.onMessage(messageFromServer);
                if(messageFromServer.text.equals("disconnected"))
                    this.client.disconnect();
            }
            catch (Exception e){
                e.printStackTrace();
                break;
            }
        }
    }
}
