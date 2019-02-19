package com.cathay.jvm.cache;


public class Config {
    public void setSoftValues(boolean softValues) {
        this.softValues = softValues;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public void setExpireMilliSeconds(long expireMilliSeconds) {
        this.expireMilliSeconds = expireMilliSeconds;
    }

    long expireMilliSeconds = 120000L;
    int maxSize = 32;
    boolean softValues = false;


    public boolean isSoftValues() {
        return this.softValues;
    }

    public int getMaxSize() {
        return this.maxSize;
    }

    public long getExpireMilliSeconds() {
        return this.expireMilliSeconds;
    }
}
