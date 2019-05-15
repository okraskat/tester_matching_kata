package io.github.okraskat.tester.matcher.repository;

import io.github.okraskat.tester.matcher.domain.Bug;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

@Repository
public class CsvBugRepository implements BugRepository {
    private final String csvFilePath;

    @Autowired
    public CsvBugRepository(@Value("${bugs.file.path}") String csvFilePath) {
        this.csvFilePath = csvFilePath;
    }

    @Override
    public List<Bug> findByTesterAndDevices(long testerId, Set<Long> devicesIds) {
        return new BasicCsvRepository<>(this::bugFromCsvData, csvFilePath, filterBugs(testerId, devicesIds))
                .findRecords();
    }

    private Predicate<Bug> filterBugs(long testerId, Set<Long> devicesIds) {
        return b -> b.getTesterId() == testerId && devicesIds.contains(b.getDeviceId());
    }

    private Optional<Bug> bugFromCsvData(List<String> csvData) {
        long bugId = Long.parseLong(csvData.get(0));
        long testerId = Long.parseLong(csvData.get(2));
        long deviceId = Long.parseLong(csvData.get(1));
        return Optional.of(new Bug(bugId, testerId, deviceId));
    }
}
