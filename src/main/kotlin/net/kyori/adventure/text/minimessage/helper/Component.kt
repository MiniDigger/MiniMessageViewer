package net.kyori.adventure.text.minimessage.helper

import kotlinx.browser.document
import kotlinx.dom.addClass
import org.w3c.dom.Element
import org.w3c.dom.HTMLSpanElement

open class Component {

  private val dom: HTMLSpanElement = document.createElement("span") as HTMLSpanElement
  private val childs: MutableList<Component> = mutableListOf()

  fun buildOutChildren(): Element {
    childs.forEach { dom.append(it.buildOutChildren()) }
    return dom
  }

  fun append(child: Component): Component {
    childs.add(child)
    return this
  }

  fun children(): List<Component> {
    return childs
  }

  fun children(children: List<Component>): Component {
    childs.clear()
    childs.addAll(children)
    return this
  }

  fun insertion(insertion: String): Component {
    TODO("Not yet implemented")
  }

  fun decorate(decoration: TextDecoration?): Component {
    when (decoration) {
      TextDecoration.OBFUSCATED -> dom.addClass("obfuscated")
      TextDecoration.BOLD -> dom.style.fontWeight = "bold"
      TextDecoration.STRIKETHROUGH -> dom.style.textDecoration = "line-through"
      TextDecoration.UNDERLINED -> dom.style.textDecoration = "underline"
      TextDecoration.ITALIC -> dom.style.fontStyle = "italic"
      null -> TODO()
    }
    return this
  }

  fun color(color: TextColor): Component {
    dom.style.color = color.asHexString()
    return this
  }

  fun insertion(): String {
    TODO("Not yet implemented")
  }

  fun style(style: Style): Component {
//    TODO("Not yet implemented")
    if (style.color() != null) {
      color(style.color()!!)
    }
    return this
  }

  fun style(): Style {
//    TODO("Not yet implemented")
    return Style(null, null)
  }

  fun hoverEvent(): HoverEvent {
    TODO("Not yet implemented")
  }

  fun hoverEvent(hoverEvent: HoverEvent): Component {
    TODO("Not yet implemented")
  }

  fun clickEvent(): ClickEvent {
    TODO("Not yet implemented")
  }

  fun clickEvent(clickEvent: ClickEvent): Component {
    TODO("Not yet implemented")
  }

  fun mergeStyle(current: Component): Component {
//    TODO("Not yet implemented")
    style(current.style())
    return this
  }

  fun hasStyling(): Boolean {
    TODO("Not yet implemented")
  }

  companion object {
    fun empty(): Component {
      return Component()
    }

    fun text(value: String): Component {
      val comp = empty()
      comp.dom.textContent = value
      return comp
    }

    fun text(value: String, color: TextColor): Component {
      TODO("Not yet implemented")
    }

    fun keybind(keybind: String): Component {
      TODO("Not yet implemented")
    }

    fun translatable(key: String): Component {
      TODO("Not yet implemented")
    }

    fun translatable(key: String, inners: MutableList<Component>): Component {
      TODO("Not yet implemented")
    }
  }
}
