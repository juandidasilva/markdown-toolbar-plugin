package com.example.mdgfm.core;

import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.TextRange;

public final class SelectionUtils {
    private SelectionUtils() {}

    public static TextRange selectionOrWord(Editor editor) {
        Caret caret = editor.getCaretModel().getCurrentCaret();
        if (caret.hasSelection()) {
            return new TextRange(caret.getSelectionStart(), caret.getSelectionEnd());
        }
        Document doc = editor.getDocument();
        CharSequence text = doc.getCharsSequence();
        int offset = caret.getOffset();
        int start = offset;
        while (start > 0 && !Character.isWhitespace(text.charAt(start - 1))) start--;
        int end = offset;
        while (end < text.length() && !Character.isWhitespace(text.charAt(end))) end++;
        caret.setSelection(start, end);
        return new TextRange(start, end);
    }

    public static TextRange selectionOrLines(Editor editor) {
        Caret caret = editor.getCaretModel().getCurrentCaret();
        Document doc = editor.getDocument();
        if (caret.hasSelection()) {
            int startLine = doc.getLineNumber(caret.getSelectionStart());
            int endLine = doc.getLineNumber(caret.getSelectionEnd());
            int start = doc.getLineStartOffset(startLine);
            int end = doc.getLineEndOffset(endLine);
            caret.setSelection(start, end);
            return new TextRange(start, end);
        }
        int line = doc.getLineNumber(caret.getOffset());
        int start = doc.getLineStartOffset(line);
        int end = doc.getLineEndOffset(line);
        caret.setSelection(start, end);
        return new TextRange(start, end);
    }
}
