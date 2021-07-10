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

import net.kyori.adventure.text.minimessage.MiniMessage.Builder
import net.kyori.adventure.text.minimessage.helper.Component
import net.kyori.adventure.text.minimessage.markdown.MarkdownFlavor
import net.kyori.adventure.text.minimessage.markdown.MiniMarkdownParser
import net.kyori.adventure.text.minimessage.transformation.TransformationRegistry

/**
 * not public api.
 *
 * @since 4.0.0
 */
class MiniMessageImpl internal constructor(private val markdown: Boolean, markdownFlavor: MarkdownFlavor, registry: TransformationRegistry, placeholderResolver: (String) -> Component?, strict: Boolean, debugOutput: Appendable?, parsingErrorMessageConsumer: (List<String>) -> Unit) : MiniMessage {
  private val markdownFlavor: MarkdownFlavor
  private val parser: MiniMessageParser
  private val strict: Boolean
  private val debugOutput: Appendable?
  private val parsingErrorMessageConsumer: (List<String>) -> Unit

  override fun deserialize(input: String): Component {
    var input = input
    if(markdown) {
      input = MiniMarkdownParser.parse(input, markdownFlavor)
    }
    return parser.parseFormat(input, Context.of(strict, debugOutput, input, this))
  }

  override fun parse(input: String, vararg placeholders: String): Component {
    var input = input
    if(markdown) {
      input = MiniMarkdownParser.parse(input, markdownFlavor)
    }
    return parser.parseFormat(input, Context.of(strict, debugOutput, input, this), *placeholders)
  }

  override fun parse(input: String, placeholders: Map<String, String>): Component {
    var input = input
    if(markdown) {
      input = MiniMarkdownParser.parse(input, markdownFlavor)
    }
    return parser.parseFormat(input, placeholders, Context.of(strict, debugOutput, input, this))
  }

  override fun parse(input: String, vararg placeholders: Any): Component {
    val templates: MutableList<Template> = ArrayList()
    var key: String? = null
    for(i in placeholders.indices) {
      val `object`: Any = placeholders[i]
      if(`object` is Template) {
        // add as a template directly
        templates.add(`object`)
      } else {
        // this is a `key=[string|component]` template
        key = if(key == null) {
          // get the key
          if(`object` is String) {
            `object`
          } else {
            throw IllegalArgumentException("Argument " + i + " in placeholders is key, must be String, was " + `object`::class.js)
          }
        } else {
          // get the value
          when(`object`) {
            is Component -> {
              templates.add(Template.of(key, `object`))
              null
            }
            is String -> {
              templates.add(Template.of(key, `object`))
              null
            }
            else -> {
              throw IllegalArgumentException("Argument " + i + " in placeholders is a value, must be Component or String, was " + `object`::class.js)
            }
          }
        }
      }
    }
    require(key == null) { "Found a key in placeholders that wasn't followed by a value: $key" }
    return this.parse(input, templates)
  }

  override fun parse(input: String, vararg placeholders: Template): Component {
    var input = input
    if(markdown) {
      input = MiniMarkdownParser.parse(input, markdownFlavor)
    }
    return parser.parseFormat(input, Context.of(strict, debugOutput, input, this, placeholders), *placeholders)
  }

  override fun parse(input: String, placeholders: List<Template>): Component {
    var input = input
    if(markdown) {
      input = MiniMarkdownParser.parse(input, markdownFlavor)
    }
    return parser.parseFormat(input, placeholders, Context.of(strict, debugOutput, input, this, placeholders.toTypedArray()))
  }

  override fun escapeTokens(input: String): String {
    return parser.escapeTokens(input)
  }

  override fun stripTokens(input: String): String {
    var input = input
    if(markdown) {
      input = MiniMarkdownParser.stripMarkdown(input, markdownFlavor)
    }
    return parser.stripTokens(input)
  }

  /**
   * not public api.
   *
   * @return huhu.
   * @since 4.1.0
   */

  fun parsingErrorMessageConsumer(): (List<String>) -> Unit {
    return parsingErrorMessageConsumer
  }

  fun toBuilder(): Builder {
    return BuilderImpl(this)
  }

  /* package */
  internal class BuilderImpl : Builder {
    private var markdown = false
    private var markdownFlavor: MarkdownFlavor = MarkdownFlavor.defaultFlavor()
    private var registry: TransformationRegistry = TransformationRegistry.standard()
    private var placeholderResolver: (String) -> Component? = DEFAULT_PLACEHOLDER_RESOLVER
    private var strict = false
    private var debug: Appendable? = null
    private var parsingErrorMessageConsumer: (List<String>) -> Unit = DEFAULT_ERROR_CONSUMER

    constructor() {}
    constructor(serializer: MiniMessageImpl) {
      markdown = serializer.markdown
    }

    override fun markdown(): Builder {
      markdown = true
      return this
    }

    override fun transformations(transformationRegistry: TransformationRegistry): Builder {
      registry = transformationRegistry
      return this
    }

    override fun markdownFlavor(markdownFlavor: MarkdownFlavor): Builder {
      this.markdownFlavor = markdownFlavor
      return this
    }

    override fun placeholderResolver(placeholderResolver: (String) -> Component): Builder {
      this.placeholderResolver = placeholderResolver
      return this
    }

    override fun strict(strict: Boolean): Builder {
      this.strict = strict
      return this
    }

    override fun debug(debugOutput: Appendable?): Builder {
      debug = debugOutput
      return this
    }

    override fun parsingErrorMessageConsumer(consumer: (List<String>) -> Unit): Builder {
      parsingErrorMessageConsumer = consumer
      return this
    }

    override fun build(): MiniMessage {
      return if(markdown) {
        MiniMessageImpl(true, markdownFlavor, registry, placeholderResolver, strict, debug, parsingErrorMessageConsumer)
      } else {
        MiniMessageImpl(false, MarkdownFlavor.defaultFlavor(), registry, placeholderResolver, strict, debug, parsingErrorMessageConsumer)
      }
    }
  }

  companion object {
    val DEFAULT_PLACEHOLDER_RESOLVER: (String) -> Component? = { s -> null }
    val DEFAULT_ERROR_CONSUMER: (List<String>) -> Unit = { message -> message.forEach { e -> println(e) } }
    val INSTANCE: MiniMessage = MiniMessageImpl(false, MarkdownFlavor.defaultFlavor(), TransformationRegistry.standard(), DEFAULT_PLACEHOLDER_RESOLVER, false, null, DEFAULT_ERROR_CONSUMER)
    val MARKDOWN: MiniMessage = MiniMessageImpl(true, MarkdownFlavor.defaultFlavor(), TransformationRegistry.standard(), DEFAULT_PLACEHOLDER_RESOLVER, false, null, DEFAULT_ERROR_CONSUMER)
  }

  init {
    this.markdownFlavor = markdownFlavor
    parser = MiniMessageParser(registry, placeholderResolver)
    this.strict = strict
    this.debugOutput = debugOutput
    this.parsingErrorMessageConsumer = parsingErrorMessageConsumer
  }
}
