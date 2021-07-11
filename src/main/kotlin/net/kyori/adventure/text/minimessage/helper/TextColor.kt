package net.kyori.adventure.text.minimessage.helper

open class TextColor(val value: Int) {
  fun red(): Int {
    return this.value shr 16 and 0xff
  }

  fun green(): Int {
    return this.value shr 8 and 0xff
  }

  fun blue(): Int {
    return this.value and 0xff
  }

  fun asHexString(): String {
    return "#" + this.value.toString(16).padStart(6, '0')
  }

  override fun toString(): String {
    return "TextColor(hex=${asHexString()})"
  }

  companion object {
    fun fromHexString(name: String): TextColor? {
      return if (name.startsWith("#")) {
        TextColor(name.substring(1).toInt(16))
      } else {
        null
      }
    }

    fun color(r: Int, g: Int, b: Int): TextColor {
      return TextColor(r and 0xff shl 16 or (g and 0xff shl 8) or (b and 0xff))
    }
  }
}
