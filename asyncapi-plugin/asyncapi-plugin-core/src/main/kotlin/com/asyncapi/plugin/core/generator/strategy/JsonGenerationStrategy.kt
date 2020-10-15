package com.asyncapi.plugin.core.generator.strategy

import com.asyncapi.plugin.core.io.AsyncAPISchemaLoader
import com.asyncapi.plugin.core.generator.settings.GenerationSettings
import com.asyncapi.plugin.core.generator.GenerationStrategy
import com.asyncapi.plugin.core.generator.exception.AsyncAPISchemaGenerationException
import com.asyncapi.plugin.core.io.FileSystem
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import kotlin.jvm.Throws

/**
 * AsyncAPI schema generation strategy in json format.
 *
 * @param generationSettings schema generation settings.
 *
 * @author Pavel Bodiachevskii
 * @since 1.0.0-RC1
 */
class JsonGenerationStrategy(
        private val generationSettings: GenerationSettings
): GenerationStrategy {

    private val asyncAPISchemaLoader = AsyncAPISchemaLoader(generationSettings.logger, generationSettings.sources)

    private val objectMapper: ObjectMapper by lazy {
        val instance = ObjectMapper()

        if (!generationSettings.rules.includeNulls) {
            instance.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        }

        instance
    }

    @Throws(AsyncAPISchemaGenerationException::class)
    override fun generate() {
        val schemas = asyncAPISchemaLoader.load()
        schemas.forEach(this::save)
    }

    private fun save(schemaClass: Class<*>) {
        val schema = serialize(schemaClass)
        val schemaFileName = "${schemaClass.simpleName}-${generationSettings.schemaFile.namePostfix}.json"
        val schemaFilePath = generationSettings.schemaFile.path

        FileSystem.save(schemaFileName, schema, schemaFilePath)
    }

    @Throws(AsyncAPISchemaGenerationException::class)
    @Suppress("Duplicates")
    private fun serialize(schemaClass: Class<*>): String {
        return try {
            val foundAsyncAPI = schemaClass.newInstance()

            if (generationSettings.rules.prettyPrint) {
                objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(foundAsyncAPI)
            } else {
                objectMapper.writeValueAsString(foundAsyncAPI)
            }
        } catch (exception: Exception) {
            throw AsyncAPISchemaGenerationException("Can't serialize: ${schemaClass.simpleName}. Because of: ${exception.message}", exception)
        }
    }

}