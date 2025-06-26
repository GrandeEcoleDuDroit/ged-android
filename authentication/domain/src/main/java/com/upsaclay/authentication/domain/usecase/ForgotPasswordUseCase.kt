package com.upsaclay.authentication.domain.usecase

class ForgotPasswordUseCase {

    operator fun invoke(email : String){
        TODO("réfléchir à comment utiliser ce code : Firebase.auth.sendPasswordResetEmail(email)\n" +
                "            .addOnCompleteListener { task ->\n" +
                "                if (task.isSuccessful) {\n" +
                "                    Log.d(TAG, \"Email sent.\")\n" +
                "                }\n" +
                "            }" + "qui l'utilise et qui renvoie quel donnée en sachant que c'est le vue modède qui doit choisir quel message à afficher" +
                "au useCase de rempplir le cas d'utilisation nécéssaire au vue modèle, et à la classe qui wrape Firebase.auth de donner l'utils pour envoyer le mail" +
                "de réinisitalisation")
    }
}