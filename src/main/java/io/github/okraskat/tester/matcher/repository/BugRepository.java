package io.github.okraskat.tester.matcher.repository;

import io.github.okraskat.tester.matcher.domain.Bug;

import java.util.List;
import java.util.Set;

public interface BugRepository {
    List<Bug> findByTesterAndDevices(long testerId, Set<Long> devicesIds);
}
