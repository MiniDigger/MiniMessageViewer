package net.kyori.adventure.text.minimessage.helper

import kotlin.random.Random
import kotlinx.browser.document
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import org.w3c.dom.css.CSS.Companion.escape

open class Component {

  val dom = document.createElement("span") as HTMLElement
  private val childs: MutableList<Component> = mutableListOf()
  private val style: Style = Style.style()
  var hoverEvent: HoverEvent? = null
  var clickEvent: ClickEvent? = null
  var insertion: String? = null

  fun buildOutChildren(): Element {
    childs.forEach { dom.append(it.buildOutChildren()) }
    hoverEvent?.buildOut(dom)
    clickEvent?.buildOut(dom)
    insertion?.let {
      buildOutInsertion()
    }
    return dom
  }

  private fun buildOutInsertion() {
    val id = Random.nextInt(0, 100)

    val el = document.createElement("span")
    el.addClass("click hover hover-$id")
    el.innerHTML = "Insertion: ${escape(insertion ?: "")}"

    dom.addClass("click-source hover-source hover-source-$id")
    dom.append(el)
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

  fun decorate(decoration: TextDecoration?): Component {
    style.decoration = decoration
    when(decoration) {
      TextDecoration.OBFUSCATED -> dom.addClass("obfuscated")
      TextDecoration.BOLD -> dom.style.fontWeight = "bold"
      TextDecoration.STRIKETHROUGH -> dom.style.textDecoration = "line-through"
      TextDecoration.UNDERLINED -> dom.style.textDecoration = "underline"
      TextDecoration.ITALIC -> dom.style.fontStyle = "italic"
      null -> {
        dom.removeClass("obfuscated")
        dom.style.fontWeight = ""
        dom.style.textDecoration = ""
        dom.style.fontStyle = ""
      }
    }
    return this
  }

  fun color(color: TextColor): Component {
    dom.style.color = color.asHexString()
    style.innerColor = color
    return this
  }

  fun style(style: Style): Component {
    if(style.color() != null) {
      color(style.color()!!)
    }
    if(style.decoration != null) {
      decorate(style.decoration)
    }
    return this
  }

  fun style(): Style {
    return style
  }

  fun mergeStyle(current: Component): Component {
//    TODO("Not yet implemented")
    println("merge style ")
    style(current.style())
    return this
  }

  fun hasStyling(): Boolean {
    TODO("Not yet implemented")
  }

  override fun toString(): String {
    return "Component(childs=$childs, style=$style, hoverEvent=$hoverEvent, clickEvent=$clickEvent, insertion=$insertion)"
  }


  companion object {
    fun empty(): Component {
      return Component()
    }

    fun text(value: String): Component {
      val comp = TextComponent()
      comp.content = value
      return comp
    }

    fun text(value: String, color: TextColor): Component {
      val comp = text(value)
      comp.color(color)
      return comp
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
