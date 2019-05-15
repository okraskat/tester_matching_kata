package io.github.okraskat.tester.matcher.search;

import java.util.Collections;
import java.util.Set;

public class SearchCriteria {
    private static final String ALL = "ALL";

    private final Set<String> countries;
    private final Set<String> devices;

    public SearchCriteria(Set<String> countries, Set<String> devices) {
        this.countries = countries;
        this.devices = devices;
    }

    public Set<String> getCountries() {
        return Collections.unmodifiableSet(countries);
    }

    public Set<String> getDevices() {
        return Collections.unmodifiableSet(devices);
    }

    public boolean searchAllCountries() {
        return countries.isEmpty() || countries.contains(ALL);
    }

    public boolean searchAllDevices() {
        return devices.isEmpty() || devices.contains(ALL);
    }
}
