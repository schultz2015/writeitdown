package cn.itcast.writeitdown;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class MyService extends Service {
    private static final int Miniute=10*60*1000;//定时为10分钟的毫秒数
    private static final int PENDING_REQUEST=0;
    public MyService() {
    }

    //调用Service执行方法

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Dao dao = new Dao(this);
        //这里模拟后台操作
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("Service","判断时间."+ System.currentTimeMillis());
                final Calendar c = Calendar.getInstance();
                c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
                String time = DateString.StringData()+"-"+String.valueOf(c.get(Calendar.HOUR_OF_DAY))+"-"+String.valueOf(c.get(Calendar.MINUTE));
                List<todobean> List=dao.todo();
                List<String> over = new ArrayList<>();
                List<String> almost = new ArrayList<>();
                for(int position=0;position<List.size();position++){
                    todobean todobean = List.get(position);
                    if(todobean.getState().equals("false")){
                        Log.e("Service","判断时间."+ time);
                        if(DateString.picker(time)>DateString.picker(todobean.getTime())){
                            over.add(todobean.getTodo());
                            Log.i(TAG, "已超时: "+todobean.getTodo()+todobean.getTime());
                        }
                        if(
                                (DateString.picker(time)  <=  DateString.picker(todobean.getTime()) )
                                        &&
                                (DateString.picker(time)  > (DateString.picker(todobean.getTime())-60) )
                        ){
                            almost.add(todobean.getTodo());
                            Log.i(TAG, "死线: "+todobean.getTodo()+todobean.getTime());
                        }
                    }
                }
                createNotificationForNormal(over,almost);
            }
        }).start();
        //通过AlarmManager定时启动广播
        AlarmManager alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);
        long triggerAtTime= SystemClock.elapsedRealtime()+(Miniute);//从开机到现在的毫秒书（手机睡眠(sleep)的时间也包括在内

        Intent i=new Intent(this, AlarmReceive.class);

        @SuppressLint("WrongConstant")
        PendingIntent pIntent=PendingIntent.getBroadcast(this,PENDING_REQUEST,i,PENDING_REQUEST);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pIntent);
        return super.onStartCommand(intent, flags, startId);


    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void createNotificationForNormal(List<String> over,List<String> almost) {
        // 适配8.0及以上 创建渠道
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("Message", "通知", NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }
        Intent intent = new Intent(this, todo.class);
        PendingIntent pendingIntent =  PendingIntent.getActivity(this, 0, intent, 0);
        for(int position=0;position<over.size();position++){
            Notification notification = new NotificationCompat.Builder(this,"Message")
                    .setContentText(over.get(position))
                    .setContentTitle("待办超时！")
                    .setAutoCancel(true)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .build();
            manager.notify(position+1,notification);
            Log.i(TAG, "超时！"+position);
        }
        for(int p=0;p<almost.size();p++){
            Notification notification = new NotificationCompat.Builder(this,"Message")
                    .setContentText(almost.get(p))
                    .setContentTitle("待办死线！")
                    .setAutoCancel(true)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .build();
            manager.notify(p+1+over.size(),notification);
            Log.i(TAG, "死线！"+p);
        }

    }
}