import kotlin.math.hypot
import kotlin.text.concatToString
import kotlinext.js.asJsObject
import kotlinx.browser.document
import kotlinx.browser.window
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.markdown.MarkdownFlavor
import org.w3c.dom.Element
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.asList

fun main() {
    document.addEventListener("DOMContentLoaded", {
        document.getElementById("input")!!.addEventListener("keyup", {
            doStuff(it.target?.asJsObject().unsafeCast<HTMLInputElement>().value)
        })
        document.getElementById("input")!!.addEventListener("change", {
            doStuff(it.target?.asJsObject().unsafeCast<HTMLInputElement>().value)
        })

        window.setInterval( { obfuscateAll() }, 10)
    })
}

fun obfuscateAll() {
    document.getElementsByClassName("obfuscated").asList().forEach {
        obfuscate(it)
    }
}

fun obfuscate(input: Element) {
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

fun doStuff(input: String) {
    val output = document.getElementById("output")!!
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
    input.split("\n").forEach {
        println("BEGING PARSING $it")
        output.append(miniMessage.parse(it).buildOutChildren())
        println("DONE")
        output.append(document.createElement("br"))
    }
}

fun String.escapeHtml(): String {
    return this.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&#039;")
}
