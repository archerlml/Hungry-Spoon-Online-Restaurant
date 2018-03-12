package edu.sjsu.cmpe275.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import edu.sjsu.cmpe275.service.CustomAuthenticationSuccessHandler;
import edu.sjsu.cmpe275.service.CustomUserDetailsService;

//@Configuration
//public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
//
//    @Override
//    protected void configure(HttpSecurity httpSecurity) throws Exception {
//    	httpSecurity.authorizeRequests().antMatchers("/").permitAll().and()
//        .authorizeRequests().antMatchers("/console/**").permitAll();
//
//		httpSecurity.csrf().disable();
//		httpSecurity.headers().frameOptions().disable();
//		httpSecurity.headers().cacheControl().disable();
//    }
//
//}
@Configuration
@EnableWebMvcSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Override  
    protected void configure(AuthenticationManagerBuilder auth)  
            throws Exception {  
        auth.userDetailsService(userDetailsService());  
    }  
    
    @Override
    public void configure(WebSecurity webSecurity) throws Exception
    {
        webSecurity.ignoring().antMatchers("/js/**","/css/**","/fonts/**","/images/**");
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/", "/index", "/register","/confirm").permitAll()
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/login")
                .successHandler(new CustomAuthenticationSuccessHandler())
                .failureUrl("/login?error")
                .permitAll()
                .and()
            .logout()
                .permitAll();
    }
}