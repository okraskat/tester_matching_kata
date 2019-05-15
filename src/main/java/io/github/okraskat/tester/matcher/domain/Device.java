package io.github.okraskat.tester.matcher.domain;

public class Device {
    private final long id;
    private final String description;

    public Device(long id, String description) {
        this.id = id;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
