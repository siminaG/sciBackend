package com.simina.sci.repository;

import com.simina.sci.model.ERoleName;
import com.simina.sci.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
;import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERoleName roleName);
}
