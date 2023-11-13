package stu.cn.ua.lab2.fragments;

import androidx.fragment.app.Fragment;

import stu.cn.ua.lab2.R;
import stu.cn.ua.lab2.module.UserInfo;

public class BaseFragment extends Fragment {
    protected static final String USER_SETTINGS = "USER_SETTINGS";
    protected UserInfo userInfo;

    protected void openFragment(BaseFragment fragment){
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

}
