package cn.itcast.writeitdown;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import cn.itcast.writeitdown.databinding.ActivityModifyBinding;


public class modify extends AppCompatActivity {
    private ActivityModifyBinding binding;
//    private TextView modinvis;
//    private EditText modtitle,modtext;
    private Dao dao;
    private String id,title,text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_modify);
        intiView();
        setFab();
        setData();
    }

    private void setFab() {
//        dao = new Dao(this);
        binding.confirmfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
                Log.i(TAG, "mod: "+title);
                dao.textmodify(id, title,text );
                Intent intent = new Intent();
                intent.setClass(modify.this,read.class);
                intent.putExtra("itemid",id);
                startActivity(intent);

            }
        });
    }

    private void getData() {
        title=binding.modTitle.getText().toString().trim();
        text=binding.modText.getText().toString().trim();
    }

    private void setData() {
        dao = new Dao(this);
        Intent intent = getIntent();
        id = intent.getStringExtra("itemid");
        binding.modinvis.setText(id);
        Log.i(TAG, "readid: "+binding.modinvis.getText().toString().trim());
        binding.modTitle.setText(dao.textreadtitle(binding.modinvis.getText().toString().trim(),"textinfo"));
        binding.modText.setText(dao.textreadcontent(binding.modinvis.getText().toString().trim(),"textinfo"));

    }

    private void intiView() {
        binding = ActivityModifyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        modinvis = findViewById(R.id.modinvis);=binding.modinvis
//        modtitle = findViewById(R.id.modTitle);
//        modtext = findViewById(R.id.modText);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}