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
package net.kyori.adventure.text.minimessage.parser

import net.kyori.adventure.text.minimessage.Template
import net.kyori.adventure.text.minimessage.parser.TokenType.CLOSE_TAG
import net.kyori.adventure.text.minimessage.parser.TokenType.OPEN_TAG
import net.kyori.adventure.text.minimessage.parser.TokenType.TEXT
import net.kyori.adventure.text.minimessage.parser.node.ElementNode
import net.kyori.adventure.text.minimessage.parser.node.RootNode
import net.kyori.adventure.text.minimessage.parser.node.TagNode
import net.kyori.adventure.text.minimessage.parser.node.TagPart
import net.kyori.adventure.text.minimessage.parser.node.TemplateNode
import net.kyori.adventure.text.minimessage.parser.node.TextNode
import net.kyori.adventure.text.minimessage.transformation.Inserting
import net.kyori.adventure.text.minimessage.transformation.Transformation


/**
 * Handles parsing a string into a list of tokens and then into a tree of nodes.
 *
 * @since 4.2.0
 */
object TokenParser {
  /**
   * Parse a minimessage string into a tree of nodes.
   *
   * @param message the minimessage string to parse
   * @return the root of the resulting tree
   * @since 4.2.0
   */
  fun parse(
    transformationFactory: (TagNode) -> Transformation?,
    tagNameChecker: (String, Boolean) -> Boolean,
    templates: Map<String, Template>,
    message: String,
    strict: Boolean
  ): ElementNode {
    val tokens: List<Token> = parseFirstPass(message)
    parseSecondPass(message, tokens)
    return buildTree(transformationFactory, tagNameChecker, templates, tokens, message, strict)
  }

  /*
   * First pass over the text identifies valid tags and text blocks.
   */
  private fun parseFirstPass(message: String): List<Token> {
    val elements: ArrayList<Token> = ArrayList()
    var state = FirstPassState.NORMAL
    // If the current state is escaped then the next character is skipped
    var escaped = false
    var currentTokenEnd = 0
    // Marker is the starting index for the current token
    var marker = -1
    var currentStringChar = 0.toChar()
    val length: Int = message.length
    var i = 0
    while(i < length) {
      val codePoint: Char = message[i]
//      if(!Character.isBmpCodePoint(codePoint)) {
//        i++
//      }
      if(!escaped) {
        if(codePoint == '\\') {
          escaped = true
          i++
          continue
        }
      } else {
        escaped = false
        i++
        continue
      }
      when(state) {
        FirstPassState.NORMAL -> if(codePoint == '<') {
          // Possibly a tag
          marker = i
          state = FirstPassState.TAG
        }
        FirstPassState.TAG -> when(codePoint) {
          '>' -> {
            if(i == marker + 1) {
              // This is empty, <>, so it's not a tag
              state = FirstPassState.NORMAL
              break
            }

            // We found a tag
            if(currentTokenEnd != marker) {
              // anything not matched up to this point is normal text
              elements.add(Token(currentTokenEnd, marker, TEXT))
            }
            currentTokenEnd = i + 1

            // closing tags start with </
            var thisType: TokenType = OPEN_TAG
            if(boundsCheck(message, marker, 1) && message[marker + 1] == '/') {
              thisType = CLOSE_TAG
            }
            elements.add(Token(marker, currentTokenEnd, thisType))

            // <pre> tags put us into a state where we don't parse anything
            state = if(message.regionMatches(marker, "<pre>", 0, 5)) {
              FirstPassState.PRE
            } else {
              FirstPassState.NORMAL
            }
          }
          '<' ->               // This isn't a tag, but we can re-start looking here
            marker = i
          '\'', '"' -> {
            state = FirstPassState.STRING
            currentStringChar = codePoint.toChar()
          }
        }
        FirstPassState.PRE -> if(codePoint == '<') {
          if(message.regionMatches(i, "</pre>", 0, 6)) {
            // Anything inside the <pre>...</pre> is text
            elements.add(Token(currentTokenEnd, i, TEXT))
            // the </pre> is still a closing tag though
            elements.add(Token(i, i + 6, CLOSE_TAG))
            i += 6
            currentTokenEnd = i
            state = FirstPassState.NORMAL
          }
        }
        FirstPassState.STRING -> if(codePoint == currentStringChar) {
          state = FirstPassState.TAG
        }
      }
      i++
    }

    // anything left over is plain text
    if(elements.isEmpty()) {
      elements.add(Token(0, message.length, TEXT))
    } else {
      val end: Int = elements[elements.size - 1].endIndex()
      if(end != message.length) {
        elements.add(Token(end, message.length, TEXT))
      }
    }
    return elements
  }

