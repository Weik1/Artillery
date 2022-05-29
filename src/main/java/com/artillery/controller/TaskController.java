package com.artillery.controller;

import com.artillery.entity.Task;
import com.artillery.util.StageManager;

import java.lang.reflect.Method;
import java.util.LinkedList;


public class TaskController {

    public int taskCounts; //任务数
    private int nThreads;//线程池的大小
    private PoolWorker[] threads;//用数组实现线程池
    private LinkedList queue;//任务队列


    public TaskController(int nThreads) {
        this.taskCounts = 0;
        this.nThreads = nThreads;
        queue = new LinkedList();
        threads = new PoolWorker[nThreads];

        for (int i = 0; i < nThreads; i++) {
            threads[i] = new PoolWorker();
            threads[i].start();//启动所有工作线程
        }
    }

    public void putTask(Task task) {//执行任务
        this.taskCounts++;
        synchronized (queue) {
            queue.addLast(task);
            queue.notify();
        }
    }

    private class PoolWorker extends Thread {//工作线程类

        public void run() {
            Task task;
            while (true) {
                synchronized (queue) {
                    while (queue.isEmpty()) {//如果任务队列中没有任务，等待
                        try {
                            queue.wait();
                        } catch (InterruptedException ignored) {
                        }
                    }
                    task = (Task) queue.removeFirst();//有任务时，取出任务
                }
                try {
//                    for (Payload payload : task.getPayloadArrayList()) {
//                        payload.verifier();
//                    }

                    Class<?> cls = task.getTaskVul();

                    Object obj = cls.newInstance();
                    String iphost = task.getTaskTarget();
                    Method method = cls.getMethod("Check", String.class);

                    String result = "扫描完成，不能判断是否存在漏洞";
                    try {
                        //System.out.println(iphost);
                        Object o = method.invoke(obj, iphost);
                        result = String.valueOf(o);
                        task.setTaskLog(result);
                        task.setTaskResult(result);

                    } catch (Exception e) {
                        System.out.println(e);
                        e.printStackTrace();
                    }

                    System.out.println("[TaskId：" + task.getTaskId() + "]  扫描完成：" + result);
                    MainController mainController = (MainController) StageManager.CONTROLLER.get("MainController");
                    mainController.tableTaskData.get(task.getTaskId()).setTaskStatus(result);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
