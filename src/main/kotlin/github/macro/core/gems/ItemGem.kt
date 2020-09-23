package github.macro.core.gems

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import github.macro.Util
import github.macro.core.IItem
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import org.apache.logging.log4j.LogManager
import tornadofx.*
import java.io.File
import java.io.IOException

/**
 * Created by Macro303 on 2020-Jan-13.
 */
@JsonDeserialize(using = GemDeserializer::class)
class ItemGem(
	id: String,
	name: String,
	isReleased: Boolean,
	colour: Colour,
	tags: List<String>,
	isVaal: Boolean,
	isSupport: Boolean,
	isAwakened: Boolean,
	acquisition: Acquisition
) : IItem {
	val idProperty = SimpleStringProperty()
	override var id by idProperty

	val nameProperty = SimpleStringProperty()
	override var name by nameProperty

	val isReleasedProperty = SimpleBooleanProperty()
	override var isReleased by isReleasedProperty

	val colourProperty = SimpleObjectProperty<Colour>()
	var colour by colourProperty

	val tagsProperty = SimpleListProperty<String>()
	var tags by tagsProperty

	val isVaalProperty = SimpleBooleanProperty()
	var isVaal by isVaalProperty

	val isSupportProperty = SimpleBooleanProperty()
	var isSupport by isSupportProperty

	val isAwakenedProperty = SimpleBooleanProperty()
	var isAwakened by isAwakenedProperty

	val acquisitionProperty = SimpleObjectProperty<Acquisition>()
	var acquisition by acquisitionProperty

	init {
		this.id = id
		this.name = name
		this.isReleased = isReleased
		this.colour = colour
		this.tags = FXCollections.observableList(tags)
		this.isVaal = isVaal
		this.isSupport = isSupport
		this.isAwakened = isAwakened
		this.acquisition = acquisition
	}

	override fun getDisplayName(): String {
		val prefix = if (isVaal) "Vaal " else if (isAwakened) "Awakened " else ""
		val suffix = if (isSupport) " Support" else ""
		return prefix + name + suffix
	}
}

private class GemDeserializer @JvmOverloads constructor(vc: Class<*>? = null) : StdDeserializer<ItemGem?>(vc) {

	@Throws(IOException::class, JsonProcessingException::class)
	override fun deserialize(parser: JsonParser, ctx: DeserializationContext): ItemGem? {
		val node: JsonNode = parser.readValueAsTree()

		val id = node["id"].asText()
		val name = node["name"].asText()
		val isReleased = node["isReleased"]?.asBoolean(false) ?: false
		val colour = Colour.value(node["colour"].asText())
		if (colour == null) {
			LOGGER.info("Invalid Colour: ${node["colour"].asText()}")
			return null
		}
		val tags = node["tags"].mapNotNull { it.asText() }.sorted()
		val isVaal = node["isVaal"]?.asBoolean(false) ?: false
		val isSupport = node["isSupport"]?.asBoolean(false) ?: false
		val isAwakened = node["isAwakened"]?.asBoolean(false) ?: false

		val acquisition = Util.JSON_MAPPER.treeToValue(node["acquisition"], Acquisition::class.java)

		return ItemGem(
			id = id,
			name = name,
			isReleased = isReleased,
			colour = colour,
			tags = tags,
			isVaal = isVaal,
			isSupport = isSupport,
			isAwakened = isAwakened,
			acquisition = acquisition
		)
	}

	companion object {
		private val LOGGER = LogManager.getLogger(GemDeserializer::class.java)
	}
}