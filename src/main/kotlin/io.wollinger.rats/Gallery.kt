package io.wollinger.rats

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLImageElement
import org.w3c.dom.HTMLLinkElement

fun openGallery(image: String) {
    debug("openImage(image=$image)")
    window.setSearchParam("gallery", image)
    id<HTMLElement>("gallery").style.display = "flex"
    id<HTMLImageElement>("galleryImage").src = image
    id<HTMLLinkElement>("galleryDownloadLink").href = image
}

fun closeGallery() {
    debug("closeGallery()")
    window.removeSearchParam("gallery")
    val gallery = document.getElementById("gallery") as HTMLElement
    gallery.style.display = "none"
}

fun clickRat(rat: String) {
    debug("clickRat(rat=$rat)")
    getParams().apply {
        set("rat", rat)
        window.location.search = toString()
    }
}