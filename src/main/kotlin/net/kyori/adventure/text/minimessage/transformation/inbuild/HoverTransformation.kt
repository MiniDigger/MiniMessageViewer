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

import net.kyori.adventure.text.minimessage.Tokens
import net.kyori.adventure.text.minimessage.helper.Component
import net.kyori.adventure.text.minimessage.helper.HoverEvent
import net.kyori.adventure.text.minimessage.helper.Key
import net.kyori.adventure.text.minimessage.parser.ParsingException
import net.kyori.adventure.text.minimessage.parser.node.TagPart
import net.kyori.adventure.text.minimessage.transformation.Transformation
import net.kyori.adventure.text.minimessage.transformation.TransformationParser

/**
 * A transformation that applies a [HoverEvent].
 *
 * @since 4.1.0
 */
class HoverTransformation private constructor() : Transformation() {
//  private var action: HoverEvent.Action<Any>? = null
//  private var value: Any? = null

  override fun load(name: String, args: List<TagPart>) {
    super.load(name, args)
//    if(args.size < 2) {
//      throw ParsingException("Doesn't know how to turn $args into a hover event", *this.argTokenArray())
//    }
//    val newArgs: List<TagPart> = args.let { args.subList(1, it.size) }
//    action = HoverEvent.Action.NAMES.value(args[0].value()) as HoverEvent.Action<Any?>
//    value = if(action === HoverEvent.Action.SHOW_TEXT) {
//      this.context.parse(newArgs[0].value())
//    } else if(action === HoverEvent.Action.SHOW_ITEM) {
//      parseShowItem(newArgs)
//    } else if(action === HoverEvent.Action.SHOW_ENTITY) {
//      parseShowEntity(newArgs)
//    } else {
//      throw ParsingException("Don't know how to turn '$args' into a hover event", *this.argTokenArray())
//    }
  }

//  private fun parseShowItem(args: List<TagPart>): HoverEvent.ShowItem? {
//    return try {
//      if(args.isEmpty()) {
//        throw ParsingException("Show item hover needs at least item id!")
//      }
//      val key: Key = Key.key(args[0].value())
//      val count: Int
//      count = if(args.size >= 2) {
//        args[1].value().toInt()
//      } else {
//        1
//      }
//      if(args.size == 3) {
//        HoverEvent.ShowItem.of(key, count, BinaryTagHolder.of(args[2].value()))
//      } else HoverEvent.ShowItem.of(key, count)
//    } catch(ex: NumberFormatException) {
//      throw ParsingException("Exception parsing show_item hover", ex, *args.map(TagPart::token).toTypedArray())
//    }
//  }
//
//  private fun parseShowEntity(args: List<TagPart>): HoverEvent.ShowEntity? {
//    return try {
//      if(args.size < 2) {
//        throw ParsingException("Show entity hover needs at least type and uuid!")
//      }
//      val key: Key = Key.key(args[0].value())
//      val id: UUID = UUID.fromString(args[1].value())
//      if(args.size == 3) {
//        val name: Component = this.context!!.parse(args[2].value())
//        return HoverEvent.ShowEntity.of(key, id, name)
//      }
//      HoverEvent.ShowEntity.of(key, id)
//    } catch(ex: IllegalArgumentException) {
//      throw ParsingException("Exception parsing show_entity hover", ex, *args.map(TagPart::token).toTypedArray())
//    }
//  }

  override fun apply(): Component {
    TODO("not implemented")
//    return Component.empty().hoverEvent(HoverEvent.hoverEvent(action, value))
  }

  override fun toString(): String {
    TODO("not implemented")
//    return "HoverTransformation(action=$action, value=$value)"
  }

  override fun equals(o: Any?): Boolean {
    TODO("not implemented")
//    if(this === o) return true
//    if(o == null || this::class.js != o::class.js) return false
//
//    o as HoverTransformation
//
//    if(action != o.action) return false
//    if(value != o.value) return false
//
//    return true
  }

  override fun hashCode(): Int {
    TODO("not implemented")
//    var result = 42
//    result = 31 * result + (action.hashCode() ?: 0)
//    result = 31 * result + (value?.hashCode() ?: 0)
//    return result
  }

  /**
   * Factory for [HoverTransformation] instances.
   *
   * @since 4.1.0
   */
  class Parser : TransformationParser<HoverTransformation> {

    override fun parse(): HoverTransformation {
      return HoverTransformation()
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
      return name.equals(Tokens.HOVER, true)
    }
  }
}
