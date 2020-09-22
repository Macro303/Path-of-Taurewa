package github.macro.ui.items.body_armours

import github.macro.core.Data
import github.macro.core.items.body_armours.BuildBodyArmour
import github.macro.core.items.body_armours.ItemBodyArmour
import github.macro.ui.AbstractItemSelector
import java.io.File

/**
 * Created by Macro303 on 2020-Sep-22
 */
class BodyArmourSelector : AbstractItemSelector<BuildBodyArmour, ItemBodyArmour>() {

	override fun updateSelection(selected: ItemBodyArmour?) {
		selectedItem = BuildBodyArmour(selected ?: Data.getBodyArmour(null))
		var image = selectedItem!!.item.getFile()
		if (selectedItem!!.item.name != "None" && !image.exists())
			image = File(image.parent, "placeholder.png")
		imageUrl = "file:${image.path}"
	}
}