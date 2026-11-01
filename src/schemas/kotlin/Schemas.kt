import com.mineinabyss.extracommands.ExtraConfig
import com.mineinabyss.jsonschema.writer.writeSchemas

fun main(args: Array<String>) = writeSchemas(args) {
    schema("extracommands/config", listOf("**/plugins/ExtraCommands/config.yml"), ExtraConfig.serializer())
}
