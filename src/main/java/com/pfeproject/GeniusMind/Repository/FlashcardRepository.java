package com.pfeproject.GeniusMind.Repository;
import com.pfeproject.GeniusMind.Entity.Flashcard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FlashcardRepository extends JpaRepository<Flashcard, Long> {

    Page<Flashcard> findByOwnerEmail(String ownerEmail, Pageable pageable);

    Optional<Flashcard> findByIdAndOwnerEmail(Long id, String ownerEmail);

    void deleteByIdAndOwnerEmail(Long id, String ownerEmail);
}
