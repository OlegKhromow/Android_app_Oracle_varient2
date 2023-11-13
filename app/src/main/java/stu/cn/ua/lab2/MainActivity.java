package stu.cn.ua.lab2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import stu.cn.ua.lab2.databinding.ActivityMainBinding;
import stu.cn.ua.lab2.fragments.MenuFragment;
import stu.cn.ua.lab2.module.UserInfo;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        UserInfo info = CustomSharedPreferences.getUser(this);
        if (savedInstanceState == null){
            MenuFragment menuFragment = MenuFragment.newInstance(info);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(binding.fragmentContainer.getId(), menuFragment)
                    .commit();
        }
    }
}