package com.example.mdgfm.core;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Lightweight wrapper around editor and selection state for batch operations.
 */
public class EditorContext {
    private final Editor editor;
    private final Project project;
    private final Caret caret;
    private final TextRange range;

    private EditorContext(Editor editor, Project project, Caret caret, TextRange range) {
        this.editor = editor;
        this.project = project;
        this.caret = caret;
        this.range = range;
    }

    @Nullable
    public static EditorContext from(@NotNull AnActionEvent e) {
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        Project project = e.getProject();
        if (editor == null || project == null) return null;
        Caret caret = editor.getCaretModel().getCurrentCaret();
        TextRange range = SelectionUtils.selectionOrLines(editor);
        return new EditorContext(editor, project, caret, range);
    }

    public String getSelectedText() {
        Document doc = editor.getDocument();
        return doc.getText(range);
    }

    public void replaceSelectedText(String text) {
        Document doc = editor.getDocument();
        WriteCommandAction.runWriteCommandAction(project, () -> {
            doc.replaceString(range.getStartOffset(), range.getEndOffset(), text);
            caret.setSelection(range.getStartOffset(), range.getStartOffset() + text.length());
        });
    }
}
