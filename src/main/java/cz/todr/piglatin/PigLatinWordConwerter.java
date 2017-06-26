package cz.todr.piglatin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.IntStream;

class PigLatinWordConwerter {

    private static final Logger LOG = LoggerFactory.getLogger(PigLatinImpl.class);

    static final String PUNTUATIONS = "'‘’"; /* various types of punctuations */

    private static final String VOWELS = "aeiou"; /* and 'y' ? */

    private static final String SUFFIX_WAY = "way";

    private static final String AY = "ay";

    private static class Punctuation {
        private final int position;
        private final char punctuationChar;

        private Punctuation(int position, char punctuationChar) {
            this.position = position;
            this.punctuationChar = punctuationChar;
        }
    }

    String convertSingleWord(String word) {
        if (word.isEmpty()) {
            return "";
        }

        int[] uppercasePositions = getUppercasePositions(word);
        Punctuation punctuation = getPunctuation(word);

        String lowerCaseOriginal = word.toLowerCase();

        StringBuilder result = new StringBuilder(lowerCaseOriginal);

        removePunctuation(result, punctuation);
        firstLetterAndSuffix(result);
        addPunctuation(result, punctuation, word.length());
        fixUppercase(result, uppercasePositions);

        return result.toString();
    }

    private int[] getUppercasePositions(String word) {
        return IntStream.range(0, word.length())
                .filter(i -> Character.isUpperCase(word.charAt(i)))
                .toArray();
    }

    private void removePunctuation(StringBuilder word, Punctuation punctuation) {
        if (punctuation != null) {
            word.deleteCharAt(punctuation.position);
        }
    }

    private Punctuation getPunctuation(String word) {
        for (char p : PUNTUATIONS.toCharArray()) {
            int position = word.indexOf(p);
            if (position >= 0) {
                return new Punctuation(position, p);
            }
        }

        return null;
    }

    private void addPunctuation(StringBuilder word, Punctuation punctuation, int origLength) {
        if (punctuation != null) {
            int fromEndInOrig = origLength - (punctuation.position + 1);
            int currentPosition = word.length() - fromEndInOrig;
            word.insert(currentPosition, punctuation.punctuationChar);
        }
    }

    private void fixUppercase(StringBuilder word, int[] uppercasePositions) {
        for (int pos : uppercasePositions) {
            word.setCharAt(pos, Character.toUpperCase(word.charAt(pos)));
        }
    }

    private void firstLetterAndSuffix(StringBuilder word) {
        boolean isWaySuffix = isWaySuffix(word);

        if (!isWaySuffix) {
            if (isVowel(word.charAt(0))) {
                /* Words that start with a vowel have the letters “way” added to the end. */
                word.append(SUFFIX_WAY);
            } else {
                /* Words that start with a consonant have their first letter moved to the end of the word and
                the letters “ay” added to the end.*/
                char firstChar = word.charAt(0);
                word.deleteCharAt(0).append(firstChar).append(AY);
            }
        } /* else Words that end in “way” are not modified.*/
    }

    private boolean isWaySuffix(StringBuilder word) {
        if (word.length() >= SUFFIX_WAY.length()) {
            int lastThreeChars = word.length() - SUFFIX_WAY.length();
            String suffix = word.substring(lastThreeChars).toLowerCase();
            return SUFFIX_WAY.equals(suffix);
        } else {
            return false;
        }
    }

    private boolean isVowel(char c) {
        return VOWELS.indexOf(Character.toLowerCase(c)) >= 0;
    }
}
