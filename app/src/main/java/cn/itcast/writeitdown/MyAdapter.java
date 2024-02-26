package cn.itcast.writeitdown;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.List;

import cn.itcast.writeitdown.databinding.ItemContentBinding;
import cn.itcast.writeitdown.databinding.ItemContentGrideBinding;

class ViewHolder{
    TextView iteminvis,maintitle,time,maintext;
}
public class  MyAdapter  extends BaseAdapter {
    private ItemContentGrideBinding grideBinding;
    private ItemContentBinding listbinding;
    private List<listbean> itemList;
    private Context context;
    private String type;

    public MyAdapter (Context context, List<listbean> itemList, String type){
        this.context = context;
        this.itemList=itemList;
        this.type=type;
    }
    @Override
    public int getCount() {return itemList.size();}
    @Override
    public Object getItem(int i) {return itemList.get(i);}
    @Override
    public long getItemId(int i) {return i;}


    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        listbean bean =itemList.get(position);
        if(convertView == null){
            if (type.equals("grideType")){
//                grideBinding = ItemContentGrideBinding.inflate(LayoutInflater.from(context));
//                convertView=grideBinding.getRoot();
                convertView = View.inflate( context,R.layout.item_content_gride,null);
            }
            if(type.equals("listType")){
//                listbinding = ItemContentBinding.inflate(LayoutInflater.from(context));
//                convertView = listbinding.getRoot();
                convertView = View.inflate( context,R.layout.item_content,null);
            }

            holder= new ViewHolder();
            holder.iteminvis=(TextView) convertView.findViewById(R.id.iteminvis);
            holder.maintitle=(TextView) convertView.findViewById(R.id.maintitle);
            holder.time=(TextView) convertView.findViewById(R.id.time);
            holder.maintext=(TextView) convertView.findViewById(R.id.maintext);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.iteminvis.setText(bean.getId());
        holder.maintitle.setText(bean.getTitle());
        holder.time.setText(bean.getTime());
        holder.maintext.setText(bean.getText());


        return  convertView;
    }
}

