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
import net.kyori.adventure.text.minimessage.helper.NamedTextColor
import net.kyori.adventure.text.minimessage.helper.TextColor
import net.kyori.adventure.text.minimessage.parser.ParsingException
import net.kyori.adventure.text.minimessage.parser.node.TagPart
import net.kyori.adventure.text.minimessage.transformation.Transformation
import net.kyori.adventure.text.minimessage.transformation.TransformationParser

/**
 * A transformation applying a single text color.
 *
 * @since 4.1.0
 */
class ColorTransformation private constructor() : Transformation() {
  companion object {
    private val colorAliases: MutableMap<String, String> = HashMap()

    /**
     * Get if this transformation can handle the provided tag name.
     *
     * @param name tag name to test
     * @return if this transformation is applicable
     * @since 4.1.0
     */
    fun canParse(name: String): Boolean {
      return (name.equals(Tokens.COLOR, true)
        || name.equals(Tokens.COLOR_2, true)
        || name.equals(Tokens.COLOR_3, true)
        || TextColor.fromHexString(name) != null || NamedTextColor.NAMES[name.lowercase()] != null || colorAliases.containsKey(name))
    }

    init {
      colorAliases["dark_grey"] = "dark_gray"
      colorAliases["grey"] = "gray"
    }
  }

  private var color: TextColor? = null

  override fun load(name: String, args: List<TagPart>) {
    var name = name
    super.load(name, args)
    if(name.equals(Tokens.COLOR, true)) {
      name = if(args.size == 1) {
        args[0].value()
      } else {
        throw ParsingException("Expected to find a color parameter, but found $args", *this.argTokenArray())
      }
    }
    if(colorAliases.containsKey(name)) {
      name = colorAliases[name]!!
    }
    color = if(name[0] == '#') {
      TextColor.fromHexString(name)
    } else {
      NamedTextColor.NAMES[name.lowercase()]
    }
    if(color == null) {
      throw ParsingException("Don't know how to turn '$name' into a color", *this.argTokenArray())
    }
  }

  override fun apply(): Component {
    return Component.empty().color(color!!)
  }

  override fun toString(): String {
    return "ColorTransformation(color=$color)"
  }

  override fun equals(o: Any?): Boolean {
    if(this === o) return true
    if(o == null || this::class.js != o::class.js) return false

    o as ColorTransformation

    if(color != o.color) return false

    return true
  }

  override fun hashCode(): Int {
    var result = 42
    result = 31 * result + (color.hashCode() ?: 0)
    return result
  }

  /**
   * Factory for [ColorTransformation] instances.
   *
   * @since 4.1.0
   */
  class Parser : TransformationParser<ColorTransformation> {

    override fun parse(): ColorTransformation {
      return ColorTransformation()
    }
  }
}
