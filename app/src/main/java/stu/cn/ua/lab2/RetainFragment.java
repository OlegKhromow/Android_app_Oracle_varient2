package stu.cn.ua.lab2;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import stu.cn.ua.lab2.tasks.ExecutorServiceTask;
import stu.cn.ua.lab2.module.UserInfo;
import stu.cn.ua.lab2.tasks.GenerateAnswerCallable;
import stu.cn.ua.lab2.tasks.TaskListener;
import stu.cn.ua.lab2.tasks.ViewState;

public class RetainFragment extends Fragment {

    public static final String TAG = RetainFragment.class.getSimpleName();
    private ViewState viewState = new ViewState();
    private StateListener listener;
    private ExecutorServiceTask currentTask;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void setListener(StateListener listener) {
        this.listener = listener;
        if (listener != null)
            listener.onNewState(viewState);
    }

    public void generateAnswer(String question, UserInfo user){
        viewState.showProgress = true;
        viewState.isButtonEnabled = false;
        viewState.result = "";
        updateState();
        currentTask = new ExecutorServiceTask(new GenerateAnswerCallable(question, user, getResources().getStringArray(R.array.answers_array)));;
        currentTask.execute(new TaskListener() {
            @Override
            public void onSuccess(String result) {
                viewState.showProgress = false;
                viewState.isButtonEnabled = true;
                viewState.result = result;
                updateState();
            }

            @Override
            public void onError(Throwable error) {
                Log.e(TAG, "Error!", error);
                viewState.showProgress = false;
                viewState.isButtonEnabled = true;
                viewState.result = "";
                updateState();
            }
        });
    }

    private void updateState(){
        if (listener != null)
            listener.onNewState(viewState);
    }

    public void startState(){
        viewState.showProgress = false;
        viewState.isButtonEnabled = true;
        viewState.result = "";
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (currentTask != null)
            currentTask.cancel();
    }

    public interface StateListener{
        void onNewState(ViewState viewState);
    }
}
