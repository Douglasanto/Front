package br.com.front.navigation  // ou br.com.front.core

sealed class Screens(val route: String) {
    // Autenticação
    object Login : Screens("login")
    object SignUp : Screens("signup")

    // Cadastro
    object PersonData : Screens("personData")
    object PersonalAddress : Screens("personalAddress")
    object Socioeconomic : Screens("socioeconomic")

    // App Principal
    object Home : Screens("home")
    object Avaliacoes : Screens("avaliacoes")
    object Mensagens : Screens("mensagens")
    object Perfil : Screens("perfil")

    // Detalhes
    object DetalhesLocal : Screens("detalhes/{placeId}") {
        fun createRoute(placeId: String) = "detalhes/$placeId"
    }
}