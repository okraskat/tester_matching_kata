package io.github.okraskat.tester.matcher.repository;

class TesterDevice {
    private final long testerId;
    private final long deviceId;

    TesterDevice(long testerId, long deviceId) {
        this.testerId = testerId;
        this.deviceId = deviceId;
    }

    long getTesterId() {
        return testerId;
    }

    long getDeviceId() {
        return deviceId;
    }
}
