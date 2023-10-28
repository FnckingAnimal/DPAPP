package app.dpapp;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tod on 2016/4/15.
 */
public class eqchecklist_bak extends AppCompatActivity {

    //统一定义登入用户名，其它为系统内机台代码，机台名称，及机种码
    protected String Sessionuser;
    protected String Eqid;
    protected String Eqname;
    protected String Deviceno;

    //定义数据源
    protected List<Slist> Sourcelist;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "eqchecklist Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://app.cmapp/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "eqchecklist Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://app.cmapp/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    class Slist {
        private String Sessionuser;
        private String Eqid;
        private String Eqname;
        private String Sysdocumentid;
        private String Idept;
        private String Itemid;
        private String Itemname;
        private String Value;
        private String Valuespec;
        private String Valueshowpoint;

        public String getValueshowpoint() {
            return Valueshowpoint;
        }

        public void setValueshowpoint(String str1) {
            this.Valueshowpoint = str1;
        }

        public String getValuespec() {
            return Valuespec;
        }

        public void setValuespec(String str1) {
            this.Valuespec = str1;
        }

        public String getValue() {
            return Value;
        }

        public void setValue(String str1) {
            this.Value = str1;
        }

        public String getItemname() {
            return Itemname;
        }

        public void setItemname(String str1) {
            this.Itemname = str1;
        }

        public String getItemid() {
            return Itemid;
        }

        public void setItemid(String str1) {
            this.Itemid = str1;
        }

        public String getIdept() {
            return Idept;
        }

        public void setIdept(String str1) {
            this.Idept = str1;
        }

        public String getSysdocumentide() {
            return Sysdocumentid;
        }

        public void setSysdocumentid(String str1) {
            this.Sysdocumentid = str1;
        }

        public String getEqname() {
            return Eqname;
        }

        public void setEqname(String str1) {
            this.Eqname = str1;
        }

        public String getSessionuser() {
            return Sessionuser;
        }

        public void setSessionuser(String str1) {
            this.Sessionuser = str1;
        }

        public String getEqid() {
            return Eqid;
        }

