package se.ecutb.todoapplication.data;

import org.springframework.data.jpa.repository.JpaRepository;
import se.ecutb.todoapplication.entity.AppUserRole;
import se.ecutb.todoapplication.entity.Role;

import java.util.Optional;

public interface AppUserRoleRepo extends JpaRepository<AppUserRole, Integer> {

    @Override
    Optional<AppUserRole> findById(Integer integer);
    Optional<AppUserRole> findByRole(Role userRole);
}