  /*
   * Second pass over the tag tokens identifies tag parts
   */
  private fun parseSecondPass(message: String, tokens: List<Token>) {
    for(token in tokens) {
      val type: TokenType = token.type()
      if(type !== OPEN_TAG && type !== CLOSE_TAG) {
        continue
      }

      // Only look inside the tag <[/] and >
      val startIndex: Int = if(type === OPEN_TAG) token.startIndex() + 1 else token.startIndex() + 2
      val endIndex: Int = token.endIndex() - 1
      var state = SecondPassState.NORMAL
      var escaped = false
      var currentStringChar = 0.toChar()

      // Marker is the starting index for the current token
      var marker = startIndex
      var i = startIndex
      while(i < endIndex) {
        val codePoint: Char = message[i]
//        if(!Character.isBmpCodePoint(i)) {
//          i++
//        }
        if(!escaped) {
          if(codePoint == '\\') {
            escaped = true
            i++
            continue
          }
        } else {
          escaped = false
          i++
          continue
        }
        when(state) {
          SecondPassState.NORMAL ->             // Values are split by : unless it's in a URL
            if(codePoint == ':') {
              if(boundsCheck(message, i, 2) && message[i + 1] == '/' && message[i + 2] == '/') {
                break
              }
              if(marker == i) {
                // 2 colons side-by-side like <::> or <:text> or <text::text> would lead to this happening
                insert(token, Token(i, i, TokenType.TAG_VALUE))
                marker++
              } else {
                insert(token, Token(marker, i, TokenType.TAG_VALUE))
                marker = i + 1
              }
            } else if(codePoint == '\'' || codePoint == '"') {
              state = SecondPassState.STRING
              currentStringChar = codePoint.toChar()
            }
          SecondPassState.STRING -> if(codePoint == currentStringChar) {
            state = SecondPassState.NORMAL
          }
        }
        i++
      }

      // anything not matched is the final part
      if(token.childTokens() == null || token.childTokens()!!.isEmpty()) {
        insert(token, Token(startIndex, endIndex, TokenType.TAG_VALUE))
      } else {
        val end: Int = token.childTokens()!![token.childTokens()!!.size - 1].endIndex()
        if(end != endIndex) {
          insert(token, Token(end + 1, endIndex, TokenType.TAG_VALUE))
        }
      }
    }
  }

  /*
   * Build a tree from the OPEN_TAG and CLOSE_TAG tokens
   */
  private fun buildTree(
    transformationFactory: (TagNode) -> Transformation?,
    tagNameChecker: (String, Boolean) -> Boolean,
    templates: Map<String, Template>,
    tokens: List<Token>,
    message: String,
    strict: Boolean
  ): ElementNode {
    val root = RootNode(message)
    var node: ElementNode = root
    for(token in tokens) {
      val type: TokenType = token.type()
      when(type) {
        TEXT -> node.addChild(TextNode(node, token, message))
        OPEN_TAG -> {
          val tagNode = TagNode(node, token, message, templates)
          if(tagNode.name() == "reset") {
            // <reset> tags get special treatment and don't appear in the tree
            // instead, they close all currently open tags
            if(strict) {
              throw ParsingException("<reset> tags are not allowed when strict mode is enabled", message, token)
            }
            node = root
          } else if(tagNode.name() == "pre") {
            // <pre> tags also get special treatment and don't appear in the tree
            // anything inside <pre> is raw text, so just skip
            continue
          } else {
            val template: Template? = templates[tagNode.name()]
            if(template is Template.StringTemplate) {
              // String templates are inserted into the tree as raw text nodes, not parsed
              node.addChild(TemplateNode(node, token, message, template.value()))
            } else if(tagNameChecker.invoke(tagNode.name(), true)) {
              val transformation: Transformation? = transformationFactory.invoke(tagNode)
              if(transformation == null) {
                // something went wrong, ignore it
                // if strict mode is enabled this will throw an exception for us
                node.addChild(TextNode(node, token, message))
              } else {
                // This is a recognized tag, goes in the tree
                tagNode.transformation(transformation)
                node.addChild(tagNode)
                if(transformation !is Inserting) {
                  // this tag has children
                  node = tagNode
                }
              }
            } else {
              // not recognized, plain text
              node.addChild(TextNode(node, token, message))
            }
          }
        }
        CLOSE_TAG -> {
          val childTokens: List<Token> = token.childTokens()!!
          check(!childTokens.isEmpty()) {
            "CLOSE_TAG token somehow has no children - " +
              "the parser should not allow this. Original text: " + message
          }
          val closeValues: ArrayList<String> = ArrayList(childTokens.size)
          for(childToken in childTokens) {
            closeValues.add(TagPart.unquoteAndEscape(message, childToken.startIndex(), childToken.endIndex()))
          }
          val closeTagName = closeValues[0]
          if(closeTagName.equals("reset") || closeTagName.equals("pre")) {
            // These are synthetic nodes, closing them means nothing in the context of building a tree
            continue
          }
          if(!tagNameChecker.invoke(closeTagName, false)) {
            // tag does not exist, so treat it as text
            node.addChild(TextNode(node, token, message))
            continue
          }
          var parentNode: ElementNode? = node
          while(parentNode is TagNode) {
            val openParts: List<TagPart> = parentNode.parts()
            if(tagCloses(closeValues, openParts)) {
              if(parentNode !== node && strict) {
                val msg = "Unclosed tag encountered; " + (node as TagNode).name() + " is not closed, because " +
                  closeValues[0] + " was closed first."
                throw ParsingException(msg, message, parentNode.token(), node.token(), token)
              }
              node = parentNode.parent() ?: throw IllegalStateException(
                "Root node matched with close tag value, " +
                  "this should not be possible. Original text: " + message
              )
              break
            }
            parentNode = parentNode.parent()
            if(parentNode == null || parentNode is RootNode) {
              // This means the closing tag didn't match to anything
              // Since open tags which don't match to anything is never an error, neither is this
              node.addChild(TextNode(node, token, message))
              break
            }
          }
        }
      }
    }
    if(strict && root !== node) {
      val openTags: ArrayList<TagNode> = ArrayList()
      run {
        var n: ElementNode? = node
        while(n != null) {
          if(n is TagNode) {
            openTags.add(n)
          } else {
            break
          }
          n = n.parent()
        }
      }
      val errorTokens: MutableList<Token> = mutableListOf()
      val sb = StringBuilder(
        "All tags must be explicitly closed while in strict mode. " +
          "End of string found with open tags: "
      )
      var i = 0
      val iter: ListIterator<TagNode> = openTags.listIterator(openTags.size)
      while(iter.hasPrevious()) {
        val n: TagNode = iter.previous()
        errorTokens[i++] = n.token()
        sb.append(n.name())
        if(iter.hasPrevious()) {
          sb.append(", ")
        }
      }
      throw ParsingException(sb.toString(), message, *errorTokens.toTypedArray())
    }
    return root
  }

