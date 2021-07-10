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
import net.kyori.adventure.text.minimessage.markdown.MarkdownFlavor
import net.kyori.adventure.text.minimessage.transformation.TransformationRegistry


/**
 * MiniMessage is a textual representation of components.
 *
 *
 * This class allows you to serialize and deserialize them, strip
 * or escape them, and even supports a markdown like format.
 *
 * @since 4.0.0
 */
interface MiniMessage {
  /**
   * Escapes all tokens in the input message, so that they are ignored in deserialization.
   *
   *
   * Useful for untrusted input.
   *
   * @param input the input message, with tokens
   * @return the output, with escaped tokens
   * @since 4.0.0
   */
  fun escapeTokens(input: String): String

  /**
   * Removes all tokens in the input message.
   *
   *
   * Useful for untrusted input.
   *
   * @param input the input message, with tokens
   * @return the output, without tokens
   * @since 4.0.0
   */
  fun stripTokens(input: String): String

  /**
   * Parses a string into an component.
   *
   * @param input the input string
   * @return the output component
   * @since 4.0.0
   */
  fun parse(input: String): Component {
    return this.deserialize(input)
  }

  fun deserialize(input: String): Component

  /**
   * Parses a string into an component, allows passing placeholders in key value pairs.
   *
   * @param input the input string
   * @param placeholders the placeholders
   * @return the output component
   * @since 4.1.0
   */
  fun parse(input: String, vararg placeholders: String): Component

  /**
   * Parses a string into an component, allows passing placeholders in key value pairs.
   *
   * @param input the input string
   * @param placeholders the placeholders
   * @return the output component
   * @since 4.1.0
   */
  fun parse(input: String, placeholders: Map<String, String>): Component

  /**
   * Parses a string into an component, allows passing placeholders using key component pairs.
   *
   * @param input the input string
   * @param placeholders the placeholders
   * @return the output component
   * @since 4.1.0
   */
  fun parse(input: String, vararg placeholders: Any): Component

  /**
   * Parses a string into an component, allows passing placeholders using templates (which support components).
   * MiniMessage parses placeholders from following syntax: `<placeholder_name>` where placeholder_name is [Template.key]
   *
   * @param input the input string
   * @param placeholders the placeholders
   * @return the output component
   * @since 4.0.0
   */
  fun parse(input: String, vararg placeholders: Template): Component

  /**
   * Parses a string into an component, allows passing placeholders using templates (which support components).
   * MiniMessage parses placeholders from following syntax: `<placeholder_name>` where placeholder_name is [Template.key]
   *
   * @param input the input string
   * @param placeholders the placeholders
   * @return the output component
   * @since 4.0.0
   */
  fun parse(input: String, placeholders: List<Template>): Component

  /**
   * A builder for [MiniMessage].
   *
   * @since 4.0.0
   */
  interface Builder {
    /**
     * Adds markdown support.
     *
     * @return this builder
     * @since 4.0.0
     */
    fun markdown(): Builder

    /**
     * Uses the supplied transformation registry.
     *
     * @param transformationRegistry the transformation registry to use
     * @return this builder
     * @since 4.1.0
     */
    fun transformations(transformationRegistry: TransformationRegistry): Builder

    /**
     * Sets the markdown flavor that should be used to parse markdown.
     *
     * @param markdownFlavor the markdown flavor to use
     * @return this builder
     * @since 4.1.0
     */
    fun markdownFlavor(markdownFlavor: MarkdownFlavor): Builder

    /**
     * Sets the placeholder resolve that should handle all (unresolved) placeholders.
     * <br></br>
     * It needs to return a component
     *
     * @param placeholderResolver the markdown flavor to use
     * @return this builder
     * @since 4.1.0
     */
    fun placeholderResolver(placeholderResolver: (String) -> Component): Builder

    /**
     * Allows to enable strict mode (disabled by default)
     * <br></br>
     * By default, MiniMessage will allow non-[Inserting][net.kyori.adventure.text.minimessage.transformation.Inserting] tags to be implicitly closed. When strict mode
     * is enabled, all non-inserting tags which are `<opened>` must be explicitly `</closed>` as well.
     *
     * @param strict if strict mode should be enabled
     * @return this builder
     * @since 4.1.0
     */
    fun strict(strict: Boolean): Builder

    /**
     * Print debug information to the given output (disabled by default)
     * <br></br>
     * Debug output includes detailed information about the parsing process to help debug parser behavior.
     *
     * @param debugOutput if debug mode should be enabled
     * @return this builder
     * @since 4.2.0
     */
    fun debug(debugOutput: Appendable?): Builder

    /**
     * If in lenient mode, MiniMessage will output helpful messages. This method allows you to change how they should be printed. By default, they will be printed to standard out.
     *
     * @param consumer the error message consumer
     * @return this builder
     * @since 4.1.0
     */
    fun parsingErrorMessageConsumer(consumer: (List<String>) -> Unit): Builder

    /**
     * Builds the serializer.
     *
     * @return the built serializer
     * @since 4.0.0
     */
    fun build(): MiniMessage
  }

  companion object {
    /**
     * Gets a simple instance without markdown support.
     *
     * @return a simple instance
     * @since 4.0.0
     */
    fun get(): MiniMessage {
      return MiniMessageImpl.INSTANCE
    }

    /**
     * Gets an instance with markdown support.
     *
     *
     * Uses [net.kyori.adventure.text.minimessage.markdown.GithubFlavor].<br></br>
     * For other flavors, see [.withMarkdownFlavor] or the builder.
     *
     * @return a instance of markdown support
     * @since 4.0.0
     */
    fun markdown(): MiniMessage {
      return MiniMessageImpl.MARKDOWN
    }

    /**
     * Creates an custom instances with markdown supported by the given markdown flavor.
     *
     * @param markdownFlavor the markdown flavor
     * @return your very own custom MiniMessage instance
     * @since 4.0.0
     */
    fun withMarkdownFlavor(markdownFlavor: MarkdownFlavor): MiniMessage {
      return MiniMessageImpl(true, markdownFlavor, TransformationRegistry.builder().build(), MiniMessageImpl.DEFAULT_PLACEHOLDER_RESOLVER, false, null, MiniMessageImpl.DEFAULT_ERROR_CONSUMER)
    }

    /**
     * Creates a new [Builder].
     *
     * @return a builder
     * @since 4.0.0
     */
    fun builder(): Builder {
      return MiniMessageImpl.BuilderImpl()
    }
  }
}
