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
import net.kyori.adventure.text.minimessage.parser.TokenType

/**
 * Represents a node in the tree.
 *
 * @since 4.2.0
 */
open class ElementNode internal constructor(private val parent: ElementNode?, token: Token?, sourceMessage: String) {

  private val token: Token?
  private val sourceMessage: String
  private val children: MutableList<ElementNode> = ArrayList()

  /**
   * Returns the parent of this node, if present.
   *
   * @return the parent or null
   * @since 4.2.0
   */
  fun parent(): ElementNode? {
    return parent
  }

  /**
   * Returns the token that lead to the creation of this token.
   *
   * @return the token
   * @since 4.2.0
   */
  open fun token(): Token? {
    return token
  }

  /**
   * Returns the source message of this node.
   *
   * @return the source message
   * @since 4.2.0
   */
  fun sourceMessage(): String {
    return sourceMessage
  }

  /**
   * Returns the children of this node.
   *
   * @return the children of this node
   * @since 4.2.0
   */
  fun children(): List<ElementNode> {
    return children
  }

  /**
   * Adds a child to this node.
   *
   *
   * This method will attempt to join text tokens together if possible.
   *
   * @param childNode the child node to add.
   * @since 4.2.0
   */
  fun addChild(childNode: ElementNode) {
    val last: Int = children.size - 1
    if(childNode !is TextNode || children.isEmpty() || children[last] !is TextNode) {
      children.add(childNode)
    } else {
      val lastNode: TextNode = children.removeAt(last) as TextNode
      if(lastNode.token().endIndex() == childNode.token().startIndex()) {
        val replace = Token(lastNode.token().startIndex(), childNode.token().endIndex(), TokenType.TEXT)
        children.add(TextNode(this, replace, lastNode.sourceMessage()))
      } else {
        // These nodes aren't adjacent in the string, so put the last one back
        children.add(lastNode)
        children.add(childNode)
      }
    }
  }

  /**
   * Serializes this node to a string.
   *
   * @param sb the string builder to serialize into
   * @param indent the current indent level
   * @return the passed string builder, for chaining
   * @since 4.2.0
   */
  open fun buildToString(sb: StringBuilder, indent: Int): StringBuilder {
    val `in` = ident(indent)
    sb.append(`in`).append("Node {\n")
    for(child in children) {
      child.buildToString(sb, indent + 1)
    }
    sb.append(`in`).append("}\n")
    return sb
  }

  fun ident(indent: Int): CharArray {
    val c = CharArray(indent * 2)
    c.fill(' ')
    return c
  }

  override fun toString(): String {
    return buildToString(StringBuilder(), 0).toString()
  }

  /**
   * Creates a new element node.
   *
   * @param parent the parent of this node
   * @param token the token that created this node
   * @param sourceMessage the source message
   * @since 4.2.0
   */
  init {
    this.token = token
    this.sourceMessage = sourceMessage
  }
}
