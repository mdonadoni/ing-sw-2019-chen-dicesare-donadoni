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
    private Map<String, Option> optionsMap = new HashMap<>();
    /**
     * List of groups of options mutually exclusive.
     */
    private List<Option[]> exclusiveGroups = new ArrayList<>();

    /**
     * Check whether the given option can be added or not.
     * @param opt Option to be checked
     * @return True if it can be added, false otherwise.
     */
    private boolean canAdd(Option opt) {
        return !optionsMap.containsKey(opt.getName());
    }

    /**
     * Add new option.
     * @param option Option to be added.
     */
    public void addOption(Option option) {
        if (!canAdd(option)) {
            throw new CLIParserException("Cannot have two options with same name");
        }
        optionsMap.put(option.getName(), option);
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
     * Add options that need to be mutually exclusive.
     * @param options Options mutually exclusive.
     */
    public void addMutuallyExclusiveOptions(Option ...options) {
        for (Option opt : options) {
            if (!canAdd(opt)) {
                throw new CLIParserException("Cannot add option " + opt.getName());
            }
        }

        exclusiveGroups.add(options);
        for (Option opt : options) {
            addOption(opt);
        }
    }

    /**
     * Check if this has an option with given name.
     * @param name Name of the option to be checked.
     * @return True if the option with the given name is present, false otherwise.
     */
    boolean hasOption(String name) {
        return optionsMap.containsKey(name);
    }

    /**
     * Get option with given name.
     * @param name Name of the option.
     * @return Option with given name.
     */
    Option getOption(String name) {
        return optionsMap.get(name);
    }

    /**
     * Get all the options.
     * @return List with all the options.
     */
    List<Option> getOptions() {
        return new ArrayList<>(optionsMap.values());
    }

    /**
     * Get list of groups of options mutually exclusive.
     * @return List of group of mutually exclusive options.
     */
    List<Option[]> getExclusiveGroups() {
        return exclusiveGroups;
    }
}
