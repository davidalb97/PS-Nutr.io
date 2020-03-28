package pt.isel.ps.g06.httpserver.util


data class ArrayDataClass<T>(val array: Array<T>) {

    //Default impl
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ArrayDataClass<*>

        if (!array.contentEquals(other.array)) return false

        return true
    }

    //Default impl
    override fun hashCode(): Int {
        return array.contentHashCode()
    }
}