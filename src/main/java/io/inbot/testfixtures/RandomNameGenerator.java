package io.inbot.testfixtures;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
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


            try(BufferedReader br = new BufferedReader(new InputStreamReader(RandomNameGenerator.class.getClassLoader().getResourceAsStream("firstnames.csv"), StandardCharsets.UTF_8))) {
                br.lines().forEach(line -> firstNames.add(line.trim()));
            }
            try(BufferedReader br = new BufferedReader(new InputStreamReader(RandomNameGenerator.class.getClassLoader().getResourceAsStream("lastnames.csv"), StandardCharsets.UTF_8))) {
                br.lines().forEach(line -> lastNames.add(line.trim()));
            }
            Random random = new Random(seed);
            Collections.shuffle(firstNames, random);
            Collections.shuffle(lastNames, random);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
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
