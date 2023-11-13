package stu.cn.ua.lab2.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import java.util.Calendar;
import java.util.Objects;

import stu.cn.ua.lab2.CustomSharedPreferences;
import stu.cn.ua.lab2.R;
import stu.cn.ua.lab2.databinding.FragmentSettingsBinding;
import stu.cn.ua.lab2.module.UserInfo;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends BaseFragment {
    private static final String TAG = FragmentSettingsBinding.class.getSimpleName();
    private FragmentSettingsBinding binding;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment SettingsFragment.
     */
    public static SettingsFragment newInstance(UserInfo userInfo) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putParcelable(USER_SETTINGS, userInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null) {
            userInfo = getArguments().getParcelable(USER_SETTINGS, UserInfo.class);
        }
        if(userInfo == null)
            userInfo = UserInfo.EmptyUserInfo();
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        initEnterFields();
        binding.birthDateEntry.setOnClickListener(v -> {
            setBirthDateFromDataPicker();
        });

        //hide error when radio button is checked
        binding.sexRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton = getView().findViewById(checkedId);
            userInfo.setSex(radioButton.getText().toString());
            binding.femaleRadioButton.setError(null);
        });

        binding.saveButton.setOnClickListener(v -> {
            saveSettings();
        });
        Log.d(TAG, userInfo.toString());

        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                openFragment(MenuFragment.newInstance(userInfo));
            }
        });
        return binding.getRoot();
    }

    /**
     * Initialization of editText and radioGroup components with user's information
     */
    private void initEnterFields() {
        binding.nameEdit.setText(userInfo.getName());
        binding.surnameEdit.setText(userInfo.getSurname());
        binding.birthDateEntry.setText(userInfo.getBirthDateString());
        if(userInfo.getSex().length() !=0 )
            if (Objects.equals(userInfo.getSex(), getString(R.string.male))) {
                binding.maleRadioButton.setChecked(true);
            } else {
                binding.femaleRadioButton.setChecked(true);
            }
    }

    /**
     * Creation of DatePickerDialog to choose a birthday
     */
    private void setBirthDateFromDataPicker() {
        Calendar date = userInfo.getBirthDate();
        DatePickerDialog pickerDialog = new DatePickerDialog(
                getActivity(),
                (view, year, month, dayOfMonth) -> {
                    date.set(year, month, dayOfMonth);
                    binding.birthDateEntry.setText(userInfo.getBirthDateString());
                },
                date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
        pickerDialog.show();
    }

    /**
     * Validation. If successful, the settings are saved and transferred to the main fragment
     */
    private void saveSettings() {
        boolean isOk = true;
        try {
            userInfo.setName(binding.nameEdit.getText().toString());
        } catch (IllegalArgumentException e) {
            binding.nameEdit.setText("");
            binding.nameEdit.setError(getString(R.string.enter_name_error));
            isOk = false;
        }
        try {
            userInfo.setSurname(binding.surnameEdit.getText().toString());
        } catch (IllegalArgumentException e) {
            binding.surnameEdit.setText("");
            binding.surnameEdit.setError(getString(R.string.enter_surname_error));
            isOk = false;
        }
        if(binding.sexRadioGroup.getCheckedRadioButtonId() == -1) {
            binding.femaleRadioButton.setError(getString(R.string.choose_sex_error));
            isOk = false;
        }
        if (isOk){
            Log.d(TAG, "New settings: " + userInfo);
            CustomSharedPreferences.putUser(getActivity(), userInfo);
            openFragment(MenuFragment.newInstance(userInfo));
        }
    }
}