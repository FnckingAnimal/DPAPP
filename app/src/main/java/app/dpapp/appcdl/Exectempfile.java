package app.dpapp.appcdl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import app.dpapp.Interface.Iobjectrhandler;

/**
 * Created by F5460007 on 2016/12/26.
 */
public class Exectempfile {
    private volatile static Exectempfile _signef;

    private Exectempfile() {
    }

    public static Exectempfile instance() {
        if (_signef == null) {
            synchronized (Exectempfile.class) {
                if (_signef == null) {
                    _signef = new Exectempfile();
                }
            }
        }
        return _signef;
    }

    /**
     * Save Temp fail in data
     *
     * @param filename
     * @param value
     * @return
     */
    public BaseFuncation.rrtype savefile(String filename, List<String> value) {

        BaseFuncation.rrtype r = new BaseFuncation().new rrtype();
        if (filename == null || filename.trim().equals("")) {
            r.set_rstatus(false);
            r.set_rmsg("Send filename is null");
            return r;
        }

        if (value == null || value.size() == 0) {
            r.set_rstatus(false);
            r.set_rmsg("Send value is null");
            return r;
        }

        String filePATH = "/storage/emulated/0/CMSF/Tempfile/machinecheck/machinecheck2/";
        // String filePATH= "/storage/emulated/0/CMSF/";
        File file = new File(filePATH);
        if (!file.exists()) {
            file.mkdirs();
        }

        File saveFile = new File(filePATH + filename + ".txt");
        if (saveFile.exists()) {
            saveFile.delete();
        }
        try {
            saveFile.createNewFile();
            FileOutputStream outStream = new FileOutputStream(saveFile);
            for (String s : value) {
                outStream.write((s + "\r\n").getBytes());
            }
            outStream.flush();
            outStream.close();
            r.set_rstatus(true);
            r.set_rmsg("Success");
            return r;
        } catch (IOException ex) {
            r.set_rstatus(false);
            r.set_rmsg(ex.getMessage());
            return r;
        } finally {
            file = null;
            saveFile = null;
        }
    }

    public BaseFuncation.rrtype saveFileEqChecklist(String filename, List<String> value) {

        BaseFuncation.rrtype r = new BaseFuncation().new rrtype();
        if (filename == null || filename.trim().equals("")) {
            r.set_rstatus(false);
            r.set_rmsg("Send filename is null");
            return r;
        }

        if (value == null || value.size() == 0) {
            r.set_rstatus(false);
            r.set_rmsg("Send value is null");
            return r;
        }

        String filePATH = "/storage/emulated/0/CMSF/Tempfile/machinecheck/machinecheck222/";
        // String filePATH= "/storage/emulated/0/CMSF/";
        File file = new File(filePATH);
        if (!file.exists()) {
            file.mkdirs();
        }

        File saveFile = new File(filePATH + filename + ".txt");
        if (saveFile.exists()) {
            saveFile.delete();
        }
        try {
            saveFile.createNewFile();
            FileOutputStream outStream = new FileOutputStream(saveFile);
            for (String s : value) {
                outStream.write((s + "\r\n").getBytes());
            }
            outStream.flush();
            outStream.close();
            r.set_rstatus(true);
            r.set_rmsg("Success");
            return r;
        } catch (IOException ex) {
            r.set_rstatus(false);
            r.set_rmsg(ex.getMessage());
            return r;
        } finally {
            file = null;
            saveFile = null;
        }
    }

    public BaseFuncation.rrtype savefileTwo(String filename, List<String> value) {

        BaseFuncation.rrtype r = new BaseFuncation().new rrtype();
        if (filename == null || filename.trim().equals("")) {
            r.set_rstatus(false);
            r.set_rmsg("Send filename is null");
            return r;
        }

        if (value == null || value.size() == 0) {
            r.set_rstatus(false);
            r.set_rmsg("Send value is null");
            return r;
        }

        String filePATH = "/storage/emulated/0/CMSF/Tempfile/machinecheck/";
        // String filePATH= "/storage/emulated/0/CMSF/";
        File file = new File(filePATH);
        if (!file.exists()) {
            file.mkdirs();
        }

        File saveFile = new File(filePATH + filename + ".txt");
        if (saveFile.exists()) {
            saveFile.delete();
        }
        try {
            saveFile.createNewFile();
            FileOutputStream outStream = new FileOutputStream(saveFile);
            for (String s : value) {
                outStream.write((s + "\r\n").getBytes());
            }
            outStream.flush();
            outStream.close();
            r.set_rstatus(true);
            r.set_rmsg("Success");
            return r;
        } catch (IOException ex) {
            r.set_rstatus(false);
            r.set_rmsg(ex.getMessage());
            return r;
        } finally {
            file = null;
            saveFile = null;
        }
    }

