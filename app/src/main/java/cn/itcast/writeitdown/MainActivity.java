package cn.itcast.writeitdown;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.IBinder;
import android.util.Log;

import android.view.View;

import androidx.appcompat.app.AppCompatDelegate;


import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import java.util.List;

import cn.itcast.writeitdown.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    //    private MyService.MyBinder myBinder;
//    private MyConn myConn;
    private TextView iteminvis;
//    private TextView maintitle,time,maintetx
//    private ListView listView;
//    private GridView gridView;
    private MyAdapter mAdapter ;
//    private View emptyView;//空布局
    private List<listbean> itemList;
//    private DBHelper dbHelper;
//    private Cursor cursor;
    private SharedPreferences sp ;
    private SharedPreferences.Editor editor;
    private String type,id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setBackgroundDrawable(getDrawable(R.color.purple_700));
        checktype();
        setData();
        intiView();
        setService();
        setFab();
    }

    private void setService() {
        Intent intent=new Intent(this, MyService.class);
        startService(intent);
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

    private void setFab() {
        binding.writefab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, write.class);
                startActivity(intent);

            }
        });
        binding.searchfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, search.class);
                startActivity(intent);

            }
        });

        binding.tofab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, todo.class);
                startActivity(intent);

            }
        });
    }

    @SuppressLint("Range")
    public void setData(){
        itemList = new ArrayList<>();
        Dao dao = new Dao(this);
        itemList = dao.searching("textinfo");
    }
    public void intiView(){
//        iteminvis = findViewById(R.id.iteminvis);
//        maintitle = findViewById(R.id.maintitle);
//        maintetx = findViewById(R.id.maintext);
//        time = findViewById(R.id.time);
//        emptyView = findViewById(R.id.empty);

        mAdapter =  new MyAdapter(this,itemList,type);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListView();
        setGrideView();
        Log.i(TAG, "TYPE"+type);
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
//        gridView = findViewById(R.id.gr_list); = binding.grlist
//        gridView.setAdapter(mAdapter);
//        gridView.setEmptyView(emptyView);
        binding.gridview.setAdapter(mAdapter);
        binding.gridview.setEmptyView(binding.mainempty);
        binding.gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView iteminvis;
                iteminvis=view.findViewById(R.id.iteminvis);
//                Toast.makeText(MainActivity.this, "点击的子项ID = " + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, read.class);
                intent.putExtra("itemid", iteminvis.getText().toString().trim());
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
//        listView = findViewById(R.id.rv_list); =binding.rvlist
//        listView.setAdapter(mAdapter);
//        listView.setEmptyView(emptyView);
        binding.listview.setAdapter(mAdapter);
        binding.listview.setEmptyView(binding.mainempty);
        binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                iteminvis=view.findViewById(R.id.iteminvis);
                Toast.makeText(MainActivity.this, "点击的子项ID = " + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, read.class);//this前面为当前activty名称，class前面为要跳转到得activity名称
                intent.putExtra("itemid", iteminvis.getText().toString().trim());
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

    private void alerdialog() {
        Dao dao = new Dao(this);
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
            .setCancelable(true);// 设置点击Dialog外区域可关闭
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void refresh() {
        Intent back = new Intent();
        back.setClass(MainActivity.this,MainActivity.class);
        startActivity(back);

    }

//    private class MyConn implements ServiceConnection{
//
//        @Override
//        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
//            myBinder = (MyService.MyBinder) iBinder;
//            Log.i(TAG, "onServiceConnected: 绑定成功"+myBinder.toString());
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName componentName) {
//
//        }
//    }



    //重写onBackPressed()方法
    private long currentBackPressedTime= 0;//退出时间
    private static final int BACK_PRESSED_INTERVAL= 2000;//退出间隔
    //重写onBackPressed()方法,继承自退出的方法
    @Override
    public void onBackPressed() {
        //判断时间间隔
        if(System.currentTimeMillis()-currentBackPressedTime>BACK_PRESSED_INTERVAL) {
            currentBackPressedTime= System.currentTimeMillis();
            Toast.makeText(this,"再按一次返回键退出程序", Toast.LENGTH_SHORT).show();
        }else{
            //退出
            Intent intent = new Intent(Intent.ACTION_MAIN);

            intent.addCategory(Intent.CATEGORY_HOME);

            startActivity(intent);
        }
    }


    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences sp = getSharedPreferences("data",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        int id = item.getItemId();
        Log.i(TAG, "onOptionsItemSelected: "+AppCompatDelegate.getDefaultNightMode());
        if (id == R.id.theme_settings) {
            if(AppCompatDelegate.getDefaultNightMode()!=AppCompatDelegate.MODE_NIGHT_YES){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }//一开始程序既不是夜间也不是日间，先统一设为这个
            if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            return true;
        }
        if (id == R.id.action_settings) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this,Recycle.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.layout_settings){
            if (type.equals("grideType")){
                editor.clear();
                editor.putString("type","listType");
                editor.commit();
                Intent change = new Intent();
                change.setClass(MainActivity.this,MainActivity.class);
                startActivity(change);


            }
            else if(type.equals("listType")) {
                editor.clear();
                editor.putString("type","grideType");
                editor.commit();
                Intent change = new Intent();
                change.setClass(MainActivity.this,MainActivity.class);
                startActivity(change);
            }
        }
        return super.onOptionsItemSelected(item);
    }

}
