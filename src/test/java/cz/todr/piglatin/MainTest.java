package cz.todr.piglatin;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

public class MainTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void cleanUpStreams() {
        System.setOut(null);
        System.setErr(null);
    }

    @Test
    public void main() throws Exception {
        File file = File.createTempFile( "some-prefix", "some-ext");
        file.deleteOnExit();
        addStringToFile(file, "hello apple");

        String[] filePath = {file.toPath().toString()};

        Main.main(filePath);
        assertEquals("ellohay appleway", outContent.toString().trim());
    }

    private void addStringToFile(File file, String content) throws FileNotFoundException {
        PrintWriter fileStream = new PrintWriter(file);
        fileStream.append(content);
        fileStream.close();
    }

    @Test
    public void invalidFile() throws Exception {
        String[] filePath = {"doesntexist"};

        Main.main(filePath);
        assertTrue(!errContent.toString().isEmpty());
    }

}