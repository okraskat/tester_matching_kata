package io.github.okraskat.tester.matcher.repository;

import io.github.okraskat.tester.matcher.domain.Tester;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class CsvTesterRepository implements TesterRepository {

    private final String csvFilePath;
    private final TesterDevicesRepository testerDevicesRepository;

    @Autowired
    public CsvTesterRepository(@Value("${testers.file.path}")String csvFilePath, TesterDevicesRepository testerDevicesRepository) {
        this.csvFilePath = csvFilePath;
        this.testerDevicesRepository = testerDevicesRepository;
    }

    @Override
    public List<Tester> findAll() {
        return new BasicCsvRepository<>(this::createTesterFromCsvData, csvFilePath, (t -> true)).findRecords();
    }

    @Override
    public List<Tester> findByCountries(Set<String> countries) {
        return new BasicCsvRepository<>(this::createTesterFromCsvData, csvFilePath, (t -> countries.contains(t.getCountry()))).findRecords();
    }

    private Optional<Tester> createTesterFromCsvData(List<String> csvData) {
        long testerId = Long.parseLong(csvData.get(0));
        Set<Long> devices = testerDevicesRepository.findByTesterId(testerId);
        return Optional.of(new Tester(testerId, csvData.get(1), csvData.get(2), csvData.get(3), devices));
    }


}
