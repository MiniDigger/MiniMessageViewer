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
package net.kyori.adventure.text.minimessage.transformation

import net.kyori.adventure.text.minimessage.Context
import net.kyori.adventure.text.minimessage.Template
import net.kyori.adventure.text.minimessage.helper.Component
import net.kyori.adventure.text.minimessage.parser.node.TagPart

/**
 * A registry of transformation types understood by the MiniMessage parser.
 *
 * @since 4.1.0
 */
interface TransformationRegistry {
  /**
   * Get a transformation from this registry based on the current state.
   *
   * @param name tag name
   * @param inners tokens that make up the tag arguments
   * @param templates available templates
   * @param placeholderResolver function to resolve other component types
   * @param context the debug context
   * @return a possible transformation
   * @since 4.1.0
   */
  operator fun get(name: String, inners: List<TagPart>, templates: Map<String, Template>, placeholderResolver: (String) -> Component?, context: Context): Transformation?

  /**
   * Test if any registered transformation type matches the provided key.
   *
   * @param name tag name
   * @param placeholderResolver function to resolve other component types
   * @return whether any transformation exists
   * @since 4.1.0
   */
  fun exists(name: String, placeholderResolver: (String) -> Component?): Boolean

  /**
   * A builder for [TransformationRegistry].
   *
   * @since 4.2.0
   */
  interface Builder {
    /**
     * Clears all currently set transformations.
     *
     * @return this builder
     * @since 4.2.0
     */
    fun clear(): Builder?

    /**
     * Adds a supplied transformation to the registry.
     *
     * @return this builder
     * @since 4.2.0
     */
    fun add(transformation: TransformationType<Transformation>): Builder?

    /**
     * Adds the supplied transformations to the registry.
     *
     * @return this builder
     * @since 4.2.0
     */
    fun add(vararg transformations: TransformationType<Transformation>): Builder?
    fun build(): TransformationRegistry
  }

  companion object {
    /**
     * Creates a new [Builder].
     *
     * @return a builder
     * @since 4.2.0
     */
    fun builder(): Builder {
      return TransformationRegistryImpl.BuilderImpl()
    }

    /**
     * Gets an instance of the transformation registry without any transformations.
     *
     * @return a empty transformation registry
     * @since 4.2.0
     */
    fun empty(): TransformationRegistry {
      return TransformationRegistryImpl.EMPTY
    }

    /**
     * Gets an instance of the transformation registry with only the standard transformations.
     *
     * @return a standard transformation registry
     * @since 4.2.0
     */
    fun standard(): TransformationRegistry {
      return TransformationRegistryImpl.STANDARD
    }
  }
}
