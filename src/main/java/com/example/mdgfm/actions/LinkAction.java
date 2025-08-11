package com.example.mdgfm.actions;

import com.example.mdgfm.core.MdOps;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

public class LinkAction extends AbstractMdAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        String url = Messages.showInputDialog(e.getProject(), "URL", "Insert Link", null, "https://", null);
        if (url == null) return;
        transform(e, sel -> {
            String text = sel.isBlank() ? "text" : sel;
            return MdOps.link(text, url);
        });
    }
}
