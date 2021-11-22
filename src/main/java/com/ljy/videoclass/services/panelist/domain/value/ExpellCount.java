package com.ljy.videoclass.services.panelist.domain.value;

import javax.persistence.Embeddable;

@Embeddable
public class ExpellCount {
    private static final ExpellCount INSTANCE = new ExpellCount();
    private short count;
    protected ExpellCount(){}

    public static ExpellCount getInstance() {
        INSTANCE.count = 0;
        return INSTANCE;
    }

    public short get() {
        return count;
    }

    public void increment() {
        count++;
    }

    private final static short MAXIUM = 5;
    public boolean isMaxium() {
        return count == MAXIUM;
    }
}
