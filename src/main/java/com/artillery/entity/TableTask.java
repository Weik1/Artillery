package com.artillery.entity;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TableTask {

    private final StringProperty taskUrl;
    private final StringProperty taskType;
    private final StringProperty taskVul;
    private final ObjectProperty<Task> taskTask;
    private final StringProperty taskCreateTime;
    private final StringProperty taskStatus;


    public TableTask(String taskUrl, String taskType, String taskVul, String taskCreateTime, String taskStatus, Task taskTask) {
        this.taskUrl = new SimpleStringProperty(taskUrl);
        this.taskType = new SimpleStringProperty(taskType);
        this.taskVul = new SimpleStringProperty(taskVul);
        // Some initial dummy data, just for convenient testing.
        this.taskCreateTime = new SimpleStringProperty(taskCreateTime);
        this.taskStatus = new SimpleStringProperty(taskStatus);

        this.taskTask = new SimpleObjectProperty<Task>(taskTask);
    }

    public String getTaskUrl() {
        return taskUrl.get();
    }

    public void setTaskUrl(String taskUrl) {
        this.taskUrl.set(taskUrl);
    }

    public StringProperty taskUrlProperty() {
        return taskUrl;
    }


    public String getTaskType() {
        return taskType.get();
    }

    public void setTaskType(String taskUrl) {
        this.taskType.set(taskUrl);
    }

    public StringProperty taskTypeProperty() {
        return taskType;
    }


    public String getTaskVul() {
        return taskVul.get();
    }

    public void setTaskVul(String taskType) {
        this.taskVul.set(taskType);
    }

    public StringProperty taskVulProperty() {
        return taskVul;
    }


    public String getTaskCreateTime() {
        return taskCreateTime.get();
    }

    public void setTaskCreateTime(String taskCreateTime) {
        this.taskCreateTime.set(taskCreateTime);
    }

    public StringProperty taskCreateTimeProperty() {
        return taskCreateTime;
    }

    public Task getTaskTask() {
        return taskTask.get();
    }

    public void setTaskTask(Task taskTask) {
        this.taskTask.set(taskTask);
    }

    public ObjectProperty taskTaskProperty() {
        return taskTask;
    }

    public String getTaskStatus() {
        return taskStatus.get();
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus.set(taskStatus);
    }

    public StringProperty taskStatusProperty() {
        return taskStatus;
    }
}
