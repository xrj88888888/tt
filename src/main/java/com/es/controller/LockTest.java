package com.es.controller;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockTest {
    static Lock lock = new ReentrantLock(true);
    static class Task extends Thread{
        int count = 0;
        @Override
        public void run() {
            while (true){
                lock.lock();
                count++;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                lock.unlock();
            }
        }
    }
    public static void main(String[] args) {
        Task[] tasks = new Task[5];
        for (int i = 0;i < 5;i++){
            Task task = new Task();
            task.start();
            tasks[i] = task;
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (Task task : tasks) {
            System.out.println(task.getName() + ":" + task.count);
        }
    }
}
