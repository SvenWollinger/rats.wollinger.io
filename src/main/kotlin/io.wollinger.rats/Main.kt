package io.wollinger.rats

import io.wollinger.rats.Styler.Companion.borderRadius
import io.wollinger.rats.Styler.Companion.color
import io.wollinger.rats.Styler.Companion.maxHeight
import io.wollinger.rats.Styler.Companion.maxWidth
import io.wollinger.rats.Styler.Companion.padding
import io.wollinger.rats.Styler.Companion.textAlign
import io.wollinger.rats.Styler.Companion.width
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.html.*
import kotlinx.html.dom.append
import kotlinx.html.dom.create
import kotlinx.html.js.div
import kotlinx.html.js.h1
import kotlinx.html.js.onClickFunction
import org.w3c.dom.*

fun main() {
    id<HTMLElement>("gallery").onclick = { event ->
        val target = event.target
        if(target is Element && target.id == "gallery") closeGallery()
    }

    dl<Config>("json/config.json") { config ->
        info("Config: $config")

        val selectedRat = getParams().get("rat")
        val content = id<HTMLElement>("content")

        if(selectedRat == null) {
            content.append(document.create.h1 {
                + "All Rattos:"
                style = styler {
                    textAlign to "center"
                    width to "100%"
                }
            })

            config.rats.forEach { id ->
                val ratCard = document.create.div(classes = "card")
                content.append(ratCard)
                dl<Rat>("json/rats/$id.json") { rat ->
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
                            style = styler {
                                textAlign to "center"
                                color to "black"
                            }
                        }
                    }
                }
            }
        } else {
            if(config.rats.contains(selectedRat)) {
                dl<Rat>("json/rats/$selectedRat.json") { rat ->
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
                        val thumb = document.create.img {
                            src = image
                            classes += "pointer"
                            onClickFunction = {
                                openGallery(image)
                            }
                            style = styler {
                                maxWidth to "15vw"
                                maxHeight to "15vh"
                                padding to "5px"
                                borderRadius to "10px 10px 10px 10px"
                            }
                        }
                        document.getElementById("selectedRatGallery")!!.append(thumb)
                    }
                }
            } else {
                val errorParent = id<HTMLElement>("error")
                val message = document.create.div {
                    style = styler {
                        textAlign to "center"
                    }
                    p { + "Rat not found!" }
                    a(href = "/") { + "Go back?" }
                }
                errorParent.append(message)
            }
        }
    }
}