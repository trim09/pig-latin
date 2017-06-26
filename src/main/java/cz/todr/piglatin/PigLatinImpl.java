package cz.todr.piglatin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class PigLatinImpl implements PigLatin {

    private static final Logger LOG = LoggerFactory.getLogger(PigLatinImpl.class);

    private static final String vowels = "aeiou"; /* TODO and 'y' ? */


    private boolean isVowel(char c) {
        return vowels.indexOf(Character.toLowerCase(c)) >= 0;
    }

    private int[] uppercasePositions(String word) {
        return IntStream.range(0, word.length())
                .filter(i -> Character.isUpperCase(word.charAt(i)))
                .toArray();
    }

    private String firstLetterAndSuffix(String word) {
        /* Words that end in “way” are not modified.*/
        if (word.endsWith("way")) {
            return word;
        }

        /* Words that start with a vowel have the letters “way” added to the end. */
        if (isVowel(word.charAt(0))) {
            return word + "way";
        } else {
            /* Words that start with a consonant have their first letter moved to the end of the word and
            the letters “ay” added to the end.*/
            return word.substring(1) + Character.toLowerCase(word.charAt(0)) + "ay";
        }
    }

    private String convertSingleWord(String word) {
        if (word.isEmpty()) {
            return word;
        }

        int[] uppercasePositions = uppercasePositions(word);
        Integer punctuationPosition = punctuationPosition(word);

        String result = word;

        if (punctuationPosition != null) {
            result = removeCharAt(result, punctuationPosition);
        }
        result = result.toLowerCase();

        result = firstLetterAndSuffix(result);

        result = fixUppercase(result, uppercasePositions);
        result = addPunctuation(result, punctuationPosition, word.length());

        return result;


//        return "";
    }

    private String removeCharAt(String word, int index) {
        return new StringBuilder(word).deleteCharAt(index).toString();
    }

    private String addPunctuation(String word, Integer punctuationPosition, int origLength) {
        if (punctuationPosition == null) {
            return word;
        }

        int fromEndInOrig = origLength - (punctuationPosition + 1);

        int currentPosition = word.length() - fromEndInOrig;

        return word.substring(0, currentPosition) + '\'' + word.substring(currentPosition);
    }

    private Integer punctuationPosition(String word) {
        int position = word.lastIndexOf('\'');

        if (position < 0) {
            position = word.lastIndexOf('’');
        }

        if (position < 0) {
            position = word.lastIndexOf('‘');
        }
        return position < 0 ? null : position;
    }

    private String fixUppercase(String word, int[] uppercasePositions) {
        for (int pos : uppercasePositions) {
            String prefix = word.substring(0, pos);
            String suffix = word.substring(pos + 1, word.length());

            word = prefix + Character.toUpperCase(word.charAt(pos)) + suffix;
        }

        return word;
    }






    @Override
    public String convert(String orig) {
        LOG.info("converting: {}", orig);

        if (orig == null || orig.isEmpty()) {
            return "";
        }

        StringBuilder result = new StringBuilder();


        Pattern pattern = Pattern.compile("([\\w'‘’]+)([^\\w'‘’]*)");
        Matcher matcher = pattern.matcher(orig);

        while (matcher.find()) {
            String word = matcher.group(1);
            String rest = matcher.group(2);

            result.append(convertSingleWord(word)).append(rest);
            System.out.println("word=" + word + "    rest:" + rest);

        }

        return result.toString();
    }
}
