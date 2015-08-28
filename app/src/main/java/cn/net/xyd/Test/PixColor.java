package cn.net.xyd.Test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import collect.xyd.net.cn.xinyidaicollect.R;
import collect.xyd.net.cn.xinyidaicollect.utils.L;

/**
 * Created by Administrator on 2015/7/31 0031.
 */
public class PixColor {
    private Context context;
    public PixColor(Context context){
        this.context = context;
    }
    public void getPixColor() {
        Bitmap src = BitmapFactory.decodeResource(context.getResources(), R.mipmap.login_bg22);
        int A, R, G, B;
        int pixelColor;
        int height = src.getHeight();
        int width = src.getWidth();


        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixelColor = src.getPixel(x, y);
                A = Color.alpha(pixelColor);
                R = Color.red(pixelColor);
                G = Color.green(pixelColor);
                B = Color.blue(pixelColor);

                L.e("A:", A + "");
                L.e("R:", R + "");
                L.e("G:", G + "");
                L.e("B:", B + "");


            }
        }
    }
}
