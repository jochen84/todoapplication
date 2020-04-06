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
        //Lägger till de 2 roller som finns o lägger i en lista för att sedan lägga på Admin användaren
        Set<AppUserRole> userRoles = Arrays.stream(Role.values())
                .map(userRole -> appUserRoleRepo.save(new AppUserRole(userRole)))
                .collect(Collectors.toSet());
        //Skapar en user, lägger till roleset med admin och user - sparar till databasen
        AppUser appUser = new AppUser("admin", "Olle", "Bolle", LocalDate.now(), bCryptPasswordEncoder.encode("admin"), 0);
        appUser.setRoleSet(userRoles);
        appUserRepo.save(appUser);

        //Skapar en user, lägger till roleset med bara user - sparar till databasen
        AppUser newUser = new AppUser("user", "User", "User", LocalDate.now(), bCryptPasswordEncoder.encode("user"), 0);
        AppUserRole userRole = appUserRoleRepo.findByRole(Role.USER).get();
        Set<AppUserRole> roleSet = new HashSet<>();
        roleSet.add(userRole);
        newUser.setRoleSet(roleSet);


        //Skapa Todos för att lägga till användaren
        TodoItem washTodo = new TodoItem("Tvätta bilen", "Här behövs massvis med instruktioner som berättar hur man går tillväga för att tvätta sin bil!", LocalDate.of(2020,05,01), false, 50);
        TodoItem cleanTodo = new TodoItem("Städa garderoben", "Likadant här, finns inte en karl som kan göra det utan vettiga instruktioner från sin sambo/fru..", LocalDate.of(2020,06,05), false, 100);
        Set<TodoItem> todoSet = new HashSet<>();
        
        // Detta funkar. save måste komma först.
        todoItemRepo.save(cleanTodo);
        todoItemRepo.save(washTodo);
        todoSet.add(washTodo);
        todoSet.add(cleanTodo);

        // Får inte denna att koppla till användare... Kan vara ett cascade-problem.

        appUserRepo.save(newUser);
        cleanTodo.setAssignee(newUser);
        washTodo.setAssignee(newUser);





    }



}
