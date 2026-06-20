package hu.mokegyesulet.it.dunestrat.util

object StringNormalizer {
    /**
     * Normalizes a string by:
     * - Removing accents from characters (á -> a, é -> e, etc.)
     * - Converting to lowercase
     * - Replacing spaces with underscores
     */
    fun normalize(text: String): String = removeAccents(text)
        .lowercase()
        .replace(" ", "_")

    /**
     * Removes accents from a string (á -> a, é -> e, etc.)
     * Useful when you only need accent removal without other transformations.
     */
    fun removeAccents(text: String): String {
        val accentMap = mapOf(
            'á' to 'a', 'à' to 'a', 'ä' to 'a', 'â' to 'a', 'ã' to 'a', 'å' to 'a',
            'é' to 'e', 'è' to 'e', 'ë' to 'e', 'ê' to 'e',
            'í' to 'i', 'ì' to 'i', 'ï' to 'i', 'î' to 'i',
            'ó' to 'o', 'ò' to 'o', 'ö' to 'o', 'ô' to 'o', 'õ' to 'o',
            'ú' to 'u', 'ù' to 'u', 'ü' to 'u', 'û' to 'u',
            'ý' to 'y', 'ỳ' to 'y', 'ÿ' to 'y',
            'ç' to 'c', 'ñ' to 'n',
            'Á' to 'A', 'À' to 'A', 'Ä' to 'A', 'Â' to 'A', 'Ã' to 'A', 'Å' to 'A',
            'É' to 'E', 'È' to 'E', 'Ë' to 'E', 'Ê' to 'E',
            'Í' to 'I', 'Ì' to 'I', 'Ï' to 'I', 'Î' to 'I',
            'Ó' to 'O', 'Ò' to 'O', 'Ö' to 'O', 'Ô' to 'O', 'Õ' to 'O',
            'Ú' to 'U', 'Ù' to 'U', 'Ü' to 'U', 'Û' to 'U',
            'Ý' to 'Y',
            'Ç' to 'C', 'Ñ' to 'N',
        )
        return text.map { accentMap[it] ?: it }.joinToString("")
    }
}