        public void setEqid(String str1) {
            this.Eqid = str1;
        }

    }

    protected List<Rlist> ReturnList;

    class Rlist {
        public String Sessionuser;
        public String Eqid;
        public String Sysdocumentid;
        public String Iteaid;
        public String Value;
    }

    /*
    public eqchecklist(String Suserid,String Syseqid,String Syseqname,String Sdeviceno)
    {
        Sessionuser=Suserid;
        Eqid=Syseqid;
        Eqname=Syseqname;
        Deviceno=Sdeviceno;
    }
*/

    Button bt1;
    ListView lv1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eq_checklist);

        Bundle bundle = this.getIntent().getExtras();
        Sessionuser = bundle.getString("Sessionuser");
        ;
        Eqid = bundle.getString("Eqid");
        ;
        Eqname = bundle.getString("Eqname");
        ;
        Deviceno = bundle.getString("Deviceno");
        ;

        bt1 = (Button) findViewById(R.id.eq_checklistbutton1);

        lv1 = (ListView) findViewById(R.id.eqchecklistlistview1);
        Sourcelist = new ArrayList<Slist>();

        for (int i = 0; i < 20; i++) {
            Slist s1 = new Slist();
            if (i < 3) {
                s1.Sessionuser = Sessionuser;
                s1.Eqid = Eqid;
                s1.Eqname = Eqname;
                s1.Sysdocumentid = "";
                s1.Idept = "MFG";
                s1.Itemid = "I001";
                s1.Itemname = "Test1";
                s1.Value = "";
                s1.Valuespec = "1,1,1;";
                s1.Valueshowpoint = "1";
                Sourcelist.add(s1);
            } else {
                s1.Sessionuser = Sessionuser;
                s1.Eqid = Eqid;
                s1.Eqname = Eqname;
                s1.Sysdocumentid = "";
                s1.Idept = "QRA";
                s1.Itemid = "I002";
                s1.Itemname = "Test1";
                s1.Value = "";
                s1.Valuespec = "0,0.1,0.8;";
                s1.Valueshowpoint = "0";
                Sourcelist.add(s1);
            }
        }

        lv1.setAdapter(new myAdapter1(this, Sourcelist));

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void eqchecklistbt1onclick(View v) {

        if (lv1 != null) {
            String temp1 = null;
            String temp2 = null;
            String[] strArray = null;
            for (int i = 0; i < lv1.getChildCount(); i++) {
                strArray = null;
                temp1 = ((TextView) lv1.getChildAt(i).findViewById(R.id.Mspec1)).getText().toString();
                temp2 = ((TextView) lv1.getChildAt(i).findViewById(R.id.Mvalue1)).getText().toString();
                temp2 = temp2.toUpperCase();
                strArray = temp1.split(",");

                switch (strArray[0]) {
                    case "0":
                        if (Float.parseFloat(strArray[1]) > Float.parseFloat(temp2) || Float.parseFloat(strArray[2]) < Float.parseFloat(temp2)) {
                            ((TextView) lv1.getChildAt(i).findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                        }
                        break;
                    case "1":
                        if (temp2 != "Y" && temp2 != "N") {
                            ((TextView) lv1.getChildAt(i).findViewById(R.id.Mvalue1)).setTextColor(Color.RED);
                        }
                        break;
                }
            }
        }
    }

    class myAdapter1 extends BaseAdapter {

        private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
        private List<Slist> Las1;

        /*构造函数*/
        public myAdapter1(Context context, List<Slist> As1) {
            this.mInflater = LayoutInflater.from(context);
            Las1 = As1;
        }

        @Override
        public Object getItem(int position) {
            return Las1.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getCount() {
            return Sourcelist.size();
        }

        private int index=-1;
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            //观察convertView随ListView滚动情况
            //Log.v("MyListViewBase", "getView " + position + " " + convertView);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.listview_source2,
                        null);
                holder = new ViewHolder();
                    /*得到各个控件的对象*/
                holder.depttext = (TextView) convertView.findViewById(R.id.Mdept1);
                holder.itemtext = (TextView) convertView.findViewById(R.id.Mitem1);
                holder.sepctext = (TextView) convertView.findViewById(R.id.Mspec1);
                holder.valuetext = (EditText) convertView.findViewById(R.id.Mvalue1);
                convertView.setTag(holder);//绑定ViewHolder对象

            } else {
                holder = (ViewHolder) convertView.getTag();//取出ViewHolder对象
            }
            /*设置TextView显示的内容，即我们存放在动态数组中的数据*/
            //holder.title.setText(getDate().get(position).get("ItemTitle").toString());
            //holder.text.setText(getDate().get(position).get("ItemText").toString());

            holder.depttext.setText(Sourcelist.get(position).Idept.toString());
            holder.itemtext.setText(Sourcelist.get(position).Itemname.toString());
            holder.sepctext.setText(Sourcelist.get(position).Valuespec.toString());
            holder.valuetext.setText(Sourcelist.get(position).Value.toString());

            holder.valuetext.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View view, MotionEvent event) {
                    // 在TOUCH的UP事件中，要保存当前的行下标，因为弹出软键盘后，整个画面会被重画
                    // 在getView方法的最后，要根据index和当前的行下标手动为EditText设置焦点
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        index = position;
                    }
                    return false;
                }
            });

            holder.valuetext.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable editable) {
                }
                public void beforeTextChanged(CharSequence text, int start, int count, int after) {
                }
                public void onTextChanged(CharSequence text, int start, int before, int count) {

                    Slist s1 = new Slist();
                    s1.Sessionuser = Sourcelist.get(position).Sessionuser.toString();
                    s1.Eqid =Sourcelist.get(position).Eqid.toString();
                    s1.Eqname =Sourcelist.get(position).Eqname.toString();
                    s1.Sysdocumentid = Sourcelist.get(position).Sysdocumentid.toString();
                    s1.Idept =Sourcelist.get(position).Idept.toString();
                    s1.Itemid = Sourcelist.get(position).Itemid.toString();
                    s1.Itemname = Sourcelist.get(position).Itemname.toString();
                    s1.Value =text.toString();
                    s1.Valuespec =Sourcelist.get(position).Valuespec.toString();
                    s1.Valueshowpoint =Sourcelist.get(position).Valueshowpoint.toString();

                    Sourcelist.set(position,s1);
                    // 在这个地方添加你的保存文本内容的代码，如果不保存，你就等着重新输入吧
                    // 而且不管你输入多少次，也不会有用的，因为getView全清了～～
                }
            });
            // 这个地方可以添加将保存的文本内容设置到EditText上的代码，会有用的～～
            holder.valuetext.clearFocus();

            if (index != -1 && index == position) {
                // 如果当前的行下标和点击事件中保存的index一致，手动为EditText设置焦点。
                holder.valuetext.requestFocus();
            }
            return convertView;
        }

    /*
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.listview_source2,
                        null);
                holder = new ViewHolder();

                holder.depttext = (TextView) convertView.findViewById(R.id.Mdept1);
                holder.itemtext = (TextView) convertView.findViewById(R.id.Mitem1);
                holder.sepctext = (TextView) convertView.findViewById(R.id.Mspec1);
                holder.valuetext = (EditText) convertView.findViewById(R.id.Mvalue1);
                convertView.setTag(holder);//绑定ViewHolder对象

            } else {
                holder = (ViewHolder) convertView.getTag();//取出ViewHolder对象
            }

            holder.depttext.setText(Sourcelist.get(position).Idept.toString());
            holder.itemtext.setText(Sourcelist.get(position).Itemname.toString());
            holder.sepctext.setText(Sourcelist.get(position).Valuespec.toString());
            holder.valuetext.setText(Sourcelist.get(position).Value.toString());

            holder.valuetext.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View view, MotionEvent event) {
                    // 在TOUCH的UP事件中，要保存当前的行下标，因为弹出软键盘后，整个画面会被重画
                    // 在getView方法的最后，要根据index和当前的行下标手动为EditText设置焦点
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        index = position;
                    }
                    return false;
                }
            });

            holder.valuetext.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable editable) {
                }
                public void beforeTextChanged(CharSequence text, int start, int count, int after) {
                }
                public void onTextChanged(CharSequence text, int start, int before, int count) {

                    Slist s1 = new Slist();
                    s1.Sessionuser = Sourcelist.get(position).Sessionuser.toString();
                    s1.Eqid =Sourcelist.get(position).Eqid.toString();
                    s1.Eqname =Sourcelist.get(position).Eqname.toString();
                    s1.Sysdocumentid = Sourcelist.get(position).Sysdocumentid.toString();
                    s1.Idept =Sourcelist.get(position).Idept.toString();
                    s1.Itemid = Sourcelist.get(position).Itemid.toString();
                    s1.Itemname = Sourcelist.get(position).Itemname.toString();
                    s1.Value =text.toString();
                    s1.Valuespec =Sourcelist.get(position).Valuespec.toString();
                    s1.Valueshowpoint =Sourcelist.get(position).Valueshowpoint.toString();

                    Sourcelist.set(position,s1);
                    // 在这个地方添加你的保存文本内容的代码，如果不保存，你就等着重新输入吧
                    // 而且不管你输入多少次，也不会有用的，因为getView全清了～～
                }
            });
            // 这个地方可以添加将保存的文本内容设置到EditText上的代码，会有用的～～
            holder.valuetext.clearFocus();

            if (index != -1 && index == position) {
                // 如果当前的行下标和点击事件中保存的index一致，手动为EditText设置焦点。
                holder.valuetext.requestFocus();
            }
            return convertView;
        }

        */
        public final class ViewHolder {
            public TextView depttext;
            public TextView itemtext;
            public TextView sepctext;
            public EditText valuetext;
        }
    }
}