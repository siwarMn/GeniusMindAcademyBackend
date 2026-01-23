package com.pfeproject.GeniusMind.Repository;

import com.pfeproject.GeniusMind.Entity.ReclamationComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReclamationCommentRepository extends JpaRepository<ReclamationComment, Long> {
}
