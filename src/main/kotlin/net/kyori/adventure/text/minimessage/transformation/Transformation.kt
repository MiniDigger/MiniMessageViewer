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
package net.kyori.adventure.text.minimessage.transformation

import net.kyori.adventure.text.minimessage.Context
import net.kyori.adventure.text.minimessage.helper.Component
import net.kyori.adventure.text.minimessage.helper.Style
import net.kyori.adventure.text.minimessage.parser.Token
import net.kyori.adventure.text.minimessage.parser.node.TagPart

/**
 * A transformation that can be applied while parsing a message.
 *
 *
 * A transformation instance is created for each instance of a tag in a parsed string.
 *
 * @see TransformationRegistry to access and register available transformations
 *
 * @since 4.1.0
 */
abstract class Transformation protected constructor() {
  private var name: String? = null
  private var args: List<TagPart>? = null
  protected var context: Context? = null

  /**
   * Initialize this transformation with a tag name and tokens.
   *
   * @param name the alias for this transformation
   * @param args tokens within the tags, used to define arguments. Each
   * @since 4.1.0
   */
  open fun load(name: String, args: List<TagPart>) {
    this.name = name
    this.args = args
  }

  /**
   * The tag alias used to refer to this instance.
   *
   * @return the name
   * @since 4.1.0
   */
  fun name(): String? {
    return name
  }

  /**
   * The arguments making up this instance.
   *
   * @return the args
   * @since 4.2.0
   */
  fun args(): List<TagPart>? {
    return args
  }

  /**
   * Returns the tokens which make up the arguments as an array.
   *
   * @return the arg tokens
   * @since 4.2.0
   */
  fun argTokenArray(): Array<Token> {
    return args?.map(TagPart::token)?.toTypedArray()!!
  }

  /**
   * Return a transformed `component` based on the applied parameters.
   *
   * @return the transformed component
   * @since 4.1.0
   */
  abstract fun apply(): Component
  fun context(context: Context?) {
    this.context = context
  }

  abstract override fun toString(): String
  abstract override fun equals(o: Any?): Boolean
  abstract override fun hashCode(): Int

  protected fun merge(target: Component, template: Component): Component {
    TODO("not implemented")
//    var result: Component = target.style(target.style().merge(template.style(), Style.Merge.Strategy.IF_ABSENT_ON_TARGET, Style.Merge.all()))
//    if(template.hoverEvent() != null) {
//      result = result.hoverEvent(template.hoverEvent())
//    }
//    if(template.clickEvent() != null) {
//      result = result.clickEvent(template.clickEvent())
//    }
//    if(template.insertion() != null) {
//      result = result.insertion(template.insertion())
//    }
//    return result
  }
}
