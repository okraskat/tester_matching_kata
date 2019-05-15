package io.github.okraskat.tester.matcher;

import io.github.okraskat.tester.matcher.repository.*;
import io.github.okraskat.tester.matcher.search.FoundTester;
import io.github.okraskat.tester.matcher.search.FoundTesters;
import io.github.okraskat.tester.matcher.search.SearchCriteria;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.collections.Sets;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class TesterMatcherTest {
    private TesterMatcher testerMatcher;

    @Before
    public void setUp() throws Exception {
        TesterDevicesRepository testerDeviceRepository = new CsvTesterDevicesRepository(getAbsolutePath("/tester_device.csv"));
        TesterRepository testerRepository = new CsvTesterRepository(getAbsolutePath("/testers.csv"), testerDeviceRepository);
        DeviceRepository deviceRepository = new CsvDeviceRepository(getAbsolutePath("/devices.csv"));
        BugRepository bugRepository = new CsvBugRepository(getAbsolutePath("/bugs.csv"));
        testerMatcher = new TesterMatcher(testerRepository, deviceRepository, bugRepository);
    }

    @Test
    public void shouldFindAllTestersInSortedOrder() {
        //given
        Set<String> countries = Collections.singleton("ALL");
        Set<String> devices = Collections.singleton("ALL");
        SearchCriteria searchCriteria = new SearchCriteria(countries, devices);

        //when
        FoundTesters results = testerMatcher.match(searchCriteria);

        //then
        assertThat(results).isNotNull();
        assertThat(results.getTesters()).isNotNull();
        assertThat(results.getTesters().size()).isEqualTo(9);
        FoundTester actual = results.getTesters().get(0);
        assertThat(actual.getUserName()).isNotBlank();
        assertThat(actual.getExperience()).isGreaterThan(0);
        assertThat(results.getTesters()).isSortedAccordingTo(Comparator.comparingInt(FoundTester::getExperience).reversed());
    }

    @Test
    public void shouldFindTestersFromGb() {
        //given
        Set<String> countries = Collections.singleton("GB");
        Set<String> devices = Collections.singleton("ALL");
        SearchCriteria searchCriteria = new SearchCriteria(countries, devices);

        //when
        FoundTesters results = testerMatcher.match(searchCriteria);

        //then
        assertThat(results).isNotNull();
        assertThat(results.getTesters()).isNotNull();
        assertThat(results.getTesters().size()).isEqualTo(3);
        FoundTester actual = results.getTesters().get(0);
        assertThat(actual.getUserName()).isNotBlank();
        assertThat(actual.getExperience()).isGreaterThan(0);
    }

    @Test
    public void shouldFindTestersWithiPhone4In2Ways() {
        //given
        Set<String> countries = Collections.singleton("ALL");
        Set<String> devices = Collections.singleton("iPhone 4");
        SearchCriteria searchCriteria = new SearchCriteria(countries, devices);

        //when
        FoundTesters firstSearch = testerMatcher.match(searchCriteria);
        Set<String> secondCountries = Sets.newSet("JP", "US");
        Set<String> secondDevices = Sets.newSet("iPhone 4");
        SearchCriteria secondSearchCriteria = new SearchCriteria(secondCountries, secondDevices);
        FoundTesters secondSearch = testerMatcher.match(secondSearchCriteria);

        //then
        assertThat(firstSearch).isNotNull();
        assertThat(firstSearch.getTesters()).isNotNull();
        assertThat(firstSearch.getTesters().size()).isEqualTo(4);
        FoundTester actual = firstSearch.getTesters().get(0);
        assertThat(actual.getUserName()).isNotBlank();
        assertThat(actual.getExperience()).isGreaterThan(0);

        assertThat(secondSearch.getTesters().containsAll(firstSearch.getTesters())).isTrue();
        assertThat(firstSearch.getTesters().containsAll(secondSearch.getTesters())).isTrue();
    }

    private String getAbsolutePath(String resourcePath) throws URISyntaxException {
        URL resource = TesterMatcherTest.class.getResource(resourcePath);
        URI uri = resource.toURI();
        Path path = Paths.get(uri);
        File file = path.toFile();
        return file.getAbsolutePath();
    }
}
