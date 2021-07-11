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

import kotlin.math.round
import net.kyori.adventure.text.minimessage.Tokens
import net.kyori.adventure.text.minimessage.helper.Component
import net.kyori.adventure.text.minimessage.helper.NamedTextColor
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
 * A transformation that applies a colour gradient.
 *
 * @since 4.1.0
 */
class GradientTransformation private constructor() : Transformation(), Modifying {
  private var size = 0
  private var disableApplyingColorDepth = -1
  private var index = 0
  private var colorIndex = 0
  private var factorStep = 0f
  private var colors: Array<TextColor> = emptyArray()
  private var phase = 0f
  private var negativePhase = false

  override fun load(name: String, args: List<TagPart>) {
    super.load(name, args)
    if(args.isNotEmpty()) {
      val textColors: MutableList<TextColor> = ArrayList()
      for(i in args.indices) {
        val arg: String = args[i].value()
        // last argument? maybe this is the phase?
        if(i == args.size - 1) {
          try {
            phase = arg.toFloat()
            if(phase < -1f || phase > 1f) {
              throw ParsingException("Gradient phase is out of range ($phase). Must be in the range [-1.0f, 1.0f] (inclusive).", *this.argTokenArray())
            }
            if(phase < 0) {
              negativePhase = true
              phase += 1
            }
            break
          } catch(ignored: NumberFormatException) {
          }
        }
        val parsedColor: TextColor? = if(arg[0] == '#') {
          TextColor.fromHexString(arg)
        } else {
          NamedTextColor.NAMES[arg.lowercase()]
        }
        if(parsedColor == null) {
          throw ParsingException("Unable to parse a color from '$arg'. Please use NamedTextColors or Hex colors.", *this.argTokenArray())
        }
        textColors.add(parsedColor)
      }
      if(textColors.size < 2) {
        throw ParsingException("Invalid gradient, not enough colors. Gradients must have at least two colors.", *this.argTokenArray())
      }
      colors = textColors.toTypedArray()
      if(negativePhase) {
        colors.reverse()
      }
    } else {
      colors = arrayOf(TextColor.fromHexString("#ffffff")!!, TextColor.fromHexString("#000000")!!)
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
    val sectorLength = size / (colors.size - 1)
    factorStep = 1.0f / (sectorLength + index)
    phase = phase * sectorLength
    index = 0
    return Component.empty()
  }

  override fun apply(current: Component, depth: Int): Component {
    if(disableApplyingColorDepth != -1 && depth >= disableApplyingColorDepth || current.style().color() != null) {
      if(disableApplyingColorDepth == -1) {
        disableApplyingColorDepth = depth
      }
      // This component has it's own color applied, which overrides ours
      // We still want to keep track of where we are though if this is text
      if(current is TextComponent && current.content != null && current.content!!.isNotEmpty()) {
        val content: String = current.content!!
        val len: Int = content.length
        for(i in 0 until len) {
          // increment our color index
          color()
        }
      }
      return current.children(listOf())
    }
    var parent: Component = Component.empty()
    if(current is TextComponent && current.content != null && current.content!!.isNotEmpty()) {
      val textComponent: TextComponent = current
      val content: String = textComponent.content!!

      // apply
      val holder = CharArray(1)
      val it = content.iterator()
      while(it.hasNext()) {
        holder[0] = it.nextChar()
        val comp: Component = Component.text(holder.concatToString(0, 1), color())
        parent = parent.append(comp)
      }
      return parent
    }
    return Component.empty().mergeStyle(current)
  }

  private fun color(): TextColor {
    // color switch needed?
    if(factorStep * index > 1) {
      colorIndex++
      index = 0
    }
    var factor = factorStep * (index++ + phase)
    // loop around if needed
    if(factor > 1) {
      factor = 1 - (factor - 1)
    }
    return if(negativePhase && colors.size % 2 != 0) {
      // flip the gradient segment for to allow for looping phase -1 through 1
      interpolate(colors[colorIndex + 1], colors[colorIndex], factor)
    } else {
      interpolate(colors[colorIndex], colors[colorIndex + 1], factor)
    }
  }

  private fun interpolate(color1: TextColor, color2: TextColor, factor: Float): TextColor {
    return TextColor.color(
      round(color1.red() + factor * (color2.red() - color1.red())).toInt(),
      round(color1.green() + factor * (color2.green() - color1.green())).toInt(),
      round(color1.blue() + factor * (color2.blue() - color1.blue())).toInt()
    )
  }

  override fun toString(): String {
    return "GradientTransformation(size=$size, disableApplyingColorDepth=$disableApplyingColorDepth, index=$index, colorIndex=$colorIndex, factorStep=$factorStep, colors=${colors.contentToString()}, phase=$phase, negativePhase=$negativePhase)"
  }

  override fun equals(o: Any?): Boolean {
    if(this === o) return true
    if(o == null || this::class.js != o::class.js) return false
    if(!super.equals(o)) return false

    o as GradientTransformation

    if(size != o.size) return false
    if(disableApplyingColorDepth != o.disableApplyingColorDepth) return false
    if(index != o.index) return false
    if(colorIndex != o.colorIndex) return false
    if(factorStep != o.factorStep) return false
    if(!colors.contentEquals(o.colors)) return false
    if(phase != o.phase) return false
    if(negativePhase != o.negativePhase) return false

    return true
  }

  override fun hashCode(): Int {
    var result = super.hashCode()
    result = 31 * result + size
    result = 31 * result + disableApplyingColorDepth
    result = 31 * result + index
    result = 31 * result + colorIndex
    result = 31 * result + factorStep.hashCode()
    result = 31 * result + colors.contentHashCode()
    result = 31 * result + phase.hashCode()
    result = 31 * result + negativePhase.hashCode()
    return result
  }

  /**
   * Factory for [GradientTransformation] instances.
   *
   * @since 4.1.0
   */
  class Parser : TransformationParser<GradientTransformation> {

    override fun parse(): GradientTransformation {
      return GradientTransformation()
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
      return name.equals(Tokens.GRADIENT, true)
    }
  }
}
