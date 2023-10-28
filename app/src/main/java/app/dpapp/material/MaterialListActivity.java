package app.dpapp.material;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.dpapp.R;
import app.dpapp.appcdl.MyThreadPool;
import app.dpapp.bean.MaterialLotnoBean;
import app.dpapp.bean.MaterialTypeBean;
import app.dpapp.soap.AppleSOAP;
import app.dpapp.soap.SOAPParameter;

public class MaterialListActivity extends AppCompatActivity {

    private Spinner sp_materialtype;
    private String materialtype,materialtypeflag;
    private List<MaterialTypeBean> list_materialtype;
    private ListView lv_materialList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_list2);

        sp_materialtype= (Spinner) findViewById(R.id.materiallist_sp_materialtype);
        lv_materialList= (ListView) findViewById( R.id.lv_materiallist);

        loadmaterialtype();

        sp_materialtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                materialtype=sp_materialtype.getItemAtPosition(position).toString(); //获取选择的物料类型
                materialtypeflag=list_materialtype.get(position).typeflag;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void loadmaterialtype() {
        MyThreadPool.httppool.execute(new Runnable() {
            @Override
            public void run() {
                AppleSOAP asop=new AppleSOAP();
                List<MaterialTypeBean> list = asop.getMaterialType();
                Message msg = new Message();
                msg.what = 0;
                msg.obj = list;
                handle.sendMessage(msg);
            }
        });
    }

    private Handler handle=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    list_materialtype = (List<MaterialTypeBean>) msg.obj;
                    List<String> list1=new ArrayList<>();
                    if(list_materialtype!=null){
                        for(int i=0;i<list_materialtype.size();i++){
                            list1.add(list_materialtype.get(i).materialtype);
                        }
                        ArrayAdapter adapter=new ArrayAdapter(MaterialListActivity.this, R.layout.spinner_textview,list1);
                        sp_materialtype.setAdapter(adapter);
                    }
                    break;
                case 1:
                    List<MaterialLotnoBean> list_materialLotno = (List<MaterialLotnoBean>) msg.obj;
                    if(list_materialLotno!=null){
                        MaterialListAdapter adapter=new MaterialListAdapter(MaterialListActivity.this,list_materialLotno);
                        lv_materialList.setAdapter(adapter);
                    }else{
                        lv_materialList.setAdapter(null);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public void material_list_onclick(View view) {
      if(materialtypeflag!=null && materialtypeflag!=""){
          List<SOAPParameter> parameterList=new ArrayList<>();
          parameterList.add(new SOAPParameter("materialtypeflag",materialtypeflag));
          loadMaterialListData(parameterList);
      }
    }

    private void loadMaterialListData( final List<SOAPParameter> parameterList ) {
        MyThreadPool.httppool.execute(new Runnable() {
            @Override
            public void run() {
                AppleSOAP asop=new AppleSOAP();
                List<MaterialLotnoBean> list = asop.getMaterialListData(parameterList);
                Message msg = new Message();
                msg.what = 1;
                msg.obj = list;
                handle.sendMessage(msg);

            }
        });
    }

    class MaterialListAdapter extends BaseAdapter {

        private Context context;
        private List<MaterialLotnoBean> list;

        public MaterialListAdapter(Context context, List<MaterialLotnoBean> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView==null){
                convertView = LayoutInflater.from(context).inflate(R.layout.item_materiainactivity_materiallotno, null);
                viewHolder = new ViewHolder();
                viewHolder.tv_materiallotno = (TextView) convertView.findViewById(R.id.tv_materiallotno);
                viewHolder.tv_materialno = (TextView) convertView.findViewById(R.id.tv_materialno);
                viewHolder.tv_materialname = (TextView) convertView.findViewById(R.id.tv_materialname);
                viewHolder.tv_lotnostatus = (TextView) convertView.findViewById(R.id.tv_lotnostatus);
                viewHolder.tv_materialdeviceno = (TextView) convertView.findViewById(R.id.tv_materialdeviceno);
                viewHolder.tv_materialbh = (TextView) convertView.findViewById(R.id.tv_materialbh);
                viewHolder.tv_materianum = (TextView) convertView.findViewById(R.id.tv_materialnum);
                viewHolder.tv_typeflag = (TextView) convertView.findViewById(R.id.tv_typeflag);
                convertView.setTag(viewHolder);
            }else{
                viewHolder= (ViewHolder) convertView.getTag();
            }

            viewHolder.tv_materiallotno.setText(list.get(position).getLotno());
            viewHolder.tv_materialno.setText(list.get(position).getMaterialno());
            viewHolder.tv_materialname.setText(list.get(position).getMaterialname());
            viewHolder.tv_lotnostatus.setText(list.get(position).getLotnostatus());
            viewHolder.tv_materialdeviceno.setText(list.get(position).getDeviceno());
            viewHolder.tv_materialbh.setText(list.get(position).getBh());
            viewHolder.tv_materianum.setText(list.get(position).getQty());
            viewHolder.tv_typeflag.setText(list.get(position).getTypeflag());
            return convertView;
        }
    }

    class ViewHolder{
        TextView tv_materiallotno,tv_materialno,tv_materialname,tv_lotnostatus,tv_materialdeviceno,tv_materialbh,tv_materianum,tv_typeflag;
    }
}