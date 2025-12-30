package com.pfeproject.GeniusMind.Repository;

import com.pfeproject.GeniusMind.Entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Integer> {
    List<Quiz> findAllByIsActiveTrue();
}
