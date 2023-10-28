package app.dpapp;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;

public class WriteText {

    public String LOG_FILE_NAME = "/mnt/sdcard/test_01.log";


    public WriteText(){

    }

    private String getTimeString()
    {
        java.util.Date now= new Date();
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yy-MM-dd HH:mm:ss.sss");
        String rlt = formatter.format(now);
        return rlt;
    }


    private void saveLog( String log)
    {
        try{
            OutputStream os=new FileOutputStream(LOG_FILE_NAME, true);
            os.write(log.getBytes());
            os.flush();
            os.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void writeLog(String out)
    {
        String log = getTimeString()+":    "+out+"\n";
        saveLog(log);
    }
}
