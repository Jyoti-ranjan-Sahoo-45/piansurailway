package com.railbookpro.repository;

import com.railbookpro.domain.entity.Role;
import com.railbookpro.domain.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleType name);
}
