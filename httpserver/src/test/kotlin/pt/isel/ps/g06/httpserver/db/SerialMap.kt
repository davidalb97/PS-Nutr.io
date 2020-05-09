package pt.isel.ps.g06.httpserver.db

import org.jdbi.v3.core.Jdbi

open class AutoMap<K, V>(private val valueFunc: (K) -> V) : HashMap<K, V>() {

    override fun get(key: K): V {
        var currVal = super.get(key)
        if (currVal == null) {
            currVal = valueFunc(key)
            this[key] = currVal
            return currVal
        }
        return currVal
    }
}

class TableMap internal constructor(
        private val jdbi: Jdbi,
        private val tableName: String
) : AutoMap<String, Int>({ key ->
    nextSerialValue(jdbi, tableName, key)
})

class SerialMap(private val jdbi: Jdbi) : AutoMap<String, TableMap>({ key ->
    TableMap(jdbi, key)
})