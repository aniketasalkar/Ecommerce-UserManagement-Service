package com.example.usermanagementservice.repository;

import com.example.usermanagementservice.models.Session;
import com.example.usermanagementservice.models.SessionState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    Session save(Session session);
    Optional<Session> findById(Long sessionId);
    Optional<Session> findByUserId(Long userId);
    Optional<Session> findByUserIdAndSessionState(Long userId, SessionState sessionState);
}
