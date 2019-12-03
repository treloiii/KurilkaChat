package com.example.kurilkachat;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ImageResolver {

    private MainActivity activity;
    private Context context;

    public ImageResolver(MainActivity activity, Context context) {
        this.activity = activity;
        this.context = context;
    }

    public void newImageMessage(Drawable drawable, ImageView newImg,boolean isMy, boolean isFirstLoad){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dpToPx(150),dpToPx(200));
        if(isMy) {
            params.gravity = Gravity.END;
            params.setMargins(0, 40, 20, 10);
        }
        else {
            params.gravity = Gravity.START;
            params.setMargins(20, 40, 0, 10);
        }


       // newImg.setImageDrawable(drawable);
        newImg.setScaleType(ImageView.ScaleType.FIT_XY);
//        Bitmap tmp=((BitmapDrawable)newImg.getDrawable()).getBitmap();
//        newImg.setImageBitmap(ImageHelper.getRoundedCornerBitmap(tmp,dpToPx(10)));
        newImg.setLayoutParams(params);
        if(!isFirstLoad) {
            activity.scroll_pane.addView(newImg);
            activity.scrollDown();
        }
    }
    public void newTextEmptyImageMessage(String imageName,String message,String name,String time,boolean isMy){
        View v= LayoutInflater.from(context).inflate(R.layout.message_text,null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dpToPx(150),dpToPx(200));
        params.height= LinearLayout.LayoutParams.WRAP_CONTENT;
        if(isMy) {
            params.gravity = Gravity.END;
            params.setMargins(0, 40, 20, 10);
        }
        else {
            params.gravity = Gravity.START;
            params.setMargins(20, 40, 0, 10);
        }
        v.setLayoutParams(params);
        ImageView one=v.findViewById(R.id.test);
        one.setImageDrawable(context.getDrawable(R.drawable.img_background));
        TextView tr=v.findViewById(R.id.textView);
        tr.setText(message);
        TextView nameTv=v.findViewById(R.id.name);
        nameTv.setText(name);
        TextView timeTv=v.findViewById(R.id.time);
        timeTv.setText(time);
        activity.scroll_pane.addView(v);
        activity.loadedMessageImgs.put(imageName+":"+name,v);
        activity.scrollDown();
    }
    public void newTextImageMessage(Drawable drawable,View v,boolean isMy,boolean isFirstLoad){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dpToPx(150),dpToPx(200));
        params.height= LinearLayout.LayoutParams.WRAP_CONTENT;
        if(isMy) {
            params.gravity = Gravity.END;
            params.setMargins(0, 40, 20, 10);
        }
        else {
            params.gravity = Gravity.START;
            params.setMargins(20, 40, 0, 10);
        }

        ImageView one=v.findViewById(R.id.test);
       // one.setImageDrawable(drawable);
        v.setLayoutParams(params);
        if(!isFirstLoad) {
            activity.scroll_pane.addView(v);
            activity.scrollDown();
        }
    }
    public void loadEmptyImages(String name,String imgName,boolean isMy){
        ImageView newImg=new ImageView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dpToPx(150),dpToPx(200));
        if(isMy) {
            params.gravity = Gravity.END;
            params.setMargins(0, 40, 20, 10);
        }
        else {
            params.gravity = Gravity.START;
            params.setMargins(20, 40, 0, 10);
        }
        params.setMargins(0, 40, 20, 10);
        newImg.setLayoutParams(params);
        newImg.setImageDrawable(context.getDrawable(R.drawable.img_background));
        activity.scroll_pane.addView(newImg);
        activity.loadedImgs.put(imgName+":"+name,newImg);
        activity.scrollDown();
    }

    public int dpToPx(int dp) {
        float density = context.getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }
    public float dpi(){
        return context.getResources()
                .getDisplayMetrics()
                .density;
    }
    public int getScreenHeight(){
        return context.getResources()
                .getDisplayMetrics()
                .heightPixels;
    }
    public int getScreenWidth(){
        return context.getResources()
                .getDisplayMetrics()
                .widthPixels;
    }
}
