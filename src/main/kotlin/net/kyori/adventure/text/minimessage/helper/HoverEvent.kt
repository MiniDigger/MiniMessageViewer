package net.kyori.adventure.text.minimessage.helper

import kotlin.random.Random
import kotlinx.browser.document
import kotlinx.dom.addClass
import org.w3c.dom.Element

open class HoverEvent(val component: Component) {
  fun buildOut(dom: Element) {
    val id = Random.nextInt(0, 100)

    val el = document.createElement("span")
    el.addClass("hover hover-$id")
    el.append(component.buildOutChildren())

    dom.addClass("hover-source hover-source-$id")
    dom.append(el)
  }

  override fun toString(): String {
    return "HoverEvent(component=$component)"
  }
}
