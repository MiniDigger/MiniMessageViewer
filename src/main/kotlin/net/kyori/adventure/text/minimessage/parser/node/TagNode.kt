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
import net.kyori.adventure.text.minimessage.parser.ParsingException
import net.kyori.adventure.text.minimessage.parser.Token
import net.kyori.adventure.text.minimessage.transformation.Transformation

/**
 * Node that represents a tag.
 *
 * @since 4.2.0
 */
class TagNode(
  parent: ElementNode?,
  token: Token,
  sourceMessage: String,
  templates: Map<String, Template>
) : ElementNode(parent, token, sourceMessage) {
  private val parts: List<TagPart>

  private var transformation: Transformation? = null

  /**
   * Returns the parts of this tag.
   *
   * @return the parts
   * @since 4.2.0
   */
  fun parts(): List<TagPart> {
    return parts
  }

  /**
   * Returns the name of this tag.
   *
   * @return the name
   * @since 4.2.0
   */
  fun name(): String {
    if(parts.isEmpty()) {
      throw ParsingException("Tag has no parts? $this", this.sourceMessage(), token())
    }
    return parts[0].value()
  }

  override fun token(): Token {
    return requireNotNull(super.token()) { "token is not set" }
  }

  /**
   * Gets the transformation attached to this tag node.
   *
   * @return the transformation for this tag node
   * @since 4.2.0
   */
  fun transformation(): Transformation {
    return requireNotNull(transformation) { "no transformation set" }
  }

  /**
   * Sets the transformation that is represented by this tag.
   *
   * @param transformation the transformation
   * @since 4.2.0
   */
  fun transformation(transformation: Transformation?) {
    this.transformation = transformation
  }

  override fun buildToString(sb: StringBuilder, indent: Int): StringBuilder {
    val `in`: CharArray = this.ident(indent)
    sb.append(`in`).append("TagNode(")
    val size: Int = parts.size
    for(i in 0 until size) {
      val part: TagPart = parts[i]
      sb.append('\'').append(part.value()).append('\'')
      if(i != size - 1) {
        sb.append(", ")
      }
    }
    sb.append(") {\n")
    for(child in this.children()) {
      child.buildToString(sb, indent + 1)
    }
    sb.append(`in`).append("}\n")
    return sb
  }

  companion object {
    private fun genParts(
      token: Token,
      sourceMessage: String,
      templates: Map<String, Template>
    ): List<TagPart> {
      val parts: ArrayList<TagPart> = ArrayList()
      if(token.childTokens() != null) {
        for(childToken in token.childTokens()!!) {
          parts.add(TagPart(sourceMessage, childToken, templates))
        }
      }
      return parts
    }
  }

  /**
   * Creates a new element node.
   *
   * @param parent        the parent of this node
   * @param token         the token that created this node
   * @param sourceMessage the source message
   * @since 4.2.0
   */
  init {
    parts = genParts(token, sourceMessage, templates)
  }
}
