package com.example.mdgfm.core;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.regex.Pattern;

public final class LineOps {
    private LineOps() {}

    public static String toggleBullet(String text) {
        String[] lines = text.split("\n", -1);
        boolean all = Arrays.stream(lines).filter(l -> !l.isBlank()).allMatch(l -> l.trim().startsWith("- "));
        return Arrays.stream(lines).map(l -> {
            if (l.isBlank()) return l;
            if (all) return l.replaceFirst("^\\s*- ", "");
            return "- " + l;
        }).collect(Collectors.joining("\n"));
    }

    public static String toggleOrdered(String text) {
        String[] lines = text.split("\n", -1);
        Pattern p = Pattern.compile("^\\s*\\d+\\. ");
        boolean all = Arrays.stream(lines).filter(l -> !l.isBlank()).allMatch(l -> p.matcher(l).find());
        if (all) {
            return Arrays.stream(lines).map(l -> {
                if (l.isBlank()) return l;
                return l.replaceFirst("^\\s*\\d+\\. ", "");
            }).collect(Collectors.joining("\n"));
        }
        int[] i = {1};
        return Arrays.stream(lines).map(l -> {
            if (l.isBlank()) return l;
            return (i[0]++) + ". " + l;
        }).collect(Collectors.joining("\n"));
    }

    public static String toggleQuote(String text) {
        String[] lines = text.split("\n", -1);
        boolean all = Arrays.stream(lines).filter(l -> !l.isBlank()).allMatch(l -> l.trim().startsWith(">"));
        return Arrays.stream(lines).map(l -> {
            if (l.isBlank()) return l;
            if (all) return l.replaceFirst("^\\s*> ?", "");
            return "> " + l;
        }).collect(Collectors.joining("\n"));
    }

    public static String toggleTask(String text) {
        String[] lines = text.split("\n", -1);
        Pattern p = Pattern.compile("^\\s*- \\[( |x|X)\\] ");
        boolean all = Arrays.stream(lines)
            .filter(l -> !l.isBlank())
            .allMatch(l -> p.matcher(l).find());
        return Arrays.stream(lines).map(l -> {
            if (l.isBlank()) return l;
            if (all) return l.replaceFirst("^\\s*- \\[( |x|X)\\] ", "");
            return "- [ ] " + l;
        }).collect(Collectors.joining("\n"));
    }

    public static String cycleHeading(String line) {
        String trimmed = line.stripLeading();
        if (trimmed.startsWith("### ")) {
            return trimmed.substring(4);
        } else if (trimmed.startsWith("## ")) {
            return "### " + trimmed.substring(3);
        } else if (trimmed.startsWith("# ")) {
            return "## " + trimmed.substring(2);
        }
        return "# " + line;
    }
}
