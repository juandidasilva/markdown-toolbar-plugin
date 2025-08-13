package com.example.mdgfm;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.EditorTextField;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Fallback action to show the PR toolbar as a popup near the caret/context.
 */
public class PRToolbarPopupAction extends AnAction {
    private static final Logger LOG = Logger.getInstance(PRToolbarPopupAction.class);

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        DataContext dataContext = e.getDataContext();

        AnAction a = ActionManager.getInstance().getAction("MDGFM.Toolbar.PR.Primary");
        if (!(a instanceof ActionGroup)) {
            LOG.warn("PRToolbarPopupAction: action 'MDGFM.Toolbar.PR.Primary' not found or not ActionGroup");
            return;
        }
        ActionGroup group = (ActionGroup) a;

        // Prefer to anchor to an Editor if any
        Editor editor = CommonDataKeys.EDITOR.getData(dataContext);
        if (editor == null) editor = CommonDataKeys.EDITOR_EVEN_IF_INACTIVE.getData(dataContext);
        if (editor == null) {
            Component ctx = PlatformDataKeys.CONTEXT_COMPONENT.getData(dataContext);
            EditorTextField etf = ctx != null ? UIUtil.getParentOfType(EditorTextField.class, ctx) : null;
            if (etf != null) editor = etf.getEditor();
        }

        JBPopupFactory factory = JBPopupFactory.getInstance();
        JBPopup popup = factory.createActionGroupPopup(
            "Markdown PR Toolbar",
            group,
            dataContext,
            /* showNumbers */ false,
            /* showDisabledActions */ true,
            /* honorActionMnemonics */ false,
            null,
            10,
            null
        );

        if (editor != null) {
            popup.showInBestPositionFor(editor);
        } else {
            popup.showInBestPositionFor(dataContext);
        }
    }
}
