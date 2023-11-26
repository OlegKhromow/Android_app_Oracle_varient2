package stu.cn.ua.lab2.fragments;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import stu.cn.ua.lab2.R;
import stu.cn.ua.lab2.RetainFragment;
import stu.cn.ua.lab2.databinding.FragmentQuestionBinding;
import stu.cn.ua.lab2.module.UserInfo;
import stu.cn.ua.lab2.tasks.ViewState;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestionFragment extends BaseFragment implements RetainFragment.StateListener{
    private FragmentQuestionBinding binding;
    private RetainFragment retainFragment;

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

        retainFragment = (RetainFragment) getParentFragmentManager().findFragmentByTag(RetainFragment.TAG);
        if (retainFragment == null){
            retainFragment = new RetainFragment();
            getParentFragmentManager().beginTransaction()
                    .add(retainFragment, RetainFragment.TAG)
                    .commit();
        }
        retainFragment.setListener(this);

        if (getArguments() != null) {
            userInfo = getArguments().getParcelable(USER_SETTINGS, UserInfo.class);
        }

        binding.askButton.setOnClickListener(v -> {
            getAnswer();
        });
        binding.toMenuButton.setOnClickListener(v -> {
            openFragment(MenuFragment.newInstance(userInfo));
            retainFragment.startState();
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                openFragment(MenuFragment.newInstance(userInfo));
                retainFragment.startState();
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
            retainFragment.generateAnswer(question, userInfo);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        retainFragment.setListener(null);
    }

    @Override
    public void onNewState(ViewState viewState) {
        binding.askButton.setEnabled(viewState.isButtonEnabled);
        binding.answerTextView.setText(viewState.result);
        binding.progressBar.setVisibility(viewState.showProgress ? View.VISIBLE : View.GONE);
    }
}