package io.wollinger.rats

class Styler {
    private val map = hashMapOf<String, String>()

    override fun toString(): String {
        var string = ""
        map.forEach {
            string += "${it.key}:${it.value};"
        }
        return string
    }
    infix fun CSSValue.to(other: Any?) {
        map[key] = other.toString()
    }

    companion object {
        val padding = CSSValue("padding")
        val borderRadius = CSSValue("border-radius")
        val maxWidth = CSSValue("max-width")
        val maxHeight = CSSValue("max-height")
        val textAlign = CSSValue("text-align")
        val width = CSSValue("width")
        val height = CSSValue("height")
        val color = CSSValue("color")
    }
}

data class CSSValue(val key: String)

fun styler(styler: Styler.() -> Unit): String {
    val st = Styler()
    styler.invoke(st)
    return st.toString()
}