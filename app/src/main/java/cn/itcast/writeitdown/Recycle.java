package cn.itcast.writeitdown;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.itcast.writeitdown.databinding.ActivityRecycleBinding;

public class Recycle extends AppCompatActivity {
    private ActivityRecycleBinding binding;
    private Dao dao;
    private TextView iteminvis,maintitle,time,maintetx;
    private ListView listView;
    private GridView gridView;
    private MyAdapter mAdapter;
    private View emptyView;
    private List<listbean> itemList;
    private DBHelper dbHelper;
    private Cursor cursor;
    private  String id;
    private SharedPreferences sp ;
    private SharedPreferences.Editor editor;
    private String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_recycle);
        binding = ActivityRecycleBinding.inflate(getLayoutInflater());
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
    private void setData() {
        itemList = new ArrayList<>();
        dao = new Dao(this);
        itemList = dao.searching("recycle");
    }

    private void intiView() {
//        iteminvis = findViewById(R.id.iteminvis);
//        maintitle = findViewById(R.id.maintitle);
//        maintetx = findViewById(R.id.maintext);
//        time = findViewById(R.id.time);
//        emptyView = findViewById(R.id.empty);
        mAdapter = new MyAdapter(this,itemList,type);
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

    private void setListView() {
//        listView = findViewById(R.id.rv_list);
//        listView.setEmptyView(emptyView);
//        listView.setAdapter(mAdapter);
        binding.listview.setAdapter(mAdapter);
        binding.listview.setEmptyView(binding.recycleempty);
        binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(Recycle.this, "清先长按回档", Toast.LENGTH_SHORT).show();
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

    private void setGrideView() {
//        gridView = findViewById(R.id.gr_list);
//        gridView.setEmptyView(emptyView);
//        gridView.setAdapter(mAdapter);
        binding.gridview.setAdapter(mAdapter);
        binding.gridview.setEmptyView(binding.recycleempty);
        binding.gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(Recycle.this, "清先长按回档", Toast.LENGTH_SHORT).show();
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

    private void alerdialog() {
//        Dao dao = new Dao(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("WARNING")
            .setMessage("彻底删除")
            .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dao.recycledelete(id);
                    refresh();
                    dialogInterface.dismiss();
                }
            })
            .setNegativeButton("回档", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Log.i(TAG, "onClick: "+dao.textreadtitle(id,"recycle"));
                    dao.textadd(id,dao.textreadtitle(id,"recycle"),dao.textreadcontent(id,"recycle"));
                    dao.recycledelete(id);
                    refresh();
                    dialogInterface.dismiss();
                }
            })
            .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            })
            // 设置点击Dialog外区域可关闭
            .setCancelable(true);
        AlertDialog alertDialog = builder.create();
        //显示对话框
        alertDialog.show();
    }

    private void refresh() {
        Intent back = new Intent();
        back.setClass(Recycle.this,Recycle.class);
        startActivity(back);

    }
    public void onBackPressed() {
        Intent back = new Intent();
        back.setClass(Recycle.this,MainActivity.class);
        startActivity(back);

    }




}