package io.github.okraskat.tester.matcher.repository;

import java.util.Set;

public interface TesterDevicesRepository {
    Set<Long> findByTesterId(long testerId);
}
