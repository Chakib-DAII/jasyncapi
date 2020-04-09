package com.asyncapi.v2.model.channel.message

import com.asyncapi.v2.ClasspathUtils
import com.asyncapi.v2.model.Tag
import com.google.gson.GsonBuilder
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

/**
 * @author Pavel Bodiachevskii
 */
class MessageTraitTest {

    private val gson = GsonBuilder().setPrettyPrinting().create()

    private fun buildMessageTrait(): MessageTrait {
        return MessageTrait.builder()
                .schemaFormat("application/vnd.apache.avro+json;version=1.9.0")
                .contentType("application/json")
                .build()
    }

    @Test
    @DisplayName("Compare hand crafted model with parsed json")
    fun compareModelWithParsedJson() {
        val model = ClasspathUtils.readAsString("/json/model/channel/message/messageTrait.json")

        Assertions.assertEquals(
                gson.fromJson(model, MessageTrait::class.java),
                buildMessageTrait()
        )
    }

}
