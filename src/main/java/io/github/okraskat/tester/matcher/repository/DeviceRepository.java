package io.github.okraskat.tester.matcher.repository;

import io.github.okraskat.tester.matcher.domain.Device;

import java.util.List;
import java.util.Set;

public interface DeviceRepository {
    List<Device> findAll();

    List<Device> findByDescriptions(Set<String> devices);
}
