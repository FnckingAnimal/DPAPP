package app.cmapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import app.cmapp.Interface.Iobjectrhandler;
import app.cmapp.appcdl.BaseFuncation;

/**
 * Created by F5460007 on 2016/12/26.
 */
public class Exectempfile {
    private volatile static Exectempfile _signef;
    private  Exectempfile()
    {
    }
    public static Exectempfile instance()
    {
        if (_signef == null)
        {
            synchronized (Exectempfile.class)
            {
                if (_signef == null)
                {
                    _signef = new Exectempfile();
                }
            }
        }
        return _signef;
    }

    /**
     * Save Temp fail in data
     * @param filename
     * @param value
     * @return
     */
    public BaseFuncation.rrtype savefile(String filename,List<String> value) {

        BaseFuncation.rrtype r=new BaseFuncation(). new rrtype();
        if(filename==null||filename.trim().equals(""))
        {
            r.set_rstatus(false);
            r.set_rmsg("Send filename is null");
            return r;
        }

        if(value==null||value.size()==0)
        {
            r.set_rstatus(false);
            r.set_rmsg("Send value is null");
            return r;
        }

        String filePATH= "/storage/emulated/0/CMSF/Tempfile/machinecheck/";
        //String filePATH= "/storage/emulated/0/CMSF/";
        File file = new File(filePATH);
        if (!file.exists())
        {
            file.mkdirs();
        }

        File saveFile = new File(filePATH+filename+".txt");
        if(saveFile.exists())
        {
            saveFile.delete();
        }
        try {
            saveFile.createNewFile();
            FileOutputStream outStream = new FileOutputStream(saveFile);
            for (String s:value) {
                outStream.write((s + "\r\n").getBytes());
            }
            outStream.flush();
            outStream.close();
            r.set_rstatus(true);
            r.set_rmsg("Success");
            return r;
        }
        catch(IOException ex)
        {
            r.set_rstatus(false);
            r.set_rmsg(ex.getMessage());
            return r;
        }
        finally {
            file=null;
            saveFile=null;
        }
    }

