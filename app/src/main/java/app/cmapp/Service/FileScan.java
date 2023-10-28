package app.cmapp.Service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.os.FileObserver;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/7/5.
 */
public class FileScan extends FileObserver
{
    private String mAbsolutePath;
    public FileScan(String path)
    {
        super(path);
        mAbsolutePath=path;
    }
    @Override
    public void onEvent(int event, String path) {
        switch (event) {
            case android.os.FileObserver.CREATE:

                final String filepath=mAbsolutePath+File.separator+path;
                final String filename=path;
                new Thread(){

                   // File file = new File(File.separator+path);
                    public void run(){
                        //获取图片的高和宽
                        //BitmapFactory.Options options = new BitmapFactory.Options();
                        //这一个设置使 BitmapFactory.decodeFile获得的图片是空的,但是会将图片信息写到options中
                        //options.inJustDecodeBounds = false;

                        String filetype=filename.substring(filename.length()-3,filename.length());
                        Bitmap bt1=null;

                        while(bt1== null) {
                            bt1 = BitmapFactory.decodeFile(filepath, null);
                        }
                        int degree = readPictureDegree(filepath);

                        //Bitmap bt1=BitmapFactory.decodeFile(filepath,null);

                        Matrix matrix = new Matrix();;
                        matrix.postRotate(degree);

                        Bitmap resizedBitmap=null;
                        while(resizedBitmap==null) {
                            resizedBitmap = Bitmap.createBitmap(bt1, 0, 0, bt1.getWidth(), bt1.getHeight(), matrix, true);
                        }


                        try {
                           // long timestamp = System.currentTimeMillis();

                            //refFormatNowDate()
                            String cmbuimagetarget = "CMSF-" + refFormatNowDate()+"-"+ Scanimage.sysAndroidID;
                            Bitmap bm=watermarkBitmap(resizedBitmap, cmbuimagetarget,filetype);
                            File f = new File(mAbsolutePath, filename);
                            try {
                                FileOutputStream out = new FileOutputStream(f);
                                switch (filetype) {
                                    case "jpg":
                                    bm.compress(Bitmap.CompressFormat.JPEG, 30, out);
                                        break;
                                    case "png":
                                        bm.compress(Bitmap.CompressFormat.PNG, 30, out);
                                        break;
                                    default:
                                        bm.compress(Bitmap.CompressFormat.JPEG, 30, out);
                                        break;
                                }
                                out.flush();
                                out.close();
                                bm.recycle();
                                bm=null;
                                System.gc();

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        catch(Exception ex1)
                        {
                            return;
                        }
                        finally {
                            resizedBitmap.recycle();
                            resizedBitmap=null;
                            bt1.recycle();
                            bt1=null;
                        }
                    }

                }.start();
                //String data = readFile(file);//读取文件内容操作
                break;
        }
    }

    //获取图片缩小的图片
    public  Bitmap scaleBitmap(String src,int max)
    {
        //获取图片的高和宽
        BitmapFactory.Options options = new BitmapFactory.Options();
        //这一个设置使 BitmapFactory.decodeFile获得的图片是空的,但是会将图片信息写到options中
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(src, options);
        // 计算比例 为了提高精度,本来是要640 这里缩为64
        max=max/10;
        int be = options.outWidth / max;
        if(be%10 !=0)
            be+=10;
        be=be/10;
        if (be <= 0)
            be = 1;
        options.inSampleSize = be;
        //设置可以获取数据
        options.inJustDecodeBounds = false;
        //获取图片
        return BitmapFactory.decodeFile(src, options);
    }
    // 加水印 也可以加文字
    public  Bitmap watermarkBitmap(Bitmap src, String title,String filetype) {
        if (src == null) {
            return null;
        }
        int w = src.getWidth();
        int h = src.getHeight();
        //需要处理图片太大造成的内存超过的问题,这里我的图片很小所以不写相应代码了
        Bitmap newb= Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
        Canvas cv = new Canvas(newb);
        cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src
        Paint paint=new Paint();
        //加入图片
        /*
        if (watermark != null) {
            int ww = watermark.getWidth();
            int wh = watermark.getHeight();
            paint.setAlpha(50);
            cv.drawBitmap(watermark, w - ww + 5, h - wh + 5, paint);// 在src的右下角画入水印
        }
        */
        //加入文字
        if(title!=null)
        {
            String familyName ="宋体";
            Typeface font = Typeface.create(familyName, Typeface.BOLD);
            /*
            TextPaint textPaint=new TextPaint();
            textPaint.setColor(Color.argb(50, 50, 50, 50));
            textPaint.setTypeface(font);
            textPaint.setTextSize(200);
*/
            paint.setColor(Color.argb(50, 50, 50, 50));
            paint.setTypeface(font);

            switch (filetype)
            {
                //JPG
                case "jpg":
                    paint.setTextSize(200);
                    break;
                //png
                case "png":
                    paint.setTextSize(100);
                    break;
                default:
                    paint.setTextSize(200);
                    break;
            }
            paint.setTextAlign(Paint.Align.RIGHT);

            //设置光源的方向
            float[] direction = new float[]{ 1, 1, 1 };
            //设置环境光亮度
            float light = 0.4f;
            //选择要应用的反射等级
            float specular = 6;
            //向mask应用一定级别的模糊
            float blur = 3.5f;
            EmbossMaskFilter maskfilter=new EmbossMaskFilter(direction,light,specular,blur);
            paint.setMaskFilter(maskfilter);

            //这里是自动换行的
            //StaticLayout layout = new StaticLayout(title,textPaint,w, Layout.Alignment.ALIGN_NORMAL,1.0F,0.0F,true);
            //layout.draw(cv);
            //文字就加左上角算了

            Path path = new Path();
            //path.moveTo(w/6, h/2);
            //path.quadTo(w/2, h/2-h/4, w-w/6, h/2);
            //canvas.drawPath(mPath, mPaint);
            switch (filetype)
            {
                //JPG
                case "jpg":
                    path.addArc(new RectF(w / 6, h / 5 + 250, w - w / 6, h - h / 5 + 250), 160, 220);
                    break;
                //png
                case "png":
                    path.addArc(new RectF(w / 6, h / 5 + 250, w - w / 6, h - h / 5 + 250), 140, 220);
                    break;
                default:
                    path.addArc(new RectF(w / 6, h / 5 + 250, w - w / 6, h - h / 5 + 250), 160, 220);
                    break;
            }
            //path.addArc(new RectF(w/6,h/5+250,w-w/6,h-h/5+250),160,220);
            //path.addCircle(w / 2, h / 2, w/2-w/6, Path.Direction.CW);
            //cv.drawText(title, 0, 200, paint);
            //cv.drawPath(path,paint);
            cv.drawTextOnPath(title,path,0,-20,paint);
        }
//        cv.save(Canvas.ALL_SAVE_FLAG);// 保存
        cv.restore();// 存储
        return newb;
    }

    public  int readPictureDegree(String path) {
        int degree  = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
    public String refFormatNowDate() {
        Date nowTime = new Date(System.currentTimeMillis());
        SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyyMMddHHmm");
        String retStrFormatNowDate = sdFormatter.format(nowTime);
        return retStrFormatNowDate;
    }
}
