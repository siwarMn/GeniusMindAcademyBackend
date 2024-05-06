package com.pfeproject.GeniusMind.Repository;

import com.pfeproject.GeniusMind.Entity.reclamation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReclamationRepository extends JpaRepository<reclamation, Long> {
}
