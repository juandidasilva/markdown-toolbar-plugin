package com.example.mdgfm.actions;

import com.example.mdgfm.core.SelectionUtils;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/** Action operating on whole lines. */
public abstract class AbstractLineAction extends AbstractMdAction {

    protected void transformLines(@NotNull AnActionEvent e, @NotNull Function<String, String> op) {
        Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        Project project = e.getProject();
        if (project == null) return;
        Caret caret = editor.getCaretModel().getCurrentCaret();
        TextRange range = SelectionUtils.selectionOrLines(editor);
        Document doc = editor.getDocument();
        String selected = doc.getText(range);
        WriteCommandAction.runWriteCommandAction(project, () -> {
            String replaced = op.apply(selected);
            doc.replaceString(range.getStartOffset(), range.getEndOffset(), replaced);
            caret.setSelection(range.getStartOffset(), range.getStartOffset() + replaced.length());
        });
    }
}
