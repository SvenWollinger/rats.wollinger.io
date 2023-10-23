package io.wollinger.rats

import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.html.*
import kotlinx.html.dom.append
import kotlinx.html.dom.create
import kotlinx.html.js.div
import kotlinx.html.js.h1
import kotlinx.html.js.onClickFunction
import kotlinx.html.stream.createHTML
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.w3c.dom.*
import org.w3c.dom.url.URLSearchParams
import org.w3c.xhr.XMLHttpRequest

fun main() {
    id<HTMLElement>("gallery").onclick = { event ->
        if((event.target as Element).id == "gallery") closeGallery()
    }

    dl<Config>("/json/config.json") { config ->
        info("Config: $config")

        val selectedRat = getParams().get("rat")
        val content = id<HTMLElement>("content")

        if(selectedRat == null) {
            content.append(document.create.h1 {
                + "All Rattos:"
                style = "text-align:center;width:100%;"
            })

            config.rats.forEach { id ->
                val ratCard = document.create.div(classes = "card")
                content.append(ratCard)
                dl<Rat>("/json/rats/$id.json") { rat ->
                    ratCard.append.div("cardInner") {
                        img(
                            src = rat.thumbnail,
                            classes = "ratThumbnail"
                        ) {
                            onClickFunction = { clickRat(id) }
                        }
                        h1 {
                            + rat.name
                            if(rat.passed != null) + " ${config.rbSymbol}"
                            style = "text-align:center;color:black;"
                        }
                    }
                }
            }
        } else {
            if(config.rats.contains(selectedRat)) {
                dl<Rat>("/json/rats/$selectedRat.json") { rat ->
                    id<HTMLElement>("selectedRatName").innerText = rat.name
                    id<HTMLElement>("selectedRatArea").style.display = "flex"
                    id<HTMLImageElement>("selectedRatBigPic").src = rat.bigPic
                    id<HTMLImageElement>("selectedRatBorn").innerText = rat.born
                    if(rat.passed != null) {
                        id<HTMLElement>("selectedRatRB").innerText = rat.passed
                    } else {
                        id<HTMLElement>("selectedRatRBParent").style.display = "none"
                    }

                    getParams().get("gallery")?.let { openGallery(it) }

                    id<HTMLElement>("galleryRight").onclick = {
                        var nextIndex = rat.images.indexOf(getParams().get("gallery")) + 1
                        if(nextIndex >= rat.images.size) nextIndex = 0
                        window.setSearchParam("gallery", rat.images[nextIndex])
                        openGallery(rat.images[nextIndex])
                        null
                    }
                    id<HTMLElement>("galleryLeft").onclick = {
                        var nextIndex = rat.images.indexOf(getParams().get("gallery")) - 1
                        if(nextIndex < 0) nextIndex = rat.images.size - 1
                        window.setSearchParam("gallery", rat.images[nextIndex])
                        openGallery(rat.images[nextIndex])
                        null
                    }
                    rat.images.forEach { image ->
                        val g = document.create.img {
                            src = image
                            classes += "pointer"
                            onClickFunction = {
                                openGallery(image)
                            }
                            style = """
                                        max-width: 15vw;
                                        max-height: 15vh;
                                        padding: 5px;
                                        borrderRadius: 10px 10px 10px 10px;
                                    """.trimIndent()
                        }
                        document.getElementById("selectedRatGallery")!!.append(g)
                    }
                }
            } else {
                document.getElementById("content")!!.innerHTML = """
                    <div style='text-align:center'><p>Rat not found!</p>
                    <a href='/'>Go back?</a></div>
                """.trimIndent()
            }
        }
    }
}