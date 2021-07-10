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

import net.kyori.adventure.text.minimessage.helper.Component
import net.kyori.adventure.text.minimessage.parser.node.ElementNode

/**
 * Carries needed context for minimessage around, ranging from debug info to the configured minimessage instance.
 *
 * @since 4.1.0
 */
class Context internal constructor(private val strict: Boolean, private val debugOutput: Appendable?, root: ElementNode?, ogMessage: String, replacedMessage: String?, miniMessage: MiniMessageImpl, templates: Array<out Template>) {
  private var root: ElementNode?
  private val ogMessage: String
  private var replacedMessage: String?
  private val miniMessage: MiniMessageImpl

  private val templates: Array<out Template>

  /**
   * Sets the root element.
   *
   * @param root the root element.
   * @since 4.1.0
   */
  fun root(root: ElementNode?) {
    this.root = root
  }

  /**
   * sets the replaced message.
   *
   * @param replacedMessage the replaced message
   * @since 4.1.0
   */
  fun replacedMessage(replacedMessage: String?) {
    this.replacedMessage = replacedMessage
  }

  /**
   * Returns strict mode.
   *
   * @return if strict mode is enabled
   * @since 4.1.0
   */
  fun strict(): Boolean {
    return strict
  }

  /**
   * Returns the appendable to print debug output to.
   *
   * @return the debug output to print to
   * @since 4.2.0
   */
  fun debugOutput(): Appendable? {
    return debugOutput
  }

  /**
   * Returns the root element.
   *
   * @return root
   * @since 4.1.0
   */
  fun tokens(): ElementNode? {
    return root
  }

  /**
   * Returns og message.
   *
   * @return ogMessage
   * @since 4.1.0
   */
  fun ogMessage(): String {
    return ogMessage
  }

  /**
   * Returns replaced message.
   *
   * @return replacedMessage
   * @since 4.1.0
   */
  fun replacedMessage(): String? {
    return replacedMessage
  }

  /**
   * Returns minimessage.
   *
   * @return minimessage
   * @since 4.1.0
   */
  fun miniMessage(): MiniMessageImpl {
    return miniMessage
  }

  /**
   * Parses a MiniMessage using all the settings of this context, including templates.
   *
   * @param message the message to parse
   * @return the parsed message
   * @since 4.1.0
   */
  fun parse(message: String): Component {
    return if(templates != null && templates.isNotEmpty()) {
      miniMessage.parse(message, templates)
    } else {
      miniMessage.parse(message)
    }
  }

  companion object {
    /**
     * Init.
     *
     * @param strict if strict mode is enabled
     * @param input the input message
     * @param miniMessage the minimessage instance
     * @return the debug context
     * @since 4.1.0
     */
    fun of(strict: Boolean, input: String, miniMessage: MiniMessageImpl): Context {
      return Context(strict, null, null, input, null, miniMessage, arrayOf())
    }

    /**
     * Init.
     *
     * @param strict if strict mode is enabled
     * @param debugOutput where to print debug output
     * @param input the input message
     * @param miniMessage the minimessage instance
     * @return the debug context
     * @since 4.1.0
     */
    fun of(strict: Boolean, debugOutput: Appendable?, input: String, miniMessage: MiniMessageImpl): Context {
      return Context(strict, debugOutput, null, input, null, miniMessage, arrayOf())
    }

    /**
     * Init.
     *
     * @param strict if strict mode is enabled
     * @param input the input message
     * @param miniMessage the minimessage instance
     * @param templates the templates passed to minimessage
     * @return the debug context
     * @since 4.1.0
     */
    fun of(strict: Boolean, input: String, miniMessage: MiniMessageImpl, templates: Array<out Template>): Context {
      return Context(strict, null, null, input, null, miniMessage, templates)
    }

    /**
     * Init.
     *
     * @param strict if strict mode is enabled
     * @param debugOutput where to print debug output
     * @param input the input message
     * @param miniMessage the minimessage instance
     * @param templates the templates passed to minimessage
     * @return the debug context
     * @since 4.2.0
     */
    fun of(strict: Boolean, debugOutput: Appendable?, input: String, miniMessage: MiniMessageImpl, templates: Array<out Template>): Context {
      return Context(strict, debugOutput, null, input, null, miniMessage, templates)
    }
  }

  init {
    this.root = root
    this.ogMessage = ogMessage
    this.replacedMessage = replacedMessage
    this.miniMessage = miniMessage
    this.templates = templates
  }
}
