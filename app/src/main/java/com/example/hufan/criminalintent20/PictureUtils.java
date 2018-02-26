package com.example.hufan.criminalintent20;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.Display;
import android.widget.ImageView;

/**
 * Created by hufan on 2016/8/12.
 * 从当地文件中寻得一图，缩小到适合窗口大小的尺寸
 */
public class PictureUtils {
    @SuppressWarnings("deprecation")
    public static BitmapDrawable getScaledDrawable(Activity a,String path){
        Display display=a.getWindowManager().getDefaultDisplay();
        float destWidth=display.getWidth();
        float destHeight=display.getHeight();

        //读取磁盘中照片尺寸
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;//合理解码界限
        BitmapFactory.decodeFile(path,options);

        float srcWidth=options.outWidth;
        float srcHeight=options.outHeight;

        int inSampleSize=1;
        if(srcHeight>destHeight||srcWidth>destWidth){
            if(srcWidth>srcHeight){
                inSampleSize=Math.round(srcHeight/destHeight);//取整
            }else {
                inSampleSize=Math.round(srcWidth/destWidth);
            }
        }
        options=new BitmapFactory.Options();
        options.inSampleSize=inSampleSize;

        Bitmap bitmap=BitmapFactory.decodeFile(path,options);
        return new BitmapDrawable(a.getResources(),bitmap);
    }

    //卸载图片
    public static void cleanImageView(ImageView imageView){
        if(!(imageView.getDrawable() instanceof  BitmapDrawable))
            return;
        BitmapDrawable b=(BitmapDrawable)imageView.getDrawable();
        b.getBitmap().recycle();//释放位图占用的原始存储空间
        imageView.setImageDrawable(null);

    }
}
