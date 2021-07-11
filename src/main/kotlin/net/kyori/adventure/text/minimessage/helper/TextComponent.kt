package net.kyori.adventure.text.minimessage.helper

class TextComponent : Component() {
  var content: String? = null
    set(value) {
      dom.textContent = value
      field = value
    }

  override fun toString(): String {
    return "TextComponent(content=$content) ${super.toString()}"
  }
}
