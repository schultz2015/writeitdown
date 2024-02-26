package cn.itcast.writeitdown;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.itcast.writeitdown.databinding.ActivityWriteBinding;

public class write extends AppCompatActivity implements View.OnClickListener {

    private TextView writeinvis;
    private EditText writeTitle,writeText;
    private ActivityWriteBinding binding;

    private Dao dao;
    private String itemid,text,title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        dao = new Dao(this);
        binding = ActivityWriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.savefab.setOnClickListener(this);
    }
    @SuppressLint("SetTextI18n")
    public void intiView(){
        writeinvis=findViewById(R.id.writeinvis);
        writeText = findViewById(R.id.writeText);
        writeTitle = findViewById(R.id.writeTitle);
        writeinvis.setText(dao.maxid("textinfo"));
        Log.i(TAG, "intiView: "+writeinvis.getText().toString().trim());
    }
    private void getData(){
        text=writeText.getText().toString();
        itemid = writeinvis.getText().toString().trim();
        Log.i(TAG, "getData: "+writeinvis.getText().toString().trim());
        title=writeTitle.getText().toString().trim();
    }
    private boolean DataValuable() {
        Log.i(TAG, "text: "+text+"  title:"+title);
        if(TextUtils.isEmpty(title)){
            if(TextUtils.isEmpty(text)){
                Toast.makeText(write.this,"输入为空",Toast.LENGTH_SHORT).show();
                return false;
            }
            else{
                title = "";
            }
        }
        else{
            if(TextUtils.isEmpty(text)){
                text = "";
            }
        }
        return true;
    }


    public void onBackPressed() {
        Intent back = new Intent();
        back.setClass(write.this,MainActivity.class);
        startActivity(back);

    }

    @Override
    public void onClick(View view) {
        intiView();
        getData();
        if(DataValuable()){
            dao.textadd(itemid,title,text);
            Intent intent = new Intent();
            intent.setClass(write.this, MainActivity.class);
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
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}