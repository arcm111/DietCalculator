package com.arcm.dietcalculator;


import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Asynchronous task runner.
 */
public class TaskRunner {
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    public interface Callback<T> {
        void onComplete(T result);
    }

    public <T> void execute(Callable<T> callable, Callback<T> callback) {
        executor.execute(() -> {
            try {
                final T result = callable.call();
                if (callback != null) {
                    handler.post(() -> callback.onComplete(result));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}