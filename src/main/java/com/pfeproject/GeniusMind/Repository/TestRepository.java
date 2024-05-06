package com.pfeproject.GeniusMind.Repository;


import com.pfeproject.GeniusMind.Entity.TestEntity;
import com.pfeproject.GeniusMind.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends JpaRepository<TestEntity, Long> {


}
