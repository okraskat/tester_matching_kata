package io.github.okraskat.tester.matcher.search;

import java.util.Collections;
import java.util.List;

public class FoundTesters {
    private final List<FoundTester> testers;

    public FoundTesters(List<FoundTester> testers) {
        this.testers = testers;
    }

    public List<FoundTester> getTesters() {
        return Collections.unmodifiableList(testers);
    }
}
