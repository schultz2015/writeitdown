package cn.itcast.writeitdown;

import static android.content.ContentValues.TAG;
import static com.google.android.material.resources.MaterialResources.getDrawable;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.TextView;

import java.util.List;

import cn.itcast.writeitdown.databinding.ItemContentTodoBinding;

class vHolder{
    GridView gv;
    TextView todoinvis,todotime,todotext;
    CheckBox todocheckBox;
}
public class  TodoAdapter  extends BaseAdapter {
    private ItemContentTodoBinding binding;
    private List<todobean> todobeanList;
    private Context context;
    private Dao dao;


    public TodoAdapter (Context context, List<todobean> todobeanList){
        this.context = context;
        this.todobeanList=todobeanList;
    }
    @Override
    public int getCount() {return todobeanList.size();}
    @Override
    public Object getItem(int i) {return todobeanList.get(i);}
    @Override
    public long getItemId(int i) {return i;}


    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        vHolder holder;
        dao = new Dao(context);
        todobean bean =todobeanList.get(position);
        if(convertView == null){
//            binding = ItemContentTodoBinding.inflate(LayoutInflater.from(context));
//            convertView = binding.getRoot();
//            convertView.setTag(binding);
            convertView = View.inflate( context,R.layout.item_content_todo,null);
            holder= new vHolder();
            holder.todoinvis=(TextView) convertView.findViewById(R.id.todoinvis);
            holder.todocheckBox=(CheckBox) convertView.findViewById(R.id.todocheckBox);
            holder.todotext=(TextView) convertView.findViewById(R.id.todotext);
            holder.todotime=(TextView) convertView.findViewById(R.id.todotime);
            holder.gv = convertView.findViewById(R.id.todolist);
            convertView.setTag(holder);

        }
        else {
//            binding = (ItemContentTodoBinding) convertView.getTag();
            holder = (vHolder) convertView.getTag();
        }
        holder.todoinvis.setText(bean.getId());
        holder.todotext.setText(bean.getTodo());
        holder.todotime.setText(bean.getTime());
//        binding.todoinvis.setText(bean.getId());
//        binding.todotext.setText(bean.getTodo());
//        binding.todotime.setText(bean.getTime());
        if(DateString.deadline(DateString.StringData(),bean.getTime())){
            holder.todotime.setTextColor(Color.rgb(180,0,0));
            //到时间或超时，文本变红色
        }
        if(bean.getState().equals("flase")){
            holder.todocheckBox.setChecked(false);
            holder.todotext.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);
            holder.todotext.setPaintFlags(holder.todotext.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            //checkbox的状态，未勾选，字体为正常
        }
        else if(bean.getState().equals("true")){
            holder.todocheckBox.setChecked(true);
            holder.todotext.setTypeface(Typeface.SANS_SERIF, Typeface.ITALIC);
            holder.todotext.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            //checkbox的状态，勾选，字体为斜体并划线
        }
        holder.todocheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    bean.setState("true");
                    dao.todomodifystate(bean.getId(), bean.getState());
                    holder.todotext.setTypeface(Typeface.SANS_SERIF, Typeface.ITALIC);
                    holder.todotext.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                }else {
                    bean.setState("false");
                    dao.todomodifystate(bean.getId(), bean.getState());
                    holder.todotext.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);
                    holder.todotext.setPaintFlags(holder.todotext.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }

            }
        });
        return  convertView;
    }
}
