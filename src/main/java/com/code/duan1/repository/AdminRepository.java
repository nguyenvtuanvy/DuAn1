package com.code.duan1.repository;

import com.code.duan1.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findAdminByUsername(String username);

    @Query("select ad.fullName from Admin ad where ad.id = :postId")
    String getNameById(@Param("postId") Long postId);
}
