package se.ecutb.todoapplication.data;

import org.springframework.data.jpa.repository.JpaRepository;
import se.ecutb.todoapplication.entity.AppUser;

import java.util.List;
import java.util.Optional;

public interface AppUserRepo extends JpaRepository<AppUser, Integer> {

    //Added this
    Optional<AppUser> findAppUserByUserName(String userName);

}
