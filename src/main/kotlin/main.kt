
import kotlinext.js.asJsObject
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.dom.hasClass
import net.kyori.adventure.text.minimessage.MiniMessage
import org.w3c.dom.Element
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLPreElement
import org.w3c.dom.HTMLSelectElement
import org.w3c.dom.HTMLTextAreaElement
import org.w3c.dom.asList
import org.w3c.dom.get
import org.w3c.dom.url.URLSearchParams

enum class Mode {
    CHAT_OPEN, CHAT_CLOSED, LORE, HOLOGRAM, BOOK;

    val className = "mode-${name.lowercase().replace('_', '-')}"
    val paramName = name.lowercase()

    companion object {
        val modes = values().asList()
        val index = modes.associateBy { it.name }

        /** Gets a mode from [string], returning [CHAT_CLOSED] as a default. */
        fun fromString(string: String?): Mode = index[string?.uppercase()] ?: CHAT_CLOSED
    }
}

val homeUrl by lazy { window.location.href.split('?')[0] }
var currentMode: Mode = Mode.CHAT_CLOSED

// thanks kotlin you rock
external fun decodeURIComponent(encodedURI: String): String
external fun encodeURIComponent(string: String): String

const val PARAM_INPUT = "input"
const val PARAM_MODE = "mode"

fun main() {
    document.addEventListener("DOMContentLoaded", {
        // CORRECT HOME LINK
        document.getElementById("home-link")!!.asJsObject().unsafeCast<HTMLAnchorElement>().href = homeUrl

        // SHARING
        val inputBox = document.getElementById("input")!!.asJsObject().unsafeCast<HTMLTextAreaElement>()
        val urlParams = URLSearchParams(window.location.search)
        val outputPre = document.getElementById("output-pre")!!.asJsObject().unsafeCast<HTMLPreElement>()
        val outputPane = document.getElementById("output-pane")!!.asJsObject().unsafeCast<HTMLDivElement>()
        urlParams.get(PARAM_INPUT)?.also { inputString ->
            val text = decodeURIComponent(inputString)
            inputBox.innerText = text
            println("SHARED: $text")
            parse(text)
        }
        currentMode = Mode.fromString(urlParams.get(PARAM_MODE))
        outputPre.classList.add(currentMode.className)
        outputPane.classList.add(currentMode.className)

        // INPUT
        val input = document.getElementById("input")!!.asJsObject().unsafeCast<HTMLTextAreaElement>()
        input.addEventListener("keyup", {
            parse(input.value)
        })
        input.addEventListener("change", {
            parse(input.value)
        })

        // OBFUSCATION
        window.setInterval( { obfuscateAll() }, 10)

        // CARET
        val chatBox = document.getElementById("chat-entry-box")!!.asJsObject().unsafeCast<HTMLDivElement>()
        window.setInterval({
            chatBox.innerHTML = if (chatBox.innerHTML == "_") " " else "_"
        }, 380)

        // BUTTONS
        val settingsBox = document.getElementById("settings-box")
        document.getElementsByClassName("settings-button").asList().forEach { element ->
            element.addEventListener("click", {
                settingsBox!!.classList.toggle("is-active")
            })
        }

        val modeButtons = document.getElementsByClassName("mc-mode").asList().unsafeCast<List<HTMLElement>>()
        modeButtons.forEach { element ->
            // set is-active on the current mode first
            val mode = Mode.valueOf(element.dataset["mode"]!!)
            if (currentMode == mode) {
                element.classList.add("is-active")
            }

            // then add event listeners for the rest
            element.addEventListener("click", { event ->
                // remove active
                modeButtons.forEach { button ->
                    button.classList.remove("is-active")
                }

                // now add it again lmao 10/10 code
                val button = event.target!!.asJsObject().unsafeCast<HTMLAnchorElement>()
                button.classList.add("is-active")
                currentMode = mode

                // swap the class for the pane
                Mode.modes.forEach { mode ->
                    if (currentMode == mode) {
                        outputPre.classList.add(mode.className)
                        outputPane.classList.add(mode.className)
                    } else {
                        outputPre.classList.remove(mode.className)
                        outputPane.classList.remove(mode.className)
                    }
                }
            })
        }

        // CLIPBOARD
        val shareButton = document.getElementById("share-button")!!.asJsObject().unsafeCast<HTMLAnchorElement>()
        val shareBox = document.getElementById("share-box")!!.asJsObject().unsafeCast<HTMLDivElement>()
        shareButton.addEventListener("click", {
            window.navigator.clipboard.writeText("$homeUrl?$PARAM_MODE=${currentMode.paramName}&$PARAM_INPUT=${encodeURIComponent(input.value)}").then {
                shareBox.classList.add("is-active")
            }
        })
        document.getElementsByClassName("close-share-box").asList().unsafeCast<List<HTMLElement>>().forEach { element ->
            element.addEventListener("click", {
                shareBox.classList.remove("is-active")
            })
        }

        // BURGER MENU
        val burgerMenu = document.getElementById("burger-menu")!!
        val navbarMenu = document.getElementById("navbar-menu")!!
        burgerMenu.addEventListener("click", {
            burgerMenu.classList.toggle("is-active")
            navbarMenu.classList.toggle("is-active")
        })

        // SETTINGS
        val settingBackground = document.getElementById("setting-background")!!.asJsObject().unsafeCast<HTMLSelectElement>()
        settingBackground.addEventListener("change", {
            outputPane.style.backgroundImage = "url(\"img/${settingBackground.value}.png\")"
        })
    })
}

fun obfuscateAll() {
    document.getElementsByClassName("obfuscated").asList().forEach {
        obfuscate(it)
    }
}

fun obfuscate(input: Element) {
    if (input.hasClass("hover")) return
    if (input.childElementCount > 0) {
        input.children.asList().forEach {
            obfuscate(it)
        }
    } else if (input.textContent != null){
        input.textContent = obfuscate(input.textContent!!)
    }
}

fun CharArray.map(transform: (Char) -> Char): CharArray {
    for (i in this.indices) {
        this[i] = transform(this[i])
    }
    return this
}

fun obfuscate(input: String): String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')

    return input.toCharArray()
        .map { if (it != ' ') allowedChars.random() else it }
        .concatToString()
}

fun parse(input: String) {
    val output = document.getElementById("output-pre")!!
    output.textContent = ""
    val miniMessage = MiniMessage.builder().debug(object : Appendable {
        override fun append(value: Char): Appendable {
            print(value)
            return this
        }

        override fun append(value: CharSequence?): Appendable {
            print(value)
            return this
        }

        override fun append(value: CharSequence?, startIndex: Int, endIndex: Int): Appendable {
            TODO("Not yet implemented")
        }

    }).parsingErrorMessageConsumer { }.build()
    val lines = input.split("\n")

    lines.map { line ->
        // we don't want to lose empty lines, so replace them with zero-width space
        if (line == "") "\u200B" else line
    }.forEach { line ->
        println("BEGING PARSING \"$line\"")
        val div = document.createElement("div")
        div.append(miniMessage.parse(line).buildOutChildren())
        output.append(div)
        println("DONE")
    }

    // reset scroll to bottom (like how chat works)
    if (currentMode == Mode.CHAT_OPEN || currentMode == Mode.CHAT_CLOSED) {
        output.scrollTop = output.scrollHeight.toDouble()
    }
}
