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
package net.kyori.adventure.text.minimessage

import net.kyori.adventure.text.minimessage.MiniMessageImpl.BuilderImpl

/**
 * Tokens used in the MiniMessage format.
 *
 * @since 4.0.0
 */
object Tokens {
  // vanilla components
  const val CLICK = "click"
  const val HOVER = "hover"
  const val KEYBIND = "key"
  const val TRANSLATABLE = "lang"
  const val TRANSLATABLE_2 = "translate"
  const val TRANSLATABLE_3 = "tr"
  const val INSERTION = "insert"
  const val COLOR = "color"
  const val COLOR_2 = "colour"
  const val COLOR_3 = "c"
  const val HEX = "#"
  const val FONT = "font"

  // vanilla decoration
  const val UNDERLINED = "underlined"
  const val UNDERLINED_2 = "u"
  const val STRIKETHROUGH = "strikethrough"
  const val STRIKETHROUGH_2 = "st"
  const val OBFUSCATED = "obfuscated"
  const val OBFUSCATED_2 = "obf"
  const val ITALIC = "italic"
  const val ITALIC_2 = "em"
  const val ITALIC_3 = "i"
  const val BOLD = "bold"
  const val BOLD_2 = "b"
  const val RESET = "reset"
  const val RESET_2 = "r"
  const val PRE = "pre"

  // minimessage components
  const val RAINBOW = "rainbow"
  const val GRADIENT = "gradient"

  // minimessage tags
  const val TAG_START = "<"
  const val TAG_END = ">"
  const val CLOSE_TAG = "/"
  const val SEPARATOR = ":"
}
