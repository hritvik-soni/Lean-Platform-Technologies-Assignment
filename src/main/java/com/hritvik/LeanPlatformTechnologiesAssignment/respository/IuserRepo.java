package com.hritvik.LeanPlatformTechnologiesAssignment.respository;

import com.hritvik.LeanPlatformTechnologiesAssignment.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IuserRepo extends JpaRepository<Users,Long> {

    Optional<Users> findByUsername(String username);

    List<Users> findByRole(String consultant);
}
