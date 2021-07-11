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
package net.kyori.adventure.text.minimessage.transformation.inbuild

import net.kyori.adventure.text.minimessage.Tokens
import net.kyori.adventure.text.minimessage.helper.Component
import net.kyori.adventure.text.minimessage.helper.HoverEvent
import net.kyori.adventure.text.minimessage.helper.Key
import net.kyori.adventure.text.minimessage.parser.ParsingException
import net.kyori.adventure.text.minimessage.parser.node.TagPart
import net.kyori.adventure.text.minimessage.transformation.Transformation
import net.kyori.adventure.text.minimessage.transformation.TransformationParser

/**
 * A transformation that applies a [HoverEvent].
 *
 * @since 4.1.0
 */
class HoverTransformation private constructor() : Transformation() {
  private var value: Component? = null

  override fun load(name: String, args: List<TagPart>) {
    super.load(name, args)
    if(args.size < 2) {
      throw ParsingException("Doesn't know how to turn $args into a hover event", *this.argTokenArray())
    }
    val newArgs: List<TagPart> = args.let { args.subList(1, it.size) }
    value = if(args[0].value().equals("show_text", true)) {
      this.context!!.parse(newArgs[0].value())
    } else {
      throw ParsingException("Don't know how to turn '$args' into a hover event", *this.argTokenArray())
    }
  }

  override fun apply(): Component {
    val comp = Component.empty()
    comp.hoverEvent = HoverEvent(value!!)
    return comp
  }

  override fun toString(): String {
    return "HoverTransformation(value=$value)"
  }

  override fun equals(o: Any?): Boolean {
    if(this === o) return true
    if(o == null || this::class.js != o::class.js) return false

    o as HoverTransformation

    if(value != o.value) return false

    return true
  }

  override fun hashCode(): Int {
    var result = 42
    result = 31 * result + (value?.hashCode() ?: 0)
    return result
  }

  /**
   * Factory for [HoverTransformation] instances.
   *
   * @since 4.1.0
   */
  class Parser : TransformationParser<HoverTransformation> {

    override fun parse(): HoverTransformation {
      return HoverTransformation()
    }
  }

  companion object {
    /**
     * Get if this transformation can handle the provided tag name.
     *
     * @param name tag name to test
     * @return if this transformation is applicable
     * @since 4.1.0
     */
    fun canParse(name: String): Boolean {
      return name.equals(Tokens.HOVER, true)
    }
  }
}
