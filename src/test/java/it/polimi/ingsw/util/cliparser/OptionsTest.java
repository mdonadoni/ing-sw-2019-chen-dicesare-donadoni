package it.polimi.ingsw.util.cliparser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OptionsTest {

    Options options;

    @BeforeEach
    void setUp() {
        options = new Options();
    }

    @Test
    void addOptionThrowsOnDuplicate() {
        options.addOption("opt1", false, "opt1");
        assertThrows(CLIParserException.class, () -> options.addOption("opt1", true, "asd"));
    }

    @Test
    void hasOption() {
        options.addOption("opt1", false, "opt1");
        assertEquals(true, options.hasOption("opt1"));
    }
}