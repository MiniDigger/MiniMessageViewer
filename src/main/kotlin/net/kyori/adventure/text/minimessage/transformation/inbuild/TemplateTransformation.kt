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
package net.kyori.adventure.text.minimessage.transformation.inbuild

import net.kyori.adventure.text.minimessage.Template
import net.kyori.adventure.text.minimessage.helper.Component
import net.kyori.adventure.text.minimessage.transformation.Inserting
import net.kyori.adventure.text.minimessage.transformation.Transformation

/**
 * Inserts a formatted template component into the result.
 *
 * @since 4.1.0
 */
class TemplateTransformation(template: Template.ComponentTemplate) : Transformation(), Inserting {
  private val template: Template.ComponentTemplate

  override fun apply(): Component {
    return template.value()
  }
  override fun toString(): String {
    return "TemplateTransformation(template=$template)"
  }

  override fun equals(o: Any?): Boolean {
    if(this === o) return true
    if(o == null || this::class.js != o::class.js) return false
    if(!super.equals(o)) return false

    o as TemplateTransformation

    if(template != o.template) return false

    return true
  }

  override fun hashCode(): Int {
    var result = super.hashCode()
    result = 31 * result + template.hashCode()
    return result
  }

  /**
   * Create a new template transformation applying `template`.
   *
   * @param template the template to apply
   * @since 4.1.0
   */
  init {
    this.template = template
  }
}
