package com.app.quick_links.Model

public class UserApp
{
    private lateinit var idUser: String
    private lateinit var email: String
    private lateinit var password: String

    fun getId(): String
    { return idUser }

    fun setId(idUser: String) { this.idUser = idUser }

    fun getEmail(): String { return email }

    fun setEmail(email: String) { this.email = email }

    fun getPassword(): String { return password }

    fun setPassword(password: String) { this.password = password }
}