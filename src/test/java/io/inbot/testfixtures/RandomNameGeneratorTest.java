package io.inbot.testfixtures;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.Test;

@Test
public class RandomNameGeneratorTest {

    public void shouldGenerateRandomNamesWithoutDuplicates() {
        RandomNameGenerator randomNameGenerator = new RandomNameGenerator(System.currentTimeMillis());
        Set<String> seen=new HashSet<>();
        for(int i=0;i<1000;i++) {
            String first = randomNameGenerator.nextFirstName();
            seen.add(first);
        }
        assertThat(seen.size()).isEqualTo(1000);
        // note some first names are also last names :-) so clear the set and count separately
        seen.clear();
        for(int i=0;i<1000;i++) {
            String last = randomNameGenerator.nextLastName();
            seen.add(last);
        }
        assertThat(seen.size()).isEqualTo(1000);
    }

    public void shouldRecycleNamesWhenTheyRunOut() {
        RandomNameGenerator randomNameGenerator = new RandomNameGenerator(System.currentTimeMillis());
        Set<String> seen=new HashSet<>();
        for(int i=0;i<20000;i++) {
            seen.add(randomNameGenerator.nextFirstName());
        }
        assertThat(seen.size()).isLessThan(10000);
        assertThat(seen.size()).isGreaterThan(5000);
    }

    public void shouldGenerateTheSameNamesWithTheSameSeed() {
        RandomNameGenerator r1 = new RandomNameGenerator(42);
        RandomNameGenerator r2 = new RandomNameGenerator(42);
        for(int i=0;i<100;i++) {
            assertThat(r1.nextFirstName()).isEqualTo(r2.nextFirstName());
            assertThat(r1.nextLastName()).isEqualTo(r2.nextLastName());
        }
    }

    public void shouldGenerate1MUniqueNameCombinations() {
        RandomNameGenerator randomNameGenerator = new RandomNameGenerator(System.currentTimeMillis());
        Set<String> seen=new HashSet<>();
        int count=0;
        while(count < 1000000) {
            String firstName = randomNameGenerator.nextFirstName();
            String lastName = randomNameGenerator.nextLastName();
            if(firstName.equals(lastName)) {
                String name = firstName + " " + lastName;
                if(seen.contains(name)) {
                    throw new IllegalStateException("this should not happen the first 1M combinations but happened after " + count);
                } else {
                    seen.add(name);
                }
            }
            count++;
        }
    }

    public void shouldGenerateCompanyNames() {
        RandomNameGenerator randomNameGenerator = new RandomNameGenerator(System.currentTimeMillis());
        Set<String> seen=new HashSet<>();
        for(int i=0;i<1000;i++) {
            String name = randomNameGenerator.nextCompanyName();
            seen.add(name);
        }
        assertThat(seen.size()).isEqualTo(1000);
    }

    public void shouldGeneratePersonFields() {
        RandomNameGenerator randomNameGenerator = new RandomNameGenerator(System.currentTimeMillis());
        for(int i=0;i<1000;i++) {
            System.out.println(StringUtils.join(randomNameGenerator.nextPersonFields(), ','));
        }

    }
}
