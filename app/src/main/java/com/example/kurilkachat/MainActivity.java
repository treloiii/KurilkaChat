package com.example.kurilkachat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.AlignmentSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.java_websocket.client.WebSocketClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.crossbar.autobahn.websocket.WebSocketConnection;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import tcp.client.Client;
import tcp.client.MessageHandler;

public class MainActivity extends Activity implements MessageHandler {

    private LinearLayout scroll_pane,imageAttach;
    private WebsocketClientEndpoint clientEndPoint;
    private OkHttpClient client;
    private Button btnSend,btnAttachFile;
    private TextView sostoyanie,navbar;
    private ScrollView scroll;
    private EditText textInput;
    private ImageView imgView,test;
    //protected static final  String NICKNAME="ПоЖиЛоЙйй";
    protected static final  String NICKNAME="Гришин";
    //private IWebSocketConnectionHandler wsh;
    WebSocketClient c = null;
    private final WebSocketConnection  socket = new WebSocketConnection();
    public static final MediaType MEDIA_TYPE = MediaType.parse("application/json");
    private MessageServerResponse[] messages;
    final Client client1=new Client("kurilkachat.trelloiii.site",8091,NICKNAME);

    private Map<String,ImageView> loadedImgs=new LinkedHashMap<>();
    private Handler mHandler;

    private Bitmap sendBitmap;
    private File sendFile;



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
        imageAttach=findViewById(R.id.imgAttach);
        btnSend=findViewById(R.id.btnSend);
        imgView=findViewById(R.id.imgView);
        test=findViewById(R.id.test);
        btnAttachFile=findViewById(R.id.btnSendFile);
        textInput=findViewById(R.id.textInput);
        scroll=findViewById(R.id.scroll);
        sostoyanie=findViewById(R.id.sostoyanie);
        client = new OkHttpClient();


        this.connectToServer();

        textInput.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(!textInput.getText().toString().equals("")) {
                  //  socket.sendMessage("{\"id\":\"" + NICKNAME + "\",\"message\":\"" + textInput.getText().toString() + "\"}");
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
                client1.sendMessage("text change","textInput");
//                System.out.println(s);
//                socket.sendMessage("{\"id\":\""+NICKNAME+"\",\"message\":\"6a5df9fcac0cfa3b9b264f372dae311d\"}");

            }

            @Override
            public void afterTextChanged(Editable s) {
                new android.os.Handler().postDelayed(()->{
                    client1.sendMessage("text stop","textOver");
                },1000);
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
                tcp.client.Message msg=(tcp.client.Message) message.obj;
                if(msg.getTextAdmin().equals("textInput"))
                    setTyping(msg.getName());
                else if(msg.getTextAdmin().equals("textOver"))
                    setUnTyping();
                else
                    newMessage(scroll_pane,msg.getText(),msg.getName(),nowAtime(),msg.getName().equals(NICKNAME),false);
            }
        };

//        Intent stopIntent=new Intent(this,BackgroundService.class);
//        stopIntent.setAction("stop");
//        startService(stopIntent);







        btnSend.setOnClickListener(v -> {
//            client1.sendMessage(textInput.getText().toString(),"msg");
            if(!textInput.getText().toString().equals("")) {
                client1.sendMessage(textInput.getText().toString(),"msg");
                if(sendBitmap!=null){
                    sendFile();
                    textInput.setText("");
                }
                else{
                    this.postMessageToDb(textInput.getText().toString());
                    textInput.setText("");
                }
            }
            else {
                if (sendBitmap != null) {
                   sendFile();
                }
            }
        });
        btnAttachFile.setOnClickListener(v->{

            Dexter.withActivity(this)
                    .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse response) {
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_PICK);
                            // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            //  intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                            startActivityForResult(intent,1);

                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse response) {

                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                        }
                    })
                    .check();

        });
