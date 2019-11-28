package com.example.kurilkachat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.text.Editable;
import android.text.Html;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.AlignmentSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.websocket.CloseReason;

import io.crossbar.autobahn.websocket.WebSocketConnection;
import io.crossbar.autobahn.websocket.exceptions.WebSocketException;
import io.crossbar.autobahn.websocket.interfaces.IWebSocketConnectionHandler;
import io.crossbar.autobahn.websocket.types.ConnectionResponse;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tcp.client.Client;
import tcp.client.MessageHandler;

public class MainActivity extends Activity implements MessageHandler {

    private LinearLayout scroll_pane;
    private WebsocketClientEndpoint clientEndPoint;
    private OkHttpClient client;
    private Button btnSend;
    private TextView sostoyanie,navbar;
    private ScrollView scroll;
    private EditText textInput;
    protected static final  String NICKNAME="ПоЖиЛоЙйй";
    //protected static final  String NICKNAME="Гришин";
    private IWebSocketConnectionHandler wsh;
    WebSocketClient c = null;
    private final WebSocketConnection  socket = new WebSocketConnection();
    public static final MediaType MEDIA_TYPE = MediaType.parse("application/json");
    private MessageServerResponse[] messages;


    private Handler mHandler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        navbar=findViewById(R.id.navbar);
        scroll_pane = findViewById(R.id.scroll_pane);
        btnSend=findViewById(R.id.btnSend);
        textInput=findViewById(R.id.textInput);
        scroll=findViewById(R.id.scroll);
        sostoyanie=findViewById(R.id.sostoyanie);
        client = new OkHttpClient();

        textInput.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(!textInput.getText().toString().equals("")) {
                    socket.sendMessage("{\"id\":\"" + NICKNAME + "\",\"message\":\"" + textInput.getText().toString() + "\"}");
                    textInput.setText("");
                }
                return true;
            }


        });

        TextWatcher tw=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                System.out.println(s);
