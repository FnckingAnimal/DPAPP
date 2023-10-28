package app.dpapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by S7187445 on 2018/9/4.
 */
public class ImageUtils {
    public static final String PATH="/mypicture/com/";
    private static ImageView showImageView;
    private static String mUrl;
    public static String getFileName(String url){
        int index = url.lastIndexOf("/") + 1;
        return url.substring(index);
    }

    /**
     * 读取uri所在的图片
     *
     * @param uri      图片对应的Uri
     * @param mContext 上下文对象
     * @return 获取图像的Bitmap
     */
    public static Bitmap getBitmapFromUri(Uri uri, Context mContext) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void setImageBitmap(String url,ImageView iv) {
        showImageView = iv;
        mUrl = url;
        Bitmap loacalBitmap = ImageUtils.getLoacalBitmap(Environment.getExternalStorageDirectory()+ImageUtils.PATH + ImageUtils.getFileName(url));
        if(loacalBitmap != null) {
            showImageView.setImageBitmap(loacalBitmap);
        } else {
            new DownImgAsyncTask().execute(url);
        }
    }

    public static Bitmap getBitmapByUrl(final String url) {
        URL fileUrl = null;
        InputStream is = null;
        Bitmap bitmap = null;
        try {
            fileUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) fileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                is = null;
            }
            return bitmap;
        }

    }
     public static File saveImage(Bitmap btm,String path,String fileName) {
         File appDir = new File(path);
         if(!appDir.exists()) {
             appDir.mkdir();
         }
         File file = new File(appDir,fileName);
         try {
             FileOutputStream fos = new FileOutputStream(file);
             btm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
             fos.flush();
             fos.close();
         } catch (FileNotFoundException e) {
             e.printStackTrace();
         }  catch (IOException e) {
             e.printStackTrace();
         }
          return file;
     }

    public static class DownImgAsyncTask extends AsyncTask<String,Void,Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = getBitmapByUrl(params[0]);
            return bitmap;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            if(result!= null) {
                File file = saveImage(result,Environment.getExternalStorageDirectory() + PATH,getFileName(mUrl));
                showImageView.setImageBitmap(result);
            }
        }
    }
    public static Bitmap getLoacalBitmap(String url) {
        if(url != null) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(url);
                return BitmapFactory.decodeStream(fis);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            } finally {
                if(fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    fis = null;
                }
            }
        } else {
            return  null;
        }
    }

    // 歸併排序
    public static void mergeSort(int[] array,int start,int end) {
        if(start < end) {
            //拆分成兩個小集合，分別進行遞歸
            int mid = (start +end) / 2;
            mergeSort(array,start,mid);
            mergeSort(array,mid+1,end);
            //把兩個有序小集合歸併成一個大集合
            merge(array,start,mid,end);
        }
    }

    public static void merge(int[] array,int start,int mid,int end) {
        //開闢額外大集合，設置指針
        int[] tempArray = new int[end - start + 1];
        int p1 = start,p2=mid+1,p=0;
        //比較兩個小集合的元素，分別放到大集合
        while(p1 <= mid && p2 <= end) {
            if(array[p1] <= array[p2]) {
                tempArray[p++]=array[p1++];
            } else {
                tempArray[p++]=array[p2++];
            }
        }
        //左側小集合還有剩餘，一次放入大集合尾部
        while(p1 <= mid) {
            tempArray[p++] = array[p1++];
        }
        //右側小集合還有剩餘，一次反復如大集合尾部
        while(p2 <= end) {
            tempArray[p++] = array[p2++];
        }
        //把大集合的元素複製回遠數組
        for (int i = 0; i < tempArray.length; i++) {
            array[i+start] = tempArray[i];
        }
    }

//    public static void main(String[] args) {
//        int[] array = {5,8,6,3,9,2,1,7};
//        mergeSort(array,0,array.length-1);
//        System.out.print(Arrays.toString(array));
//    }
    /**
     * 為圖片創建不同的名稱用於保存，避免覆蓋
     */
    public static String createFileName() {
        String fileName = "";
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("'IMA'_yyyyMMdd_HHssmm");
        fileName = format.format(date) + ".jpg";
        return fileName;
    }

    /**
     * XML解析
     */
    private void parseXMLWithPull(String result) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(result));

            String id = "";
            String name = "";

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG://开始解析
                        if ("county".equals(nodeName)) {
                            id = parser.getAttributeValue(null, "id");
                            name = parser.getAttributeValue(null, "name");
                        }
                        break;

                    case XmlPullParser.END_TAG://完成解析
                        if ("county".equals(nodeName)) {

                        }
                        break;
                    default:
                        break;
                }
                eventType=parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
