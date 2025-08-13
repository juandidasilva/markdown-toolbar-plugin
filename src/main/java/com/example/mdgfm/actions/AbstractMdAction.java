package com.example.mdgfm.actions;

import com.example.mdgfm.core.SelectionUtils;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.ui.EditorTextField;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/** Base class for markdown actions operating on selection or caret. */
public abstract class AbstractMdAction extends AnAction {

    /** Resolve an editor to operate on. Prefer a writable editor; fall back to EditorTextField in context. */
    @Nullable
    protected Editor resolveEditor(@NotNull AnActionEvent e) {
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        if (editor == null) editor = e.getData(CommonDataKeys.EDITOR_EVEN_IF_INACTIVE);
        if (editor != null && editor.getDocument().isWritable()) return editor;

        // Try to locate an EditorTextField from the current component context
        java.awt.Component ctx = PlatformDataKeys.CONTEXT_COMPONENT.getData(e.getDataContext());
        EditorTextField etf = ctx != null ? UIUtil.getParentOfType(EditorTextField.class, ctx) : null;
        if (etf == null && editor != null) {
            // Try from the editor's component too
            etf = UIUtil.getParentOfType(EditorTextField.class, editor.getContentComponent());
        }
        if (etf != null && etf.getEditor() != null) {
            return etf.getEditor();
        }
        return editor;
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        Editor editor = resolveEditor(e);
        boolean enabled = editor != null && editor.getDocument().isWritable();
        e.getPresentation().setEnabledAndVisible(enabled);
    }

    protected void transform(@NotNull AnActionEvent e, @NotNull Function<String, String> op) {
        Editor editor = resolveEditor(e);
        if (editor == null) return;
        Project project = e.getProject();
        if (project == null) return;
        Caret caret = editor.getCaretModel().getCurrentCaret();
        TextRange range = SelectionUtils.selectionOrWord(editor);
        Document doc = editor.getDocument();
        String selected = doc.getText(range);
        WriteCommandAction.runWriteCommandAction(project, () -> {
            String replaced = op.apply(selected);
            doc.replaceString(range.getStartOffset(), range.getEndOffset(), replaced);
            caret.setSelection(range.getStartOffset(), range.getStartOffset() + replaced.length());
        });
    }
}
