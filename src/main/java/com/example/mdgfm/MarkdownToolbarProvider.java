package com.example.mdgfm;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.impl.EditorEmbeddedComponentManager;
import com.intellij.openapi.editor.toolbar.floating.AbstractFloatingToolbarProvider;
import com.intellij.openapi.editor.toolbar.floating.FloatingToolbarComponent;
import com.intellij.openapi.util.Disposer;
import java.awt.BorderLayout;
import javax.swing.JPanel;
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
        Editor editor = CommonDataKeys.EDITOR.getData(dataContext);
        if (editor == null) {
            editor = CommonDataKeys.EDITOR_EVEN_IF_INACTIVE.getData(dataContext);
        }
        if (editor == null || !editor.getDocument().isWritable()) {
            return;
        }

        ActionGroup group = (ActionGroup) ActionManager.getInstance().getAction("MDGFM.Toolbar");
        ActionToolbar toolbar = ActionManager.getInstance()
                .createActionToolbar("MDGFM.InlineToolbar", group, true);
        toolbar.setTargetComponent(editor.getContentComponent());

        JPanel inline = new JPanel(new BorderLayout());
        inline.setOpaque(false);
        inline.add(toolbar.getComponent(), BorderLayout.CENTER);

        EditorEmbeddedComponentManager manager = EditorEmbeddedComponentManager.getInstance();
        EditorEmbeddedComponentManager.Properties props =
                new EditorEmbeddedComponentManager.Properties(null, null, false, true, 0, 0);
        // Try to use newer anchoring and layering APIs if present
        try {
            Class<?> anchorType = Class.forName(
                    "com.intellij.openapi.editor.impl.EditorEmbeddedComponentManager$AnchorType");
            Object up = anchorType.getField("UP").get(null);
            props.getClass().getMethod("setAnchored", anchorType).invoke(props, up);

            int layer = EditorEmbeddedComponentManager.class.getField("LAYER_POPUP").getInt(null);
            props.getClass().getMethod("setLayer", int.class).invoke(props, layer);
        } catch (Throwable ignored) {
        }

        Disposable disposable = manager.addComponent((EditorEx) editor, inline, props);
        Disposer.register(parentDisposable, disposable);
    }

    @Override
    public boolean getAutoHideable() {
        return true;
    }
}
