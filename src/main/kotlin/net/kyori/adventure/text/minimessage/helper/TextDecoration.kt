package net.kyori.adventure.text.minimessage.helper

enum class TextDecoration(val myName: String) {

  /**
   * A decoration which makes text obfuscated/unreadable.
   *
   * @since 4.0.0
   */
  OBFUSCATED("obfuscated"),
  /**
   * A decoration which makes text appear bold.
   *
   * @since 4.0.0
   */
  BOLD("bold"),
  /**
   * A decoration which makes text have a strike through it.
   *
   * @since 4.0.0
   */
  STRIKETHROUGH("strikethrough"),
  /**
   * A decoration which makes text have an underline.
   *
   * @since 4.0.0
   */
  UNDERLINED("underlined"),
  /**
   * A decoration which makes text appear in italics.
   *
   * @since 4.0.0
   */
  ITALIC("italic");

  companion object {
    val NAMES: Map<String, TextDecoration> = values().associateBy { it.myName }
  }
}
