package it.polimi.ingsw.util.cliparser;

import java.io.PrintStream;

/**
 * Printer of the usage help message for CLI parser.
 */
public class HelpPrinter {
    /**
     * Suffix string for options with arguments.
     */
    private static final String ARG = " <value>";
    /**
     * Prefix string for all options.
     */
    private static final String PREFIX = "--";
    /**
     * Spacing between longest option and description.
     */
    private static final int SPACING = 2;

    /**
     * HelpPrinter should not be constructed.
     */
    private HelpPrinter() {}

    /**
     * Prints an usage message for the given options.
     * @param options The given options.
     */
    public static void print(Options options) {
        PrintStream writer = new PrintStream(System.err);

        // Calculate the length of the left side in order to align the descriptions
        int lengthLeftSide = 0;
        for (Option opt : options.getOptions()) {
            int length = opt.getName().length() + PREFIX.length();
            if (opt.hasArgument()) {
                length += ARG.length();
            }
            lengthLeftSide = Math.max(lengthLeftSide, length);
        }

        // Add spacing to left side
        lengthLeftSide += SPACING;

        // Print usage help message
        writer.println("Usage:");
        for (Option opt : options.getOptions()) {
            StringBuilder output = new StringBuilder();
            output.append(PREFIX).append(opt.getName());
            if (opt.hasArgument()) {
                output.append(ARG);
            }
            while(output.length() < lengthLeftSide) {
                output.append(' ');
            }
            output.append(opt.getDescription());
            writer.println(output);
        }
        writer.flush();
    }

    /**
     * Prints an error message for the given options.
     * @param options The given options.
     * @param error The error message to print.
     */
    public static void printWithError(Options options, String error) {
        print(options);
        PrintStream writer = new PrintStream(System.err);
        writer.println();
        writer.println(error);
        writer.flush();
    }
}
