package com.ljy.videoclass.services.panelist.domain.value;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class ExpellCount {
    private static final ExpellCount INSTANCE = new ExpellCount();
    private int count;

    private ExpellCount(int count){this.count = count;}

    protected ExpellCount(){}

    public static ExpellCount getInstance() {
        INSTANCE.count = 0;
        return INSTANCE;
    }

    public int get() {
        return count;
    }

    public ExpellCount increment() {
        return new ExpellCount(count + 1);
    }

    private final static short MAXIUM = 5;
    public boolean isMaxium() {
        return count == MAXIUM;
    }

    @Override
    public String toString() {
        return "ExpellCount{" +
                "count=" + count +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpellCount that = (ExpellCount) o;
        return count == that.count;
    }

    @Override
    public int hashCode() {
        return Objects.hash(count);
    }
}
