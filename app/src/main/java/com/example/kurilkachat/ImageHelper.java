package com.example.kurilkachat;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

public class ImageHelper {
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {

        Bitmap imageRounded = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Canvas canvas = new Canvas();
        canvas.setBitmap(imageRounded);
        Paint mpaint = new Paint();
        mpaint.setAntiAlias(true);
        mpaint.setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        canvas.drawRoundRect((new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight())), 100, 100, mpaint);

        return imageRounded;
    }
}