//        test.setImageDrawable(LoadImageFromWebOperations("https://wallbox.ru/wallpapers/main/201620/37da0352f83ab7b.jpg"));
        loadOnStart();



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println(requestCode);
        if (data != null) {
            Uri contentURI = data.getData();
            String realPath=getRealPathFromURI(this,contentURI);
            sendFile=new File(realPath);
            System.out.println(realPath);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);

                Toast.makeText(MainActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                imgView.setImageBitmap(bitmap);
               // imgView.setVisibility(View.VISIBLE);
                imageAttach.setVisibility(View.VISIBLE);
                sendBitmap=bitmap;
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    private void sendFile(){
        imageAttach.setVisibility(View.GONE);
        new Thread(() -> {
            try {
            RequestBody req = new MultipartBody.Builder().addFormDataPart("file",sendFile.getName(),RequestBody.create(MediaType.parse(Files.probeContentType(sendFile.toPath())),sendFile)).
                    addFormDataPart("name", NICKNAME).
                    addFormDataPart("message", textInput.getText().toString()).build();
            final Request request = new Request.Builder()
                    .url("http://kurilkahttp.std-763.ist.mospolytech.ru/upload/img")
                    .post(req)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .build();

                Response response = client.newCall(request).execute();
                // System.out.println(response.body().string());
                loadImage("http://kurilkahttp.std-763.ist.mospolytech.ru/static/" + response.body().string() + ".jpg",new ImageView(this),false);
                sendBitmap=null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public String bitmapToBase64(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public void deleteAttached(View v){
        imageAttach.setVisibility(View.GONE);
        this.sendBitmap=null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        client1.disconnect();

//        Log.d("destroy","STOP");
//        Intent startIntent=new Intent(this,BackgroundService.class);
//        startIntent.setAction("start");
//        startForegroundService(startIntent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        System.out.println(keyCode);
        return super.onKeyDown(keyCode, event);
    }

   private  void loadOnStart(){
       scroll_pane.removeAllViews();
       new AsyncLoadOnStart().execute();
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

    public void loadEmptyImages(String imgName){
        ImageView newImg=new ImageView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dpToPx(150),dpToPx(200));
        params.gravity=Gravity.END;
        params.setMargins(0, 40, 20, 10);
        newImg.setLayoutParams(params);
        newImg.setImageDrawable(getResources().getDrawable(R.drawable.img_background));
        scroll_pane.addView(newImg);
        loadedImgs.put(imgName,newImg);
        scrollDown();
    }

    public void loadImage(String url,ImageView newImg,boolean isFirstLoad){
        AsyncLoadImg asyncLoadImg=new AsyncLoadImg();
        asyncLoadImg.setImgView(newImg);
        asyncLoadImg.setFirstLoad(isFirstLoad);
        asyncLoadImg.execute(url);
    }

    public void newImageMessage(Drawable drawable,ImageView newImg,boolean isFirstLoad){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dpToPx(150),dpToPx(200));
        params.gravity=Gravity.END;
        params.setMargins(0, 40, 20, 10);
        newImg.setLayoutParams(params);

        newImg.setImageDrawable(drawable);
        newImg.setScaleType(ImageView.ScaleType.FIT_XY);
        Bitmap tmp=((BitmapDrawable)newImg.getDrawable()).getBitmap();
        newImg.setImageBitmap(ImageHelper.getRoundedCornerBitmap(tmp,30));
        if(!isFirstLoad) {
            scroll_pane.addView(newImg);
            scrollDown();
        }
    }

    public int dpToPx(int dp) {
        float density = this.getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }


    public Drawable LoadImageFromWebOperations(String url) {

        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "img");
            return d;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    protected void onResume() {
//        scroll_pane.removeAllViews();

        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
       // moveTaskToBack(true);
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
    public void onMessage(tcp.client.Message s) {
        System.out.println("message income");
        Message message;
        if(s.getTextAdmin()!=null&&!s.getTextAdmin().equals("pong")) {
            message = mHandler.obtainMessage(0, s);
            message.sendToTarget();
        }

    }


    private void postMessageToDb(String message){
        JSONObject postdata = new JSONObject();
            try {
                postdata.put("id",  NICKNAME);
                postdata.put("message", textInput.getText().toString());
            } catch(JSONException e){
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    RequestBody body=new MultipartBody.Builder().addFormDataPart("name",NICKNAME).addFormDataPart("message",message).build();
                    final Request request = new Request.Builder()
                            .url("http://kurilkahttp.std-763.ist.mospolytech.ru/new/message")
                            .post(body)
                            .addHeader("Content-Type", "application/json")
                            .addHeader("cache-control", "no-cache")
                            .build();
                    try {
                        client.newCall(request).execute();
//                        MessageServerResponse[] message=gson.fromJson(response.body().string(),MessageServerResponse[].class);
//                        //MessageServerResponse message=gson.fromJson(response.body().string(),MessageServerResponse.class);
//                        System.out.println(message[0].getMessage());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
    }


    private void connectToServer(){
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
    }

    private void setTyping(String nickname){
        sostoyanie.setText(String.format("%s печатает...", nickname));
        new android.os.Handler().postDelayed(() -> sostoyanie.setText(""), 1000);
    }
    private void setUnTyping(){
        sostoyanie.setText("");
    }


    class AsyncLoadImg extends AsyncTask<String,Void, Drawable> {

        private ImageView imgView;
        private boolean firstLoad;
        public void setImgView(ImageView imgView) {
            this.imgView = imgView;
        }

        @Override
        protected Drawable doInBackground(String... strings) {
            return LoadImageFromWebOperations(strings[0]);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Drawable drawable) {
            super.onPostExecute(drawable);
            newImageMessage(drawable,imgView,firstLoad);
            //newImageMessage(drawable);
        }

        public void setFirstLoad(boolean firstLoad) {
            this.firstLoad = firstLoad;
        }
    }
    class AsyncLoadOnStart extends AsyncTask<Void,Void,Response>{

        @Override
        protected Response doInBackground(Void... voids) {
            final Request request = new Request.Builder()
                    .url("http://kurilkahttp.std-763.ist.mospolytech.ru/get/messages")
                    .get()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .build();
            try {
                return client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            try {
                Gson gson = new Gson();
                MessageServerResponse[] messages = gson.fromJson(response.body().string(), MessageServerResponse[].class);
                for (int i = 0; i < messages.length; i++) {
                    MessageServerResponse message = messages[i];
                    String msg=gson.toJson(message,MessageServerResponse.class);
                    String a=msg;
                    if (message.getMessage().equals("") && !message.getImg().equals("")) {
                        //loadImage("http://kurilkahttp.std-763.ist.mospolytech.ru/static/" + message.getImg() + ".jpg");
                        loadEmptyImages(message.getImg());
                    } else {
                        newMessage(scroll_pane, message.getMessage(), String.valueOf(message.getName()), nowAtime(), NICKNAME.equals(message.getName()), true);
                    }
                }
                List<String> alKeys = new ArrayList<>(loadedImgs.keySet());
                Collections.reverse(alKeys);
                for(String key:alKeys){
                    loadImage("http://kurilkahttp.std-763.ist.mospolytech.ru/static/" + key + ".jpg", loadedImgs.get(key),true);
                }
//                for (Map.Entry<String, ImageView> entry : loadedImgs.entrySet()) {
//                        loadImage("http://kurilkahttp.std-763.ist.mospolytech.ru/static/" + entry.getKey() + ".jpg", entry.getValue(), true);
//                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}

