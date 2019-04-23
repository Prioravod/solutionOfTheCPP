package com.petrovpavel.datamodule.entity;

import java.math.BigInteger;

public class TaskResult {

    private BigInteger taskIndex;

    private Route minRoute;


    public TaskResult(Route minRoute, BigInteger taskIndex) {
        this.taskIndex = taskIndex;
        this.minRoute = minRoute;
    }

    public BigInteger getTaskIndex() {
        return taskIndex;
    }

    public Route getMinRoute() {
        return minRoute;
    }

    public String toFileFormat() {
        return "taskIndex=" + getTaskIndex() +
                "\r\n" + minRoute.toFileFormat();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskResult)) return false;

        TaskResult that = (TaskResult) o;

        if (!getTaskIndex().equals(that.getTaskIndex())) return false;
        return getMinRoute().equals(that.getMinRoute());
    }

    @Override
    public int hashCode() {
        int result = getTaskIndex().hashCode();
        result = 31 * result + getMinRoute().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "TaskResult{" +
                "taskIndex=" + taskIndex +
                ", minRoute=" + minRoute +
                '}';
    }
}
