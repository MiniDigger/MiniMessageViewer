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

import kotlin.math.PI
import kotlin.math.sin
import net.kyori.adventure.text.minimessage.Tokens
import net.kyori.adventure.text.minimessage.helper.Component
import net.kyori.adventure.text.minimessage.helper.TextColor
import net.kyori.adventure.text.minimessage.helper.TextComponent
import net.kyori.adventure.text.minimessage.parser.ParsingException
import net.kyori.adventure.text.minimessage.parser.node.ElementNode
import net.kyori.adventure.text.minimessage.parser.node.TagPart
import net.kyori.adventure.text.minimessage.parser.node.ValueNode
import net.kyori.adventure.text.minimessage.transformation.Modifying
import net.kyori.adventure.text.minimessage.transformation.Transformation
import net.kyori.adventure.text.minimessage.transformation.TransformationParser

/**
 * Applies rainbow color to a component.
 *
 * @since 4.1.0
 */
class RainbowTransformation private constructor() : Transformation(), Modifying {
  private var size = 0
  private var disableApplyingColorDepth = -1
  private var colorIndex = 0
  private var center = 128f
  private var width = 127f
  private var frequency = 1.0
  private var phase = 0

  override fun load(name: String, args: List<TagPart>) {
    super.load(name, args)
    if(args.size == 1) {
      try {
        phase = args[0].value().toInt()
      } catch(ex: NumberFormatException) {
        throw ParsingException("Expected phase, got " + args[0], *this.argTokenArray())
      }
    }
  }

  override fun visit(curr: ElementNode?) {
    if(curr is ValueNode) {
      val value: String = curr.value()
      size += value.length
    }
  }

  override fun apply(): Component {
    // init
    center = 128f
    width = 127f
    frequency = PI * 2 / size
    return Component.empty()
  }

  override fun apply(current: Component, depth: Int): Component {
    if(disableApplyingColorDepth != -1 && depth >= disableApplyingColorDepth || current.style().color() != null) {
      if(disableApplyingColorDepth == -1) {
        disableApplyingColorDepth = depth
      }
      // This component has it's own color applied, which overrides ours
      // We still want to keep track of where we are though if this is text
      if(current is TextComponent) {
        val content: String = current.content()
        val len: Int = content.length
        for(i in 0 until len) {
          // increment our color index
          color(phase.toFloat())
        }
      }
      return current
    }
    disableApplyingColorDepth = -1
    if(current is TextComponent && current.content().isNotEmpty()) {
      val textComponent: TextComponent = current
      val content: String = textComponent.content()
      var parent: Component = Component.empty()

      // apply
      val holder = CharArray(1)
      val it = content.toCharArray().iterator()
      while(it.hasNext()) {
        holder[0] = it.nextChar()
        val comp: Component = Component.text(holder.concatToString(0, 1), color(phase.toFloat()))
        parent = parent.append(comp)
      }
      return parent
    }
    return Component.empty().mergeStyle(current)
  }

  private fun color(phase: Float): TextColor {
    val index = colorIndex++
    val red = (sin(frequency * index + 2 + phase) * width + center).toInt()
    val green = (sin(frequency * index + 0 + phase) * width + center).toInt()
    val blue = (sin(frequency * index + 4 + phase) * width + center).toInt()
    return TextColor.color(red, green, blue)
  }

  override fun toString(): String {
    return "RainbowTransformation(size=$size, disableApplyingColorDepth=$disableApplyingColorDepth, colorIndex=$colorIndex, center=$center, width=$width, frequency=$frequency, phase=$phase)"
  }

  override fun equals(o: Any?): Boolean {
    if(this === o) return true
    if(o == null || this::class.js != o::class.js) return false
    if(!super.equals(o)) return false

    o as RainbowTransformation

    if(size != o.size) return false
    if(disableApplyingColorDepth != o.disableApplyingColorDepth) return false
    if(colorIndex != o.colorIndex) return false
    if(center != o.center) return false
    if(width != o.width) return false
    if(frequency != o.frequency) return false
    if(phase != o.phase) return false

    return true
  }

  override fun hashCode(): Int {
    var result = super.hashCode()
    result = 31 * result + size
    result = 31 * result + disableApplyingColorDepth
    result = 31 * result + colorIndex
    result = 31 * result + center.hashCode()
    result = 31 * result + width.hashCode()
    result = 31 * result + frequency.hashCode()
    result = 31 * result + phase
    return result
  }

  /**
   * Factory for [RainbowTransformation] instances.
   *
   * @since 4.1.0
   */
  class Parser : TransformationParser<RainbowTransformation> {

    override fun parse(): RainbowTransformation {
      return RainbowTransformation()
    }
  }

  companion object {
    /**
     * Get if this transformation can handle the provided tag name.
     *
     * @param name tag name to test
     * @return if this transformation is applicable
     * @since 4.1.0
     */
    fun canParse(name: String): Boolean {
      return name.equals(Tokens.RAINBOW, true)
    }
  }
}
