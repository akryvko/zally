package de.zalando.zally.rule

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import java.net.URL

class ObjectTreeReader {

    private val jsonRegex = "^\\s*\\{".toRegex()

    private val jsonMapper = ObjectMapper()
    private val yamlMapper = ObjectMapper(YAMLFactory())

    fun read(content: String): JsonNode =
            if (isYaml(content))
                readYaml(content)
            else
                readJson(content)

    fun readJson(content: String): JsonNode =
            jsonMapper.readTree(content)

    fun readYaml(content: String): JsonNode =
            yamlMapper.readTree(content)

    fun readYaml(yamlUrl: URL): JsonNode =
            yamlMapper.readTree(yamlUrl)

    fun readJson(jsonUrl: URL): JsonNode =
            jsonMapper.readTree(jsonUrl)

    fun read(parser: JsonParser): JsonNode =
            jsonMapper.readTree(parser)

    fun isYaml(specContent: String): Boolean =
            !specContent.matches(jsonRegex)

    fun getParser(specContent: String): JsonParser =
            if (isYaml(specContent))
                yamlMapper.factory.createParser(specContent)
            else
                jsonMapper.factory.createParser(specContent)
}
