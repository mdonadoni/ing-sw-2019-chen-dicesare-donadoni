package it.polimi.ingsw.util.cliparser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that represents a set of options.
 */
public class Options {
    /**
     * Map between option's name and actual Option object.
     */
    private Map<String, Option> options = new HashMap<>();

    /**
     * Add new option.
     * @param option Option to be added.
     */
    public void addOption(Option option) {
        if (options.containsKey(option.getName())) {
            throw new CLIParserException("Cannot have two options with same name");
        }
        options.put(option.getName(), option);
    }

    /**
     * Construct and add a new Option.
     * @param name Name of the option.
     * @param hasArgument Whether the option needs an argument.
     * @param description Description of the option.
     */
    public void addOption(String name, boolean hasArgument, String description) {
        addOption(new Option(name, hasArgument, description));
    }

    /**
     * Check if this has an option with given name.
     * @param name Name of the option to be checked.
     * @return True if the option with the given name is present, false otherwise.
     */
    boolean hasOption(String name) {
        return options.containsKey(name);
    }

    /**
     * Get option with given name.
     * @param name Name of the option.
     * @return Option with given name.
     */
    Option getOption(String name) {
        return options.get(name);
    }

    /**
     * Get all the options.
     * @return List with all the options.
     */
    List<Option> getOptions() {
        return new ArrayList<>(options.values());
    }
}
