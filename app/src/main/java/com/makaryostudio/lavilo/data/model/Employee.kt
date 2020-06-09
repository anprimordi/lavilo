package com.makaryostudio.lavilo.data.model

// Kelas model data karyawan
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