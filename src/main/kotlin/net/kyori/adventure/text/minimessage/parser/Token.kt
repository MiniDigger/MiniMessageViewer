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
package net.kyori.adventure.text.minimessage.parser

/**
 * Represents a token for the lexer.
 *
 * @since 4.2.0
 */
class Token(private val startIndex: Int, private val endIndex: Int, type: TokenType) {
  private val type: TokenType
  private var childTokens: MutableList<Token>? = null

  /**
   * Returns the start index of this token.
   *
   * @return the start index
   * @since 4.2.0
   */
  fun startIndex(): Int {
    return startIndex
  }

  /**
   * Returns the end index of this token.
   *
   * @return the end index
   * @since 4.2.0
   */
  fun endIndex(): Int {
    return endIndex
  }

  /**
   * Returns the type of this token.
   *
   * @return the type
   * @since 4.2.0
   */
  fun type(): TokenType {
    return type
  }

  /**
   * Returns the children of this token.
   *
   * @return the child tokens
   * @since 4.2.0
   */
  fun childTokens(): MutableList<Token>? {
    return childTokens
  }

  /**
   * Sets the children of this token.
   *
   * @param childTokens the new children
   * @since 4.2.0
   */
  fun childTokens(childTokens: MutableList<Token>?) {
    this.childTokens = childTokens
  }

  override fun toString(): String {
    return "Token(startIndex=$startIndex, endIndex=$endIndex, type=$type, childTokens=$childTokens)"
  }

  /**
   * Creates a new token.
   *
   * @param startIndex the start index of the token
   * @param endIndex the end index of the token
   * @param type the type of the token
   * @since 4.2.0
   */
  init {
    this.type = type
  }
}
