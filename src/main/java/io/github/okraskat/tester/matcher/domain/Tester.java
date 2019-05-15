package io.github.okraskat.tester.matcher.domain;

import java.util.Collections;
import java.util.Set;

public class Tester {
    private final long id;
    private final String firstName;
    private final String lastName;
    private final String country;
    private final Set<Long> devices;

    public Tester(long id, String firstName, String lastName, String country, Set<Long> devices) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
        this.devices = devices;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCountry() {
        return country;
    }

    public Set<Long> getDevices() {
        return Collections.unmodifiableSet(devices);
    }

    public String getUserName() {
        return String.format("%s %s", lastName, firstName);
    }
}
