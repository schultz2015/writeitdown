package cn.itcast.writeitdown;

import static android.content.ContentValues.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.collection.ArraySet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Dao {
    DBHelper dbHelper;
    SQLiteDatabase db ;
    Cursor cursor;
    ContentValues values;
    public Dao(Context context) {
        dbHelper = new DBHelper(context);
    }
    public String imgread(){
        String num,address = null;
        db = dbHelper.getWritableDatabase();
        int no =(int)(Math.random()*30);
        Log.i(TAG, "no: "+ no);
        if(no>30){
            String address_part1="https://images.pexels.com/photos/";
            num = String.valueOf(16307114+no);
            String address_part3="/pexels-photo-";
            String address_part5=".jpeg";
            address=address_part1+num+address_part3+num+address_part5;
        }
        else {
            num = String.valueOf(no);
            cursor = db.query("imgsource",null,"_id=?",new String[]{num},null,null,null);
            if(cursor.getCount()!=0){
                cursor.moveToFirst();
                address=cursor.getString(1);
            }
        }

        db.close();
        cursor.close();
        Log.i(TAG, "imgread: "+ address);
        return address;
    }
    public List<listbean> searching(String table){
        List<listbean> itemList;
        itemList = new ArrayList<>();
        db = dbHelper.getWritableDatabase();
        cursor=db.query(table,new String[]{"_id,title,content,time"},null,null,null,null,null);
        Log.i(TAG, "set: "+cursor.getCount());
        if(cursor.getCount()!=0){
            if(cursor.moveToFirst()){
                while(!cursor.isAfterLast()){
                    listbean bean = new listbean();
                    bean.setId(String.valueOf(cursor.getInt(0)));
                    bean.setTitle(cursor.getString(1));
                    bean.setText(cursor.getString(2));
                    bean.setTime(cursor.getString(3));
                    itemList.add(bean);
                    cursor.moveToNext();
                }
            }
        }
        db.close();
        cursor.close();
        return itemList;
    }
    public String textreadtitle(String id,String tablename){
        db = dbHelper.getWritableDatabase();
        String title= null;
        cursor = db.query(tablename,null,"_id=?",new String[]{id},null,null,null);
        if(cursor.getCount()!=0){
            cursor.moveToFirst();
            title=cursor.getString(1);
        }
        db.close();
        cursor.close();
        return title;
    }
    public String textreadcontent(String id,String tablename){
        db = dbHelper.getWritableDatabase();
        String content = null;
        cursor = db.query(tablename,null,"_id=?",new String[]{id},null,null,null);
        if(cursor.getCount()!=0){
            cursor.moveToFirst();
            content=cursor.getString(2);
        }
        db.close();
        cursor.close();
        return content;
    }
    public String textreadtime(String id,String tablename){
        db = dbHelper.getWritableDatabase();
        String time = null;
        cursor = db.query(tablename,null,"_id=?",new String[]{id},null,null,null);
        if(cursor.getCount()!=0){
            cursor.moveToFirst();
            time=cursor.getString(3);
        }
        db.close();
        cursor.close();
        return time;
    }
    public void textadd(String id,String title,String content){
        db = dbHelper.getWritableDatabase();
        cursor = db.query("textinfo",null,"_id=?",new String[]{id},null,null,null);
        if(cursor.getCount()==0){
            values = new ContentValues();
            // 创建ContentValues对象
            values.put("_id",Integer.parseInt(id));
            values.put("title", title);           // 将数据添加到ContentValues对象
            values.put("content", content);
            values.put("time",DateString.StringData().toString().trim());
            db.insert("textinfo", null, values);
            Log.i(TAG, "textadd: "+title);
        }else{
            Log.i(TAG, "textadd: "+id);
            cursor = db.query("textinfo",null,null,null,null,null,null);
            Log.i(TAG, "setData: "+cursor.getCount());
        }
        db.close();
        cursor.close();
    }
    public void textdelete(String id){
        db = dbHelper.getWritableDatabase();
        cursor = db.query("textinfo",null,"_id=?",new String[]{id},null,null,null);
        if(cursor.getCount()!=0){
            db.delete("textinfo","_id=?",new String[]{id});
        }
        db.close();
        cursor.close();
    }
    public void recycledelete(String id){
        db = dbHelper.getWritableDatabase();
        cursor = db.query("recycle",null,"_id=?",new String[]{id},null,null,null);
        if(cursor.getCount()!=0){
            db.delete("recycle","_id=?",new String[]{id});
        }
        db.close();
        cursor.close();
    }
    public void recycle(String id,String title,String content,String time){
        db = dbHelper.getWritableDatabase();
        cursor = db.query("recycle",null,"_id=?",new String[]{id},null,null,null);
        if(cursor.getCount()==0){
            values = new ContentValues();
            // 创建ContentValues对象
            values.put("_id",Integer.parseInt(id));
            values.put("title", title);           // 将数据添加到ContentValues对象
            values.put("content", content);
            values.put("time",time);
            db.insert("recycle", null, values);
        }else{
            Log.i(TAG, "Recycleadd: "+id);
            cursor = db.query("recycle",null,null,null,null,null,null);
            Log.i(TAG, "setData: "+cursor.getCount());
        }
        db.close();
        cursor.close();
    }
    public void textmodify(String id,String title,String content){
        db = dbHelper.getWritableDatabase();
        cursor = db.query("textinfo",null,"_id=?",new String[]{id},null,null,null);
        if(cursor.getCount()!=0){
            values = new ContentValues();       // 创建ContentValues对象
            values.put("_id", Integer.parseInt(id));
            values.put("title", title);           // 将数据添加到ContentValues对象
            values.put("content", content);
            db.update("textinfo", values, "_id=?", new String[]{id}); // 更新并得到行数
        }
        db.close();
        cursor.close();
    }
    public String maxid(String table){
        db = dbHelper.getWritableDatabase();
        String id = null;
        cursor = db.query(table,new String[]{"_id"},null,null,null,null,"_id desc");
        if(cursor.getCount()!=0){
            cursor.moveToFirst();
            id=cursor.getString(0);
            int num = Integer.parseInt(id)+1;
            id=String.valueOf(num);
        }
        else{
            id ="1";
        }
        db.close();
        cursor.close();
        return id;
    }

    public void tododelete(String id) {
        db = dbHelper.getWritableDatabase();
        cursor = db.query("todolist",null,"_id=?",new String[]{id},null,null,null);
        if(cursor.getCount()!=0){
            db.delete("todolist","_id=?",new String[]{id});
        }
        db.close();
        cursor.close();
    }
    public List<todobean> todo(){
        List<todobean> itemList;
        itemList = new ArrayList<>();
        db = dbHelper.getWritableDatabase();
        cursor=db.query("todolist",new String[]{"_id,todo,time,state"},null,null,null,null,null);
        Log.i(TAG, "set: "+cursor.getCount());
        if(cursor.getCount()!=0){
            if(cursor.moveToFirst()){
                while(!cursor.isAfterLast()){
                    todobean bean = new todobean();
                    bean.setId(String.valueOf(cursor.getInt(0)));
                    bean.setTodo(cursor.getString(1));
                    bean.setTime(cursor.getString(2));
                    bean.setState(cursor.getString(3));
                    itemList.add(bean);
                    cursor.moveToNext();
                }
            }
        }
        Log.i(TAG, "todo: "+itemList.size());
        db.close();
        cursor.close();
        return itemList;
    }
    public void todoadd(String id,String todo,String time){
        db = dbHelper.getWritableDatabase();
        cursor = db.query("todolist",null,"_id=?",new String[]{id},null,null,null);
        if(cursor.getCount()==0){
            values = new ContentValues();
            // 创建ContentValues对象
            values.put("_id",Integer.parseInt(id));
            values.put("todo", todo);           // 将数据添加到ContentValues对象
            values.put("time",time);
            values.put("state","false");
            db.insert("todolist", null, values);
            Log.i(TAG, "todolist: "+todo);
        }else{
            Log.i(TAG, "todoadd: "+id);
            cursor = db.query("todolist",null,null,null,null,null,null);
            Log.i(TAG, "setData: "+cursor.getCount());
        }
        db.close();
        cursor.close();
    }
    public void todomodifystate(String id,String state){
        db = dbHelper.getWritableDatabase();
        cursor = db.query("todolist",null,"_id=?",new String[]{id},null,null,null);
        if(cursor.getCount()!=0){
            values = new ContentValues();       // 创建ContentValues对象
            values.put("_id", Integer.parseInt(id));
            values.put("state", state);           // 将数据添加到ContentValues对象
            db.update("todolist", values, "_id=?", new String[]{id}); // 更新并得到行数
        }
        db.close();
        cursor.close();
    }

    public void todomodifytext(String id, String todo, String time) {
        db = dbHelper.getWritableDatabase();

        cursor = db.query("todolist",null,"_id=?",new String[]{id},null,null,null);
        if(cursor.getCount()!=0){
            values = new ContentValues();       // 创建ContentValues对象
            values.put("_id", Integer.parseInt(id));
            values.put("todo", todo);           // 将数据添加到ContentValues对象
            values.put("time", time);
            db.update("todolist", values, "_id=?", new String[]{id}); // 更新并得到行数
        }
        db.close();
        cursor.close();
    }
}
