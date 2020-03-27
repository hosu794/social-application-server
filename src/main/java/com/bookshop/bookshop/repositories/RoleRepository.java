package com.bookshop.bookshop.repositories;

import com.bookshop.bookshop.model.Role;
import com.bookshop.bookshop.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleName roleName);

}
