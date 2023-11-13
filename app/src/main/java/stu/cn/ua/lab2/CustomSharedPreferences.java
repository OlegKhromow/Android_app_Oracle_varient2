package stu.cn.ua.lab2;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import stu.cn.ua.lab2.module.UserInfo;

public class CustomSharedPreferences{

    private static final String TAG = CustomSharedPreferences.class.getSimpleName();
    private static final String USER_SETTINGS = "USER_SETTINGS";
    private static final String APP_NAME = "oracle";

    /**
     * Method used to save user's settings
     * @param context to get shared preferences
     * @param userInfo an instance of the {@link UserInfo} class
     */
    public static void putUser(Context context, UserInfo userInfo){
        SharedPreferences sharedPreferences = context.getSharedPreferences(APP_NAME, Context.MODE_PRIVATE);
        String json = new Gson().toJson(userInfo);
        Log.d(TAG, json);
        sharedPreferences.edit()
                .putString(USER_SETTINGS, json)
                .apply();
    }

    /**
     * Method used to retrieve user's settings from file
     * @param context to get shared preferences
     * @return an instance of the {@link UserInfo} class
     */
    public static UserInfo getUser(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(APP_NAME, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(USER_SETTINGS, "");
        UserInfo userInfo;
        if (json.isEmpty())
            userInfo = UserInfo.EmptyUserInfo();
        else
            userInfo = new Gson().fromJson(json, UserInfo.class);
        return userInfo;
    }
}
