/*
 * This file is part of adventure-text-minimessage, licensed under the MIT License.
 *
 * Copyright (c) 2018-2021 KyoriPowered
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.kyori.adventure.text.minimessage.parser.node

import net.kyori.adventure.text.minimessage.Template
import net.kyori.adventure.text.minimessage.parser.Token
import net.kyori.adventure.text.minimessage.parser.TokenParser

/**
 * Represents an inner part of a tag.
 *
 * @since 4.2.0
 */
class TagPart(
  sourceMessage: String,
  token: Token,
  templates: Map<String, Template>
) {
  private val value: String
  private val token: Token

  /**
   * Returns the value of this tag part.
   *
   * @return the value
   * @since 4.2.0
   */
  fun value(): String {
    return value
  }

  /**
   * Returns the token that created this tag part.
   *
   * @return the token
   * @since 4.2.0
   */
  fun token(): Token {
    return token
  }

  override fun toString(): String {
    return value
  }

  companion object {
    private fun isTag(text: String): Boolean {
      return text[0] == '<' || text[text.length - 1] == '>'
    }

    /**
     * Removes leading/trailing quotes from the given string, if necessary, and removes escaping `'\'` characters.
     *
     * @param text the input text
     * @param start the starting index of the substring
     * @param end the ending index of the substring
     * @return the output substring
     * @since 4.2.0
     */
    fun unquoteAndEscape(text: String, start: Int, end: Int): String {
      if(start == end) {
        return ""
      }
      var startIndex = start
      var endIndex = end
      val firstChar: Char = text[startIndex]
      val lastChar: Char = text[endIndex - 1]
      if(firstChar == '\'' || firstChar == '"') {
        startIndex++
      }
      if(lastChar == '\'' || lastChar == '"') {
        endIndex--
      }
      return TokenParser.unescape(text, startIndex, endIndex)
    }
  }

  /**
   * Constructs a new tag part.
   *
   * @param sourceMessage the source message
   * @param token the token that creates this tag part
   * @since 4.2.0
   */
  init {
    var v = unquoteAndEscape(sourceMessage, token.startIndex(), token.endIndex())
    if(isTag(v)) {
      val text = v.substring(1, v.length - 1)
      val template: Template? = templates[text]
      if(template is Template.StringTemplate) {
        v = template.value()
      }
    }
    value = v
    this.token = token
  }
}
