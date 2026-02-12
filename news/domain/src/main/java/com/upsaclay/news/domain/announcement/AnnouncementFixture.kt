package com.upsaclay.news.domain.announcement

import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.domain.usersFixture
import com.upsaclay.news.domain.announcement.Announcement.AnnouncementState
import java.time.LocalDateTime

val longAnnouncementFixture = Announcement(
    id = "1",
    title = "🌴Planification des congés d'été - Soumission des demandes avant le 15 juin 😎☀️",
    date = LocalDateTime.of(2024, 7, 20, 10, 0),
    content = "Bonjour Général,\n\n" +
            "Comme chaque année, la période estivale nécessite une organisation particulière afin de concilier au mieux " +
            "continuité de service et temps de repos pour chacun.\n\n" +
            "Conformément aux recommandations des Ressources Humaines, je vous invite à transmettre les propositions de congés " +
            "de vos équipes pour la période allant du [date de début] au [date de fin], en veillant à assurer une présence " +
            "suffisante pour maintenir l’activité essentielle de vos services.\n\n" +
            "Il est important que chaque agent puisse bénéficier d’un temps de repos estival, tout en garantissant la continuité " +
            "des missions prioritaires. Une attention particulière devra être portée à l’équilibre entre les besoins du service " +
            "et les souhaits des personnels.\n\n" +
            "Merci de bien vouloir faire remonter les plannings prévisionnels au plus tard le [date limite], afin de permettre " +
            "une validation en temps utile.\n\n" +
            "Je reste à votre disposition pour toute précision complémentaire.\n\n" +
            "Bien cordialement,\n" +
            "Patrick Dupont",
    author = userFixture,
    state = AnnouncementState.PUBLISHED
)

val announcementFixture = Announcement(
    id = "1",
    title = "Rappel : Visite de cabinet le 23/03.",
    date = LocalDateTime.of(2024, 7, 20, 10, 0),
    content = "Nous vous informons que la visite de votre " +
            "cabinet médical est programmée pour le 23 mars. " +
            "Cette visite a pour but de s'assurer que toutes les normes de sécurité " +
            "et de conformité sont respectées, ainsi que de vérifier l'état général " +
            "des installations et des équipements médicaux." +
            "Nous vous recommandons de préparer tous les documents nécessaires et " +
            "de veiller à ce que votre personnel soit disponible pour répondre " +
            "à d'éventuelles questions ou fournir des informations supplémentaires. " +
            "Une préparation adéquate permettra de garantir que la visite se déroule " +
            "sans heurts et de manière efficace. N'hésitez pas à nous contacter si " +
            "vous avez des questions ou si vous avez besoin de plus amples informations " +
            "avant la date prévue",
    author = userFixture,
    state = AnnouncementState.PUBLISHED
)

val announcementsFixture = listOf(
    Announcement(
        id = "1",
        title = "Soirée pyjama !",
        content = "Ceci est une annonce de soirée pyjama.",
        date = LocalDateTime.now(),
        author = usersFixture[0],
        state = AnnouncementState.PUBLISHED
    ),
    Announcement(
        id = "2",
        title = "Rappel : Rendu de dossier le 23/03",
        content = "Ceci est une annonce de rendu de dossier.",
        date = LocalDateTime.now().minusDays(1),
        author = usersFixture[1],
        state = AnnouncementState.PUBLISHED
    ),
    Announcement(
        id = "3",
        title = null,
        content = "Bonjour à tous, voici la liste des étudiants qui seront absent durant la journée portes ouvertes.",
        date = LocalDateTime.now().minusDays(3),
        author = usersFixture[2],
        state = AnnouncementState.PUBLISHED
    ),
    Announcement(
        id = "4",
        title = "Attention à la neige ❄️",
        content = "Ceci est une annonce de rendu de dossier.",
        date = LocalDateTime.now().minusYears(1),
        author = usersFixture[5],
        state = AnnouncementState.PUBLISHED
    ),
    Announcement(
        id = "5",
        title = "Aide au devoir",
        content = "Ceci est une annonce pour l'aide au devoir.",
        date = LocalDateTime.now().minusMonths(1),
        author = usersFixture[4],
        state = AnnouncementState.PUBLISHED
    ),
    Announcement(
        id = "6",
        title = "Rendez-vous accueil",
        content = "Ceci est une annonce pour un rendez-vous d'accueil.",
        date = LocalDateTime.now().minusWeeks(1),
        author = usersFixture[3],
        state = AnnouncementState.PUBLISHED
    ),
    Announcement(
        id = "7",
        title = null,
        content = "Cadeau 🎁",
        date = LocalDateTime.now().minusYears(1),
        author = usersFixture[6],
        state = AnnouncementState.PUBLISHED
    )
)