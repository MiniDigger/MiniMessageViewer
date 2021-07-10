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
import net.kyori.adventure.text.minimessage.transformation.TransformationRegistry.Companion.empty

/**
 * A placeholder in a message, which can replace a tag with a component.
 *
 * @since 4.0.0
 */
interface Template {
  /**
   * Get the key for this template.
   *
   * @return the key
   * @since 4.2.0
   */
  fun key(): String

  /**
   * Get the value for this template.
   *
   * @return the value
   * @since 4.2.0
   */
  fun value(): Any

  /**
   * A template with a value that will be parsed as a MiniMessage string.
   *
   * @since 4.0.0
   */
  class StringTemplate internal constructor(private val key: String, private val value: String) : Template {

    override fun key(): String {
      return key
    }

    override fun value(): String {
      return value
    }

    override fun toString(): String {
      return "StringTemplate(key='$key', value='$value')"
    }
  }

  /**
   * A template with a [Component] value that will be inserted directly.
   *
   * @since 4.0.0
   */
  open class ComponentTemplate(private val key: String, value: Component) : Template {

    private val value: Component

    override fun key(): String {
      return key
    }

    override fun value(): Component {
      return value
    }

    override fun toString(): String {
      return "ComponentTemplate(key='$key', value=$value)"
    }

    init {
      this.value = value
    }
  }

  /**
   * A template with a lazily provided [Component] value that will be inserted directly.
   *
   * @since 4.2.0
   */
  class LazyComponentTemplate(key: String, value: () -> Component) : ComponentTemplate(key, Component.empty()) {

    private val value: () -> Component

    override fun value(): Component {
      return value.invoke()
    }

    override fun toString(): String {
      return "LazyComponentTemplate(value=$value)"
    }

    init {
      this.value = value
    }
  }

  companion object {
    /**
     * Constructs a template that gets replaced with a string.
     *
     * @param key the placeholder
     * @param value the value to replace the key with
     * @return the constructed template
     * @since 4.0.0
     */
    fun of(key: String, value: String): Template {
      return StringTemplate(key, value)
    }

    /**
     * Constructs a template that gets replaced with a component.
     *
     * @param key the placeholder
     * @param value the component to replace the key with
     * @return the constructed template
     * @since 4.0.0
     */
    fun of(key: String, value: Component): Template {
      return ComponentTemplate(key, value)
    }

    /**
     * Constructs a template that gets replaced with a component lazily.
     *
     * @param key the placeholder
     * @param value the supplier that supplies the component to replace the key with
     * @return the constructed template
     * @since 4.2.0
     */
    fun of(key: String, value: () -> Component): Template {
      return LazyComponentTemplate(key, value)
    }
  }
}
