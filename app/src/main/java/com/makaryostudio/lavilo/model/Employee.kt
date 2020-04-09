package com.makaryostudio.lavilo.model

data class Employee(
    var id: String? = null,
    var name: String? = null,
    var email: String? = null,
    var password: String? = null
) {
    constructor() : this(
        "",
        "",
        "",
        ""
    )
}