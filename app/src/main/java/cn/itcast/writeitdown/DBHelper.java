package cn.itcast.writeitdown;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "writeitdown.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE textinfo(_id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT,  content TEXT,time TEXT)");
        db.execSQL("CREATE TABLE imgsource(_id INTEGER PRIMARY KEY AUTOINCREMENT, address TEXT)");
        db.execSQL("CREATE TABLE recycle(_id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT,  content TEXT,time TEXT)");
        db.execSQL("CREATE TABLE todolist(_id INTEGER PRIMARY KEY AUTOINCREMENT, todo TEXT, time TEXT, state TEXT)");

//            db.execSQL("INSERT INTO imgsource VALUES(1,'https://images.pexels.com/photos/15483821/pexels-photo-15483821.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1')");
//            db.execSQL("INSERT INTO imgsource VALUES(2,'https://images.pexels.com/photos/12051956/pexels-photo-12051956.jpeg?auto=compress&cs=tinysrgb&w=600&lazy=load')");
//            db.execSQL("INSERT INTO imgsource VALUES(3,'https://images.pexels.com/photos/11042709/pexels-photo-11042709.jpeg?auto=compress&cs=tinysrgb&w=600&lazy=load')");
//            db.execSQL("INSERT INTO imgsource VALUES(4,'https://images.pexels.com/photos/16668845/pexels-photo-16668845.jpeg?auto=compress&cs=tinysrgb&w=600&lazy=load')");
//            db.execSQL("INSERT INTO imgsource VALUES(5,'https://images.pexels.com/photos/16719683/pexels-photo-16719683.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1')");
//            db.execSQL("INSERT INTO imgsource VALUES(6,'https://images.pexels.com/photos/16056937/pexels-photo-16056937.jpeg?auto=compress&cs=tinysrgb&w=600&lazy=load')");
//            db.execSQL("INSERT INTO imgsource VALUES(7,'https://images.pexels.com/photos/16107388/pexels-photo-16107388.jpeg?auto=compress&cs=tinysrgb&w=600&lazy=load')");
//            db.execSQL("INSERT INTO imgsource VALUES(8,'https://images.pexels.com/photos/16772184/pexels-photo-16772184.jpeg?auto=compress&cs=tinysrgb&w=600&lazy=load')");
//            db.execSQL("INSERT INTO imgsource VALUES(9,'https://images.pexels.com/photos/14213855/pexels-photo-14213855.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1')");
//            db.execSQL("INSERT INTO imgsource VALUES(11,'https://images.pexels.com/photos/15639057/pexels-photo-15639057.jpeg?auto=compress&cs=tinysrgb&w=600&lazy=load')");
//            db.execSQL("INSERT INTO imgsource VALUES(12,'https://images.pexels.com/photos/15172864/pexels-photo-15172864.jpeg?auto=compress&cs=tinysrgb&w=600&lazy=load')");
//            db.execSQL("INSERT INTO imgsource VALUES(13,'https://images.pexels.com/photos/16446086/pexels-photo-16446086.jpeg?auto=compress&cs=tinysrgb&w=600&lazy=load')");
//            db.execSQL("INSERT INTO imgsource VALUES(14,'https://images.pexels.com/photos/16520256/pexels-photo-16520256.jpeg?auto=compress&cs=tinysrgb&w=600&lazy=load')");
//            db.execSQL("INSERT INTO imgsource VALUES(15,'https://images.pexels.com/photos/13234875/pexels-photo-13234875.jpeg?auto=compress&cs=tinysrgb&w=600&lazy=load')");
//            db.execSQL("INSERT INTO imgsource VALUES(16,'https://images.pexels.com/photos/15658170/pexels-photo-15658170.jpeg?auto=compress&cs=tinysrgb&w=600&lazy=load')");
//            db.execSQL("INSERT INTO imgsource VALUES('')");
   }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}