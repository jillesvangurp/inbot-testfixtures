# Inbot Testfixtures

This library uses csvs with companies, first and last names to implement a simple pseudo random name generator for names that generates human readable names for people and companies without generating duplicate names until you have generated millions of first and last name combinations.

This is perfect for unit tests where you want to guarantee that there are no duplicate names during the test execution.

# Install from maven cental

```xml
<dependency>
  <groupId>io.inbot</groupId>
  <artifactId>inbot-testfixtures</artifactId>
  <version>1.8</version>
</dependency>
```

# Usage

To use simply instantiate with a seed.

```
RandomNameGenerator randomNameGenerator = new RandomNameGenerator(666);
String first = randomNameGenerator.nextFirstName();
String last = randomNameGenerator.nextLastName();
String company = randomNameGenerator.nextCompanyName();
```

# How it works

The library loads the csvs in memory and shuffles the two lists using the seed. It maintains an index to cycle through the lists so it is guaranteed to not generate duplicate names until it runs through the entire list. After that, it simply cycles through from the beginning.

With 5000+ first names and 88000+ last names that means you get at least 88000 unique first name last name combinations before it starts recycling the names. However, because the list lengths are different, you merely get different combinations of the same names after that. In practice, you can generate many millions of unique first name last name combinations before encountering duplicates.

Because the RandomNameGenerator is instantiated with a seed, you can trigger the same order of names by re-instantiating with the same seed. This is important if you want to replay tests when they break.

# Release notes
- 1.8 some long overdue maintenance and close some issues
  - Finally add support for a Person object instead of an array.
  - Implement ability to re-seed using a new `shuffle` method
  - Migrate the project to gradle, update dependencies
- 1.7
  - Merge pull request with Turkish and other names.
- 1.6
  -  Username concatenated of Galen Ullrich ends up containing null as a substring. Fixed it with a simple replaceAll null -> n_ll.
- 1.5
  - Remove 'Null' and 'Nulle' as valid last names :-)
- 1.4
  - Remove the last name Ternullo due to the occurence of 'null' in this string. This messes up our null checks in the Inbot tests occasionally.
- 1.3
  - improve random email addresses by filtering out non alphanumeric characters
- 1.2
  - add support for random company names based on Nasdaq registered companies
- 1.1
  - resolve classloading issues with loading the csv
  - resolve concurrency issues with running the tests
- 1.0
  - initial release
