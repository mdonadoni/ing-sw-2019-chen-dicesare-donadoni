package it.polimi.ingsw.util.cliparser;

/**
 * Class that represents a single CLI option.
 */
public class Option {
    /**
     * Name of the option.
     */
    private String name;
    /**
     * Whether this option has an argument.
     */
    private boolean hasArgument;
    /**
     * Description of this option.
     */
    private String description;

    /**
     * Constructor of Option.
     * @param name Name of the option.
     * @param hasArgument Whether this option has an argument or not.
     * @param description Description of the option.
     */
    public Option(String name, boolean hasArgument, String description) {
        this.name = name;
        this.hasArgument = hasArgument;
        this.description = description;
    }

    /**
     * Get name of option.
     * @return Name of option.
     */
    public String getName() {
        return name;
    }

    /**
     * Get whether this Option has an argument.
     * @return True if this option needs an argument, false otherwise.
     */
    public boolean hasArgument() {
        return hasArgument;
    }

    /**
     * Get the description of the option
     * @return Description of the option.
     */
    public String getDescription() {
        return description;
    }
}
