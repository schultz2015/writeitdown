package cn.itcast.writeitdown;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import cn.itcast.writeitdown.databinding.ActivityReadBinding;

public class read extends AppCompatActivity {
    private ActivityReadBinding binding;
//    private TextView readinvis,readtitle,readtext;
//    private ImageView imageView;
    private Dao dao;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        intiView();
        setFab();
        setData();
    }

    private void setFab() {
        binding.modifyfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(read.this,modify.class);//this前面为当前activty名称，class前面为要跳转到得activity名称
                intent.putExtra("itemid",id);
                startActivity(intent);

            }
        });
        binding.deletefab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerdialog();
            }
        });
    }

    private void intiView() {
        binding = ActivityReadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        dao = new Dao(this);
//
//        readinvis = findViewById(R.id.readinvis);
//        readtext  = findViewById(R.id.readText);
//        readtitle = findViewById(R.id.readTitle);
//        imageView = findViewById(R.id.imageView);
    }

    private void setData() {
        dao = new Dao(this);
        Intent intent = getIntent();
        id = intent.getStringExtra("itemid");
        binding.readinvis.setText(id);
        Log.i(TAG, "readid: "+binding.readinvis.getText().toString().trim());
        binding.readTitle.setText(dao.textreadtitle(binding.readinvis.getText().toString().trim(),"textinfo"));
        binding.readText.setText(dao.textreadcontent(binding.readinvis.getText().toString().trim(),"textinfo"));
        Glide.with(binding.imageView.getContext())
                .load(dao.imgread())
                .error(R.drawable.error_img)
                .into(binding.imageView);

    }


    private void alerdialog() {
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
                // 设置点击Dialog外区域可关闭
                .setCancelable(true);
        AlertDialog alertDialog = builder.create();
        //显示对话框
        alertDialog.show();
    }


    private void refresh() {
        Intent back = new Intent();
        back.setClass(read.this,MainActivity.class);
        startActivity(back);

    }


    @Override
    public void onBackPressed() {
        Intent back = getIntent();
        if(back.getBooleanExtra("search",false)){
            super.onBackPressed();

        }
        else {
            back = new Intent();
            back.setClass(read.this,MainActivity.class);
            startActivity(back);
        }
    }
}