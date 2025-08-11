package com.example.mdgfm.actions;

import com.example.mdgfm.core.MdOps;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

public class CodeBlockAction extends AbstractMdAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        String lang = Messages.showInputDialog(e.getProject(), "Language", "Code Block", null);
        if (lang == null) lang = "";
        final String l = lang;
        transform(e, sel -> MdOps.fencedBlock(l, sel));
    }
}
