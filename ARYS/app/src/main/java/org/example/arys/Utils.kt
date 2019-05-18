package org.example.arys

import org.json.JSONArray

class Utils {

    operator fun JSONArray.iterator(): Iterator<Any> = (0 until length()).asSequence().map { get(it) }.iterator()
}