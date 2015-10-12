package io.inbot.testfixtures;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class RandomNameGenerator {

    // use synchronizedList to avoid concurrency issues triggering premature duplicates (this actually happened with concurrent test execution)
    private final List<String> firstNames=Collections.synchronizedList(new ArrayList<>());
    private final List<String> lastNames=Collections.synchronizedList(new ArrayList<>());

    // use AtomicInteger so multiple threads can use this without getting the same index
    private final AtomicInteger firstNameIndex=new AtomicInteger(0);
    private final AtomicInteger lastNameIndex=new AtomicInteger(0);

    public RandomNameGenerator(long seed) {
        try {
            Files.lines(getResourcePath("firstnames.csv")).forEach(line -> {
                firstNames.add(line.trim());
            });
            Files.lines(getResourcePath("lastnames.csv")).forEach(line -> {
                lastNames.add(line.trim());
            });
            Random random = new Random(seed);
            Collections.shuffle(firstNames, random);
            Collections.shuffle(lastNames, random);
        } catch (IOException | URISyntaxException e) {
            throw new IllegalStateException(e);
        }
    }

    private static Path getResourcePath(String resourceName) throws URISyntaxException {
        URL url = RandomNameGenerator.class.getClassLoader().getResource(resourceName);
        return Paths.get(url.toURI());
    }

    public String nextFirstName() {
        int counter = firstNameIndex.incrementAndGet();
        int index = counter % firstNames.size();
        return firstNames.get(index);
    }

    public String nextLastName() {
        return lastNames.get((lastNameIndex.incrementAndGet() % lastNames.size()));
    }
}
