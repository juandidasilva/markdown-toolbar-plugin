package com.example.mdgfm;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.KeyboardFocusManager;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jetbrains.annotations.NotNull;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.impl.EditorEmbeddedComponentManager;
import com.intellij.openapi.editor.toolbar.floating.AbstractFloatingToolbarProvider;
import com.intellij.openapi.editor.toolbar.floating.FloatingToolbarComponent;
import com.intellij.openapi.util.Disposer;
import com.intellij.ui.EditorTextField;
import com.intellij.util.ui.UIUtil;

/**
 * Overlay toolbar provider for GitHub PR comment fields.
 */
public class PRToolbarProvider extends AbstractFloatingToolbarProvider {
    private static final Logger LOG = Logger.getInstance(PRToolbarProvider.class);

    public PRToolbarProvider() {
        super("MDGFM.Toolbar.PR.Primary");
    }

    public boolean isEnabledByDefault() {
        return true;
    }

    @Override
    public boolean isApplicable(@NotNull DataContext dataContext) {
        // Try to resolve an editor if present
        Editor editor = CommonDataKeys.EDITOR.getData(dataContext);
        if (editor == null) {
            editor = CommonDataKeys.EDITOR_EVEN_IF_INACTIVE.getData(dataContext);
        }

        // Resolve UI component context (editor or context component)
        JComponent cc = null;
        if (editor != null) {
            cc = editor.getContentComponent();
        }
        if (cc == null) {
            Component ctx = PlatformDataKeys.CONTEXT_COMPONENT.getData(dataContext);
            if (ctx instanceof JComponent) cc = (JComponent) ctx;
        }

        if (cc != null) {
            // Do NOT show over the comment field itself; we'll show over the diff viewer
            if (UIUtil.getParentOfType(EditorTextField.class, cc) != null) {
                return false;
            }
            // Heuristics: within diff/review/PR UI (GitHub, etc.)
            for (Component p = cc; p != null; p = p.getParent()) {
                String cn = p.getClass().getName().toLowerCase();
                if (cn.contains("github") || cn.contains("code.review") || cn.contains("codereview")
                    || cn.contains("pullrequest") || cn.contains("review") || cn.contains("diff")) {
                    return true;
                }
            }
        }

        // No positive signal that we're in a PR diff/review context
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
            Component ctx = PlatformDataKeys.CONTEXT_COMPONENT.getData(dataContext);
            EditorTextField etf = ctx != null ? UIUtil.getParentOfType(EditorTextField.class, ctx) : null;
            if (etf != null) {
                editor = etf.getEditor();
            }
        }
        if (editor == null) {
            LOG.warn("PRToolbarProvider.register: no editor found in context");
            return;
        }

        AnAction a = ActionManager.getInstance().getAction("MDGFM.Toolbar.PR.Primary");
        if (!(a instanceof ActionGroup)) {
            LOG.warn("PRToolbarProvider.register: action 'MDGFM.Toolbar.PR.Primary' not found or not an ActionGroup");
            return;
        }
        ActionGroup group = (ActionGroup) a;

        ActionToolbar toolbar = ActionManager.getInstance().createActionToolbar("MDGFM.PR.Toolbar", group, true);
        // Anchor visually over the diff editor, but direct actions to the nearest PR comment EditorTextField if present
        JComponent editorComp = editor.getContentComponent();
        JComponent target = editorComp;
        // Try focused component first
        Component focus = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
        EditorTextField focusedEtf = focus != null ? UIUtil.getParentOfType(EditorTextField.class, focus) : null;
        if (focusedEtf != null) {
            target = focusedEtf;
        } else {
            // Find an EditorTextField within the nearest review/diff container
            for (Component p = editorComp; p != null; p = p.getParent()) {
                if (p instanceof JComponent) {
                    String cn = p.getClass().getName().toLowerCase();
                    if (cn.contains("github") || cn.contains("code.review") || cn.contains("codereview")
                        || cn.contains("pullrequest") || cn.contains("review") || cn.contains("diff")) {
                        EditorTextField etf = UIUtil.findComponentOfType((JComponent) p, EditorTextField.class);
                        if (etf != null) { target = etf; break; }
                    }
                }
            }
        }
        toolbar.setTargetComponent(target);
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
        } catch (Throwable t) {
            LOG.warn("PRToolbarProvider.register: failed to configure anchor/layer via reflection", t);
        }

        if (!(editor instanceof EditorEx)) {
            LOG.warn("PRToolbarProvider.register: editor is not EditorEx; cannot embed toolbar");
            return;
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
