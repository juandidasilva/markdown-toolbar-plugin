package com.example.mdgfm.core;

public final class MdOps {
    private MdOps() {}

    public static String toggleWrap(String text, String marker) {
        String trimmed = text.trim();
        if (trimmed.startsWith(marker) && trimmed.endsWith(marker) && trimmed.length() >= marker.length() * 2) {
            String core = trimmed.substring(marker.length(), trimmed.length() - marker.length());
            return text.replace(trimmed, core);
        }
        return marker + text + marker;
    }

    public static String fencedBlock(String lang, String body) {
        if (lang == null) lang = "";
        return "```" + lang + "\n" + body + "\n```";
    }

    public static String suggestionBlock(String body) {
        return fencedBlock("suggestion", body.isBlank() ? "// your change here" : body);
    }

    public static String table() {
        return "| Col1 | Col2 |\n| :--- | ---: |\n|      |      |";
    }

    public static String mermaid() {
        return "```mermaid\ngraph TD\n  A --> B\n```";
    }

    public static String details() {
        return "<details>\n<summary>Click to expand</summary>\n\nContent…\n\n</details>";
    }

    public static String diffBlock() {
        return "```diff\n+ added\n- removed\n```";
    }

    public static String link(String text, String url) {
        return "[" + text + "](" + url + ")";
    }

    public static String prefixIfMissing(String text, String prefix) {
        return text.startsWith(prefix) ? text : prefix + text;
    }

    public static String mention(String text) {
        return prefixIfMissing(text, "@");
    }

    public static String issueRef(String text) {
        return prefixIfMissing(text, "#");
    }

    public static String commitRef(String text) {
        return prefixIfMissing(text, "commit:");
    }

    public static String toggleTag(String text, String tag) {
        String open = "<" + tag + ">";
        String close = "</" + tag + ">";
        String trimmed = text.trim();
        if (trimmed.startsWith(open) && trimmed.endsWith(close) && trimmed.length() >= open.length() + close.length()) {
            String core = trimmed.substring(open.length(), trimmed.length() - close.length());
            return text.replace(trimmed, core);
        }
        return open + text + close;
    }

    public static String footnote(String text) {
        String trimmed = text.trim();
        if (trimmed.startsWith("[^") && trimmed.endsWith("]") && trimmed.length() >= 3) {
            String core = trimmed.substring(2, trimmed.length() - 1);
            return text.replace(trimmed, core);
        }
        return "[^" + text + "]";
    }
}
