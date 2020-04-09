package com.asyncapi.v2.model.info

import com.asyncapi.v2.ClasspathUtils
import com.google.gson.GsonBuilder
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

/**
 * @author Pavel Bodiachevskii
 */
class LicenseTest {

    private val gson = GsonBuilder().setPrettyPrinting().create()

    private fun buildLicense(): License {
        return License(
                "Apache License 2.0",
                "http://www.apache.org/licenses/"
        )
    }

    @Test
    @DisplayName("Compare hand crafted model with parsed json")
    fun compareModelWithParsedJson() {
        val model = ClasspathUtils.readAsString("/json/model/info/license.json")

        Assertions.assertEquals(
                gson.fromJson(model, License::class.java),
                buildLicense()
        )
    }

}
