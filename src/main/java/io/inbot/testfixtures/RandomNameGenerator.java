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

    private List<String> firstNames;
    private List<String> lastNames;
    private List<String> companies;
    private final List<String> firstNamesOriginal;
    private final List<String> lastNamesOriginal;
    private final List<String> companiesOriginal;

    // use AtomicInteger so multiple threads can use this without getting the same index
    private final AtomicInteger firstNameIndex=new AtomicInteger(0);
    private final AtomicInteger lastNameIndex=new AtomicInteger(0);
    private final AtomicInteger companyIndex=new AtomicInteger(0);

    public RandomNameGenerator(List<String> firstNames, List<String> lastNames, List<String> companies) {
        firstNamesOriginal=firstNames;
        lastNamesOriginal=lastNames;
        companiesOriginal=companies;
        shuffle(new Random());
    }

    public RandomNameGenerator(Random random) {
        this(loadNames("inbot-testfixtures/firstnames.csv"),loadNames("inbot-testfixtures/lastnames.csv"),loadNames("inbot-testfixtures/companies.csv"));
        shuffle(random);
    }

    public RandomNameGenerator(long seed) {
        this(loadNames("inbot-testfixtures/firstnames.csv"),loadNames("inbot-testfixtures/lastnames.csv"),loadNames("inbot-testfixtures/companies.csv"));
        shuffle(new Random(seed));
    }

    public void shuffle(Random random) {
        // use synchronizedList to avoid concurrency issues triggering premature duplicates (this actually happened with concurrent test execution)
        firstNames = Collections.synchronizedList(new ArrayList<>(firstNamesOriginal));
        Collections.shuffle(firstNames, random);
        firstNameIndex.set(0);
        lastNames = Collections.synchronizedList(new ArrayList<>(lastNamesOriginal));
        Collections.shuffle(lastNames, random);
        lastNameIndex.set(0);
        companies = Collections.synchronizedList(new ArrayList<>(companiesOriginal));
        Collections.shuffle(companies, random);
        companyIndex.set(0);
    }

    private static List<String> loadNames(String resource) {
        List<String> names = new ArrayList<>();
        // use classloader that loaded the jar with this class to ensure we can get the csvs
        try(BufferedReader br = new BufferedReader(new InputStreamReader(RandomNameGenerator.class.getClassLoader().getResourceAsStream(resource), StandardCharsets.UTF_8))) {
            br.lines().map(String::trim).filter(line -> line.length()>0).forEach(names::add);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return Collections.unmodifiableList(names);
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
        Person person = nextPerson();
        return new String[] {person.getFirstName(),person.getLastName(),person.getCompany(),person.getDomainName(),person.getEmail()};
    }

    public Person nextPerson() {
        return new Person(nextFirstName(), nextLastName(), nextCompanyName());
    }
}
