package app.dpapp.appcdl.SFCBLLPack;/**
 * Created by F5460007 on 2017/4/10.
 */
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import app.dpapp.appcdl.BaseFuncation;
import app.dpapp.appcdl.execloadactivity;

/**
 * Owner:F5460007
 * CreateDate:2017/4/10 13:15
 */
public class ActivityInteractive extends AppCompatActivity {
    /**
     * 執行任務方法體
     */
    private SFCTaskVoidInterface _shi;
    /**
     * 迴圈執行任務方法體
     */
    private SFCTaskVoidInterface _shi2;
    /**
     * 等待鎖
     */
    private Object _lock=new Object();

    private Object _circlelock=new Object();
    /**
     * 等待返回值
     */
    private Object _value;
    /**
     * 任務方法線程
     */
    private Thread _t;
    /**
     * 執行任務名稱
     */
    private String _taskname;
    /**
     * 任務執行狀態
     */
    private Boolean _taskstatus=false;
    /**
     * 當前Activity是否依然再運行
     */
    private Boolean _activitystatus=true;

    /**
     * 啟動執行任務
     * @param shi 執行任務的方法體
     * @throws Exception
     */
    public void ExecTask(@NonNull SFCTaskVoidInterface shi,String tn) throws Exception{
        _taskname=tn;

        if(tn.isEmpty()) {
            throw new Exception("Task Name is null");
        }

        if(!_taskstatus) {
            _taskstatus = true;
            _shi = shi;

            _t=new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        _shi.taskvoid(null);
                    } catch (Exception ex) {
                        _handler.obtainMessage(1000, ex.getMessage()).sendToTarget();
                    }
                    finally {
                        _taskstatus=false;
                        execloadactivity.canclediglog();
                    }
                }
            });
            _t.start();
            execloadactivity.opendialog(this,"正在執行");
        }
        else
        {
            throw new Exception(_taskname+" is running");
        }
    }

    /**
     * 啟動執行任務
     * @param shi 載入任務的方法體
     * @param shi2 迴圈執行任務的方法體
     * @param timespan 間隔執行時間
     * @throws Exception
     */
    public void ExecTask(@NonNull SFCTaskVoidInterface shi,@NonNull SFCTaskVoidInterface shi2,String tn, final int timespan) throws Exception {
        _taskname = tn;

        if (tn.isEmpty()) {
            throw new Exception("Task Name is null");
        }

        if (!_taskstatus) {
            _taskstatus = true;
            _shi = shi;
            _shi2=shi2;
            _t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        _shi.taskvoid(null);
                        execloadactivity.canclediglog();
                        while (_activitystatus) {
                            synchronized (_circlelock) {
                                _circlelock.wait(timespan * 1000);
                            }
                            _shi2.taskvoid(null);
                        }
                    } catch (Exception ex) {
                        _handler.obtainMessage(1000, ex.getMessage()).sendToTarget();
                    } finally {
                        _taskstatus = false;
                        execloadactivity.canclediglog();
                    }
                }
            });
            _t.start();
            execloadactivity.opendialog(this,"正在執行");
        } else {
            throw new Exception(_taskname + " is running");
        }
    }

    public void realexec() {
        synchronized (_circlelock) {
            _circlelock.notify();
        }
    }

    /**
     * 調用彈出DialogActivity
     * @param title Dialog Header名稱
     * @param content Dialog顯示內容說明
     * @return DialogResult.OK,DialogResutl.Cancel
     * @throws Exception
     */
    public BaseFuncation.DialogResult MessageBox(String title,String content) throws Exception {
        if(!_taskstatus)
            throw new Exception("Task thread not running,this method can't  exec");
        String[] ss={title,content};
        _handler.obtainMessage(1, ss).sendToTarget();
        synchronized (_lock) {
            _lock.wait();
        }
        BaseFuncation.DialogResult dr= BaseFuncation.DialogResult.Cancel;

        switch (_value.toString()) {
            case "1":
                dr = BaseFuncation.DialogResult.OK;
                break;
            case "0":
                dr = BaseFuncation.DialogResult.Cancel;
                break;
        }
        return dr;
    }

    /**
     * 調用彈出DialogActivity選擇列表式
     * @param title Dialog Header名稱
     * @param content Dialog顯示內容說明
     * @param Items 數據選擇列表
     * @return Object{ DialogResult,String }
     * @throws Exception
     */
    public Object MessageBox(String title,String content,String[] Items) throws Exception {
        if (!_taskstatus)
            throw new Exception("Task thread not running,this method can't  exec");
        Object[] ss = {title, content, Items};
        //_handler.obtainMessage(1, ss).sendToTarget();
        _handler.obtainMessage(8, ss).sendToTarget();
         synchronized (_lock) {
            _lock.wait();
        }
        BaseFuncation.DialogResult dr = BaseFuncation.DialogResult.Cancel;

        int ri = Integer.parseInt(_value.toString());

        if (ri >= 0 && ri < Items.length) {
            return new Object[]{BaseFuncation.DialogResult.OK, ri};
        } else {
            return new Object[]{BaseFuncation.DialogResult.Cancel, -1};
        }
    }

    /**
     * 設定頁面UI控件值,將需設定的控件方法寫入接口o
     * @param o 需要設定值的接口方法體
     * @param valueo 接口o傳入參數 valueo
     * @throws Exception
     */
    public void SetObjectValue(@NonNull SFCTaskVoidInterface o,Object valueo) throws Exception {
        if (!_taskstatus)
            throw new Exception("Task thread not running,this method can't  exec");
        Object[] os = new Object[]{o, valueo};
        _handler.obtainMessage(4, os).sendToTarget();
        synchronized (_lock) {
            _lock.wait();
        }
    }

    /**
     * 獲取頁面UI控件值,將需獲取的控件方法寫入接口o
     * @param o 需要獲取值的接口方法體
     * @param <T> 返回類型
     * @return 返回<T>類型結果
     * @throws Exception
     */
    public <T> T GetObjectValue(@NonNull SFCTaskObjectInterface o) throws Exception {
        if(!_taskstatus)
            throw new Exception("Task thread not running,this method can't  exec");
        Object[] os = new Object[]{o, null};
        _handler.obtainMessage(5, os).sendToTarget();
        synchronized (_lock) {
            _lock.wait();
        }
        return (T)this._value;
    }

    /**
     * 獲取頁面UI控件值,將需獲取的控件方法寫入接口o
     * @param o 需要獲取值的接口方法體
     * @param <T> 返回類型
     * @return 返回<T>類型結果
     * @throws Exception
     */
    public <T> T GetObjectValue(@NonNull SFCTaskObjectInterface o,Object v) throws Exception {
        if(!_taskstatus)
            throw new Exception("Task thread not running,this method can't  exec");
        Object[] os = new Object[]{o, v};
        _handler.obtainMessage(5, os).sendToTarget();
        synchronized (_lock) {
            _lock.wait();
        }
        return (T)this._value;
    }

    /**
     * 獲取ViewGroup類控件的條數
     * @param v
     * @return
     * @throws Exception
     */
    public Integer getChildCount( ViewGroup v) throws Exception {
        return GetObjectValue(new SFCTaskObjectInterface() {
            @Override
            public Integer taskvoid(Object valueo) throws Exception {
                return ((ViewGroup) valueo).getChildCount();
            }
        }, v);
    }

    public Object getSelectedItem(AdapterView av) throws Exception{
        return GetObjectValue(new SFCTaskObjectInterface() {
            @Override
            public Object taskvoid(Object valueo) throws Exception {
                return ((AdapterView) valueo).getSelectedItem();
            }
        }, av);
    }

    /**
     * 設定控件的可用性
     * @param tv 控件TextView
     * @param b Boolean
     * @throws Exception
     */
    public void setEnabled(TextView tv,Boolean b) throws Exception {
        Object[] otv = new Object[]{tv, b};
        SetObjectValue(new SFCTaskVoidInterface() {
            @Override
            public void taskvoid(Object valueo) throws Exception {
                Object[] otv1 = (Object[]) valueo;
                ((TextView) otv1[0]).setEnabled((Boolean) otv1[1]);
            }
        }, otv);
    }


    /**
     * 獲取TextView類控件的Visibility
     * @param tv TextView
     * @return
     * @throws Exception
     */
    public int getVisibility(TextView tv) throws Exception {
        return GetObjectValue(new SFCTaskObjectInterface() {
            @Override
            public Object taskvoid(Object valueo) throws Exception {
                return ((TextView) valueo).getVisibility();
            }
        }, tv);
    }

    /**
     * 設定控件的 Visibility
     * @param tv 控件TextView
     * @param b Boolean
     * @throws Exception
     */
    public void setVisibility(TextView tv,int b) throws Exception {
        Object[] otv = new Object[]{tv, b};
        SetObjectValue(new SFCTaskVoidInterface() {
            @Override
            public void taskvoid(Object valueo) throws Exception {
                Object[] otv1 = (Object[]) valueo;
                ((TextView) otv1[0]).setVisibility((int) otv1[1]);
            }
        }, otv);
    }

    /**
     * 獲取TextView類控件的Visibility
     * @param tv EditText
     * @return
     * @throws Exception
     */
    public int getVisibility(EditText tv) throws Exception {
        return GetObjectValue(new SFCTaskObjectInterface() {
            @Override
            public Object taskvoid(Object valueo) throws Exception {
                return ((EditText) valueo).getVisibility();
            }
        }, tv);
    }

    /**
     * 設定控件的 Visibility
     * @param tv 控件EditText
     * @param b Boolean
     * @throws Exception
     */
    public void setVisibility(EditText tv,int b) throws Exception {
        Object[] otv = new Object[]{tv, b};
        SetObjectValue(new SFCTaskVoidInterface() {
            @Override
            public void taskvoid(Object valueo) throws Exception {
                Object[] otv1 = (Object[]) valueo;

                ((EditText) otv1[0]).setVisibility((int) otv1[1]);
            }
        }, otv);
    }


    /**
     * 獲取Spinner類控件的Visibility
     * @param tv EditText
     * @return
     * @throws Exception
     */
    public int getVisibility(Spinner tv) throws Exception {
        return GetObjectValue(new SFCTaskObjectInterface() {
            @Override
            public Object taskvoid(Object valueo) throws Exception {
                return ((Spinner) valueo).getVisibility();
            }
        }, tv);
    }

    /**
     * 設定控件的 Visibility
     * @param tv 控件Spinner
     * @param b Boolean
     * @throws Exception
     */
    public void setVisibility(Spinner tv,int b) throws Exception {
        Object[] otv = new Object[]{tv, b};
        SetObjectValue(new SFCTaskVoidInterface() {
            @Override
            public void taskvoid(Object valueo) throws Exception {
                Object[] otv1 = (Object[]) valueo;

                ((Spinner) otv1[0]).setVisibility((int) otv1[1]);
            }
        }, otv);
    }


    /**
     * 獲取CheckBox類控件的Visibility
     * @param tv EditText
     * @return
     * @throws Exception
     */
    public int getVisibility(CheckBox tv) throws Exception {
        return GetObjectValue(new SFCTaskObjectInterface() {
            @Override
            public Object taskvoid(Object valueo) throws Exception {
                return ((CheckBox) valueo).getVisibility();
            }
        }, tv);
    }

    /**
     * 設定控件的 Visibility
     * @param tv 控件CheckBox
     * @param b Boolean
     * @throws Exception
     */
    public void setVisibility(CheckBox tv,int b) throws Exception {
        Object[] otv = new Object[]{tv, b};
        SetObjectValue(new SFCTaskVoidInterface() {
            @Override
            public void taskvoid(Object valueo) throws Exception {
                Object[] otv1 = (Object[]) valueo;

                ((CheckBox) otv1[0]).setVisibility((int) otv1[1]);
            }
        }, otv);
    }


    /**
     * 獲取TextView類控件的Enable
     * @param tv TextView
     * @return
     * @throws Exception
     */
    public Boolean getEnabled(TextView tv) throws Exception {
        return GetObjectValue(new SFCTaskObjectInterface() {
            @Override
            public Object taskvoid(Object valueo) throws Exception {
                return ((TextView) valueo).isEnabled();
            }
        }, tv);
    }

    /**
     * 獲取CompoundButton控件的Checked
     * @param cb CompoundButton
     * @return Boolean
     * @throws Exception
     */
    public boolean isChecked(CompoundButton cb)  throws Exception {
        return GetObjectValue(new SFCTaskObjectInterface() {
            @Override
            public Object taskvoid(Object valueo) throws Exception {
                return ((CompoundButton) valueo).isChecked();
            }
        }, cb);
    }

    /**
     * 設定CompoundButton控件的Checked
     * @param cb CompoundButton
     * @param cbv 設定值
     * @throws Exception
     */
    public void setChecked(CompoundButton cb,Boolean cbv) throws Exception {
        Object[] otv = new Object[]{cb, cbv};
        SetObjectValue(new SFCTaskVoidInterface() {
            @Override
            public void taskvoid(Object valueo) throws Exception {
                Object[] otv1 = (Object[]) valueo;
                ((CompoundButton) otv1[0]).setChecked((Boolean) otv1[1]);
            }
        }, otv);
    }

    /**
     * 獲取Spinner類控件的Enable
     * @param tv Spinner
     * @return
     * @throws Exception
     */
    public Boolean getEnabled(Spinner tv) throws Exception {
        return GetObjectValue(new SFCTaskObjectInterface() {
            @Override
            public Object taskvoid(Object valueo) throws Exception {
                return ((Spinner) valueo).isEnabled();
            }
        }, tv);
    }

    /**
     * 獲取Spinner類控件的Enable
     * @param tv Spinner
     * @return
     * @throws Exception
     */
    public Boolean getclickable(Spinner tv) throws Exception {
        return GetObjectValue(new SFCTaskObjectInterface() {
            @Override
            public Object taskvoid(Object valueo) throws Exception {
                return ((Spinner) valueo).isClickable();
            }
        }, tv);
    }

    /**
     * 獲取RadioGroup類控件的Enable
     * @param tv RadioGroup
     * @return
     * @throws Exception
     */
    public Boolean getEnabled(RadioGroup tv) throws Exception {
        return GetObjectValue(new SFCTaskObjectInterface() {
            @Override
            public Object taskvoid(Object valueo) throws Exception {
                return ((RadioGroup) valueo).isEnabled();
            }
        }, tv);
    }

    /**
     * 獲取RadioGroup類控件的Enable
     * @param tv RadioButton
     * @return
     * @throws Exception
     */
    public Boolean getEnabled(RadioButton tv) throws Exception {
        return GetObjectValue(new SFCTaskObjectInterface() {
            @Override
            public Object taskvoid(Object valueo) throws Exception {
                return ((RadioButton) valueo).isEnabled();
            }
        }, tv);
    }
    /**
     * 增減ViewGroup控件中View
     * @param vg ViewGroup
     * @param v 等待增加的View
     * @throws Exception
     */
    public void addView(ViewGroup vg,View v) throws Exception {
        Object[] otv = new Object[]{vg, v};
        SetObjectValue(new SFCTaskVoidInterface() {
            @Override
            public void taskvoid(Object valueo) throws Exception {
                Object[] otv1 = (Object[]) valueo;
                ((ViewGroup) otv1[0]).addView((View) otv1[1]);
            }
        }, otv);
    }
    /**
     * Remove ViewGroup控件中View
     * @param vg ViewGroup
     * @param v 等待增加的View
     * @throws Exception
     */
    public void removeView(ViewGroup vg,View v)throws Exception {
        Object[] otv = new Object[]{vg, v};
        SetObjectValue(new SFCTaskVoidInterface() {
            @Override
            public void taskvoid(Object valueo) throws Exception {
                Object[] otv1 = (Object[]) valueo;
                ((ViewGroup) otv1[0]).removeView((View) otv1[1]);
            }
        }, otv);
    }
    /**
     * 設定控件的可用性
     * @param tv 控件Spinner
     * @param b Boolean
     * @throws Exception
     */
    public void setEnabled(Spinner tv,Boolean b) throws Exception {
        Object[] otv = new Object[]{tv, b};
        SetObjectValue(new SFCTaskVoidInterface() {
            @Override
            public void taskvoid(Object valueo) throws Exception {
                Object[] otv1 = (Object[]) valueo;
                ((Spinner) otv1[0]).setEnabled((Boolean) otv1[1]);
            }
        }, otv);
    }

    /**
     * 設定控件的可用性
     * @param tv 控件Spinner
     * @param b Boolean
     * @throws Exception
     */
    public void setclickable(Spinner tv,Boolean b) throws Exception {
        Object[] otv = new Object[]{tv, b};
        SetObjectValue(new SFCTaskVoidInterface() {
            @Override
            public void taskvoid(Object valueo) throws Exception {
                Object[] otv1 = (Object[]) valueo;
                ((Spinner) otv1[0]).setClickable((Boolean) otv1[1]);
            }
        }, otv);
    }
    /**
     * 設定控件的可用性
     * @param tv 控件RadioGroup
     * @param b Boolean
     * @throws Exception
     */
    public void setEnabled(RadioGroup tv,Boolean b) throws Exception {
        Object[] otv = new Object[]{tv, b};
        SetObjectValue(new SFCTaskVoidInterface() {
            @Override
            public void taskvoid(Object valueo) throws Exception {
                Object[] otv1 = (Object[]) valueo;
                ((RadioGroup) otv1[0]).setEnabled((Boolean) otv1[1]);
            }
        }, otv);
    }
    /**
     * 設定控件的可用性
     * @param tv 控件RadioButton
     * @param b Boolean
     * @throws Exception
     */
    public void setEnabled(RadioButton tv,Boolean b) throws Exception {
        Object[] otv = new Object[]{tv, b};
        SetObjectValue(new SFCTaskVoidInterface() {
            @Override
            public void taskvoid(Object valueo) throws Exception {
                Object[] otv1 = (Object[]) valueo;
                ((RadioButton) otv1[0]).setEnabled((Boolean) otv1[1]);
            }
        }, otv);
    }
    /**
     * 獲取TextView類控件的Text
     * @param tv 控件
     * @return Text CharSequence
     * @throws Exception
     */
    public CharSequence getText(TextView tv) throws Exception {
        return GetObjectValue(new SFCTaskObjectInterface() {
            @Override
            public CharSequence taskvoid(Object valueo) throws Exception {
                return ((TextView) valueo).getText();
            }
        }, tv);
    }

    /**
     * 設定TextView類控件的Text
     * @param tv 控件
     * @param v Text值
     * @throws Exception
     */
    public void setText(TextView tv,String v) throws Exception {
        Object[] otv = new Object[]{tv, v};
        SetObjectValue(new SFCTaskVoidInterface() {
            @Override
            public void taskvoid(Object valueo) throws Exception {
                Object[] otv1 = (Object[]) valueo;
                ((TextView) otv1[0]).setText(otv1[1].toString());
            }
        }, otv);
    }

    /**
     * 設定Spinner類控件的Adapter
     * @param s 控件
     * @param sa Adapter
     * @throws Exception
     */
    public void setAdapter(Spinner s,SpinnerAdapter sa) throws Exception {
        Object[] otv = new Object[]{s, sa};
        SetObjectValue(new SFCTaskVoidInterface() {
            @Override
            public void taskvoid(Object valueo) throws Exception {
                Object[] otv1 = (Object[]) valueo;
                ((Spinner) otv1[0]).setAdapter((SpinnerAdapter) otv1[1]);
            }
        }, otv);
    }

    /**
     * 設定AdapterView類控件獲得焦點
     * @param av 控件AdapterView
     * @param focusable Ture/False
     * @throws Exception
     */
    public void setFocusable(AdapterView av,boolean focusable)   throws Exception {
        Object[] oap = new Object[]{av, focusable};
        SetObjectValue(new SFCTaskVoidInterface() {
            @Override
            public void taskvoid(Object valueo) throws Exception {
                Object[] otv1 = (Object[]) valueo;
                ((AdapterView) otv1[0]).setFocusable((boolean) otv1[1]);
            }
        }, oap);
    }

    /**
     * 設定View類控件獲得焦點
     * @param v
     * @param focusable
     * @throws Exception
     */
    public void setFocusable(View v,Boolean focusable) throws Exception {
        Object[] oap = new Object[]{v, focusable};
        SetObjectValue(new SFCTaskVoidInterface() {
            @Override
            public void taskvoid(Object valueo) throws Exception {
                Object[] otv1 = (Object[]) valueo;
                ((View) otv1[0]).setFocusable((boolean) otv1[1]);
            }
        }, oap);
    }

    /**
     * 獲取AdapterView類控件特定Position的Item
     * @param av 控件AdapterView
     * @param position position
     * @return
     * @throws Exception
     */
    public Object getItemAtPosition(AdapterView av,int position)  throws Exception {
        Object[] oap = new Object[]{av, position};
        return GetObjectValue(new SFCTaskObjectInterface() {
            @Override
            public Object taskvoid(Object valueo) throws Exception {
                Object[] oap1 = (Object[]) valueo;
                return ((AdapterView) oap1[0]).getItemAtPosition(Integer.parseInt(oap1[1].toString()));
            }
        }, oap);
    }

    /**
     * 設定Spinner類控件的Selection
     * @param s 控件
     * @param i 位置值
     * @throws Exception
     */
    public void setSelection(Spinner s,int i)throws Exception {
        Object[] otv = new Object[]{s, i};
        SetObjectValue(new SFCTaskVoidInterface() {
            @Override
            public void taskvoid(Object valueo) throws Exception {
                Object[] otv1 = (Object[]) valueo;
                ((Spinner) otv1[0]).setSelection(Integer.parseInt(otv1[1].toString()));
            }
        }, otv);
    }

    /** * 設定控件的可用性 * @param tv 控件為View * @param b Boolean * @throws Exception */
    public void setEnabled(View tv,Boolean b) throws Exception

    {
        Object[] otv = new Object[]{tv, b};
        SetObjectValue(new SFCTaskVoidInterface()
        {
            @Override
            public void taskvoid(Object valueo) throws Exception
            {
                Object[] otv1 = (Object[]) valueo;
                ((TextView) otv1[0]).setEnabled((Boolean) otv1[1]);
            }
        }, otv);


    }


    /**
     * 清除ViewGroup的所有ChidlView
     * @param vg 控件
     * @throws Exception
     */
    public void removeAllViewsInLayout(ViewGroup vg) throws Exception {
        Object[] otv = new Object[]{vg, null};
        SetObjectValue(new SFCTaskVoidInterface() {
            @Override
            public void taskvoid(Object valueo) throws Exception {
                Object[] otv1 = (Object[]) valueo;
                ((ViewGroup) otv1[0]).removeAllViewsInLayout();
            }
        }, otv);
    }



    /**
     * 顯示提示消息信息
     * @param msg 消息文字
     * @throws Exception
     */
    public void ShowMessage(@NonNull String msg) throws  Exception {
        if(!_taskstatus)
            throw new Exception("Task thread not running,this method can't  exec");
        _handler.obtainMessage(6,msg).sendToTarget();
        synchronized (_lock) {

            _lock.wait();
        }
    }

    /**
     * 關閉Activity and close Thread,,Finish,
     * @throws Exception
     */
    public void Close() throws Exception {
        if (!_taskstatus)
            throw new Exception("Task thread not running,this method can't  exec");
        _handler.obtainMessage(9, null).sendToTarget();
        _t = null;
    }

    /**
     * 打開新的活動
     * @param sendc 新活動Context
     * @param o 傳入參數
     * @return 活動Context返回Bundle
     * @throws Exception
     */
    public Bundle CreatNewActivity(@NonNull Class sendc,@NonNull String... o) throws Exception {
        Object[] os = new Object[]
                {sendc, o};
        _handler.obtainMessage(7, os).sendToTarget();
        synchronized (_lock) {
            _lock.wait();
        }
        return (Bundle) _value;
    }

    private Handler _handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Boolean rb=false;
            try {
                switch (msg.what) {
                    //調用Diaglog類別 1:
                    case 1:
                        String[] ss1=(String[])msg.obj;
                        AlertDialog.Builder adb = new AlertDialog.Builder(ActivityInteractive.this);
                        adb.setTitle(ss1[0]);
                        adb.setMessage(ss1[1]);
                        adb.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                _handler.obtainMessage(999, 1).sendToTarget();
                            }
                        });
                        adb.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                _handler.obtainMessage(999, 0).sendToTarget();
                            }
                        });
                        adb.show();
                        break;
                    case 2:
                        break;
                    //設定控件值類別 4:
                    case 4:
                        Object[] os = (Object[]) msg.obj;
                        ((SFCTaskVoidInterface) os[0]).taskvoid(os[1]);
                        try {
                            synchronized (_lock) {
                                _lock.notify();
                            }
                        } catch (Exception ex) {
                            throw ex;
                        }
                        break;
                    //獲取控件值類型 5:
                    case 5:
                        Object[] os5 = (Object[]) msg.obj;
                        _value = ((SFCTaskObjectInterface) os5[0]).taskvoid(os5[1]);
                        try {
                            synchronized (_lock) {
                                _lock.notify();
                            }
                        } catch (Exception ex) {
                            throw ex;
                        }
                        break;
                    //消息提示類型 6:
                    case 6:
                        BaseFuncation.showmessage(ActivityInteractive.this, msg.obj.toString());
                        try {
                            synchronized (_lock) {
                                _lock.notify();
                            }
                        } catch (Exception ex) {
                            throw ex;
                        }
                        break;
                    case 7:
                        Object[] os7 = (Object[]) msg.obj;

                        Intent i = new Intent(ActivityInteractive.this, (Class) os7[0]);
                        if (os7[1] != null) {
                            Bundle bundler = new Bundle();
                            bundler.putStringArray("sendvalue", (String[]) os7[1]);
                            i.putExtras(bundler);
                            ActivityInteractive.this
                                    .startActivityForResult(i, 1001, bundler);
                        } else
                            ActivityInteractive.this
                                    .startActivityForResult(i, 1001);
                        break;
                    //調用Diaglog類別 1:DropDownlist
                    case 8:
                        Object[] os8=(Object[])msg.obj;
                        String[] items8=(String[])os8[2];
                        AlertDialog.Builder adb8 = new AlertDialog.Builder(ActivityInteractive.this);
                        adb8.setTitle(os8[0].toString());
                        //adb8.setMessage(os8[1].toString());
                        adb8.setItems(items8, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                _handler.obtainMessage(999, which).sendToTarget();
                            }
                        });
                        adb8.create();
                        adb8.show();
                        break;
                    case 9:
                        ActivityInteractive.this.finish();
                        break;
                    //內部通訊類別: 999,
                    // 獲取返回結果，并通知調用方法重新運行
                    case 999:
                        _value = msg.obj;
                        try {
                            synchronized (_lock) {
                                _lock.notify();
                            }
                        } catch (Exception ex) {
                            throw ex;
                        }
                        break;
                    //異常回歸類別: 1000
                    case 1000:
                        throw new Exception(msg.obj.toString());
                        //用戶取消類別: 1002
                    case 1002:
                        BaseFuncation.showmessage(ActivityInteractive.this, msg.obj.toString());
                        _t=null;
                        execloadactivity.canclediglog();
                        break;
                    case 10:
                        final int selectedindex=0;
                        Object[] os10=(Object[])msg.obj;
                        final String[] items10=(String[])os10[2];
                        AlertDialog.Builder adb10 = new AlertDialog.Builder(ActivityInteractive.this);
                        adb10.setTitle(os10[0].toString());
                        adb10.setSingleChoiceItems(items10, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //selectedindex = which;
                            }
                        });
                        adb10.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Toast.makeText(ActivityInteractive.this, items10[selectedindex], Toast.LENGTH_SHORT).show();
                                _handler.obtainMessage(999, which).sendToTarget();
                            }
                        });

                        adb10.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Toast.makeText(ActivityInteractive.this,items10[selectedindex],Toast.LENGTH_SHORT).show();
                            }
                        });
                        adb10.create();
                        adb10.show();
                        break;
                }
                rb=true;
            } catch (Exception ex) {
                if (ActivityInteractive.this._activitystatus) {
                    BaseFuncation.showmessage(ActivityInteractive.this, _taskname + "-Error:" + ex.getMessage());
                    _t = null;
                    execloadactivity.canclediglog();
                }
            }
            return rb;
        }
    });

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == 1001) {
                    Bundle bundle = data.getExtras();
                    _handler.obtainMessage(999, bundle).sendToTarget();
                }
            } else if(resultCode==RESULT_CANCELED) {

                if (requestCode == 1001) {
                    if (data == null)
                        _handler.obtainMessage(1002, "UnknowActivity User Cancel").sendToTarget();
                    else
                        _handler.obtainMessage(1002, data.getExtras().getString("classname") + " User Cancel").sendToTarget();
                } else {
                    if (data == null)
                        _handler.obtainMessage(1000, "UnknowActivity-Error:None Result Value").sendToTarget();
                    else
                        _handler.obtainMessage(1000, data.getExtras().getString("classname") + "-Error:None Result Value").sendToTarget();
                }
            }
        }
        catch (Exception ex)
        {
            _handler.obtainMessage(1000, "UnknowActivity-Error:"+ex.getMessage()).sendToTarget();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            CloseActivity(null);
        }
        return false;
    }

    /**
     * 關閉Activity并設定返回值=finish
     * @param b Bundle返回值
     */
    private void CloseActivity(Bundle b) {
        Intent i = new Intent();
        if (b != null) {
            b.putString("classname", this.getClass().getName());
        } else {
            b = new Bundle();
            b.putString("classname", this.getClass().getName());
        }
        i.putExtras(b);
        this.setResult(RESULT_OK, i);
        _activitystatus=false;
        super.finish();
    }

    public void finish()
    {
        CloseActivity(null);
    }

    protected void finish(Bundle b)
    {
        CloseActivity(b);
    }



}
