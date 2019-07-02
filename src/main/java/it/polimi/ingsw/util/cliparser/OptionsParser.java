package it.polimi.ingsw.util.cliparser;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Parser for the command line arguments.
 */
public class OptionsParser {
    /**
     * Parse the given options from the given command line arguments.
     * @param options Options to be parsed.
     * @param args Command line arguments.
     * @return Options parsed from the given command line arguments.
     */
    public ParsedOptions parse(Options options, String[] args) {
        Iterator<String> argsIter = Arrays.asList(args).iterator();
        ParsedOptions parsed = new ParsedOptions();

        while (argsIter.hasNext()) {
            // Find option name
            String optionName = getOptionName(argsIter.next());

            if (!options.hasOption(optionName)) {
                throw new CLIParserException("Option not found: " + optionName);
            }

            Option option = options.getOption(optionName);

            if (option.hasArgument()) {
                if (!argsIter.hasNext()) {
                    throw new CLIParserException("Option has no argument: " + optionName);
                }
                String nextToken = argsIter.next();
                if (isOption(nextToken)) {
                    throw new CLIParserException("Option has no argument: " + optionName);
                }
                parsed.addOption(option, nextToken);
            } else {
                parsed.addOption(option);
            }
        }

        // Check mutually exclusive
        for (Option[] group : options.getExclusiveGroups()) {
            int count = 0;
            for (Option opt : group) {
                if (parsed.hasOption(opt)) {
                    count++;
                }
            }
            if (count > 1) {
                throw new CLIParserException("Cannot have more than one exclusive option");
            }
        }

        return parsed;
    }

    private boolean isOption(String token) {
        return token.startsWith("-") && !token.startsWith("---");
    }

    private String getOptionName(String token) {
        if (!isOption(token)) {
            throw new CLIParserException("Invalid option: " + token);
        } else if (token.startsWith("--")) {
            return token.substring(2);
        } else {
            return token.substring(1);
        }
    }
}
