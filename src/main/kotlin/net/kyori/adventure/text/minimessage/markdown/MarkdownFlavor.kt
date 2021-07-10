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
package net.kyori.adventure.text.minimessage.markdown

/**
 * A type of markdown.
 *
 * @since 4.1.0
 */
interface MarkdownFlavor {
  /**
   * Whether a pair of format characters indicate bolding.
   *
   * @param current the character being inspected
   * @param next the next character
   * @return whether the characters delimit a bold block
   * @since 4.1.0
   */
  fun isBold(current: Char, next: Char): Boolean

  /**
   * Whether a pair of format characters indicate italics.
   *
   * @param current the character being inspected
   * @param next the next character
   * @return whether the characters delimit an italics block
   * @since 4.1.0
   */
  fun isItalic(current: Char, next: Char): Boolean

  /**
   * Whether a pair of format characters indicate an underline.
   *
   * @param current the character being inspected
   * @param next the next character
   * @return whether the characters delimit an underlined block
   * @since 4.1.0
   */
  fun isUnderline(current: Char, next: Char): Boolean

  /**
   * Whether a pair of format characters indicate a strikethrough.
   *
   * @param current the character being inspected
   * @param next the next character
   * @return whether the characters delimit a strikethrough block
   * @since 4.1.0
   */
  fun isStrikeThrough(current: Char, next: Char): Boolean

  /**
   * Whether a pair of format characters indicate an obfuscated block.
   *
   *
   * These may also be described as spoiler blocks.
   *
   * @param current the character being inspected
   * @param next the next character
   * @return whether the characters delimit an obfuscated block
   * @since 4.1.0
   */
  fun isObfuscate(current: Char, next: Char): Boolean

  companion object {
    /**
     * Get the default markdown flavour.
     *
     *
     * This is currently the [LegacyFlavor] for backwards compatibility,
     * but will be changed to [in the near future][DiscordFlavor].
     *
     * @return the default flavour
     * @since 4.1.0
     */
    fun defaultFlavor(): MarkdownFlavor {
      return LegacyFlavor.get() // TODO: change the default to DiscordFlavor in a few releases
    }
  }
}
