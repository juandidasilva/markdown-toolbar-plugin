package com.example.mdgfm;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.EditorFactoryEvent;
import com.intellij.openapi.editor.event.EditorFactoryListener;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.impl.EditorEmbeddedComponentManager;
import com.intellij.ui.EditorTextField;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class PRCommentEditorListener implements EditorFactoryListener {
    private static final Logger LOG = Logger.getInstance(PRCommentEditorListener.class);
    private static final String CLIENT_KEY = "MDGFM_PR_TOOLBAR_ATTACHED";

    @Override
    public void editorCreated(@NotNull EditorFactoryEvent event) {
        Editor editor = event.getEditor();
        JComponent cc = editor.getContentComponent();

        // Only attach for editors inside EditorTextField
        EditorTextField etf = UIUtil.getParentOfType(EditorTextField.class, cc);
        if (etf == null) return;

        // Avoid duplicates
        if (cc.getClientProperty(CLIENT_KEY) != null) return;

        // Heuristic: ensure we're inside a GitHub PR/review UI
        boolean inGhReview = false;
        for (Component p = cc; p != null; p = p.getParent()) {
            String cn = p.getClass().getName().toLowerCase();
            if (cn.contains("github") || cn.contains("pullrequest") || cn.contains("code.review")
                || cn.contains("codereview") || cn.contains("review") || cn.contains("diff")) {
                inGhReview = true;
                break;
            }
        }
        if (!inGhReview) return;

        AnAction a = ActionManager.getInstance().getAction("MDGFM.Toolbar.PR.Primary");
        if (!(a instanceof ActionGroup)) {
            LOG.warn("PRCommentEditorListener: action 'MDGFM.Toolbar.PR.Primary' not found or not ActionGroup");
            return;
        }
        ActionGroup group = (ActionGroup) a;

        ActionToolbar toolbar = ActionManager.getInstance().createActionToolbar("MDGFM.PR.Toolbar", group, true);
        toolbar.setTargetComponent(cc);
        toolbar.setReservePlaceAutoPopupIcon(true);

        JPanel inline = new JPanel(new BorderLayout());
        inline.setOpaque(false);
        inline.add(toolbar.getComponent(), BorderLayout.CENTER);

        if (!(editor instanceof EditorEx)) {
            LOG.warn("PRCommentEditorListener: editor is not EditorEx; cannot embed toolbar");
            return;
        }

        EditorEmbeddedComponentManager manager = EditorEmbeddedComponentManager.getInstance();
        EditorEmbeddedComponentManager.Properties props =
            new EditorEmbeddedComponentManager.Properties(null, null, false, true, 0, 0);
        try {
            Class<?> anchorType = Class.forName("com.intellij.openapi.editor.impl.EditorEmbeddedComponentManager$AnchorType");
            Object up = anchorType.getField("UP").get(null);
            props.getClass().getMethod("setAnchored", anchorType).invoke(props, up);

            int layer = EditorEmbeddedComponentManager.class.getField("LAYER_POPUP").getInt(null);
            props.getClass().getMethod("setLayer", int.class).invoke(props, layer);
        } catch (Throwable t) {
            LOG.warn("PRCommentEditorListener: failed to configure anchor/layer via reflection", t);
        }

        Disposable disposable = manager.addComponent((EditorEx) editor, inline, props);
        cc.putClientProperty(CLIENT_KEY, disposable);
    }

    @Override
    public void editorReleased(@NotNull EditorFactoryEvent event) {
        Editor editor = event.getEditor();
        JComponent cc = editor.getContentComponent();
        Object d = cc.getClientProperty(CLIENT_KEY);
        if (d instanceof Disposable) {
            try {
                ((Disposable) d).dispose();
            } catch (Throwable ignored) {
            }
        }
        cc.putClientProperty(CLIENT_KEY, null);
    }
}
