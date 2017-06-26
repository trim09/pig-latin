package cz.todr.piglatin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Main {

  private static final Logger LOG = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) {
    if (args.length != 1) {
      System.err.println("USAGE: java -jar pig-latin.jar <path_to_file>");
      return;
    }

    Path path = Paths.get(args[0]);

    convertFromFile(path);
  }

  private static void convertFromFile(Path path) {
    PigLatin pigLatin = new PigLatinImpl();

    try (Stream<String> lines = Files.lines(path)) {

      lines.forEach(line -> System.out.println(pigLatin.convert(line)));

    } catch (MalformedInputException e) {
      LOG.error("Could not parse the input file. Is it UTF-8 encoded?", e);
    } catch (IOException e) {
      LOG.error("Could not parse the input file.", e);
    }
  }

}




