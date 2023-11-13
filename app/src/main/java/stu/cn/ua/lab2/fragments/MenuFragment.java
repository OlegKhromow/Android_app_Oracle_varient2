package stu.cn.ua.lab2.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import stu.cn.ua.lab2.R;
import stu.cn.ua.lab2.databinding.FragmentMenuBinding;
import stu.cn.ua.lab2.module.UserInfo;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuFragment extends BaseFragment {
    private static final String TAG = MenuFragment.class.getSimpleName();
    private FragmentMenuBinding binding;

    public MenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param info user settings.
     * @return A new instance of fragment MenuFragment.
     */
    public static MenuFragment newInstance(UserInfo info) {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putParcelable(USER_SETTINGS, info);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null) {
            userInfo = getArguments().getParcelable(USER_SETTINGS, UserInfo.class);
            Log.d(TAG, userInfo.toString());
        }
        binding = FragmentMenuBinding.inflate(inflater, container, false);

        binding.settingsButton.setOnClickListener(v -> {
            openFragment(SettingsFragment.newInstance(userInfo));
        });

        binding.askButton.setOnClickListener(v -> {
            if(userInfo.getName().isEmpty())
                showAlertDialog();
            else
                openFragment(QuestionFragment.newInstance(userInfo));
        });
        binding.exitButton.setOnClickListener(v -> {
            getActivity().finish();
        });
        return binding.getRoot();
    }

    /**
     * Show alert dialog with an offer to enter user's data
     */
    private void showAlertDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.alert_dialog_title)
                .setMessage(R.string.alert_dialog_message)
                .setPositiveButton(R.string.alert_dialog_yes, (dialog, which) -> {
                    openFragment(SettingsFragment.newInstance(userInfo));;
                })
                .setNegativeButton(R.string.alert_dialog_no, (dialog, which) -> {
                    dialog.cancel();
                })
                .setCancelable(false)
                .create();
        alertDialog.show();
    }
}