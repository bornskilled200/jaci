/******************************************************************************
 * Copyright (C) 2014 Yevgeny Krasik                                          *
 *                                                                            *
 * Licensed under the Apache License, Version 2.0 (the "License");            *
 * you may not use this file except in compliance with the License.           *
 * You may obtain a copy of the License at                                    *
 *                                                                            *
 * http://www.apache.org/licenses/LICENSE-2.0                                 *
 *                                                                            *
 * Unless required by applicable law or agreed to in writing, software        *
 * distributed under the License is distributed on an "AS IS" BASIS,          *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.   *
 * See the License for the specific language governing permissions and        *
 * limitations under the License.                                             *
 ******************************************************************************/

package com.github.ykrasik.jerminal.collections.trie;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * An <b>immutable</b> prefix tree.<br>
 * The internal state of the Trie cannot be changed by any operation. All methods return a copy of the Trie
 * with the alteration performed.
 *
 * @author Yevgeny Krasik
 */
// TODO: Consider Optimizing: Use word segments as children, not characters.
public interface Trie<T> {
    /**
     * @return The amount of words in this Trie.
     */
    int size();

    /**
     * @return True if this Trie does not contain any words.
     */
    boolean isEmpty();

    /**
     * @param word The word to check.
     * @return True if this Trie contains the word.
     */
    boolean contains(String word);

    /**
     * @param word The word to retrieve the value for.
     * @return The value associated with the word.
     */
    Optional<T> get(String word);

    /**
     * @return The longest common prefix in this Trie.<br>
     *         For example, for the words 'abc', 'abcd' and 'abcde', the longest common prefix is 'abc'.
     */
    String getLongestPrefix();

    /**
     * @param prefix The prefix to get a subTrie for.
     * @return A {@link Trie} with words that start with the prefix.<br>
     *         If no such words exist in the Trie, an empty Trie is returned.<br>
     *         Does not alter this Trie.
     */
    Trie<T> subTrie(String prefix);

    /**
     * @param function Function to apply to each value in this Trie.
     * @param <A> Type to transform values to.
     * @return A Trie in which the value of each word was transformed by calling {@link Function#apply(T)}.<br>
     *         If the result of the transformation returned 'null', that word and value will not appear in the returned Trie.<br>
     *         Does not alter this Trie.
     */
    <A> Trie<A> map(Function<T, A> function);

    // TODO: Add a lazy map that saves the function and only applies it when traversed.

    /**
     * @param filter Predicate that determines which values will remain in the Trie.
     * @return A Trie in which all values for which {@link Predicate#apply(T)} returned false are removed.<br>
     *         Does not alter this Trie.
     */
    Trie<T> filter(Predicate<T> filter);

    /**
     * @param other The other Trie of the union.
     * @return A Trie which contains words and values from this Trie and the other Trie.<br>
     *         If a word was contained in both Tries, the returned Trie will map that word to either one
     *         of the possible values, without any guarantees.<br>
     *         Does not alter this Trie.
     */
    Trie<T> union(Trie<T> other);

    /**
     * Calls {@link TrieVisitor#visit(String, T)} for each word-value pair in this Trie.
     *
     * @param visitor The visitor that will visit each word-value pair.
     */
    void visitWords(TrieVisitor<T> visitor);

    /**
     * @return All the words in this Trie.
     */
    Collection<String> words();

    /**
     * @return All the values in this Trie.
     */
    Collection<T> values();

    /**
     * @return A {@link Set} containing all word-value pairs as {@link Entry entries} from this Trie.
     */
    Set<Entry<String, T>> entrySet();

    /**
     * @return A {@link Map} containing all word-value pairs from this Trie.
     */
    Map<String, T> toMap();
}
