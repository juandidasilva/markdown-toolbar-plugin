package com.example.mdgfm;

import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.toolbar.floating.AbstractFloatingToolbarProvider;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

/**
 * Floating toolbar provider for Markdown files.
 */
public class MarkdownToolbarProvider extends AbstractFloatingToolbarProvider {

    public MarkdownToolbarProvider() {
        super("MDGFM.Toolbar.Markdown");
    }

    @Override
    public boolean isApplicable(@NotNull DataContext dataContext) {
        Editor editor = CommonDataKeys.EDITOR.getData(dataContext);
        if (editor == null || !editor.getDocument().isWritable()) {
            return false;
        }
        VirtualFile file = CommonDataKeys.VIRTUAL_FILE.getData(dataContext);
        return file != null && "md".equalsIgnoreCase(file.getExtension());
    }

    @Override
    public boolean getAutoHideable() {
        return true;
    }
}
