package org.example.circuitbreaker;

public class FakeCurrentTime implements CurrentTime {
    private long millis;

    FakeCurrentTime(long start) {
        this.millis = start;
    }

    void advance(long deltaMillis) {
        millis += deltaMillis;
    }

    @Override
    public long getCurrentTime() {
        return millis;
    }
}
