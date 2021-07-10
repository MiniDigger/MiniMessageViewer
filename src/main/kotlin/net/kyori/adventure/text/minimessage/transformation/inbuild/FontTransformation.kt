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
import net.kyori.adventure.text.minimessage.helper.Key
import net.kyori.adventure.text.minimessage.helper.Style
import net.kyori.adventure.text.minimessage.parser.ParsingException
import net.kyori.adventure.text.minimessage.parser.node.TagPart
import net.kyori.adventure.text.minimessage.transformation.Transformation
import net.kyori.adventure.text.minimessage.transformation.TransformationParser

/**
 * A decoration that applies a font name.
 *
 * @since 4.1.0
 */
class FontTransformation private constructor() : Transformation() {
  private var font: Key? = null

  override fun load(name: String, args: List<TagPart>) {
    super.load(name, args)
    if(args.size == 1) {
      val fontKey: String = args[0].value()
      font = Key.key(fontKey)
    }
    if(args.size != 2) {
      throw ParsingException("Doesn't know how to turn $args into a click event", *this.argTokenArray())
    }
    val namespaceKey: String = args[0].value()
    val fontKey: String = args[1].value()
    font = Key.key(namespaceKey, fontKey)
  }

  override fun apply(): Component {
    return Component.empty().style(Style.style().font(font))
  }

  override fun toString(): String {
    return "FontTransformation(font=$font)"
  }

  override fun equals(o: Any?): Boolean {
    if(this === o) return true
    if(o == null || this::class.js != o::class.js) return false

    o as FontTransformation

    if(font != o.font) return false

    return true
  }

  override fun hashCode(): Int {
    var result = 42
    result = 31 * result + (font.hashCode() ?: 0)
    return result
  }

  /**
   * Factory for [FontTransformation] instances.
   *
   * @since 4.1.0
   */
  class Parser : TransformationParser<FontTransformation> {

    override fun parse(): FontTransformation {
      return FontTransformation()
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
      return name.equals(Tokens.FONT, true)
    }
  }
}
