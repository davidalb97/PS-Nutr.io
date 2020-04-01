package pt.isel.ps.g06.httpserver.dataAccess.api

private const val ZOMATO_BASE_URL = "https://developers.zomato.com/api/v2.1/"
private const val ZOMATO_GEOCODE_URL = "${ZOMATO_BASE_URL}geocode?"
private const val ZOMATO_SEARCH_URL = "${ZOMATO_BASE_URL}search?"
private const val ZOMATO_CITIES_URL = "${ZOMATO_BASE_URL}cities?"

enum class Entity { city, subzone, zone, landmark, metro, group }

enum class Sort { cost, rating, real_distance }

enum class Order { asc, desc }

fun geocode(lat: Float, lon: Float) = "${ZOMATO_GEOCODE_URL}lat=$lat&lon=$lon"

fun cities(lat: Float,
           lon: Float,
           count: Int = 30,
           city_ids: Array<String>?
) = "${ZOMATO_CITIES_URL}lat=$lat&lon=$lon&count=$count" +
        (city_ids?.joinToString(",", "&cuisines=") ?: "")


fun search(
        lat: Float,
        lon: Float,
        radius: Int = 1000,
        count: Int = 100,
        skip: Int = 0,
        sort: Sort = Sort.real_distance,
        order: Order = Order.asc,
        entity_id: Int? = null,
        entity_type: Entity? = null,
        establishment_type_id : Int? = null,
        cuisines: Array<String>? = null,
        collection_id: Int? = null,
        category_id: Int? = null
) = ZOMATO_SEARCH_URL +
        "start=$skip" +
        "&count=$count" +
        "&lat=$lat" +
        "&lon=$lon" +
        "&radius=$radius" +
        "&sort=$sort" +
        "&order=$order" +
        (if(entity_id != null) "&entity_id=$entity_id" else "") +
        (if(entity_type != null) "&entity_type=$entity_type" else "") +
        (cuisines?.joinToString(",", "&cuisines=") ?: "") +
        (if(establishment_type_id != null) "&establishment_type=$establishment_type_id" else "") +
        (if(collection_id != null) "&collection_id=$collection_id" else "") +
        (if(category_id != null) "&category=$category_id" else "")