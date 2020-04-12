package se.ecutb.todoapplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import se.ecutb.todoapplication.data.AppUserRepo;
import se.ecutb.todoapplication.data.AppUserRoleRepo;
import se.ecutb.todoapplication.data.TodoItemRepo;
import se.ecutb.todoapplication.entity.AppUser;
import se.ecutb.todoapplication.entity.AppUserRole;
import se.ecutb.todoapplication.entity.Role;
import se.ecutb.todoapplication.entity.TodoItem;
import se.ecutb.todoapplication.service.TodoItemService;

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
    private TodoItemRepo todoItemRepo;


    @Autowired
    public Seeder(AppUserRepo appUserRepo, AppUserRoleRepo appUserRoleRepo, BCryptPasswordEncoder bCryptPasswordEncoder, TodoItemRepo todoItemRepo) {
        this.appUserRepo = appUserRepo;
        this.appUserRoleRepo = appUserRoleRepo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.todoItemRepo = todoItemRepo;
    }

    @PostConstruct
    @Transactional
    public void init(){
        // Add roles to database, and a set
        Set<AppUserRole> userRoles = Arrays.stream(Role.values())
                .map(userRole -> appUserRoleRepo.save(new AppUserRole(userRole)))
                .collect(Collectors.toSet());

        // Create user, give admin rights, save to database
        AppUser appUser = new AppUser("admin", "Olle", "Bolle", LocalDate.now(), bCryptPasswordEncoder.encode("admin"), 0);
        appUser.setRoleSet(userRoles);
        appUserRepo.save(appUser);

        // Create user, give user rights, save to database
        AppUser newUser = new AppUser("user", "User", "User", LocalDate.now(), bCryptPasswordEncoder.encode("user"), 0);
        AppUserRole userRole = appUserRoleRepo.findByRole(Role.USER).get();
        Set<AppUserRole> roleSet = new HashSet<>();
        roleSet.add(userRole);
        newUser.setRoleSet(roleSet);


        // Create todos, save to database
        TodoItem washTodo = new TodoItem("Tvätta bilen", "Här behövs massvis med instruktioner som berättar hur man går tillväga för att tvätta sin bil!", LocalDate.of(2020,05,01), false, 50);
        TodoItem cleanTodo = new TodoItem("Städa garderoben", "Likadant här, finns inte en karl som kan göra det utan vettiga instruktioner från sin sambo/fru..", LocalDate.of(2020,06,05), false, 100);
        TodoItem blahTodo = new TodoItem("Blaha blabla", "Likadant här, bla blra bla blra blblblblblba..", LocalDate.of(2021,06,05), false, 100);

        newUser.addUsersTodo(cleanTodo);
        newUser.addUsersTodo(washTodo);
        newUser.addUsersTodo(blahTodo);

        newUser = appUserRepo.save(newUser);

    }



}
