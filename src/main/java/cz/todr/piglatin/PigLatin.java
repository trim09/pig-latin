package cz.todr.piglatin;

import java.util.stream.Stream;

public interface PigLatin {

    /**
     * Converts a word or paragraph represented by {@code orig} into stream of Strings in "pig-latin"
     * @param orig A string to be converted
     * @return Converted string
     */
    String convert(String orig);

}
