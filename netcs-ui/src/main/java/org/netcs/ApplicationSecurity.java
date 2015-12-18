package org.netcs;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Configure the security settings of the WWW app.
 *
 * @author ichatz@gmail.com
 */
@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(Ordered.LOWEST_PRECEDENCE - 8)
public class ApplicationSecurity
        extends WebSecurityConfigurerAdapter {
    /**
     * a log4j logger to print messages.
     */
    protected static final Logger LOGGER = Logger.getLogger(Application.class);
    @Autowired
    NETCSAuthenticationProvider netcsAuthenticationProvider;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(netcsAuthenticationProvider);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/css/**")
                .antMatchers("/fonts/**")
                .antMatchers("/js/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);

        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/register").permitAll()
                .antMatchers("/ws/**").permitAll()
                .antMatchers("/algorithm/**").hasRole("USER")
                .antMatchers("/algorithm2/**").hasRole("USER")
                .antMatchers("/transitions/**").hasRole("USER")
                .antMatchers("/generate/**").hasRole("USER")
                .antMatchers("/coef").hasRole("USER")
                .antMatchers("/experiment").hasRole("USER")
                .antMatchers("/api/**").hasRole("USER");
//                .and().httpBasic()


        //This will generate a login form if none is supplied.
        http.formLogin()
                .defaultSuccessUrl("/")
                .loginPage("/login");

        // Customize logout
        http.logout()
                .deleteCookies("remove")
                .invalidateHttpSession(true)
                .logoutSuccessUrl("/");
        http.csrf().disable();
    }


    @Override
    @Bean
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return super.userDetailsServiceBean();
    }
}
