package cz.todr.piglatin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cz.todr.piglatin.PigLatinWordConwerter.PUNTUATIONS;

public class PigLatinImpl implements PigLatin {

    private static final Logger LOG = LoggerFactory.getLogger(PigLatinImpl.class);

    /* It is used to split a paragraph into words (1st group) and characters between words (2nd group). */
    private static final Pattern PATTERN = Pattern.compile("([\\w"  + PUNTUATIONS + "]+)" +
                                                           "([^\\w" + PUNTUATIONS + "]*)");

    private PigLatinWordConwerter wordConverter = new PigLatinWordConwerter();

    @Override
    public String convert(String orig) {
        LOG.debug("Converting: {}", orig);

        String converted = convertAllWords(orig, wordConverter::convertSingleWord);

        LOG.debug("Result: {}", converted);
        return converted ;
    }

    private String convertAllWords(String orig, Function<String, String> pigLatinWordConverter) {
        if (orig == null || orig.isEmpty()) {
            LOG.trace("Input was null or empty");
            return "";
        }

        StringBuilder converted = new StringBuilder();

        Matcher matcher = PATTERN.matcher(orig);
        while (matcher.find()) {
            String word = matcher.group(1);
            String rest = matcher.group(2);

            LOG.trace("Processing word='{}' and '{}'", word, rest);
            converted.append(pigLatinWordConverter.apply(word)).append(rest);
        }
        return converted.toString();
    }

}