  /**
   * Determine if a set of close string parts closes the given list of open tag parts. If the open parts starts with
   * the set of close parts, then this method returns `true`.
   *
   * @param closeParts The parts of the close tag
   * @param openParts The parts of the open tag
   * @return `true` if the given close parts closes the open tag parts.
   */
  private fun tagCloses(closeParts: List<String>, openParts: List<TagPart>): Boolean {
    if(closeParts.size > openParts.size) {
      return false
    }
    for(i in closeParts.indices) {
      if(closeParts[i] != openParts[i].value()) {
        return false
      }
    }
    return true
  }

  /**
   * Returns `true` if it's okay to check for characters up to the given length. Returns `false` if the
   * string is too short.
   *
   * @param text The string to check.
   * @param index The index to start from.
   * @param length The length to check.
   * @return `true` if the string's length is at least as long as `index + length`.
   */
  private fun boundsCheck(text: String, index: Int, length: Int): Boolean {
    return index + length < text.length
  }

  /**
   * Optimized insert method for adding child tokens to the given `token`.
   *
   * @param token The token to add `value` as a child.
   * @param value The token to add to `token`.
   */
  private fun insert(token: Token, value: Token) {
    if(token.childTokens() == null) {
      token.childTokens(mutableListOf(value))
      return
    }
    if(token.childTokens()!!.size == 1) {
      val list: ArrayList<Token> = ArrayList(3)
      list.add(token.childTokens()!![0])
      list.add(value)
      token.childTokens(list)
    } else {
      token.childTokens()!!.add(value)
    }
  }

  /**
   * Removes escaping `'\`` characters from a substring. In general, this removes all `'\`` characters,
   * though the pattern `'\\'` will be replaced with `'\'`.
   *
   * @param text the input text
   * @param startIndex the starting index of the substring
   * @param endIndex the ending index of the substring
   * @return the output escaped substring
   * @since 4.2.0
   */
  fun unescape(text: String, startIndex: Int, endIndex: Int): String {
    var from = startIndex
    var i = text.indexOf('\\', from)
    if(i == -1 || i >= endIndex) {
      return text.substring(from, endIndex)
    }
    val sb = StringBuilder(endIndex - startIndex)
    while(i != -1 && i < endIndex) {
      sb.append(text, from, i)
      i++
      if(i >= endIndex) {
        from = endIndex
        break
      }
      val codePoint: Char = text[i]
      sb.append(codePoint)
      i++ // TODO idk if 1 or 2
//      sb.appendCodePoint(codePoint)
//      i += if(Character.isBmpCodePoint(codePoint)) {
//        1
//      } else {
//        2
//      }
      if(i >= endIndex) {
        from = endIndex
        break
      }
      from = i
      i = text.indexOf('\\', from)
    }
    sb.append(text, from, endIndex)
    return sb.toString()
  }

  internal enum class FirstPassState {
    NORMAL, TAG, PRE, STRING
  }

  internal enum class SecondPassState {
    NORMAL, STRING
  }
}
