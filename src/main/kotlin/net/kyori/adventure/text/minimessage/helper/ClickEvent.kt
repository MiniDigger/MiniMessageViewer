package net.kyori.adventure.text.minimessage.helper

import escapeHtml
import kotlin.random.Random
import kotlinx.browser.document
import kotlinx.dom.addClass
import org.w3c.dom.Element

class ClickEvent(val action: Action, val value: String) {

  fun buildOut(dom: Element) {
    val id = Random.nextInt(0, 100)

    val el = document.createElement("span")
    el.addClass("click hover hover-$id")
    el.innerHTML = "Action: ${action.name}, Value: ${value.escapeHtml()}"

    dom.addClass("click-source hover-source hover-source-$id")
    dom.append(el)
  }

  override fun toString(): String {
    return "ClickEvent(action=$action, value='$value')"
  }

  enum class Action(val myName: String, val readable: Boolean) {

    /**
     * Opens a url when clicked.
     *
     * @since 4.0.0
     */
    OPEN_URL("open_url", true),
    /**
     * Opens a file when clicked.
     *
     * <p>This action is not readable, and may only be used locally on the client.</p>
     *
     * @since 4.0.0
     */
    OPEN_FILE("open_file", false),
    /**
     * Runs a command when clicked.
     *
     * @since 4.0.0
     */
    RUN_COMMAND("run_command", true),
    /**
     * Suggests a command into the chat box.
     *
     * @since 4.0.0
     */
    SUGGEST_COMMAND("suggest_command", true),
    /**
     * Changes the page of a book.
     *
     * @since 4.0.0
     */
    CHANGE_PAGE("change_page", true),
    /**
     * Copies text to the clipboard.
     *
     * @since 4.0.0
     * @sinceMinecraft 1.15
     */
    COPY_TO_CLIPBOARD("copy_to_clipboard", true);

    companion object {
      val NAMES: Map<String, Action> = values().associateBy { it.myName }
    }
  }
}
