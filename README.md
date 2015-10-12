# Inbot Testfixtures

This library uses two csvs with first and last names to implement a simple pseudo random name generator for names that generates human readable names without generating duplicate first name last name combinations until you have generated millions of first and last name combinations.

To use simply instantiate with a seed.

```
RandomNameGenerator randomNameGenerator = new RandomNameGenerator(666);
String first = randomNameGenerator.nextFirstName();
String last = randomNameGenerator.nextLastName();
```

# How it works

The library loads the csvs in memory and shuffles the two lists using the seed. It maintains an index to cycle through the lists so it is guaranteed to not generate duplicate names until it runs through the entire list. After that, it simply cycles through from the beginning.

With 5000+ first names and 88000+ last names that means you get at leaast 88000 unique first name last name combinations before it starts recycling the names. However, because the list lengths are different, you merely get different combinations of the same names after that. In practice, you can generate many millions of unique first name last name combinations before encountering duplicates.
