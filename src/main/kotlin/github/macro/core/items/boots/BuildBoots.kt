package github.macro.core.items.boots

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import github.macro.Util
import github.macro.core.Data
import github.macro.core.IBuildItem
import github.macro.core.IItem
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import org.apache.logging.log4j.LogManager
import tornadofx.*
import java.io.IOException

/**
 * Created by Macro303 on 2020-Sep-22
 */
@JsonDeserialize(using = BuildBootsDeserializer::class)
@JsonSerialize(using = BuildBootsSerializer::class)
class BuildBoots(
	item: ItemBoots,
	nextItem: BuildBoots? = null,
	reason: String? = null
) : IBuildItem {
	val itemProperty = SimpleObjectProperty<IItem>()
	override var item by itemProperty

	val nextItemProperty = SimpleObjectProperty<IBuildItem?>()
	override var nextItem by nextItemProperty

	val reasonProperty = SimpleStringProperty()
	override var reason by reasonProperty

	init {
		this.item = item
		this.nextItem = nextItem
		this.reason = reason
	}
}

private class BuildBootsDeserializer @JvmOverloads constructor(vc: Class<*>? = null) :
	StdDeserializer<BuildBoots?>(vc) {

	@Throws(IOException::class, JsonProcessingException::class)
	override fun deserialize(parser: JsonParser, ctx: DeserializationContext?): BuildBoots? {
		val node: JsonNode = parser.codec.readTree(parser)

		val boots = Data.getBootsById(node["Item"].asText())
		val nextBoots = if (node["Next Item"].isNull)
			null
		else
			Util.YAML_MAPPER.treeToValue(node["Next Item"], BuildBoots::class.java)
		val reason = node["Reason"]?.asText()

		return BuildBoots(
			item = boots,
			nextItem = nextBoots,
			reason = reason
		)
	}

	companion object {
		private val LOGGER = LogManager.getLogger()
	}
}

private class BuildBootsSerializer @JvmOverloads constructor(t: Class<BuildBoots>? = null) :
	StdSerializer<BuildBoots>(t) {

	@Throws(IOException::class, JsonProcessingException::class)
	override fun serialize(value: BuildBoots, parser: JsonGenerator, provider: SerializerProvider?) {
		parser.writeStartObject()
		parser.writeStringField("Item", value.item.id)
		parser.writeObjectField("Next Item", (value.nextItem as BuildBoots?))
		parser.writeStringField("Reason", value.reason)
		parser.writeEndObject()
	}
}