package com.example.notif_backend.repository;

import com.example.notif_backend.model.Envoi;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface EnvoiRepository extends JpaRepository<Envoi, Long> {



    @Query("SELECT e FROM Envoi e JOIN e.notifications n WHERE n.id = :notificationId")
    List<Envoi> findByNotificationsId(@Param("notificationId") Long notificationId);



    @Query("SELECT DISTINCT e FROM Envoi e LEFT JOIN FETCH e.notifications n LEFT JOIN FETCH n.destinataire WHERE e.datePrevueEnvoi <= :now AND e.dateEnvoi IS NULL")
    List<Envoi> findReadyEnvois(@Param("now") LocalDate now);









    List<Envoi> findByDatePrevueEnvoiBeforeAndDateEnvoiIsNull(LocalDate date);

//    // Requête pour récupérer les envois associés à une notification précise (par son id)
//    @Query("SELECT e FROM Envoi e JOIN e.notifications n WHERE n.id = :notificationId")
//    List<Envoi> findByNotificationsId(@Param("notificationId") Long notificationId);
//
//    // Méthode que tu utilises pour récupérer les envois prêts à être envoyés
//    @Query("SELECT e FROM Envoi e WHERE e.datePrevueEnvoi <= :date AND e.destinataires IS NOT EMPTY AND e.dateEnvoi IS NULL")
//    List<Envoi> findReadyEnvoisWithDestinataires(@Param("date") LocalDate date);

}
