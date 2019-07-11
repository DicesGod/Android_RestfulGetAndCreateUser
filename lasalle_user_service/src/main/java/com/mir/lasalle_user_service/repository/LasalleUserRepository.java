package com.mir.lasalle_user_service.repository;

import com.mir.lasalle_user_service.model.LasalleUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LasalleUserRepository extends JpaRepository<LasalleUser, Long> {

    public LasalleUser findOneLasalleUserByEmail(String email);
}
