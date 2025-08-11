package com.example.mdgfm.actions;

import com.example.mdgfm.core.MdOps;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class TableAction extends AbstractMdAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        transform(e, sel -> MdOps.table());
    }
}
