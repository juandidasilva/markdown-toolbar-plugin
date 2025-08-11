package com.example.mdgfm.actions;

import com.example.mdgfm.core.LineOps;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.stream.Collectors;

public class HeadingCycleAction extends AbstractLineAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        transformLines(e, sel -> Arrays.stream(sel.split("\n", -1))
                .map(LineOps::cycleHeading)
                .collect(Collectors.joining("\n")));
    }
}
