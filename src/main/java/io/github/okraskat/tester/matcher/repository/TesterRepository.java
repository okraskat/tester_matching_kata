package io.github.okraskat.tester.matcher.repository;

import io.github.okraskat.tester.matcher.domain.Tester;

import java.util.List;
import java.util.Set;

public interface TesterRepository {

    List<Tester> findAll();

    List<Tester> findByCountries(Set<String> countries);
}
