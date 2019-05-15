package io.github.okraskat.tester.matcher.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class CsvTesterDevicesRepository implements TesterDevicesRepository {
    private final String csvFilePath;

    @Autowired
    public CsvTesterDevicesRepository(@Value("${tester.devices.file.path}") String csvFilePath) {
        this.csvFilePath = csvFilePath;
    }

    @Override
    public Set<Long> findByTesterId(long testerId) {
        return new BasicCsvRepository<>(this::testerDeviceFromCsvData, csvFilePath, td -> td.getTesterId() == testerId)
                .findRecords()
                .stream()
                .map(TesterDevice::getDeviceId)
                .collect(Collectors.toSet());
    }

    private Optional<TesterDevice> testerDeviceFromCsvData(List<String> csvData) {
        return Optional.of(new TesterDevice(Long.parseLong(csvData.get(0)), Long.parseLong(csvData.get(1))));
    }
}
