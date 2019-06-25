package it.polimi.ingsw.util.cliparser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OptionsParserTest {
    Options options;
    OptionsParser parser = new OptionsParser();

    @BeforeEach
    void setUp() {
        options = new Options();
        options.addOption("opt1", true, "opt1");
        options.addOption("opt2", false, "opt2");
    }

    @Test
    void partialOptions() {
        ParsedOptions parsed = parser.parse(options, new String[]{"--opt2"});
        assertFalse(parsed.hasOption("opt1"));
        assertTrue(parsed.hasOption("opt2"));
    }

    @Test
    void optionWithoutArgumentThrows() {
        assertThrows(CLIParserException.class, () -> parser.parse(options, new String[]{"--opt1"}));
    }

    @Test
    void optionWithArgument() {
        ParsedOptions parsed = parser.parse(options, new String[]{"--opt1", "lol"});
        assertEquals("lol", parsed.getOptionValue("opt1"));
    }
}