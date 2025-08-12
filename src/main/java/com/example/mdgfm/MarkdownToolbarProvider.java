package com.example.mdgfm;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.toolbar.floating.AbstractFloatingToolbarProvider;
import com.intellij.openapi.editor.toolbar.floating.FloatingToolbarComponent;
import org.jetbrains.annotations.NotNull;

/**
 * Provides floating toolbar with Markdown/GFM actions.
 */
public class MarkdownToolbarProvider extends AbstractFloatingToolbarProvider {

    public MarkdownToolbarProvider() {
        super("MDGFM.Toolbar");
    }

    @Override
    public boolean isApplicable(@NotNull DataContext dataContext) {
        Editor editor = CommonDataKeys.EDITOR.getData(dataContext);
        if (editor == null) {
            editor = CommonDataKeys.EDITOR_EVEN_IF_INACTIVE.getData(dataContext);
        }
        return editor != null && editor.getDocument().isWritable();
    }

    @Override
    public void register(@NotNull DataContext dataContext,
                         @NotNull FloatingToolbarComponent component,
                         @NotNull Disposable parentDisposable) {
        // no dynamic actions to register
    }

    @Override
    public boolean getAutoHideable() {
        return true;
    }
}
