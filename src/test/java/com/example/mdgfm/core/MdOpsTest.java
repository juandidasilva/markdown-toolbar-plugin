package com.example.mdgfm.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MdOpsTest {

    @Test
    void boldToggle() {
        assertEquals("**text**", MdOps.toggleWrap("text", "**"));
        assertEquals("text", MdOps.toggleWrap("**text**", "**"));
    }

    @Test
    void inlineCode() {
        assertEquals("`code`", MdOps.toggleWrap("code", "`"));
        assertEquals("code", MdOps.toggleWrap("`code`", "`"));
    }

    @Test
    void bulletList() {
        String out = LineOps.toggleBullet("a\nb");
        assertEquals("- a\n- b", out);
        assertEquals("a\nb", LineOps.toggleBullet(out));
    }

    @Test
    void orderedList() {
        String out = LineOps.toggleOrdered("a\nb");
        assertEquals("1. a\n2. b", out);
        assertEquals("a\nb", LineOps.toggleOrdered(out));
    }

    @Test
    void headingCycle() {
        assertEquals("# title", LineOps.cycleHeading("title"));
        assertEquals("## title", LineOps.cycleHeading("# title"));
        assertEquals("### title", LineOps.cycleHeading("## title"));
        assertEquals("title", LineOps.cycleHeading("### title"));
    }

    @Test
    void taskList() {
        String out = LineOps.toggleTask("task");
        assertEquals("- [ ] task", out);
        assertEquals("task", LineOps.toggleTask(out));
    }

    @Test
    void suggestionBlock() {
        assertEquals("```suggestion\nabc\n```", MdOps.suggestionBlock("abc"));
    }

    @Test
    void tableSnippet() {
        assertTrue(MdOps.table().contains("| Col1 |"));
    }
}
