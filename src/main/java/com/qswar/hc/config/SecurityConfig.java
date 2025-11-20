package com.qswar.hc.config;

import com.qswar.hc.config.helper.AuthenticationFilter;
import com.qswar.hc.config.helper.AuthorizationFilter;
import com.qswar.hc.config.helper.UserDetailsServiceImpl;
import com.qswar.hc.constants.APIConstant;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import java.util.Arrays;


@EnableAutoConfiguration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private UserDetailsService userDetailsService;


    private static final String[] AUTH_WHITELIST = {
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/f4e/public/**"
    };

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }

    protected void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll()
                .antMatchers(APIConstant.PUBLIC+"/*").permitAll()
                .antMatchers(APIConstant.PRIVATE+"/*").authenticated()
                .and().addFilter(new AuthenticationFilter(authenticationManager()))
                .addFilter(new AuthorizationFilter(authenticationManager(), (UserDetailsServiceImpl)userDetailsService))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpSecurity.cors().disable();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000","localhost:3000","localhost",
                "http://www.fight4edu.com", "www.fight4edu.com","fight4edu.com",
                "http://www.fight4edu.in", "www.fight4edu.in","fight4edu.in",
                "http://www.fight4edu.org", "www.fight4edu.org","fight4edu.org",
                "http://www.fightforedu.com","www.fightforedu.com", "fightforedu.com",
                "http://www.fightforedu.in","www.fightforedu.in", "fightforedu.in",
                "http://www.fightforedu.org","www.fightforedu.org", "fightforedu.org"
                ));
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "DELETE", "OPTIONS"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}