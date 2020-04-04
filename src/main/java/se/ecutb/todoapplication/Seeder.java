package se.ecutb.todoapplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import se.ecutb.todoapplication.data.AppUserRepo;
import se.ecutb.todoapplication.data.AppUserRoleRepo;
import se.ecutb.todoapplication.entity.AppUser;
import se.ecutb.todoapplication.entity.AppUserRole;
import se.ecutb.todoapplication.entity.Role;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class Seeder {

    private AppUserRepo appUserRepo;
    private AppUserRoleRepo appUserRoleRepo;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public Seeder(AppUserRepo appUserRepo, AppUserRoleRepo appUserRoleRepo, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.appUserRepo = appUserRepo;
        this.appUserRoleRepo = appUserRoleRepo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostConstruct
    @Transactional
    public void init(){
        Set<AppUserRole> userRoles = Arrays.stream(Role.values())
                .map(userRole -> appUserRoleRepo.save(new AppUserRole(userRole)))
                .collect(Collectors.toSet());

        AppUser appUser = new AppUser("admin", "Olle", "Bolle", LocalDate.now(), bCryptPasswordEncoder.encode("admin"), 0);
        appUser.setRoleSet(userRoles);
        appUserRepo.save(appUser);

        AppUser newUser = new AppUser("user", "User", "User", LocalDate.now(), bCryptPasswordEncoder.encode("user"), 0);
        AppUserRole userRole = appUserRoleRepo.findByRole(Role.USER).get();
        Set<AppUserRole> roleSet = new HashSet<>();
        roleSet.add(userRole);
        newUser.setRoleSet(roleSet);
        newUser = appUserRepo.save(newUser);
    }



}
