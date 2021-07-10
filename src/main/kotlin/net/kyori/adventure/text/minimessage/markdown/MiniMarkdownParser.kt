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

import net.kyori.adventure.text.minimessage.Tokens

/**
 * Internal class for markdown handling.
 *
 * @since 4.0.0
 */
object MiniMarkdownParser {
  /**
   * Strip any markdown formatting that would be interpreted by `markdownFlavor`.
   *
   * @param input the input string
   * @param markdownFlavor markdown flavor to test against
   * @return the stripped input
   * @since 4.1.0
   */
  fun stripMarkdown(input: String, markdownFlavor: MarkdownFlavor): String {
    return handle(input, true, markdownFlavor)
  }

  /**
   * Parse the markdown input and return it as a MiniMessage string.
   *
   * @param input the input string
   * @param markdownFlavor the flavour of markdown to recognize
   * @return a modified string
   * @since 4.1.0
   */
  fun parse(input: String, markdownFlavor: MarkdownFlavor): String {
    return handle(input, false, markdownFlavor)
  }

  private fun handle(input: String, strip: Boolean, markdownFlavor: MarkdownFlavor): String {
    val sb = StringBuilder()
    var bold = -1
    var boldSkip: Insert? = null
    var italic = -1
    var italicSkip: Insert? = null
    var underline = -1
    var underlineSkip: Insert? = null
    var strikeThrough = -1
    var strikeThroughSkip: Insert? = null
    var obfuscate = -1
    var obfuscateSkip: Insert? = null
    val inserts: MutableList<Insert> = ArrayList()
    var skip = 0
    var i = 0
    while(i + skip < input.length) {
      val currIndex = i + skip
      val c: Char = input[currIndex]
      val n = next(currIndex, input)
      var shouldSkip = false
      if(markdownFlavor.isBold(c, n)) {
        if(bold == -1) {
          bold = sb.length
          boldSkip = Insert(sb.length, c.toString() + "")
        } else {
          inserts.add(Insert(bold, "<" + Tokens.BOLD + ">"))
          inserts.add(Insert(sb.length, "</" + Tokens.BOLD + ">"))
          bold = -1
        }
        skip += if(c == n) 1 else 0
        shouldSkip = true
      } else if(markdownFlavor.isItalic(c, n)) {
        if(italic == -1) {
          italic = sb.length
          italicSkip = Insert(sb.length, c.toString() + "")
        } else {
          inserts.add(Insert(italic, "<" + Tokens.ITALIC + ">"))
          inserts.add(Insert(sb.length, "</" + Tokens.ITALIC + ">"))
          italic = -1
        }
        skip += if(c == n) 1 else 0
        shouldSkip = true
      } else if(markdownFlavor.isUnderline(c, n)) {
        if(underline == -1) {
          underline = sb.length
          underlineSkip = Insert(sb.length, c.toString() + "")
        } else {
          inserts.add(Insert(underline, "<" + Tokens.UNDERLINED + ">"))
          inserts.add(Insert(sb.length, "</" + Tokens.UNDERLINED + ">"))
          underline = -1
        }
        skip += if(c == n) 1 else 0
        shouldSkip = true
      } else if(markdownFlavor.isStrikeThrough(c, n)) {
        if(strikeThrough == -1) {
          strikeThrough = sb.length
          strikeThroughSkip = Insert(sb.length, c.toString() + "")
        } else {
          inserts.add(Insert(strikeThrough, "<" + Tokens.STRIKETHROUGH + ">"))
          inserts.add(Insert(sb.length, "</" + Tokens.STRIKETHROUGH + ">"))
          strikeThrough = -1
        }
        skip += if(c == n) 1 else 0
        shouldSkip = true
      } else if(markdownFlavor.isObfuscate(c, n)) {
        if(obfuscate == -1) {
          obfuscate = sb.length
          obfuscateSkip = Insert(sb.length, c.toString() + "")
        } else {
          inserts.add(Insert(obfuscate, "<" + Tokens.OBFUSCATED + ">"))
          inserts.add(Insert(sb.length, "</" + Tokens.OBFUSCATED + ">"))
          obfuscate = -1
        }
        skip += if(c == n) 1 else 0
        shouldSkip = true
      }
      if(!shouldSkip) {
        sb.append(c)
      }
      i++
    }
    if(strip) {
      inserts.clear()
    } else {
      inserts.sortedWith(compareBy { obj: Insert -> obj.pos() }.thenByDescending { obj: Insert -> obj.value() })
    }
    if(underline != -1) {
      inserts.add(underlineSkip!!)
    }
    if(bold != -1) {
      inserts.add(boldSkip!!)
    }
    if(italic != -1) {
      inserts.add(italicSkip!!)
    }
    if(strikeThrough != -1) {
      inserts.add(strikeThroughSkip!!)
    }
    if(obfuscate != -1) {
      inserts.add(obfuscateSkip!!)
    }
    for(el in inserts) {
      sb.insert(el.pos(), el.value())
    }
    return sb.toString()
  }

  private fun next(index: Int, input: String): Char {
    return if(index < input.length - 1) {
      input[index + 1]
    } else {
      ' '
    }
  }

  internal class Insert(private val pos: Int, private val value: String) {
    fun pos(): Int {
      return pos
    }

    fun value(): String {
      return value
    }

    override fun toString(): String {
      return "Insert(pos=$pos, value='$value')"
    }
  }
}