//                socket.sendMessage("{\"id\":\""+NICKNAME+"\",\"message\":\"6a5df9fcac0cfa3b9b264f372dae311d\"}");

            }

            @Override
            public void afterTextChanged(Editable s) {
//                System.out.println("перестал печатать");
//                //socket.sendMessage("{\"id\":\""+NICKNAME+"\",\"message\":\"7f021a1415b86f2d013b2618fb31ae53\"}");
//                new android.os.Handler().postDelayed(
//                        () -> {socket.sendMessage("{\"id\":\""+NICKNAME+"\",\"message\":\"7f021a1415b86f2d013b2618fb31ae53\"}");},
//                        1000);
            }
        };
        textInput.addTextChangedListener(tw);

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                System.out.println(message.obj.toString());
                Gson gson=new Gson();
                tcp.client.Message msg =gson.fromJson(message.obj.toString(), tcp.client.Message.class);
                newMessage(scroll_pane,msg.getText(),NICKNAME,"12:15",true,false);
                // This is where you do your work in the UI thread.
                // Your worker tells you in the message what to do.
            }
        };

        Intent stopIntent=new Intent(this,BackgroundService.class);
        stopIntent.setAction("stop");
        startService(stopIntent);
        final Client client1=new Client("kurilkachat.trelloiii.site",8091,NICKNAME);
        try {
            Thread a = new Thread(() -> {
                client1.listenToServer(this);
            });
            a.start();
            //a.join();
        }
        catch (Exception e){
            e.printStackTrace();
        }




        btnSend.setOnClickListener(v -> {
            client1.sendMessage(textInput.getText().toString());
//            if(!textInput.getText().toString().equals("")) {
//                socket.sendMessage("{\"id\":\"" + NICKNAME + "\",\"message\":\"" + textInput.getText().toString() + "\"}");
//                System.out.println(textInput.getText().toString());
//                JSONObject postdata = new JSONObject();
//                try {
//                    postdata.put("id",  NICKNAME);
//                    postdata.put("message", textInput.getText().toString());
//                } catch(JSONException e){
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        final Request request = new Request.Builder()
//                                .url("http://kurilka.std-763.ist.mospolytech.ru/post.php?id="+NICKNAME+"&message="+textInput.getText().toString())
//                                .get()
//                                .addHeader("Content-Type", "application/json")
//                                .addHeader("cache-control", "no-cache")
//                                .build();
//                        try {
//                            Gson gson=new Gson();
//                            Response response=client.newCall(request).execute();
//                            MessageServerResponse[] message=gson.fromJson(response.body().string(),MessageServerResponse[].class);
//                            //MessageServerResponse message=gson.fromJson(response.body().string(),MessageServerResponse.class);
//                            System.out.println(message[0].getMessage());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).start();
//                textInput.setText("");
//            }

        });





    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("destroy","STOP");
        Intent startIntent=new Intent(this,BackgroundService.class);
        startIntent.setAction("start");
        startForegroundService(startIntent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        System.out.println(keyCode);
        return super.onKeyDown(keyCode, event);
    }

   private  void loadOnStart(){
       Handler handler=new Handler();
       handler.post(new Runnable() {
           @Override
           public void run() {
               scroll_pane.removeAllViews();
               final Request request = new Request.Builder()
                       .url("http://kurilka.std-763.ist.mospolytech.ru/getMessages.php")
                       .get()
                       .addHeader("Content-Type", "application/json")
                       .addHeader("cache-control", "no-cache")
                       .build();
               try {
                   Gson gson=new Gson();
                   Response response=client.newCall(request).execute();
                   //System.out.println(response.body().string());
                   MessageServerResponse[] messages1=gson.fromJson(response.body().string(),MessageServerResponse[].class);
                   //MessageServerResponse message=gson.fromJson(response.body().string(),MessageServerResponse.class);
                   for (MessageServerResponse message:messages1) {
                       newMessage(scroll_pane,message.getMessage(),message.getId(),message.getTime(),NICKNAME.equals(message.getId()),true);
                   }

               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
       });
   }

    String nowAtime(){
        Date date = new Date();   // given date
        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setTime(date);


       return calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE);
    }

    void showNotif(String title,String text){
        NotificationHelper nh = new NotificationHelper(MainActivity.this);
        nh.createNotification(title, text);
    }
    public void newMessage(LinearLayout ll, String message,String id,String time, boolean isMy,boolean isFirstLoad){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        params.setMargins(20, 40, 20, 10);
        if(isMy) {
            params.gravity = Gravity.END;
            params.setMargins(0, 40, 20, 10);
        }
        TextView text = new TextView(this);
        String result_message_text=id+"  \n"+message+"  \n"+time;
        SpannableString styled_message=new SpannableString(result_message_text);

        styled_message.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_NORMAL),0,id.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        styled_message.setSpan(new ForegroundColorSpan(getColor(R.color.nickname_color)),0,id.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        styled_message.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_OPPOSITE),id.length()+6+message.length(),styled_message.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        styled_message.setSpan(new ForegroundColorSpan(getColor(R.color.message_time)),id.length()+6+message.length(),styled_message.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setText(styled_message);
        text.setLayoutParams(params);
        text.setBackground(getDrawable(R.drawable.back));
        text.setPadding(15,0,15,0);
        text.setTextColor(getResources().getColor(R.color.white));
        ll.addView(text);
        scrollDown();
        if(!isMy&&!id.equals("")&&!isFirstLoad){
            showNotif(id,message);
        }


    }


    @Override
    protected void onResume() {
        scroll_pane.removeAllViews();
        loadOnStart();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        moveTaskToBack(true);
    }

    void scrollDown(){
        scroll.post(new Runnable() {
            @Override
            public void run() {
                scroll.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    @Override
    public void onMessage(String s) {
        System.out.println("message income");
        Message message = mHandler.obtainMessage(0,s);
        message.sendToTarget();



    }
}