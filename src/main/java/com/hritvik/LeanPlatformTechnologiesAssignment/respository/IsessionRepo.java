package com.hritvik.LeanPlatformTechnologiesAssignment.respository;

import com.hritvik.LeanPlatformTechnologiesAssignment.model.Sessions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IsessionRepo extends JpaRepository<Sessions, Long>{
    List<Sessions> findByUserIdAndDateAfterOrderByDateAsc(Long userId, LocalDateTime currentDate);
}
