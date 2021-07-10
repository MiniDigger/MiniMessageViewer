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
import net.kyori.adventure.text.minimessage.helper.Style
import net.kyori.adventure.text.minimessage.helper.TextComponent
import net.kyori.adventure.text.minimessage.parser.ParsingException
import net.kyori.adventure.text.minimessage.parser.TokenParser
import net.kyori.adventure.text.minimessage.parser.node.ElementNode
import net.kyori.adventure.text.minimessage.parser.node.TagNode
import net.kyori.adventure.text.minimessage.parser.node.ValueNode
import net.kyori.adventure.text.minimessage.transformation.Modifying
import net.kyori.adventure.text.minimessage.transformation.Transformation
import net.kyori.adventure.text.minimessage.transformation.TransformationRegistry

internal class MiniMessageParser {
  private val registry: TransformationRegistry
  private val placeholderResolver: (String) -> Component?

  constructor() {
    registry = TransformationRegistry.standard()
    placeholderResolver = MiniMessageImpl.DEFAULT_PLACEHOLDER_RESOLVER
  }

  constructor(registry: TransformationRegistry, placeholderResolver: (String) -> Component?) {
    this.registry = registry
    this.placeholderResolver = placeholderResolver
  }

  fun escapeTokens(richMessage: String): String {
    TODO("not implemented")
//    val sb = StringBuilder()
//    val matcher = pattern.matcher(richMessage)
//    var lastEnd = 0
//    while(matcher.find()) {
//      val startIndex: Int = matcher.start()
//      val endIndex: Int = matcher.end()
//      if(startIndex > lastEnd) {
//        sb.append(richMessage, lastEnd, startIndex)
//      }
//      lastEnd = endIndex
//      val start: String = matcher.group(START)
//      var token: String = matcher.group(TOKEN)
//      val inner: String = matcher.group(INNER)
//      val end: String = matcher.group(END)
//
//      // also escape inner
//      if(inner != null) {
//        token = token.replace(inner, escapeTokens(inner))
//      }
//      sb.append("\\").append(start).append(token).append(end)
//    }
//    if(richMessage.length > lastEnd) {
//      sb.append(richMessage.substring(lastEnd))
//    }
//    return sb.toString()
  }

  fun stripTokens(richMessage: String): String {
    TODO("not implemented")
//    val sb = StringBuilder()
//    val matcher = pattern.matcher(richMessage)
//    var lastEnd = 0
//    while(matcher.find()) {
//      val startIndex: Int = matcher.start()
//      val endIndex: Int = matcher.end()
//      if(startIndex > lastEnd) {
//        sb.append(richMessage, lastEnd, startIndex)
//      }
//      lastEnd = endIndex
//    }
//    if(richMessage.length > lastEnd) {
//      sb.append(richMessage.substring(lastEnd))
//    }
//    return sb.toString()
  }

  fun parseFormat(richMessage: String, context: Context, vararg placeholders: String): Component {
    if(placeholders.size % 2 != 0) {
      throw ParsingException(
        "Invalid number placeholders defined, usage: parseFormat(format, key, value, key, value...)"
      )
    }
    val t: MutableList<Template> = mutableListOf()
    var i = 0
    while(i < placeholders.size) {
      t.add(Template.of(placeholders[i], placeholders[i + 1]))
      i += 2
    }
    return this.parseFormat(richMessage, context, *t.toTypedArray())
  }

  fun parseFormat(richMessage: String, placeholders: Map<String, String>, context: Context): Component {
    val t: MutableList<Template> = mutableListOf()
    for(entry in placeholders.entries) {
      t.add(Template.of(entry.key, entry.value))
    }
    return this.parseFormat(richMessage, context, *t.toTypedArray())
  }

  fun parseFormat(input: String, context: Context, vararg placeholders: Template): Component {
    val map: MutableMap<String, Template> = HashMap()
    for(placeholder in placeholders) {
      map[placeholder.key()] = placeholder
    }
    return this.parseFormat0(input, map, context)
  }

  fun parseFormat(input: String, placeholders: List<Template>, context: Context): Component {
    val map: MutableMap<String, Template> = HashMap()
    for(placeholder in placeholders) {
      map[placeholder.key()] = placeholder
    }
    return this.parseFormat0(input, map, context)
  }

  fun parseFormat(richMessage: String, context: Context): Component {
    return this.parseFormat0(richMessage, mapOf(), context)
  }

  fun parseFormat0(richMessage: String, templates: Map<String, Template>, context: Context): Component {
    return this.parseFormat0(richMessage, templates, registry, placeholderResolver, context)
  }

