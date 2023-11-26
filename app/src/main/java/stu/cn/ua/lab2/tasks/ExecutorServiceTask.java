package stu.cn.ua.lab2.tasks;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
public class ExecutorServiceTask{
    private boolean executed;
    private boolean cancelled;
    private TaskListener taskListener;
    private static final Handler handler = new Handler(Looper.getMainLooper());

    private Callable<String> callable;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Future<?> future;

    public ExecutorServiceTask(Callable<String> callable) {
        this.callable = callable;
    }

    public void execute(TaskListener listener) {
        if (executed) throw new RuntimeException("Already executed");
        executed = true;
        taskListener = listener;
        future = executorService.submit(()->{
            try {
                String result = callable.call();
                publishSuccess(result);
            }catch (Exception e){
                publishError(e);
            }
        });
    }

    public void cancel() {
        if (!cancelled){
            cancelled = true;
            taskListener = null;
            if (future != null){
                future.cancel(true);
                future = null;
            }
        }
    }

    protected final void publishSuccess(String result){
        runOnMainThread(()->{
            if (taskListener != null){
                taskListener.onSuccess(result);
                taskListener = null;
            }
        });
    }

    protected final void publishError(Throwable error){
        runOnMainThread(()->{
            if (taskListener != null){
                taskListener.onError(error);
                taskListener = null;
            }
        });
    }

    private void runOnMainThread(Runnable action){
        if (Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId()){
            action.run();
        }else {
            handler.post(action);
        }
    }
}
