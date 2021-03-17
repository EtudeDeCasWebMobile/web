package com.amboucheba.etudeDeCasWeb.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import com.amboucheba.etudeDeCasWeb.Services.AuthService;


@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthService authService;

    @Autowired
    JwtRequestFilter jwtRequestFilter;

    @Override
    @Autowired 
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //auth.userDetailsService(authService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable(); //we don't make use of cookies
        http.headers().frameOptions().disable();
//        http.authorizeRequests()
//            .antMatchers(HttpMethod.POST,"/authenticate").permitAll()
//            .antMatchers(HttpMethod.PUT, "/*").permitAll()
//            .antMatchers(HttpMethod.POST, "/custom").permitAll()
//            .antMatchers("/h2-console/**").permitAll()
//            .anyRequest().authenticated();
//        http.headers().frameOptions().disable();
//        http.exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
//
//        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
//        http.addFilterBefore( jwtRequestFilter , UsernamePasswordAuthenticationFilter.class);
    }
    
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("*");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/",
                "/documentation",
                "/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui/*",
                "/webjars/**");
        web.ignoring().mvcMatchers(HttpMethod.OPTIONS, "/**");
        web.ignoring().mvcMatchers("/swagger-ui.html/**", "/configuration/**", "/swagger-resources/**", "/v2/api-docs","/webjars/**");
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
