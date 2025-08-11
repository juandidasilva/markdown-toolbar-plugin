package com.example.mdgfm.actions;

import com.example.mdgfm.core.LineOps;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class TaskListAction extends AbstractLineAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        transformLines(e, LineOps::toggleTask);
    }
}
