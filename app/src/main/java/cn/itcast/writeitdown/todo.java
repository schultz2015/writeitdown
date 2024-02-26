package cn.itcast.writeitdown;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import cn.itcast.writeitdown.databinding.ActivityTodoBinding;
import cn.itcast.writeitdown.databinding.ItemContentTodoBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;



public class todo extends AppCompatActivity {
    private ActivityTodoBinding binding;
    private ItemContentTodoBinding itembinding;
    private List<todobean> todobeanList;
    private TodoAdapter myAdapter;
    private Dao dao;
    private String id,sign,t;

//    private FloatingActionButton dofab;
//    private ListView listView;
//    private View emptyView;//空布局
//    private TextView todotime;
//    private CheckBox todocheckBox;
    private TextView todoinvis;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_todo);
        setData();
        intiView();
        setFab();
    }

    private void setFab() {
        binding.dofab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                todoalert("null");
                Log.i(TAG, "时间: "+ t);
            }
        });
    }

    private void todoalert(String text) {
//        Dao dao = new Dao(this);
        boolean add;
        final EditText inputServer = new EditText(todo.this);
        if(text.equals("null")){
            inputServer.setHint("输入");
            add=true;
        }
        else {
            inputServer.setText(text);
            add=false;
        }
        inputServer.setWidth(200);
        inputServer.setPadding(100,0,100,30);
        inputServer.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});//设置最多只能输入50个字符

        AlertDialog.Builder builder = new AlertDialog.Builder(todo.this);//构建对话框
        builder.setMessage(" ")
                .setTitle("待添加事项.....")
                .setView(inputServer)
                .setNegativeButton("取消", null)//取消
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//确认键监听
                    public void onClick(DialogInterface dialog, int which) {
                        sign = inputServer.getText().toString();
                        if (sign != null && !sign.isEmpty()) {
                            calendar(add);
                        } else {
                            Toast.makeText(todo.this, "签名为空", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                })
//                .setOnDismissListener(new DialogInterface.OnDismissListener() {
//                    @Override
//                    public void onDismiss(DialogInterface dialogInterface) {
//                        Toast.makeText(todo.this, "弹窗关闭", Toast.LENGTH_SHORT).show();
//                        dialogInterface.dismiss();
//                    }
//                })
                // 设置点击Dialog外区域可关闭
                .setCancelable(true);
        builder.show();
    }

    private void calendar(boolean add) {
//        Dao dao = new Dao(this);
        Calendar mcalendar = Calendar.getInstance();     //  获取当前时间    —   年、月、日
        int y = mcalendar.get(Calendar.YEAR);         //  得到当前年
        int m = mcalendar.get(Calendar.MONTH);       //  得到当前月
        final int d = mcalendar.get(Calendar.DAY_OF_MONTH);  //  得到当前日
        new DatePickerDialog(todo.this, new DatePickerDialog.OnDateSetListener() {      //  日期选择对话框
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                t = Integer.toString(year)+'-'+Integer.toString(month+1)+'-'+Integer.toString(dayOfMonth);
                Log.i(TAG, "返回的日期是: "+t);
                if(add){
                    Log.i(TAG, "onDateSet: "+sign);
//                    id = dao.maxid("todolist");
//                    dao.todoadd(id,sign,t);
                    timepicker(add);
                }
                else{
                    Log.i(TAG, "onDateSet: "+sign);
                    timepicker(add);
//                    dao.todomodifytext(id,sign,t);
                }

            }
        },y,m,d).show();
    }

    private void timepicker(boolean add) {
        Calendar calendar = Calendar.getInstance();
        int hourOfDay  = calendar.get(Calendar.HOUR_OF_DAY);    //得到小时
        int minute  = calendar.get(Calendar.MINUTE);            //得到分钟
        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                t += '-'+Integer.toString(hourOfDay)+'-'+Integer.toString(minute);
                //  这个方法是得到选择后的 小时、分钟，分别对应着三个参数 —   hourOfDay、minute
                if(add){
                    Log.i(TAG, "onDateSet: "+sign);
                    id = dao.maxid("todolist");
                    dao.todoadd(id,sign,t);
                }
                else{
                    Log.i(TAG, "onDateSet: "+sign);
                    dao.todomodifytext(id,sign,t);
                }
                refresh();
            }
        }, hourOfDay, minute, true).show();
    }

    private void setData() {
        todobeanList = new ArrayList<>();
        dao = new Dao(this);
        todobeanList = dao.todo();
        Collections.sort(todobeanList, new Comparator<todobean>() {
            @Override
            public int compare(todobean t1, todobean t2) {
                return DateString.picker(t2.getTime())-DateString.picker(t1.getTime());
            }
        });
//        for (todobean b:todobeanList) {}
    }

    private void intiView() {

//        todoinvis = findViewById(R.id.todoinvis);   =binding(todoinvis)
//        todotime = findViewById(R.id.todotime);
//        dofab=findViewById(R.id.dofab);
//        emptyView = findViewById(R.id.todoempty);
//        todocheckBox = findViewById(R.id.todocheckBox);


        myAdapter = new TodoAdapter(this, todobeanList);
        binding = ActivityTodoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListView();


    }

    private void setListView() {
//        listView = findViewById(R.id.todolist);
//        listView = binding.todolist;
        binding.todolist.setEmptyView(binding.todoempty);
        binding.todolist.setAdapter(myAdapter);
        binding.todolist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView todotext = view.findViewById(R.id.todotext);
                todoinvis = view.findViewById(R.id.todoinvis);
                id = todoinvis.getText().toString().trim();
                todoalert(todotext.getText().toString());
//                Toast.makeText(todo.this, "开关", Toast.LENGTH_SHORT).show();
            }
        });
        binding.todolist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                todoinvis = view.findViewById(R.id.todoinvis);
                id = todoinvis.getText().toString().trim();
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
                        dao.tododelete(id);
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
                .setCancelable(true);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void refresh() {
        Intent back = new Intent();
        back.setClass(todo.this, todo.class);
        startActivity(back);

    }

    public void onBackPressed() {
        Intent back = new Intent();
        back.setClass(todo.this, MainActivity.class);
        startActivity(back);

    }

}