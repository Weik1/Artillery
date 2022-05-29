package com.artillery.entity;

import java.util.ArrayList;
import java.util.HashMap;

public class Task {
    private int taskId;
    private String taskName;
    private String taskTarget;
    private String taskType;
    private Class<?> taskVul;
    private HashMap<String, String> taskPayloadInfo;
    private ArrayList<String> taskLog;
    private String taskResult;

    public Task(int taskId, String taskName, String taskTarget, String taskType, Class<?> taskVul, HashMap<String, String> taskPayloadInfo) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskTarget = taskTarget;
        this.taskType = taskType;
        this.taskVul = taskVul;
        this.taskLog = new ArrayList<String>();
        this.taskPayloadInfo = taskPayloadInfo;
        this.taskResult = "无返回结果";
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }


    public Class<?> getTaskVul() {
        return taskVul;
    }

    public void setTaskVul(Class<?> taskVul) {
        this.taskVul = taskVul;
    }


    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }


    public String getTaskTarget() {
        return taskTarget;
    }

    public void setTaskTarget(String taskTarget) {
        this.taskTarget = taskTarget;
    }


    public HashMap<String, String> getTaskPayloadInfo() {
        return taskPayloadInfo;
    }

    public void setTaskPayloadInfo(HashMap<String, String> taskPayloadInfo) {
        this.taskPayloadInfo = taskPayloadInfo;
    }

    public ArrayList<String> getTaskLog() {
        return taskLog;
    }

    public void setTaskLog(String taskLog) {
        this.taskLog.add(taskLog);
    }

    public String getTaskResult() {
        return taskResult;
    }

    public void setTaskResult(String taskResult) {
        this.taskResult = taskResult;
    }
}
