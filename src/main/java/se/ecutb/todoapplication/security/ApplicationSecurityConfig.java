package se.ecutb.todoapplication.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //http.authorizeRequests().antMatchers("/users/**").permitAll();

        http.authorizeRequests()
                .antMatchers("/users/**").hasAnyAuthority("ADMIN","USER")
                .antMatchers("/todos/todolist").hasAnyAuthority("ADMIN", "USER")
                .antMatchers("/todos/create").hasAuthority("ADMIN")
                .antMatchers("/**").permitAll()
                .and()
                .formLogin()
                .loginPage("/login") //Get this
                .loginProcessingUrl("/login") //Post metod som VI inte har skapat (Spring Security gissar jag?)
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/users/userlist")
                //.failureForwardUrl("/login?error")  //Se felhantering rad 20 i login-form.html
                .permitAll()
                .and()
                .logout()
                .invalidateHttpSession(true)
                //.deleteCookies("JSESSIONID")
                .logoutUrl("/logout") //Post metod som VI inte har definerat (Spring Security gissar jag?)
                .logoutSuccessUrl("/login?logout");

    }
}
