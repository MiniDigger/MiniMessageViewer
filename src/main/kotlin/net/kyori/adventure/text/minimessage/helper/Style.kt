package net.kyori.adventure.text.minimessage.helper

class Style(var innerFont: Key?, var innerColor: TextColor?, var decoration: TextDecoration?) {
  fun font(font: Key?): Style {
    this.innerFont = font
    return this
  }

  fun color(): TextColor? {
    return innerColor
  }

  override fun toString(): String {
    return "Style(font=$innerFont, color=$innerColor, decoration=$decoration)"
  }

  companion object {
    fun style(): Style {
      return Style(null, null, null)
    }

  }
}
