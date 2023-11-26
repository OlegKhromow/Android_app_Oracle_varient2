package stu.cn.ua.lab2.tasks;

public interface TaskListener{
    void onSuccess(String result);
    void onError(Throwable error);
}
