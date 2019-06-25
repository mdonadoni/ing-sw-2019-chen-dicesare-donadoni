package it.polimi.ingsw.util.cliparser;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a set of options parsed from the command line arguments.
 */
public class ParsedOptions {
    /**
     * Map between option name and corresponding value.
     */
    private Map<String, String> values = new HashMap<>();
    /**
     * Map between option name and correspoing Option object.
     */
    private Map<String, Option> options = new HashMap<>();

    /**
     * Check whether the given option was parsed from the command line arguments.
     * @param option Option to be checked.
     * @return True if the option was parsed, false otherwise.
     */
    public boolean hasOption(Option option) {
        return option.equals(options.get(option.getName()));
    }

    /**
     * Check whether an option with given name was parsed from the command line arguments.
     * @param name Name of the option.
     * @return True if the option was parsed, false otherwise.
     */
    public boolean hasOption(String name) {
        return options.containsKey(name);
    }

    /**
     * Get value of option with given name.
     * @param name Name of the option.
     * @return The option's value or null if the option was not parsed or it doesn't
     * have a value.
     */
    public String getOptionValue(String name) {
        return values.get(name);
    }

    /**
     * Get value of given option.
     * @param option Option whose value is asked.
     * @return The option's value or null if the option was not parsed or it doesn't
     * have a value.
     */
    public String getOptionValue(Option option) {
        return getOptionValue(option.getName());
    }

    /**
     * Add parsed option and value.
     * @param option Parsed option.
     * @param value Value of the parsed option.
     */
    void addOption(Option option, String value) {
        if (options.containsKey(option.getName())) {
            throw new CLIParserException("Option with same name already parsed");
        }
        options.put(option.getName(), option);
        if (value != null) {
            values.put(option.getName(), value);
        }
    }

    /**
     * Add parsed option without value.
     * @param option Parsed option.
     */
    void addOption(Option option) {
        addOption(option, null);
    }
}
