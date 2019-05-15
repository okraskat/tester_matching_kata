package io.github.okraskat.tester.matcher.domain;

public class Bug {
    private final long id;
    private final long testerId;
    private final long deviceId;

    public Bug(long id, long testerId, long deviceId) {
        this.id = id;
        this.testerId = testerId;
        this.deviceId = deviceId;
    }

    public long getId() {
        return id;
    }

    public long getTesterId() {
        return testerId;
    }

    public long getDeviceId() {
        return deviceId;
    }
}
