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
import net.kyori.adventure.text.minimessage.parser.ParsingException
import net.kyori.adventure.text.minimessage.parser.node.TagPart
import net.kyori.adventure.text.minimessage.transformation.TransformationRegistry.Builder
import net.kyori.adventure.text.minimessage.transformation.inbuild.TemplateTransformation

internal class TransformationRegistryImpl(types: List<TransformationType<out Transformation>>) : TransformationRegistry {
  companion object {
    private val DEFAULT_TRANSFORMATIONS: MutableList<TransformationType<out Transformation>> = ArrayList()
    var EMPTY: TransformationRegistry
    var STANDARD: TransformationRegistry

    init {
      DEFAULT_TRANSFORMATIONS.add(TransformationType.COLOR)
      DEFAULT_TRANSFORMATIONS.add(TransformationType.DECORATION)
      DEFAULT_TRANSFORMATIONS.add(TransformationType.HOVER_EVENT)
      DEFAULT_TRANSFORMATIONS.add(TransformationType.CLICK_EVENT)
      DEFAULT_TRANSFORMATIONS.add(TransformationType.KEYBIND)
      DEFAULT_TRANSFORMATIONS.add(TransformationType.TRANSLATABLE)
      DEFAULT_TRANSFORMATIONS.add(TransformationType.INSERTION)
      DEFAULT_TRANSFORMATIONS.add(TransformationType.FONT)
      DEFAULT_TRANSFORMATIONS.add(TransformationType.GRADIENT)
      DEFAULT_TRANSFORMATIONS.add(TransformationType.RAINBOW)
      EMPTY = TransformationRegistryImpl(listOf())
      STANDARD = TransformationRegistry.builder().build()
    }
  }

  private val types: List<TransformationType<out Transformation>>
  private fun tryLoad(transformation: Transformation, name: String, inners: List<TagPart>, context: Context): Transformation? {
    return try {
      transformation.context(context)
      transformation.load(name, inners.subList(1, inners.size))
      transformation
    } catch(exception: ParsingException) {
      exception.originalText(context.ogMessage())
      throw exception
    }
  }

  override operator fun get(name: String, inners: List<TagPart>, templates: Map<String, Template>, placeholderResolver: (String) -> Component?, context: Context): Transformation? {
    // first try if we have a custom placeholder resolver
    val potentialTemplate: Component? = placeholderResolver.invoke(name)
    if(potentialTemplate != null) {
      return tryLoad(TemplateTransformation(Template.ComponentTemplate(name, potentialTemplate)), name, inners, context)
    }
    // then check our registry
    for(type in types) {
      if(type.canParse.invoke(name)) {
        return tryLoad(type.parser.parse(), name, inners, context)
      } else if(templates.containsKey(name)) {
        val template: Template? = templates[name]
        // The parser handles StringTemplates
        if(template is Template.ComponentTemplate) {
          return tryLoad(TemplateTransformation(template), name, inners, context)
        }
      }
    }
    return null
  }

  override fun exists(name: String, placeholderResolver: (String) -> Component?): Boolean {
    // first check the placeholder resolver
    if(placeholderResolver.invoke(name) != null) {
      return true
    }
    // then check registry
    for(type in types) {
      if(type.canParse.invoke(name)) {
        return true
      }
    }
    return false
  }

  internal class BuilderImpl : Builder {
    private val types: MutableList<TransformationType<out Transformation>> = ArrayList(DEFAULT_TRANSFORMATIONS)

    override fun clear(): Builder {
      types.clear()
      return this
    }

    override fun add(transformation: TransformationType<Transformation>): Builder {
      types.add(transformation)
      return this
    }

    override fun add(vararg transformations: TransformationType<Transformation>): Builder {
      types.addAll(transformations)
      return this
    }

    override fun build(): TransformationRegistry {
      return TransformationRegistryImpl(types)
    }
  }

  /**
   * Create a transformation registry with the specified transformation types.
   *
   * @param types known transformation types
   * @since 4.1.0
   */
  init {
    this.types = types
  }
}
