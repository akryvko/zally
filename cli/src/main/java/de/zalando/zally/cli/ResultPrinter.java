package de.zalando.zally.cli;

import de.zalando.zally.cli.domain.Rule;
import de.zalando.zally.cli.domain.Violation;
import de.zalando.zally.cli.domain.ViolationType;
import de.zalando.zally.cli.domain.ViolationsCount;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

/**
 * Display violations in a user friendly manner.
 */
public class ResultPrinter {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    private final Writer writer;

    public ResultPrinter(OutputStream outputStream) {
        writer = new OutputStreamWriter(outputStream);
    }

    public void printMessage(String message) throws IOException {
        if (!message.isEmpty()) {
            printHeader(ANSI_CYAN, "Server message");
            writer.write(message + "\n\n");
            writer.flush();
        }
    }

    public void printViolations(List<Violation> violations, ViolationType violationType) throws IOException {
        if (!violations.isEmpty()) {
            String header = String.format("Found the following %s violations", violationType.name());
            String headerColor = getHeaderColor(violationType);

            printHeader(headerColor, header);

            for (Violation violation : violations) {
                writer.write(formatViolation(headerColor, violation) + "\n");
            }
            writer.flush();
        }
    }

    public void printRules(List<Rule> rules) throws IOException {
        printHeader(ANSI_CYAN, "Supported Rules");

        for (Rule rule : rules) {
            writer.write(formatRule(rule) + "\n");
        }

        writer.flush();
    }

    public void printSummary(ViolationsCount counters) throws IOException {
        printHeader(ANSI_CYAN, "Summary:");
        for (ViolationType violationType : ViolationType.values()) {
            String name = violationType.name();
            writer.write(name + " violations: " + counters.get(name.toLowerCase()) + "\n");
        }
        writer.flush();
    }

    public void printHeader(String ansiColor, String header) throws IOException {
        String headerUnderline = new String(new char[header.length()]).replace("\0", "=");
        writer.write(ansiColor + "\n" + header + "\n" + headerUnderline + "\n\n" + ANSI_RESET);
        writer.flush();
    }

    public static String formatViolation(String headerColor, Violation violation) {
        final String title = violation.getTitle();
        final String description = violation.getDescription();
        final List<String> paths = violation.getPaths();

        StringBuilder sb = new StringBuilder();
        sb.append(headerColor + title + "\n" + ANSI_RESET);
        sb.append("\t" + description + "\n");

        String ruleLink = violation.getRuleLink();
        if (ruleLink != null) {
            sb.append("\t" + ANSI_CYAN + ruleLink + "\n" + ANSI_RESET);
        }
        if (!paths.isEmpty()) {
            sb.append("\tViolated at:\n");
            for (String path : paths) {
                sb.append("\t\t");
                sb.append(path);
                sb.append("\n");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static String formatRule(Rule rule) {
        final String color = rule.isActive() ? ANSI_GREEN : ANSI_RED;

        StringBuilder sb = new StringBuilder();
        sb.append(color + rule.getCode() + ANSI_RESET + " ");
        sb.append(rule.getType() + " ");
        sb.append(rule.getTitle() + "\n");
        sb.append("\t(" + rule.getUrl() + ")");

        return sb.toString();
    }

    private String getHeaderColor(ViolationType violationType) {
        switch (violationType) {
            case MUST:
                return ANSI_RED;
            case SHOULD:
                return ANSI_YELLOW;
            case MAY:
                return ANSI_GREEN;
            case HINT:
                return ANSI_CYAN;
            default:
                return ANSI_CYAN;
        }
    }
}
