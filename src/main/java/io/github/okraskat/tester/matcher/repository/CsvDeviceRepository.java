package io.github.okraskat.tester.matcher.repository;

import io.github.okraskat.tester.matcher.domain.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class CsvDeviceRepository implements DeviceRepository {
    private final String csvFilePath;

    @Autowired
    public CsvDeviceRepository(@Value("${devices.file.path}") String csvFilePath) {
        this.csvFilePath = csvFilePath;
    }

    @Override
    public List<Device> findAll() {
        return new BasicCsvRepository<>(this::createDeviceFromCsvData, csvFilePath, (t -> true)).findRecords();
    }

    @Override
    public List<Device> findByDescriptions(Set<String> devices) {
        return new BasicCsvRepository<>(this::createDeviceFromCsvData, csvFilePath, (d -> devices.contains(d.getDescription()))).findRecords();
    }

    private Optional<Device> createDeviceFromCsvData(List<String> csvData) {
        return Optional.of(new Device(Long.parseLong(csvData.get(0)), csvData.get(1)));
    }
}
