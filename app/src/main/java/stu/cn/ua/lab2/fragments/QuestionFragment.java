package stu.cn.ua.lab2.fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import stu.cn.ua.lab2.MyService;
import stu.cn.ua.lab2.R;
import stu.cn.ua.lab2.databinding.FragmentQuestionBinding;
import stu.cn.ua.lab2.module.UserInfo;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestionFragment extends BaseFragment {
    private final String KEY_ANSWER = "ANSWER";
    private FragmentQuestionBinding binding;

    private MyService service;

    public QuestionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param info user settings.
     * @return A new instance of fragment QuestionFragment.
     */
    public static QuestionFragment newInstance(UserInfo info) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putParcelable(USER_SETTINGS, info);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentQuestionBinding.inflate(inflater, container, false);

        if (getArguments() != null) {
            userInfo = getArguments().getParcelable(USER_SETTINGS, UserInfo.class);
        }
        if (savedInstanceState != null)
            binding.answerTextView.setText(savedInstanceState.getCharSequence(KEY_ANSWER));

        binding.askButton.setOnClickListener(v -> {
            getAnswer();
        });
        binding.toMenuButton.setOnClickListener(v -> {
            openFragment(MenuFragment.newInstance(userInfo));
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                openFragment(MenuFragment.newInstance(userInfo));
            }
        });
        return binding.getRoot();
    }

    /**
     * Get the user's question and call service to generate answer
     */
    private void getAnswer() {
        String question = binding.editQuestion.getText().toString().trim();
        binding.answerTextView.setText("");
        if (question.isEmpty()) {
            binding.editQuestion.setError(getString(R.string.enter_question_error));
            binding.editQuestion.setText("");
        }
        else {
            binding.answerTextView.setText(service.generateAnswer(question, userInfo));
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence(KEY_ANSWER, binding.answerTextView.getText());
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(getActivity(), MyService.class);
        getActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unbindService(connection);
    }

    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            service = ((MyService.MyBinder)binder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            service = null;
        }
    };
}