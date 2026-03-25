package com.upsaclay.news.domain.post

import java.time.LocalDateTime

val postFixture = Post(
    id = "1",
    title = "Les voitures nouvelle génération",
    content =
        """
            Les voitures nouvelle génération redéfinissent la façon dont nous concevons la mobilité. 
            Alliant innovation technologique, respect de l’environnement et design futuriste, elles offrent une expérience de conduite unique. 
            Des moteurs électriques performants aux systèmes d’assistance intelligents, en passant par des habitacles connectés et confortables, chaque détail est pensé pour le conducteur de demain. 
            Plus qu’un simple moyen de transport, ces véhicules représentent une vision ambitieuse de l’avenir, où sécurité, efficacité et plaisir de conduite coexistent harmonieusement. 
            Embarquez dès aujourd’hui dans le futur de l’automobile.
        """.trimIndent(),
    link = "https://www.instagram.com/p/DDhO3CDo85r/?igsh=b3dvY28wM3BrN2Ny",
    source = Post.PostSource.INSTAGRAM,
    date = LocalDateTime.now(),
    state = Post.PostState.Published(
        imageUrls = listOf(
            "https://images.pexels.com/photos/68256/pexels-photo-68256.jpeg",
            "https://images.pexels.com/photos/4044066/pexels-photo-4044066.jpeg" +
                    "https://images.pexels.com/photos/8359715/pexels-photo-8359715.jpeg"
        )
    )
)

val postFixture2 = Post(
    id = "2",
    title = "🚀 L'avenir de la filière juridique se construit aujourd'hui",
    content =
       "Le Grenelle du Droit a tenu ses promesses : le programme de cette 6ᵉ édition a permis d'aborder les thématiques clés qui façonnent l’avenir de la profession, au travers d’un programme dense et orienté vers l’action :\n"+
       "1- L’IA dans la pratique juridique, atelier animé par Grégoire Hanquier\n"+
       "2- La formation initiale face aux exigences du marché, atelier animé par Marie Hombrouck\n"+
       "3- Les enjeux intergénérationnels, atelier animé par Nicolas Sarraquigne\n"+
       "4- La mobilité interprofessionnelle, atelier animé par Marie-Astrid d'Evry\n" +
       "5- L’hyperféminisation des métiers du droit, atelier animé par François Ameli\n"+
       "6- Se préparer aux risques géopolitiques, atelier animé par Marc Mossé\n"+
       "7- Les contrats de demain, atelier animé par Olivier Petit\n"+
       "8- Le handicap dans les métiers du droit, atelier animé par Stéphane Baller\n"+
       "9- L’évolution du droit de la concurrence en matière de durabilité, atelier animé par Alexandrine Lavaury\n"+
       "Un grand merci à l’ensemble des intervenants et de nos partenaires (Lefebvre Dalloz Lamy Liaisons - Groupe Karnov, LexisNexis, Sirion, Wolters Kluwer, Université Paris 1 Panthéon-Sorbonne, Le Monde du Droit, "+
       "4Change) pour leur soutien essentiel à la réussite de cette 6ᵉ édition du Grenelle du Droit."+
       "✨ Et une mention spéciale à Virginie Delalande ✨ (https://lnkd.in/ewfHrAfk) pour son mot de la fin, à la fois inspirant et stimulant :" +
       "« Oui, le droit mène à tout. Mais seulement si on y met du courage, de la curiosité et… un peu de folie. Alors sortons du cadre. Réinventons les codes. Faisons du droit une aventure humaine, vivante, vibrante. »",
    link = "https://www.instagram.com/p/DDhO3CDo85r/?igsh=b3dvY28wM3BrN2Ny",
    source = Post.PostSource.LINKEDIN,
    date = LocalDateTime.now().minusHours(2),
    state = Post.PostState.Published()
)

val postFixture3 = Post(
    id = "3",
    title = "🚀 L'avenir de la filière juridique se construit aujourd'hui",
    content =
        "Le Grenelle du Droit a tenu ses promesses : le programme de cette 6ᵉ édition a permis d'aborder les thématiques clés qui façonnent l’avenir de la profession, au travers d’un programme dense et orienté vers l’action :\n"+
                "1- L’IA dans la pratique juridique, atelier animé par Grégoire Hanquier\n"+
                "2- La formation initiale face aux exigences du marché, atelier animé par Marie Hombrouck\n"+
                "3- Les enjeux intergénérationnels, atelier animé par Nicolas Sarraquigne\n"+
                "4- La mobilité interprofessionnelle, atelier animé par Marie-Astrid d'Evry\n" +
                "5- L’hyperféminisation des métiers du droit, atelier animé par François Ameli\n"+
                "6- Se préparer aux risques géopolitiques, atelier animé par Marc Mossé\n"+
                "7- Les contrats de demain, atelier animé par Olivier Petit\n"+
                "8- Le handicap dans les métiers du droit, atelier animé par Stéphane Baller\n"+
                "9- L’évolution du droit de la concurrence en matière de durabilité, atelier animé par Alexandrine Lavaury\n"+
                "Un grand merci à l’ensemble des intervenants et de nos partenaires (Lefebvre Dalloz Lamy Liaisons - Groupe Karnov, LexisNexis, Sirion, Wolters Kluwer, Université Paris 1 Panthéon-Sorbonne, Le Monde du Droit, "+
                "4Change) pour leur soutien essentiel à la réussite de cette 6ᵉ édition du Grenelle du Droit."+
                "✨ Et une mention spéciale à Virginie Delalande ✨ (https://lnkd.in/ewfHrAfk) pour son mot de la fin, à la fois inspirant et stimulant :" +
                "« Oui, le droit mène à tout. Mais seulement si on y met du courage, de la curiosité et… un peu de folie. Alors sortons du cadre. Réinventons les codes. Faisons du droit une aventure humaine, vivante, vibrante. »",
    link = "https://www.instagram.com/p/DDhO3CDo85r/?igsh=b3dvY28wM3BrN2Ny",
    source = Post.PostSource.LINKEDIN,
    date = LocalDateTime.now().minusHours(2),
    state = Post.PostState.Published()
)

val postsFixture = listOf(postFixture, postFixture2)