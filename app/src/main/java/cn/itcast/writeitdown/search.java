package cn.itcast.writeitdown;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import cn.itcast.writeitdown.databinding.ActivitySearchBinding;

public class search extends AppCompatActivity {
    private ActivitySearchBinding binding;
    private Dao dao;
    private SearchView searchview;
    private  MyAdapter myAdapter;
    private Context context;
    private ListView listView;
    private GridView gridView;
    private View emptyView;//空布局
    private List<listbean> itemList;

    private DBHelper dbHelper;
    private Cursor cursor;
    private TextView iteminvis,maintitle,time,maintetx;
    private String id;
    private SharedPreferences sp ;
    private SharedPreferences.Editor editor;
    private String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_search);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        checktype();
        setData();
        intiView();
    }

    private void checktype() {
        sp = getSharedPreferences("data",MODE_PRIVATE);
        editor = sp.edit();
        if(sp.getString("type","none").equals("none")){
            editor.putString("type","listType");
            editor.commit();
        }
        if(sp.getString("type","none").equals("listType")){
            type="listType";
        }
        if(sp.getString("type","none").equals("grideType")){
            type="grideType";
        }
    }

    private void intiView() {
//        iteminvis = findViewById(R.id.iteminvis);
//        maintitle = findViewById(R.id.maintitle);
//        maintetx = findViewById(R.id.maintext);
//        time = findViewById(R.id.time);
//        emptyView = findViewById(R.id.searchempty);
        myAdapter =  new MyAdapter(this,itemList,type);
        setListView();
        setGrideView();
        if(type.equals("listType")){
            binding.listview.setVisibility(View.VISIBLE);
            binding.gridview.setVisibility(View.GONE);
        }
        else if(type.equals("grideType")){
            binding.listview.setVisibility(View.GONE);
            binding.gridview.setVisibility(View.VISIBLE);
        }
    }

    private void setGrideView() {
//        gridView = findViewById(R.id.gr_list);
//        gridView.setAdapter(myAdapter);
//        gridView.setEmptyView(emptyView);
//        gridView.setTextFilterEnabled(true);
        binding.gridview.setEmptyView(binding.searchempty);
        binding.gridview.setAdapter(myAdapter);
        binding.gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                iteminvis=view.findViewById(R.id.iteminvis);
//                Toast.makeText(search.this, "点击的子项ID = " + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(search.this, read.class);
                intent.putExtra("itemid", iteminvis.getText().toString().trim());
                intent.putExtra("search",true);
                startActivity(intent);

            }
        });
        binding.gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                iteminvis=view.findViewById(R.id.iteminvis);
                id =iteminvis.getText().toString().trim();
                alerdialog();
                return true;
            }
        });
    }

    private void setListView() {
//        listView = findViewById(R.id.rv_list);
//        listView.setEmptyView(emptyView);
//        listView.setAdapter(myAdapter);
//        listView.setTextFilterEnabled(true);
        binding.listview.setEmptyView(binding.searchempty);
        binding.listview.setAdapter(myAdapter);
        binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                iteminvis=view.findViewById(R.id.iteminvis);
                Toast.makeText(search.this, "点击的子项ID = " + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(search.this, read.class);
                intent.putExtra("itemid", iteminvis.getText().toString().trim());
                intent.putExtra("search",true);
                startActivity(intent);

            }
        });
        binding.listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                iteminvis=view.findViewById(R.id.iteminvis);
                id =iteminvis.getText().toString().trim();
                alerdialog();
                return true;
            }
        });
    }

    public void setData(){
        dao = new Dao(this);
        itemList = new ArrayList<>();
//        searchview = findViewById(R.id.search);
        binding.searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                itemList.clear();
                String title,text,searchid;
                List<listbean> List;
                List<String> idlist = new ArrayList<>();
                List=dao.searching("textinfo");
                for(int position=0;position<List.size();position++){
                    listbean listbean = List.get(position);
                    searchid= listbean.getId().trim();
                    text = listbean.getText().trim();
                    title = listbean.getTitle().trim();
                    if((check(s))&&
                            (text.toLowerCase().contains(s.toLowerCase())||
                                    title.toLowerCase().contains(s.toLowerCase())))
                    {
                            itemList.add(listbean);
                            idlist.add(searchid);
                    }
                }
                intiView();
                Log.i(TAG, "onQuery: "+itemList.size());
                return false;
            }
        });
    }
    public boolean check(String s) {
        if (!s.equals("")) {
            if (s.trim().length() > 0) {
                Log.i(TAG, "TRUE");
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
    private void alerdialog() {
//        Dao dao = new Dao(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("WARNING")
                .setMessage("删除至回收站")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dao.recycle(id,dao.textreadtitle(id,"textinfo"),dao.textreadcontent(id,"textinfo"),dao.textreadtime(id,"textinfo"));
                        dao.textdelete(id);
                        refresh();
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                // 窗口消失的回调
//                .setOnDismissListener(new DialogInterface.OnDismissListener() {
//                    @Override
//                    public void onDismiss(DialogInterface dialogInterface) {
//                        Toast.makeText(search.this, "dismiss", Toast.LENGTH_SHORT).show();
//                    }
//                })
                // 设置点击Dialog外区域可关闭
                .setCancelable(true);
        AlertDialog alertDialog = builder.create();
//        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
//            @Override
//            public void onShow(DialogInterface dialog) {
//                Log.e(TAG,"对话框显示了");
//            }
//        });
//        //对话框消失的监听事件
//        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                Log.e(TAG,"对话框消失了");
//            }
//        });
        //显示对话框
        alertDialog.show();
    }

    private void refresh() {
        Intent back = new Intent();
        back.setClass(search.this,MainActivity.class);
        startActivity(back);

    }
    public void onBackPressed() {
        Intent back = new Intent();
        back.setClass(search.this,MainActivity.class);
        startActivity(back);

    }

}