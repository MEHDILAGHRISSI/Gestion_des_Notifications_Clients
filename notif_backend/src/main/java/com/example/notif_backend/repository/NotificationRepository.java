package com.example.notif_backend.repository;

import com.example.notif_backend.dto.NotificationDashboardDTO;
import com.example.notif_backend.model.EtatNotification;
import com.example.notif_backend.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.notif_backend.model.Envoi;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Optional<Notification> findById(Long id);

    @Query("SELECT n FROM Notification n LEFT JOIN FETCH n.attachements WHERE n.id = :id")
    Optional<Notification> findByIdWithAttachments(@Param("id") Long id);

    @Query(value = """
    SELECT n.id as id, 
           n.objet as objet, 
           n.date_creation as dateCreation, 
           n.signataire as signataire,
           COUNT(e.numero_envoi) as envoisLies,
           GROUP_CONCAT(DISTINCT e.forme) as formes
    FROM notification n
    LEFT JOIN envoi e ON e.numero_envoi = n.envoi_id
    GROUP BY n.id, n.objet, n.date_creation, n.signataire
    """, nativeQuery = true)
    List<NotificationDashboardDTO> fetchDashboardNotifications();


    @Query("select n from Notification n " +
            "left join fetch n.utilisateur u " +
            "left join fetch n.envoi e " +
            "left join fetch n.destinataire d " +
            "where n.id = :id")
    Optional<Notification> findByIdWithDetails(@Param("id") Long id);

    List<Notification> findByEtatIn(List<EtatNotification> created);

    @Query("SELECT n FROM Notification n WHERE n.template.type = :type")
    List<Notification> findByTypeNotification(@Param("type") String type);

    @Query("SELECT n FROM Notification n JOIN FETCH n.template t WHERE t.type = :templateType AND n.etat = :etat")
    List<Notification> findWithTemplateByTypeAndEtat(@Param("templateType") String templateType, @Param("etat") EtatNotification  etat);

}

