package com.petrovpavel.uimodule;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Timer;
import java.util.TimerTask;

import static com.petrovpavel.datamodule.core.DataManager.removeResultFiles;
import static com.petrovpavel.datamodule.core.DataManager.resultIterator;

public class TaskResultWaiter {

    private Timer timer;
    private TimerTask timerTask;

    private BigInteger taskCount;
    private BigInteger currentTaskCount;
    private Path taskResFolderPath;


    public interface Callback {
        void onTimeoutTick(BigInteger resultsCount);

        void onAllResultsExist();

        void onException(Exception e);
    }


    public TaskResultWaiter(String taskResFolderPath, BigInteger taskCount) {
        this.taskResFolderPath = Paths.get(taskResFolderPath);
        this.taskCount = taskCount;
    }

    public void start(Callback callback, long delay) throws IOException {
        removeResultFiles(taskResFolderPath);
        this.currentTaskCount = BigInteger.ZERO;
        this.timer = new Timer();
        this.timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    resultIterator(taskCount, taskResFolderPath, resultPath -> {
                        currentTaskCount = currentTaskCount.add(BigInteger.ONE);
                        if (currentTaskCount.equals(taskCount)) {
                            callback.onAllResultsExist();
                            stop();
                        }
                    });

                    callback.onTimeoutTick(currentTaskCount);

                } catch (IOException e) {
                    callback.onException(e);
                    stop();
                }
            }
        };
        this.timer.schedule(this.timerTask, delay, delay);


    }

    public void stop() {
        timerTask.cancel();
        timer.cancel();
    }

}