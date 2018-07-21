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

/**
 * Simple name generator for first name, last name that generates unique names using a csv file.
 *
 * The number of unique first and last name combinations ranges well into the millions which means you have to generate
 * millions before you start encountering
 * duplicates. This property, makes this generator ideal as a test fixture for unit and integration tests.
 *
 * The random seed can be controlled via the constructor of this class. This ensures that you can regenerate the same
 * sequence of names if you use the same seed.
 *
 * Limitations: the names are English and do not contain any special characters currently. I welcome pull requests to address this obvious limitation.
 */
public class RandomNameGenerator {

    // use synchronizedList to avoid concurrency issues triggering premature duplicates (this actually happened with concurrent test execution)
    private final List<String> firstNames;
    private final List<String> lastNames;
    private final List<String> companies;

    // use AtomicInteger so multiple threads can use this without getting the same index
    private final AtomicInteger firstNameIndex=new AtomicInteger(0);
    private final AtomicInteger lastNameIndex=new AtomicInteger(0);
    private final AtomicInteger companyIndex=new AtomicInteger(0);

    public RandomNameGenerator(long seed) {
        try {
            firstNames=loadNames("inbot-testfixtures/firstnames.csv");
            lastNames=loadNames("inbot-testfixtures/lastnames.csv");
            companies=loadNames("inbot-testfixtures/companies.csv");

            Random random = new Random(seed);
            Collections.shuffle(firstNames, random);
            Collections.shuffle(lastNames, random);
            Collections.shuffle(companies, random);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private List<String> loadNames(String resource) throws IOException {
        List<String> names = Collections.synchronizedList(new ArrayList<>());
        // use classloader that loaded the jar with this class to ensure we can get the csvs
        try(BufferedReader br = new BufferedReader(new InputStreamReader(RandomNameGenerator.class.getClassLoader().getResourceAsStream(resource), StandardCharsets.UTF_8))) {
            br.lines().map(String::trim).filter(line -> line.length()>0).forEach(names::add);
        }
        return names;
    }

    public String nextFirstName() {
        int counter = firstNameIndex.incrementAndGet();
        int index = counter % firstNames.size();
        return firstNames.get(index);
    }

    public String nextLastName() {
        return lastNames.get((lastNameIndex.incrementAndGet() % lastNames.size()));
    }

    public String nextCompanyName() {
        return companies.get((companyIndex.incrementAndGet() % companies.size()));
    }

    public String nextFullName() {
        return nextFirstName() + " " + nextLastName();
    }

    /**
     * Useful to generate random sets of person fields and some derived fields.
     * @return array of first name, lastname, company name, domain name, email address
     */
    @Deprecated // use getNextPerson, keeping this because of legacy tests
    public String[] nextPersonFields() {
        Person person = getNextPerson();
        return new String[] {person.getFirstName(),person.getLastName(),person.getCompany(),person.getDomainName(),person.email()};
    }

    public Person getNextPerson() {
        return new Person(nextFirstName(), nextLastName(), nextCompanyName());
    }
}
