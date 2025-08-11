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

    @Test
    void mention() {
        assertEquals("@user", MdOps.mention("user"));
        assertEquals("@user", MdOps.mention("@user"));
    }

    @Test
    void issueRef() {
        assertEquals("#123", MdOps.issueRef("123"));
        assertEquals("#123", MdOps.issueRef("#123"));
    }

    @Test
    void commitRef() {
        assertEquals("commit:abc", MdOps.commitRef("abc"));
        assertEquals("commit:abc", MdOps.commitRef("commit:abc"));
    }

    @Test
    void emojiToggle() {
        assertEquals(":smile:", MdOps.toggleWrap("smile", ":"));
        assertEquals("smile", MdOps.toggleWrap(":smile:", ":"));
    }

    @Test
    void supSubToggle() {
        assertEquals("<sup>x</sup>", MdOps.toggleTag("x", "sup"));
        assertEquals("x", MdOps.toggleTag("<sup>x</sup>", "sup"));
        assertEquals("<sub>x</sub>", MdOps.toggleTag("x", "sub"));
        assertEquals("x", MdOps.toggleTag("<sub>x</sub>", "sub"));
    }

    @Test
    void footnoteToggle() {
        assertEquals("[^1]", MdOps.footnote("1"));
        assertEquals("1", MdOps.footnote("[^1]"));
    }
}
