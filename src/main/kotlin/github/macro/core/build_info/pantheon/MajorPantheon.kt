package github.macro.core.build_info.pantheon

/**
 * Created by Macro303 on 2020-Sep-28
 */
enum class MajorPantheon {
	LUNARIS,
	ARAKAALI,
	SOLARIS,
	THE_BRINE_KING;

	companion object {
		fun value(name: String?): MajorPantheon? = values().firstOrNull {
			it.name.replace("_", " ").equals(name, ignoreCase = true)
		}
	}
}