  fun parseFormat0(richMessage: String, templates: Map<String, Template>, registry: TransformationRegistry, placeholderResolver: (String) -> Component?, context: Context): Component {
    val debug = context.debugOutput()
    debug?.append("Beginning parsing message ")?.append(richMessage)?.append('\n')
    val transformationFactory: (TagNode) -> Transformation?
    if(debug != null) {
      transformationFactory = label@ { node ->
        try {
          debug.append("Attempting to match node '").append(node.name()).append("' at column ")
            .append(node.token().startIndex().toString()).append('\n')
          val transformation: Transformation? = registry[node.name(), node.parts(), templates, placeholderResolver, context]
          if(transformation == null) {
            debug.append("Could not match node '").append(node.name()).append("'\n")
          } else {
            debug.append("Successfully matched node '").append(node.name()).append("' to transformation ")
              .append(transformation.toString()).append('\n')
          }
          return@label transformation
        } catch(e: ParsingException) {
          if(e.tokens().isEmpty()) {
            e.tokens(arrayOf(node.token()))
          }
          debug.append("Could not match node '").append(node.name()).append("' - ").append(e.message).append('\n')
          return@label null
        }
      }
    } else {
      transformationFactory = label@ { node ->
        try {
          return@label registry.get(node.name(), node.parts(), templates, placeholderResolver, context)
        } catch(ignored: ParsingException) {
          return@label null
        }
      }
    }
    val tagNameChecker: (String, Boolean) -> Boolean = { name, includeTemplates -> registry.exists(name, placeholderResolver) || includeTemplates && templates.containsKey(name) }
    val root: ElementNode = TokenParser.parse(transformationFactory, tagNameChecker, templates, richMessage, context.strict())
    if(debug != null) {
      debug.append("Text parsed into element tree:\n")
      debug.append(root.toString())
    }
    context.root(root)
    val comp: Component = parse(root)
    // at the end, take a look if we can flatten the tree a bit
    return this.flatten(comp)
  }

  fun parse(node: ElementNode): Component {
    var comp: Component
    var transformation: Transformation? = null
    if(node is ValueNode) {
      comp = Component.text(node.value())
    } else if(node is TagNode) {
      val tag: TagNode = node
      transformation = tag.transformation()

      // special case for gradient and stuff
      if(transformation is Modifying) {
        val modTransformation: Modifying = transformation

        // first walk the tree
        val toVisit: MutableList<ElementNode> = mutableListOf(*node.children().toTypedArray())
        while(toVisit.isNotEmpty()) {
          val curr: ElementNode = toVisit.removeFirst()
          modTransformation.visit(curr)
          toVisit.addAll(0, curr.children())
        }
      }
      comp = transformation.apply()
    } else {
      comp = Component.empty()
    }
    for(child in node.children()) {
      comp = comp.append(parse(child))
    }

    // special case for gradient and stuff
    if(transformation is Modifying) {
      comp = handleModifying(transformation, comp, 0)
    }
    return comp
  }

  private fun handleModifying(modTransformation: Modifying, current: Component, depth: Int): Component {
    var newComp: Component = modTransformation.apply(current, depth)!!
    for(child in current.children()) {
      newComp = newComp.append(handleModifying(modTransformation, child, depth + 1))
    }
    return newComp
  }

  private fun flatten(comp: Component): Component {
    var comp: Component = comp
    if(comp.children().isEmpty()) {
      return comp
    }
    val oldChildren: List<Component> = comp.children()
    val newChildren: ArrayList<Component> = ArrayList(oldChildren.size)
    for(child in oldChildren) {
      newChildren.add(this.flatten(child))
    }
    comp = comp.children(newChildren)
    if(comp !is TextComponent) {
      return comp
    }
    val root: TextComponent = comp
    if(root.content().isEmpty()) {
      // this seems to be some kind of empty node, lets see if we can discard it, or if we have to merge it
      val hasNoStyle = !root.hasStyling() && root.hoverEvent() == null && root.clickEvent() == null
      if(root.children().size === 1 && hasNoStyle) {
        // seems to be the root node, just discord it
        return root.children().get(0)
      } else if(!root.children().isEmpty() && hasNoStyle) {
        // see if we can at least flatten the first child
        val child: Component = newChildren[0]
        if(child.hasStyling()) {
          // We can't, the child styling might interfere with a sibling
          return comp
        }
        val copiedChildren: ArrayList<Component> = ArrayList(root.children().size - 1 + child.children().size)
        copiedChildren.addAll(child.children())
        copiedChildren.addAll(newChildren.subList(1, newChildren.size))
        return root.content(if(child is TextComponent) child.content() else "")
          .style(mergeStyle(root, child))
          .children(copiedChildren)
      } else if(root.children().size == 1) {
        // we got something we can merge
        val child: Component = newChildren[0]
        return child.style(mergeStyle(root, child))
      }
    }
    return comp
  }

  companion object {
    // regex group names
    private const val START = "start"
    private const val TOKEN = "token"
    private const val INNER = "inner"
    private const val END = "end"

    // https://regex101.com/r/8VZ7uA/10
    private val pattern = Regex("((?<start><)(?<token>[^<>]+(:(?<inner>['\"]?([^'\"](\\\\['\"])?)+['\"]?))*)(?<end>>))+?")
    private fun mergeStyle(base: Component, target: Component): Style {
      TODO("not implemented")
//      return target.style().merge(base.style(), Style.Merge.Strategy.IF_ABSENT_ON_TARGET, Style.Merge.all())
    }
  }
}
