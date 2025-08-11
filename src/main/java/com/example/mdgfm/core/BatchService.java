package com.example.mdgfm.core;

import org.jetbrains.annotations.NotNull;

/**
 * Service providing batch operations on selections.
 */
public class BatchService {

    public void addSuggestion(@NotNull EditorContext ctx) {
        String selected = ctx.getSelectedText();
        String replaced = MdOps.suggestionBlock(selected);
        ctx.replaceSelectedText(replaced);
    }

    public void insertSuggestions(@NotNull EditorContext ctx) {
        addSuggestion(ctx);
    }
}
