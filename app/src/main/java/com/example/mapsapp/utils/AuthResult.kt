package com.example.mapsapp.utils
/*
* Aquesta classe ens servirà per obtenir el resultat de l’execució els mètodes de login i registre de Supabase Authentication.*/
sealed class AuthResult {
    object Success : AuthResult()
    data class Error(val message: String) : AuthResult()
}
