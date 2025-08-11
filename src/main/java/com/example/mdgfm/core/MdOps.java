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
}