    /**
     * Read Temp file
     *
     * @param filename
     * @param i
     * @return
     */
    public BaseFuncation.rrtype getfile(String filename, Iobjectrhandler i, int leadi) {
        BaseFuncation.rrtype r = new BaseFuncation().new rrtype();
        if (filename == null || filename.trim().equals("")) {
            r.set_rstatus(false);
            r.set_rmsg("Send filename is null");
            return r;
        }

        BufferedReader reader = null;
        List<String> data = new ArrayList<>();
        File file;
        FileInputStream inStream;
        String line;
        try {
            file = new File("/storage/emulated/0/CMSF/Tempfile/machinecheck/machinecheck2/" + filename + ".txt");
            if (!file.exists()) {
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

    public BaseFuncation.rrtype getfile222(String filename, Iobjectrhandler i, int leadi) {
        BaseFuncation.rrtype r = new BaseFuncation().new rrtype();
        if (filename == null || filename.trim().equals("")) {
            r.set_rstatus(false);
            r.set_rmsg("Send filename is null");
            return r;
        }

        BufferedReader reader = null;
        List<String> data = new ArrayList<>();
        File file;
        FileInputStream inStream;
        String line;
        try {
            file = new File("/storage/emulated/0/CMSF/Tempfile/machinecheck/machinecheck222/" + filename + ".txt");
            if (!file.exists()) {
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

    public BaseFuncation.rrtype getfileTwo(String filename, Iobjectrhandler i, int leadi) {
        BaseFuncation.rrtype r = new BaseFuncation().new rrtype();
        if (filename == null || filename.trim().equals("")) {
            r.set_rstatus(false);
            r.set_rmsg("Send filename is null");
            return r;
        }

        BufferedReader reader = null;
        List<String> data = new ArrayList<>();
        File file;
        FileInputStream inStream;
        String line;
        try {
            file = new File("/storage/emulated/0/CMSF/Tempfile/machinecheck/" + filename + ".txt");
            if (!file.exists()) {
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
     *
     * @param filename
     * @return
     */
    public BaseFuncation.rrtype removefile(String filename) {
        BaseFuncation.rrtype rt = new BaseFuncation().new rrtype();
        String filePATH = null;
        File file;
        try {
            if (filename == null || filename.trim().equals("")) {
                rt.set_rstatus(false);
                rt.set_rmsg("Send filename is null");
                return rt;
            }
            filePATH = "/storage/emulated/0/CMSF/Tempfile/machinecheck/";
            file = new File(filePATH + filename + ".txt");
            if (file.exists()) {
                file.delete();
            }
            rt.set_rstatus(true);
            return rt;
        } catch (Exception ex) {
            rt.set_rstatus(false);
            rt.set_rmsg(ex.getMessage());
            return rt;
        } finally {
            filePATH = null;
            file = null;
        }
    }

    /**
     * Clear all cache file
     *
     * @return
     */
    public BaseFuncation.rrtype clearcachefile() {
        BaseFuncation.rrtype r = new BaseFuncation().new rrtype();
        try {
            String filePATH = "/storage/emulated/0/CMSF/Tempfile/machinecheck/";

            File file = new File(filePATH);
            if (file.exists()) {
                String[] tempList = file.list();
                for (String s : tempList
                ) {
                    File f1 = new File(filePATH + s);
                    f1.delete();
                }
            }
            r.set_rstatus(true);
            return r;
        } catch (Exception ex) {
            r.set_rstatus(false);
            r.set_rmsg(ex.getMessage());
            return r;
        }
    }


    /**
     * Save Temp fail in data
     *
     * @param filename
     * @param value
     * @return
     */
    public BaseFuncation.rrtype savefilenew(String filename, List<String> value, String fileurl) {

        BaseFuncation.rrtype r = new BaseFuncation().new rrtype();
        if (filename == null || filename.trim().equals("")) {
            r.set_rstatus(false);
            r.set_rmsg("Send filename is null");
            return r;
        }

        if (value == null || value.size() == 0) {
            r.set_rstatus(false);
            r.set_rmsg("Send value is null");
            return r;
        }

        String filePATH = "/storage/emulated/0/CMSF/Tempfile/machinecheck/";
        // String filePATH= "/storage/emulated/0/CMSF/";
        filePATH = filePATH + fileurl;
        File file = new File(filePATH);
        if (!file.exists()) {
            file.mkdirs();
        }

        File saveFile = new File(filePATH + filename + ".txt");
        if (saveFile.exists()) {
            saveFile.delete();
        }
        try {
            saveFile.createNewFile();
            FileOutputStream outStream = new FileOutputStream(saveFile);
            for (String s : value) {
                outStream.write((s + "\r\n").getBytes());
            }
            outStream.flush();
            outStream.close();
            r.set_rstatus(true);
            r.set_rmsg("Success");
            return r;
        } catch (IOException ex) {
            r.set_rstatus(false);
            r.set_rmsg(ex.getMessage());
            return r;
        } finally {
            file = null;
            saveFile = null;
        }
    }

    /**
     * Read Temp file
     *
     * @param fileurl
     * @return
     */
    public String[] getfilenew(String fileurl) {
        String filePATH = "/storage/emulated/0/CMSF/Tempfile/machinecheck/";
///storage/emulated/0/CMSF/Tempfile/machinecheck/M1710C001-003_AIDGDGD/M1710C001-003.txt
        File file;
        try {
            filePATH = filePATH + fileurl;
            file = new File(filePATH);
            if (!file.exists()) {
                return null;
            }
            String[] strArray = file.list();
            return strArray;
        } catch (Exception ex) {
            return null;
        } finally {
            filePATH = null;
            file = null;
        }
    }

    /**
     * Remove file
     *
     * @param filename
     * @return
     */
    public BaseFuncation.rrtype removefilenew(String filename, String fileurl) {
        BaseFuncation.rrtype rt = new BaseFuncation().new rrtype();
        String filePATH = null;
        File file;
        try {
            if (filename == null || filename.trim().equals("")) {
                rt.set_rstatus(false);
                rt.set_rmsg("Send filename is null");
                return rt;
            }
            filePATH = "/storage/emulated/0/CMSF/Tempfile/machinecheck/";
            filePATH = filePATH + fileurl;
            file = new File(filePATH + filename + ".txt");
            if (file.exists()) {
                file.delete();
            }
            rt.set_rstatus(true);
            return rt;
        } catch (Exception ex) {
            rt.set_rstatus(false);
            rt.set_rmsg(ex.getMessage());
            return rt;
        } finally {
            filePATH = null;
            file = null;
        }
    }

    /**
     * Clear all cache file
     *
     * @return
     */
    public BaseFuncation.rrtype clearcachefilenew() {
        BaseFuncation.rrtype r = new BaseFuncation().new rrtype();
        try {
            String filePATH = "/storage/emulated/0/CMSF/Tempfile/machinecheck/";
            File file = new File(filePATH);
            if (file.exists()) {
                File[] tempList = file.listFiles();
                for (File s : tempList
                ) {
                    s.delete();
                }
            }
            r.set_rstatus(true);
            r.set_rmsg("Success");
            return r;
        } catch (Exception ex) {
            r.set_rstatus(false);
            r.set_rmsg(ex.getMessage());
            return r;
        }
    }

    /**
     * 刪除 文件夾 及其目錄下的文件
     *
     * @param filePath 被刪除木流的文件路徑
     * @return 目錄刪除成功返回true 否則返回false
     */

    public boolean deleteDirectory(String filePath) {
        boolean flag = false;
        if (!filePath.endsWith(File.separator)) {
            filePath = filePath + File.separator;
        }
        File dirFile = new File(filePath);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            } else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag)
            return false;
        return dirFile.delete();

    }

    /**
     * 刪除單個文件
     *
     * @param filePath 被刪除文件的文件名
     * @return 文件刪除成功返回true 否則返回false
     */

    public boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }

    /**
     * 根據路徑刪除指定的文件或目錄，無論存在與否
     *
     * @param filePath 要刪除的文件或目錄
     * @return 刪除成功返回true  否則返回false
     */


    public boolean DeleteFolder(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return false;
        } else {
            if (file.isFile()) {
                return deleteFile(filePath);
            } else {
                return deleteDirectory(filePath);
            }
        }
    }

}
