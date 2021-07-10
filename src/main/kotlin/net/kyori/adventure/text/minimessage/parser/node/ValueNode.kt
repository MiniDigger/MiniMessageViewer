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

import net.kyori.adventure.text.minimessage.parser.Token

/**
 * Represents a node in the tree which has a text value.
 *
 * @since 4.2.0
 */
abstract class ValueNode
/**
 * Creates a new element node.
 *
 * @param parent the parent of this node
 * @param token the token that created this node
 * @param sourceMessage the source message
 * @since 4.2.0
 */ internal constructor(parent: ElementNode?, token: Token?, sourceMessage: String, private val value: String) : ElementNode(parent, token, sourceMessage) {
  abstract fun valueName(): String?

  /**
   * Returns the value of this text node.
   *
   * @return the value
   * @since 4.2.0
   */
  fun value(): String {
    return value
  }

  override fun token(): Token {
    return requireNotNull(super.token()) { "token is not set" }
  }

  override fun buildToString(sb: StringBuilder, indent: Int): StringBuilder {
    val `in`: CharArray = this.ident(indent)
    sb.append(`in`).append(valueName()).append("('").append(value).append("')\n")
    return sb
  }
}
