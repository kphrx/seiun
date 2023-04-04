package io.github.akiomik.seiun.model.com.atproto.repo

import com.squareup.moshi.JsonClass
import io.github.akiomik.seiun.model.type.HasNsid

@JsonClass(generateAdapter = true)
data class StrongRef(
    val uri: String,
    val cid: String
) : HasNsid by Companion {
    companion object : HasNsid {
        override val nsid: String
            get() = "com.atproto.repo.strongRef"
    }
}
