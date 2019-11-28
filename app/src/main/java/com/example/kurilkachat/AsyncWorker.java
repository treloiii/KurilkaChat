//package com.example.kurilkachat;
//
//import android.os.AsyncTask;
//
//public class AsyncWorker extends AsyncTask<Void,String,Void> {
//    @Override
//    protected Object doInBackground(Object[] objects) {
//        return null;
//    }
//
//    @Override
//    protected void onPostExecute(Object o) {
//        super.onPostExecute(o);
//    }
//
//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//    }
//
//    @Override
//    protected void onProgressUpdate(Object[] values) {
//        super.onProgressUpdate(values);
//    }
//}

//TODO WEB SOCKET COMMENTS

//        try {
//            socket.connect("ws://188.225.27.155:8047/krkla", wsh=new IWebSocketConnectionHandler() {
//
//                @Override
//                public void onConnect(ConnectionResponse response) {
//                    navbar.setText(NICKNAME.concat(":  Online"));
//                    //showNotif("Ну ты лох","Соединение установлено");
//
//                }
//
//                @Override
//                public void onOpen() {
////                    tt.setText("CONNECT");
//                    socket.sendMessage("{\"id\":\""+NICKNAME+"\",\"message\":\"connect\"}");
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            while (true) {
//                                socket.sendMessage("{\"id\":\""+NICKNAME+"\",\"message\":\"stillAlive\"}");
//                                try {
//                                    Thread.sleep(5000);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }
//                    }).start();
//                    loadOnStart();
//
//
//
//                }
//
//                @Override
//                public void onClose(int code, String reason) {
//                    //showNotif(reason,"Соединение оборвано");
//                    navbar.setText(NICKNAME.concat(":  Offline"));
//                    new android.os.Handler().postDelayed(
//                            () -> {
//                                try {
//                                    socket.connect("ws://188.225.27.155:8047/krkla",wsh);
//                                } catch (WebSocketException e) {
//                                    e.printStackTrace();
//                                }
//                            },
//                            5000);
//                }
//
//                @Override
//                public void onMessage(String payload) {
//                    try {
//                        if (!payload.split("[:]")[1].equals(" 6a5df9fcac0cfa3b9b264f372dae311d") && !payload.split("[:]")[1].equals(" 7f021a1415b86f2d013b2618fb31ae53") && !payload.split("[:]")[1].equals(" b640a0ce465fa2a4150c36b305c1c11b") && !payload.split("[:]")[1].equals(" 9d634e1a156dc0c1611eb4c3cff57276") && !payload.split("[:]")[1].equals(" pong")&&!payload.split("[:]")[1].equals("")) {
//                            newMessage(scroll_pane, payload.split("[:]")[1], payload.split("[:]")[0], nowAtime(), payload.split("[:]")[0].equals(NICKNAME), false);
//                        } else if (payload.split("[:]")[1].equals(" 7f021a1415b86f2d013b2618fb31ae53")) {
//                            //TODO end typing
//                            sostoyanie.setText("");
//                        } else if (payload.split("[:]")[1].equals(" b640a0ce465fa2a4150c36b305c1c11b")) {
//                            //TODO user connected
//                            if (!payload.split("[:]")[0].equals(NICKNAME)) {
//                                sostoyanie.setText(payload.split("[:]")[0] + " подключился");
//                                new android.os.Handler().postDelayed(
//                                        () -> sostoyanie.setText(""),
//                                        1000);
//                            }
//
//                        } else if (payload.split("[:]")[1].equals(" 9d634e1a156dc0c1611eb4c3cff57276")) {
//                            //TODO user disconnected
//                            sostoyanie.setText(payload.split("[:]")[0] + " отключился");
//                            new android.os.Handler().postDelayed(
//                                    () -> sostoyanie.setText(""),
//                                    1000);
//                        } else if (payload.split("[:]")[1].equals(" 6a5df9fcac0cfa3b9b264f372dae311d")) {
//                            //TODO typing
//                            if (!payload.split("[:]")[0].equals(NICKNAME)) {
//                                sostoyanie.setText(payload.split("[:]")[0] + " печатает...");
//                            }
//                        }
//                    }
//                    catch (Exception e){
//
//                    }
//
//                }
//
//                @Override
//                public void onMessage(byte[] payload, boolean isBinary) {
//
//                }
//
//                @Override
//                public void onPing() {
//
//                }
//
//                @Override
//                public void onPing(byte[] payload) {
//
//                }
//
//                @Override
//                public void onPong() {
//
//                }
//
//                @Override
//                public void onPong(byte[] payload) {
//
//                }
//
//                @Override
//                public void setConnection(WebSocketConnection connection) {
//
//                }
//            });
//
//
//
//        } catch(WebSocketException wse) {
//            Log.d("WEBSOCKETS", wse.getMessage());
//        }


// new android.os.Handler().postDelayed(
//                        () -> {socket.sendMessage("{\"id\":\""+NICKNAME+"\",\"message\":\"7f021a1415b86f2d013b2618fb31ae53\"}");},
//                        1000);