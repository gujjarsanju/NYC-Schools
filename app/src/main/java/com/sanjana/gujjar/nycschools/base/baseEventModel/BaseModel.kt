package com.sanjana.gujjar.nycschools.base.baseEventModel

open class ScreenEvent {
    override fun equals(other: Any?): Boolean {
        return other != null && this::class.java.name == other::class.java.name
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}

open class StateEvent {
    override fun equals(other: Any?): Boolean {
        return other != null && this::class.java.name == other::class.java.name
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}

open class ActionEvent {
    override fun equals(other: Any?): Boolean {
        return other != null && this::class.java.name == other::class.java.name
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}

open class MessageEvent(
    val id: Int = 0,
    val title: String? = null,
    val message: String? = null,
    val positiveOptionLabel: String? = null,
    val negativeOptionLabel: String? = null,
    val isCancelable: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        return other is MessageEvent && id == other.id
    }
}
