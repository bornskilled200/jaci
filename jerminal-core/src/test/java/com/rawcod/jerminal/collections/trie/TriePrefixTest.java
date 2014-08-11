package com.rawcod.jerminal.collections.trie;

import org.junit.Before;
import org.junit.Test;

/**
 * User: ykrasik
 * Date: 11/08/2014
 * Time: 21:41
 */
public class TriePrefixTest extends AbstractTrieTest {
    @Override
    @Before
    public void setUp() {
        super.setUp();

        addWord("p", "prefix1");
        addWord("pr", "prefix2");
        addWord("pre", "prefix3");
        addWord("pre1fix", "prefix4");
        addWord("pre2", "prefix5");
        addWord("prefix", "prefix6");
        addWord("prefiz", "prefix7");
        addWord("other", "other");

        build();
    }

    @Test
    public void testPrefix1() {
        // Root
        assertNotEmpty();
        assertWords("p", "pr", "pre", "pre1fix", "pre2", "prefix", "prefiz", "other");
        assertLongestPrefix("");
    }

    @Test
    public void testPrefix2() {
        // "p"
        successfulSubTrie("p");
        assertNotEmpty();
        assertWords("p", "pr", "pre", "pre1fix", "pre2", "prefix", "prefiz");
        assertLongestPrefix("p");
    }

    @Test
    public void testPrefix3() {
        // "pr"
        successfulSubTrie("pr");
        assertNotEmpty();
        assertWords("pr", "pre", "pre1fix", "pre2", "prefix", "prefiz");
        assertLongestPrefix("pr");
    }

    @Test
    public void testPrefix4() {
        // "pre"
        successfulSubTrie("pre");
        assertNotEmpty();
        assertWords("pre", "pre1fix", "pre2", "prefix", "prefiz");
        assertLongestPrefix("pre");
    }

    @Test
    public void testPrefix5() {
        // "pre1" - only "pre1fix" is possible from here.
        successfulSubTrie("pre1");
        assertNotEmpty();
        assertWords("pre1fix");
        assertLongestPrefix("pre1fix");
    }

    @Test
    public void testPrefix6() {
        // "pre2" - only "pre2" is possible from here.
        successfulSubTrie("pre2");
        assertNotEmpty();
        assertWords("pre2");
        assertLongestPrefix("pre2");
    }

    @Test
    public void testPrefix7() {
        // "pre2" - only "pre2" is possible from here.
        successfulSubTrie("pre2");
        assertNotEmpty();
        assertWords("pre2");
        assertLongestPrefix("pre2");
    }

    @Test
    public void testPrefix8() {
        // "pref"
        successfulSubTrie("pref");
        assertNotEmpty();
        assertWords("prefix", "prefiz");
        assertLongestPrefix("prefi");
    }

    @Test
    public void testPrefix9() {
        // "prefi"
        successfulSubTrie("prefi");
        assertNotEmpty();
        assertWords("prefix", "prefiz");
        assertLongestPrefix("prefi");
    }

    @Test
    public void testPrefix10() {
        // "prefix"
        successfulSubTrie("prefix");
        assertNotEmpty();
        assertWords("prefix");
        assertLongestPrefix("prefix");
    }

    @Test
    public void testPrefix11() {
        // "prefiz"
        successfulSubTrie("prefiz");
        assertNotEmpty();
        assertWords("prefiz");
        assertLongestPrefix("prefiz");
    }

    @Test
    public void testFailedPrefix() {
        // Invalid prefixes
        failedSubTrie("prefix1");
        failedSubTrie("pred");
        failedSubTrie("predix");
        failedSubTrie("ob");
        failedSubTrie("othar");
        failedSubTrie("others");
    }
}
