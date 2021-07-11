package net.kyori.adventure.text.minimessage.helper

class NamedTextColor(val name: String, value: Int): TextColor(value) {

  override fun toString(): String {
    return "NamedTextColor(name='$name')"
  }

  companion object {

    private const val BLACK_VALUE = 0x000000
    private const val DARK_BLUE_VALUE = 0x0000aa
    private const val DARK_GREEN_VALUE = 0x00aa00
    private const val DARK_AQUA_VALUE = 0x00aaaa
    private const val DARK_RED_VALUE = 0xaa0000
    private const val DARK_PURPLE_VALUE = 0xaa00aa
    private const val GOLD_VALUE = 0xffaa00
    private const val GRAY_VALUE = 0xaaaaaa
    private const val DARK_GRAY_VALUE = 0x555555
    private const val BLUE_VALUE = 0x5555ff
    private const val GREEN_VALUE = 0x55ff55
    private const val AQUA_VALUE = 0x55ffff
    private const val RED_VALUE = 0xff5555
    private const val LIGHT_PURPLE_VALUE = 0xff55ff
    private const val YELLOW_VALUE = 0xffff55
    private const val WHITE_VALUE = 0xffffff

    val BLACK = NamedTextColor("black", BLACK_VALUE)

    /**
     * The standard `dark_blue` colour.
     *
     * @since 4.0.0
     */
    val DARK_BLUE = NamedTextColor("dark_blue", DARK_BLUE_VALUE)

    /**
     * The standard `dark_green` colour.
     *
     * @since 4.0.0
     */
    val DARK_GREEN = NamedTextColor("dark_green", DARK_GREEN_VALUE)

    /**
     * The standard `dark_aqua` colour.
     *
     * @since 4.0.0
     */
    val DARK_AQUA = NamedTextColor("dark_aqua", DARK_AQUA_VALUE)

    /**
     * The standard `dark_red` colour.
     *
     * @since 4.0.0
     */
    val DARK_RED = NamedTextColor("dark_red", DARK_RED_VALUE)

    /**
     * The standard `dark_purple` colour.
     *
     * @since 4.0.0
     */
    val DARK_PURPLE = NamedTextColor("dark_purple", DARK_PURPLE_VALUE)

    /**
     * The standard `gold` colour.
     *
     * @since 4.0.0
     */
    val GOLD = NamedTextColor("gold", GOLD_VALUE)

    /**
     * The standard `gray` colour.
     *
     * @since 4.0.0
     */
    val GRAY = NamedTextColor("gray", GRAY_VALUE)

    /**
     * The standard `dark_gray` colour.
     *
     * @since 4.0.0
     */
    val DARK_GRAY = NamedTextColor("dark_gray", DARK_GRAY_VALUE)

    /**
     * The standard `blue` colour.
     *
     * @since 4.0.0
     */
    val BLUE = NamedTextColor("blue", BLUE_VALUE)

    /**
     * The standard `green` colour.
     *
     * @since 4.0.0
     */
    val GREEN = NamedTextColor("green", GREEN_VALUE)

    /**
     * The standard `aqua` colour.
     *
     * @since 4.0.0
     */
    val AQUA = NamedTextColor("aqua", AQUA_VALUE)

    /**
     * The standard `red` colour.
     *
     * @since 4.0.0
     */
    val RED = NamedTextColor("red", RED_VALUE)

    /**
     * The standard `light_purple` colour.
     *
     * @since 4.0.0
     */
    val LIGHT_PURPLE = NamedTextColor("light_purple", LIGHT_PURPLE_VALUE)

    /**
     * The standard `yellow` colour.
     *
     * @since 4.0.0
     */
    val YELLOW = NamedTextColor("yellow", YELLOW_VALUE)

    /**
     * The standard `white` colour.
     *
     * @since 4.0.0
     */
    val WHITE = NamedTextColor("white", WHITE_VALUE)

    private val VALUES: List<NamedTextColor> = listOf(BLACK, DARK_BLUE, DARK_GREEN, DARK_AQUA, DARK_RED, DARK_PURPLE, GOLD, GRAY, DARK_GRAY, BLUE, GREEN, AQUA, RED, LIGHT_PURPLE, YELLOW, WHITE)

    /**
     * An index of name to color.
     *
     * @since 4.0.0
     */
    val NAMES: Map<String, NamedTextColor> = VALUES.associateBy { it.name }
  }
}
