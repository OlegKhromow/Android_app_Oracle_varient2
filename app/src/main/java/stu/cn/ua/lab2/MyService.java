package stu.cn.ua.lab2;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import stu.cn.ua.lab2.module.UserInfo;

public class MyService extends Service {
    private MyBinder binder = new MyBinder();

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_NOT_STICKY;
    }

    /**
     * Generates an answer to the user's question depending on the question, settings and current date using a hash code
     */
    public String generateAnswer(String question, UserInfo userInfo) {
        long time = System.currentTimeMillis() / 1000 / 60 / 60 / 24;
        String allData = question + userInfo + time + "";
        int hash = allData.hashCode();
        String[] answers = getResources().getStringArray(R.array.answers_array);
        return answers[Math.abs(hash) % answers.length];
    }

    public class MyBinder extends Binder{
        public MyService getService(){
            return MyService.this;
        }
    }
}