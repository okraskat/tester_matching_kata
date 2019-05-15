package io.github.okraskat.tester.matcher;

import io.github.okraskat.tester.matcher.domain.Bug;
import io.github.okraskat.tester.matcher.domain.Device;
import io.github.okraskat.tester.matcher.domain.Tester;
import io.github.okraskat.tester.matcher.repository.BugRepository;
import io.github.okraskat.tester.matcher.repository.DeviceRepository;
import io.github.okraskat.tester.matcher.repository.TesterRepository;
import io.github.okraskat.tester.matcher.search.FoundTester;
import io.github.okraskat.tester.matcher.search.FoundTesters;
import io.github.okraskat.tester.matcher.search.SearchCriteria;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class TesterMatcher {
    private final TesterRepository testerRepository;
    private final DeviceRepository deviceRepository;
    private final BugRepository bugRepository;

    public TesterMatcher(TesterRepository testerRepository, DeviceRepository deviceRepository, BugRepository bugRepository) {
        this.testerRepository = testerRepository;
        this.deviceRepository = deviceRepository;
        this.bugRepository = bugRepository;
    }

    public FoundTesters match(SearchCriteria searchCriteria) {
        List<Tester> testers = findTesters(searchCriteria);
        List<Device> devices = findDevices(searchCriteria);
        List<Tester> testersWithDevices = filterTestersWithDevices(testers, devices);
        Map<Tester, List<Bug>> testersBugs = findTestersBugs(testersWithDevices);
        List<FoundTester> foundTesters = convertResults(testersBugs);
        return new FoundTesters(foundTesters);
    }

    private List<Tester> findTesters(SearchCriteria searchCriteria) {
        List<Tester> testers;
        if (searchCriteria.searchAllCountries()) {
            testers = testerRepository.findAll();
        } else {
            testers = testerRepository.findByCountries(searchCriteria.getCountries());
        }
        return testers;
    }

    private List<Device> findDevices(SearchCriteria searchCriteria) {
        List<Device> devices;
        if (searchCriteria.searchAllDevices()) {
            devices = deviceRepository.findAll();
        } else {
            devices = deviceRepository.findByDescriptions(searchCriteria.getDevices());
        }
        return devices;
    }

    private Map<Tester, List<Bug>> findTestersBugs(List<Tester> testersWithDevices) {
        Map<Tester, List<Bug>> testersBugs = new HashMap<>(testersWithDevices.size());
        testersWithDevices.forEach(t -> {
            List<Bug> testerBugs = findTesterBugs(t);
            if (!testerBugs.isEmpty()) {
                testersBugs.put(t, testerBugs);
            }
        });
        return testersBugs;
    }

    private List<Bug> findTesterBugs(Tester tester) {
        return bugRepository.findByTesterAndDevices(tester.getId(), tester.getDevices());
    }

    private List<Tester> filterTestersWithDevices(List<Tester> testers, List<Device> devices) {
        Set<Long> devicesIds = devices.stream()
                .map(Device::getId)
                .collect(Collectors.toSet());
        return testers.stream()
                .filter(t -> t.getDevices().stream().anyMatch(devicesIds::contains))
                .map(t -> new Tester(t.getId(), t.getFirstName(), t.getLastName(), t.getCountry(), filterInterestedDevices(t.getDevices(), devicesIds)))
                .collect(Collectors.toList());
    }

    private Set<Long> filterInterestedDevices(Set<Long> testerDevices, Set<Long> searchedDevices) {
        Set<Long> mutualDevices = new HashSet<>(testerDevices);
        mutualDevices.retainAll(searchedDevices);
        return mutualDevices;
    }

    private List<FoundTester> convertResults(Map<Tester, List<Bug>> testersBugs) {
        return testersBugs
                .entrySet()
                .stream()
                .map(this::toFoundTester)
                .sorted(Comparator.comparingInt(FoundTester::getExperience).reversed())
                .collect(Collectors.toList());
    }

    private FoundTester toFoundTester(Map.Entry<Tester, List<Bug>> e) {
        FoundTester foundTester = new FoundTester();
        foundTester.setUserName(e.getKey().getUserName());
        foundTester.setExperience(e.getValue().size());
        return foundTester;
    }
}
