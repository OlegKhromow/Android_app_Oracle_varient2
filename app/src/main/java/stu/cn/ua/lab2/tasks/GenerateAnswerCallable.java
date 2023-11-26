package stu.cn.ua.lab2.tasks;

import java.util.concurrent.Callable;

import stu.cn.ua.lab2.module.UserInfo;

public class GenerateAnswerCallable implements Callable<String> {
    private String question;
    private UserInfo userInfo;
    private String[] answers;

    public GenerateAnswerCallable(String question, UserInfo userInfo, String[] answers) {
        this.question = question;
        this.userInfo = userInfo;
        this.answers = answers;
    }

    @Override
    public String call() throws Exception {
        Thread.sleep(2000);
        long time = System.currentTimeMillis() / 1000 / 60 / 60 / 24;
        String allData = question + userInfo + time + "";
        int hash = allData.hashCode();
        return answers[Math.abs(hash) % answers.length];
    }
}
