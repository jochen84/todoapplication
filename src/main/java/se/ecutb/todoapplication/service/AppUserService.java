package se.ecutb.todoapplication.service;

import se.ecutb.todoapplication.dto.AppUserFormDto;
import se.ecutb.todoapplication.entity.AppUser;

import java.util.Optional;

public interface AppUserService {

    AppUser registerNew(AppUserFormDto userFormDto);
    AppUser save(AppUser appUser);
    Optional<AppUser> findById(int userId);
    Optional<AppUser> findByUserName(String userName);
}
