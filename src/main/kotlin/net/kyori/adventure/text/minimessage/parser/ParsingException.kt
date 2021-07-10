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

/**
 * An exception that happens while parsing.
 *
 * @since 4.1.0
 */
class ParsingException : RuntimeException {

  private var originalText: String?
  private var tokens: Array<out Token>

  /**
   * Create a new parsing exception.
   *
   * @param message the detail message
   * @param originalText the origina text which was parsed
   * @param tokens the token which caused the error
   * @since 4.1.0
   */
  constructor(
    message: String,
    originalText: String,
    vararg tokens: Token
  ) : super(message) {
    this.tokens = tokens
    this.originalText = originalText
  }

  /**
   * Create a new parsing exception.
   *
   * @param message the detail message
   * @param originalText the origina text which was parsed
   * @param cause the cause
   * @param tokens the token which caused the error
   * @since 4.1.0
   */
  constructor(
    message: String,
    originalText: String?,
    cause: Throwable?,
    vararg tokens: Token
  ) : super(message, cause) {
    this.tokens = tokens
    this.originalText = originalText
  }

  /**
   * Create a new parsing exception.
   *
   * @param message the detail message
   * @param tokens the token which caused the error
   * @since 4.1.0
   */
  constructor(message: String, vararg tokens: Token) : this(message, null, null, *tokens) {}

  /**
   * Create a new parsing exception.
   *
   * @param message the detail message
   * @param cause the cause
   * @param tokens the token which caused the error
   * @since 4.1.0
   */
  constructor(
    message: String,
    cause: Throwable?,
    vararg tokens: Token
  ) : this(message, null, cause, *tokens) {
  }

  override val message: String
    get() {
      val arrowInfo = if(this.tokens().size != 0) """
	${arrow()}""" else ""
      val messageInfo = if(this.originalText() != null) """
	${this.originalText()}$arrowInfo""" else ""
      return super.message + messageInfo
    }

  /**
   * Get the message which caused this exception.
   *
   * @return the original message
   * @since 4.2.0
   */
  fun originalText(): String? {
    return originalText
  }

  /**
   * Set the message which caused this exception.
   *
   * @param originalText the original message
   * @since 4.2.0
   */
  fun originalText(originalText: String) {
    this.originalText = originalText
  }

  /**
   * Gets the tokens associated with this parsing error.
   *
   * @return the tokens for this error
   * @since 4.1.0
   */
  fun tokens(): Array<out Token> {
    return tokens
  }

  /**
   * Sets the tokens associated with this parsing error.
   *
   * @param tokens the tokens for this error
   * @since 4.2.0
   */
  fun tokens(tokens: Array<Token>) {
    this.tokens = tokens
  }

  private fun arrow(): String {
    val ts: Array<out Token> = this.tokens
    val chars = CharArray(ts[ts.size - 1].endIndex())
    var i = 0
    for(t in ts) {
      chars.fill(' ', i, t.startIndex())
      chars[t.startIndex()] = '^'
      chars.fill( '~', t.startIndex() + 1, t.endIndex() - 1)
      chars[t.endIndex() - 1] = '^'
      i = t.endIndex()
    }
    return chars.concatToString()
  }

  companion object {
    private const val serialVersionUID = 2507190809441787201L
  }
}
