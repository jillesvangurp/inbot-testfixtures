package io.inbot.testfixtures;

import static org.assertj.core.api.StrictAssertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Test
public class RandomNameGeneratorTest {

    private RandomNameGenerator randomNameGenerator;

    @BeforeMethod
    public void before() {
        randomNameGenerator = new RandomNameGenerator(System.currentTimeMillis());
    }

    public void shouldGenerateRandomNamesWithoutDuplicates() {
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
        Set<String> seen=new HashSet<>();
        int count=0;
        while(count < 1000000) {
            String name = randomNameGenerator.nextFirstName() + " " + randomNameGenerator.nextLastName();
            if(seen.contains(name)) {
                throw new IllegalStateException("this should not happen the first 1M combinations");
            } else {
                seen.add(name);
            }
            count++;
        }
    }

}
