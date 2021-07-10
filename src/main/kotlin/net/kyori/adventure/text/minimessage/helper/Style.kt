package net.kyori.adventure.text.minimessage.helper

class Style(var innerFont: Key?, var innerColor: TextColor?) {
  fun font(font: Key?): Style {
    this.innerFont = font
    return this
  }

  fun color(): TextColor? {
    return innerColor
  }

  companion object {
    fun style(): Style {
      return Style(null, null)
    }

  }
}
