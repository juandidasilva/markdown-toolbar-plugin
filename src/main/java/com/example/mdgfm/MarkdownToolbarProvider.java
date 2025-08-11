package com.example.mdgfm;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.toolbar.floating.AbstractFloatingToolbarProvider;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Provides floating toolbar with Markdown/GFM actions.
 */
public class MarkdownToolbarProvider extends AbstractFloatingToolbarProvider {

    private static final List<String> ACTION_IDS = List.of(
            "MDGFM.Bold",
            "MDGFM.Italic",
            "MDGFM.InlineCode",
            "MDGFM.Strike",
            "MDGFM.CodeBlock",
            "MDGFM.Heading",
            "MDGFM.BulletList",
            "MDGFM.OrderedList",
            "MDGFM.Quote",
            "MDGFM.TaskList",
            "MDGFM.Suggestion",
            "MDGFM.Table",
            "MDGFM.Link",
            "MDGFM.Mermaid",
            "MDGFM.Details",
            "MDGFM.DiffBlock"
    );

    public MarkdownToolbarProvider() {
        super("MDGFM.Toolbar");
    }

    @Override
    protected void registerActions(@NotNull List<? super AnAction> actions, @NotNull Editor editor) {
        ActionManager am = ActionManager.getInstance();
        ACTION_IDS.stream().map(am::getAction).forEach(actions::add);
    }

    @Override
    protected boolean isApplicable(@NotNull Editor editor) {
        return editor.getDocument().isWritable();
    }
}