    /**
     * Read Temp file
     * @param filename
     * @param i
     * @return
     */
    public BaseFuncation.rrtype getfile(String filename, Iobjectrhandler i,int leadi)
    {
        BaseFuncation.rrtype r=new BaseFuncation(). new rrtype();
        if(filename==null||filename.trim().equals("")) {
            r.set_rstatus(false);
            r.set_rmsg("Send filename is null");
            return r;
        }

        BufferedReader reader = null;
        List<String> data=new ArrayList<String>();
        File file;
        FileInputStream inStream;
        String line;
        try {
            file = new File("/storage/emulated/0/CMSF/Tempfile/machinecheck/"+filename+".txt");
            if(!file.exists())
            {
                r.set_rstatus(true);
                return r;
            }
            inStream = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(inStream));
            while ((line = reader.readLine()) != null) {
                data.add(line);
            }
            reader.close();
            inStream.close();

            i.execobject(leadi, data);
            r.set_rstatus(true);
            return r;
        } catch (Exception ex) {
            r.set_rstatus(false);
            r.set_rmsg(ex.getMessage());
            return r;
        } finally {
            reader = null;
            file = null;
            inStream = null;
            line = null;
        }
    }

    /**
     * Remove file
     * @param filename
     * @return
     */
    public BaseFuncation.rrtype removefile(String filename)
    {
        BaseFuncation.rrtype rt=new BaseFuncation().new rrtype();
        String filePATH=null;
        File file;
        try
        {
            if(filename==null||filename.trim().equals(""))
            {
                rt.set_rstatus(false);
                rt.set_rmsg("Send filename is null");
                return rt;
            }
            filePATH = "/storage/emulated/0/CMSF/Tempfile/machinecheck/";
            file = new File(filePATH+filename+".txt");
            if (file.exists()) {
                file.delete();
            }
            rt.set_rstatus(true);
            return rt;
        }
        catch(Exception ex)
        {
            rt.set_rstatus(false);
            rt.set_rmsg(ex.getMessage());
            return rt;
        }
        finally {
            filePATH=null;
            file=null;
        }
    }

    /**
     * Clear all cache file
     * @return
     */
    public BaseFuncation.rrtype clearcachefile()
    {
        BaseFuncation.rrtype r=new BaseFuncation(). new rrtype();
        try {
            String filePATH = "/storage/emulated/0/CMSF/Tempfile/machinecheck/";

            File file = new File(filePATH);
            if (file.exists()) {
                String[] tempList = file.list();
                for (String s:tempList
                     ) {
                    File f1=new File(filePATH+s);
                    f1.delete();
                }
            }
            r.set_rstatus(true);
            return r;
        }
        catch (Exception ex)
        {
            r.set_rstatus(false);
            r.set_rmsg(ex.getMessage());
            return r;
        }
    }



    /**
     * Save Temp fail in data
     * @param filename
     * @param value
     * @return
     */
    public BaseFuncation.rrtype savefilenew(String filename,List<String> value,String fileurl) {

        BaseFuncation.rrtype r=new BaseFuncation(). new rrtype();
        if(filename==null||filename.trim().equals(""))
        {
            r.set_rstatus(false);
            r.set_rmsg("Send filename is null");
            return r;
        }

        if(value==null||value.size()==0)
        {
            r.set_rstatus(false);
            r.set_rmsg("Send value is null");
            return r;
        }

        String filePATH= "/storage/emulated/0/CMSF/Tempfile/machinecheck/";
        //String filePATH= "/storage/emulated/0/CMSF/";
        filePATH=filePATH+fileurl;
        File file = new File(filePATH);
        if (!file.exists())
        {
            file.mkdirs();
        }

        File saveFile = new File(filePATH+filename+".txt");
        if(saveFile.exists())
        {
            saveFile.delete();
        }
        try {
            saveFile.createNewFile();
            FileOutputStream outStream = new FileOutputStream(saveFile);
            for (String s:value) {
                outStream.write((s + "\r\n").getBytes());
            }
            outStream.flush();
            outStream.close();
            r.set_rstatus(true);
            r.set_rmsg("Success");
            return r;
        }
        catch(IOException ex)
        {
            r.set_rstatus(false);
            r.set_rmsg(ex.getMessage());
            return r;
        }
        finally {
            file=null;
            saveFile=null;
        }
    }

    /**
     * Read Temp file
     * @param fileurl
     * @return
     */
    public String[] getfilenew(String fileurl)
    {
        String filePATH= "/storage/emulated/0/CMSF/Tempfile/machinecheck/";

        File file;
        try {
            filePATH=filePATH+fileurl;
            file = new File(filePATH);
            if(!file.exists())
            {
                return null;
            }
            String[] strArray=file.list();
            return strArray;
        } catch (Exception ex) {
            return null;
        }
        finally {
            filePATH=null;
            file=null;
        }
    }

    /**
     * Remove file
     * @param filename
     * @return
     */
    public BaseFuncation.rrtype removefilenew(String filename,String fileurl)
    {
        BaseFuncation.rrtype rt=new BaseFuncation().new rrtype();
        String filePATH=null;
        File file;
        try
        {
            if(filename==null||filename.trim().equals(""))
            {
                rt.set_rstatus(false);
                rt.set_rmsg("Send filename is null");
                return rt;
            }
            filePATH = "/storage/emulated/0/CMSF/Tempfile/machinecheck/";
            filePATH=filePATH+fileurl;
            file = new File(filePATH+filename+".txt");
            if (file.exists()) {
                file.delete();
            }
            rt.set_rstatus(true);
            return rt;
        }
        catch(Exception ex)
        {
            rt.set_rstatus(false);
            rt.set_rmsg(ex.getMessage());
            return rt;
        }
        finally {
            filePATH=null;
            file=null;
        }
    }

    /**
     * Clear all cache file
     * @return
     */
    public BaseFuncation.rrtype clearcachefilenew(String fileurl)
    {
        BaseFuncation.rrtype r=new BaseFuncation(). new rrtype();
        try {
            String filePATH = "/storage/emulated/0/CMSF/Tempfile/machinecheck/";
            filePATH=filePATH+fileurl;
            File file = new File(filePATH);
            if (file.exists()) {
                String[] tempList = file.list();
                for (String s:tempList
                        ) {
                    File f1=new File(filePATH+s);
                    f1.delete();
                }
            }
            r.set_rstatus(true);
            return r;
        }
        catch (Exception ex)
        {
            r.set_rstatus(false);
            r.set_rmsg(ex.getMessage());
            return r;
        }
    }
}