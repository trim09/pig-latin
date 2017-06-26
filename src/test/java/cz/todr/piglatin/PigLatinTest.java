package cz.todr.piglatin;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class PigLatinTest {

    @Parameterized.Parameters(name="{index}: {0} -> {1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                /* requirements */
                { "hello", "ellohay"},
                { "apple", "appleway"},
                { "stairway", "stairway"},
                { "can’t", "antca’y"},
                { "end", "endway"},
                { "this-thing", "histay-hingtay"},
                { "Beach", "Eachbay"},
                { "McCloud", "CcLoudmay"},
                { "Hello", "Ellohay"},

                /* null safety */
                { null, ""},
                { "", ""},

                /* extra */
                { "h", "hay"},
                { "o", "oway"},

                /* extra uppercase */
                { "hellO", "elloHay"},
                { "applE", "applEway"},
                { "stairwaY", "stairwaY"},

                /* another types of apostrophe */
                { "can't", "antca'y"},
                { "can‘t", "antca‘y"},

                /* paragraph */
                { "hello apple", "ellohay appleway"},
        });
    }

    /* Object under test */
    private static PigLatin pigLatin;

    private String input;

    private String output;


    public PigLatinTest(String input, String output) {
        this.input = input;
        this.output = output;
    }

    @BeforeClass
    public static void setUp() {
        pigLatin = new PigLatinImpl();
    }

    @Test
    public void test() {
        assertEquals(output, pigLatin.convert(input));
    }

}