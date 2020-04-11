package se.ecutb.todoapplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.ecutb.todoapplication.data.AppUserRepo;
import se.ecutb.todoapplication.data.AppUserRoleRepo;
import se.ecutb.todoapplication.dto.AppUserFormDto;
import se.ecutb.todoapplication.entity.AppUser;
import se.ecutb.todoapplication.entity.AppUserRole;
import se.ecutb.todoapplication.entity.Role;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AppUserServiceImpl implements AppUserService {

    private AppUserRepo appUserRepo;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private AppUserRoleRepo appRoleRepo;

    @Autowired
    public AppUserServiceImpl(AppUserRepo appUserRepo, BCryptPasswordEncoder bCryptPasswordEncoder, AppUserRoleRepo appRoleRepo) {
        this.appUserRepo = appUserRepo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.appRoleRepo = appRoleRepo;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public AppUser registerNew(AppUserFormDto userFormDto) {
        AppUserRole userRole = appRoleRepo.findByRole(Role.USER).get(); //.orElseThrow(()-> new IllegalArgumentException("Couln't find role of " + Role.USER));
        AppUserRole adminRole = appRoleRepo.findByRole(Role.ADMIN).get();
       Set<AppUserRole> roleSet = new HashSet<>();
       roleSet.add(userRole);
       //If Admin checkbox is checked on first creation it runs this
       if (userFormDto.isAdmin()){
           roleSet.add(adminRole);
       }

        AppUser newUser = new AppUser(
                userFormDto.getUserName(),
                userFormDto.getFirstName(),
                userFormDto.getLastName(),
                LocalDate.now(),
                bCryptPasswordEncoder.encode(userFormDto.getPassword()),
                0
        );

        newUser = appUserRepo.save(newUser);
        newUser.setRoleSet(roleSet);
        return newUser;
    }

    @Override
    public AppUser save(AppUser appUser) {
        return appUserRepo.save(appUser);
    }

    @Override
    public Optional<AppUser> findById(int userId) {
        return appUserRepo.findById(userId);
    }

    @Override
    public Optional<AppUser> findByUserName(String userName) {
        return appUserRepo.findAppUserByUserName(userName);
    }

    @Override
    public List<AppUser> findAll() {
        return appUserRepo.findAll();
    }

    @Override
    public AppUser delete(AppUser appUser) {
        appUserRepo.delete(appUser);
        return appUser;
    }


}
