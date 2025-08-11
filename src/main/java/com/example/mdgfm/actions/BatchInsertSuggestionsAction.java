package com.example.mdgfm.actions;

import com.example.mdgfm.core.BatchService;
import com.example.mdgfm.core.EditorContext;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class BatchInsertSuggestionsAction extends AbstractMdAction {
    private final BatchService service = new BatchService();

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        EditorContext ctx = EditorContext.from(e);
        if (ctx != null) {
            service.insertSuggestions(ctx);
        }
    }
}
