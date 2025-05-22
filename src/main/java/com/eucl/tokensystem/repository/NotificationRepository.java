package com.eucl.tokensystem.repository;

import com.eucl.tokensystem.model.Notification;
import com.eucl.tokensystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserOrderByIssuedDateDesc(User user);
    
    List<Notification> findBySentIsFalse();
}
