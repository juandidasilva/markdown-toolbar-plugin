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
import com.intellij.ui.EditorTextField;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

/**
 * Overlay toolbar provider for GitHub PR comment fields.
 */
public class PRToolbarProvider extends AbstractFloatingToolbarProvider {

    public PRToolbarProvider() {
        super("MDGFM.Toolbar.PR.Primary");
    }

    @Override
    public boolean isApplicable(@NotNull DataContext dataContext) {
        Editor editor = CommonDataKeys.EDITOR.getData(dataContext);
        if (editor == null) {
            editor = CommonDataKeys.EDITOR_EVEN_IF_INACTIVE.getData(dataContext);
        }
        if (editor == null || !editor.getDocument().isWritable()) {
            return false;
        }

        JComponent cc = editor.getContentComponent();
        boolean inEditorTextField = UIUtil.getParentOfType(EditorTextField.class, cc) != null;
        if (!inEditorTextField) {
            return false;
        }
        for (Component p = cc; p != null; p = p.getParent()) {
            String cn = p.getClass().getName().toLowerCase();
            if (cn.contains("codereview") || cn.contains("code.review") || cn.contains("github")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void register(@NotNull DataContext dataContext,
                         @NotNull FloatingToolbarComponent component,
                         @NotNull Disposable parentDisposable) {
        Editor editor = CommonDataKeys.EDITOR.getData(dataContext);
        if (editor == null) {
            editor = CommonDataKeys.EDITOR_EVEN_IF_INACTIVE.getData(dataContext);
        }
        if (editor == null) {
            return;
        }

        ActionGroup group = (ActionGroup) ActionManager.getInstance().getAction("MDGFM.Toolbar.PR.Primary");
        ActionToolbar toolbar = ActionManager.getInstance().createActionToolbar("MDGFM.PR.Toolbar", group, true);
        toolbar.setTargetComponent(editor.getContentComponent());
        toolbar.setReservePlaceAutoPopupIcon(true);

        JPanel inline = new JPanel(new BorderLayout());
        inline.setOpaque(false);
        inline.add(toolbar.getComponent(), BorderLayout.CENTER);

        EditorEmbeddedComponentManager manager = EditorEmbeddedComponentManager.getInstance();
        EditorEmbeddedComponentManager.Properties props =
                new EditorEmbeddedComponentManager.Properties(null, null, false, true, 0, 0);
        try {
            Class<?> anchorType = Class.forName("com.intellij.openapi.editor.impl.EditorEmbeddedComponentManager$AnchorType");
            Object up = anchorType.getField("UP").get(null);
            props.getClass().getMethod("setAnchored", anchorType).invoke(props, up);

            int layer = EditorEmbeddedComponentManager.class.getField("LAYER_POPUP").getInt(null);
            props.getClass().getMethod("setLayer", int.class).invoke(props, layer);
        } catch (Throwable ignored) {
        }

        Disposable disposable = manager.addComponent((EditorEx) editor, inline, props);
        // dispose together with the editor
        Disposer.register(parentDisposable, disposable);
    }

    @Override
    public boolean getAutoHideable() {
        return true;
    }
}
