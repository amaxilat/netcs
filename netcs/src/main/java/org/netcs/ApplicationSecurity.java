//package org.netcs;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
//import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
//
///**
// * Configure the security settings of the WWW app.
// *
// * @author ichatz@gmail.com
// */
//@EnableWebSecurity
//@Configuration
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//@Order(Ordered.LOWEST_PRECEDENCE - 8)
//public class ApplicationSecurity
//        extends WebSecurityConfigurerAdapter {
//
//
//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("user").password("password").roles("USER");
//    }
//
//    @Bean
//    AuthenticationEntryPoint authenticationEntryPoint() {
//        BasicAuthenticationEntryPoint point = new BasicAuthenticationEntryPoint();
//        point.setRealmName("netcs");
//        return point;
//    }
//
//    @Bean
//    BasicAuthenticationFilter basicAuthenticationFilter() throws Exception {
//        return new BasicAuthenticationFilter(authenticationManager(), authenticationEntryPoint());
//    }
//
//
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring()
//                .antMatchers("/css/**")
//                .antMatchers("/img/**")
//                .antMatchers("/fonts/**")
//                .antMatchers("/js/**");
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers("/").permitAll();
//
//        http.authorizeRequests()
//                .antMatchers("/admin/**").hasRole("ADMIN");
//
//        //This will generate a login form if none is supplied.
//        http.formLogin()
//                .defaultSuccessUrl("/")
//                .loginPage("/login");
//
//        // Customize logout
//        http.logout()
//                .deleteCookies("remove")
//                .invalidateHttpSession(false)
//                .logoutSuccessUrl("/");
//
//        http.csrf().disable();
//    }
//
//}
