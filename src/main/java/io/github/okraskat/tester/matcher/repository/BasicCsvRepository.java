package io.github.okraskat.tester.matcher.repository;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Function;
import java.util.function.Predicate;

class BasicCsvRepository<T> {

    private static final String COMMA_DELIMITER = ",";
    private final Function<List<String>, Optional<T>> transformFunction;
    private final String csvFilePath;
    private Predicate<? super T> filterPredicate;

    BasicCsvRepository(Function<List<String>, Optional<T>> transformFunction, String csvFilePath, Predicate<? super T> filterPredicate) {
        this.transformFunction = transformFunction;
        this.csvFilePath = csvFilePath;
        this.filterPredicate = filterPredicate;
    }

    List<T> findRecords() {
        List<T> records = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(csvFilePath))) {
            skipFirstLineIfPresent(scanner);
            while (scanner.hasNextLine()) {
                getRecordFromLine(scanner.nextLine(), filterPredicate)
                        .ifPresent(records::add);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return records;
    }

    private void skipFirstLineIfPresent(Scanner scanner) {
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }
    }

    private Optional<T> getRecordFromLine(String line, Predicate<? super T> filterPredicate) {
        if (StringUtils.isEmpty(line)) {
            return Optional.empty();
        }
        List<String> csvColumns = getColumnsFromCsvRecord(line);
        return transformFunction.apply(csvColumns)
                .filter(filterPredicate);
    }

    private List<String> getColumnsFromCsvRecord(String line) {
        List<String> values = new ArrayList<>();
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(COMMA_DELIMITER);
            while (rowScanner.hasNext()) {
                String value = trimQuotationMarks(rowScanner.next());
                values.add(value);
            }
        }
        return values;
    }

    private String trimQuotationMarks(String stringWithQuotationMarks) {
        if (StringUtils.isNotEmpty(stringWithQuotationMarks)) {
            return stringWithQuotationMarks.substring(1, stringWithQuotationMarks.length() - 1);
        }
        return stringWithQuotationMarks;
    }
}
