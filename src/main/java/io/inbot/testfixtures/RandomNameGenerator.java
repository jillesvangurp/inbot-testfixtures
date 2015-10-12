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

public class RandomNameGenerator {

    private final List<String> firstNames=new ArrayList<>();
    private final List<String> lastNames=new ArrayList<>();

    int firstNameIndex=0;
    int lastNameIndex=0;

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
        return firstNames.get((firstNameIndex++ % firstNames.size()));
    }

    public String nextLastName() {
        return lastNames.get((lastNameIndex++ % lastNames.size()));
    }
}
