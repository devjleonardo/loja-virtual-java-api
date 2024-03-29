package com.joseleonardo.lojavirtual.config;

import javax.servlet.http.HttpSessionListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.joseleonardo.lojavirtual.security.JWTApiAutenticacaoFilter;
import com.joseleonardo.lojavirtual.security.JWTLoginFilter;
import com.joseleonardo.lojavirtual.service.ImplementacaoUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter implements HttpSessionListener {

	@Autowired
	private ImplementacaoUserDetailsService implementacaoUserDetailsService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		    .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
		    .disable()
		    .authorizeRequests().antMatchers("/").permitAll()
		    
		    .antMatchers(
		        "/index", "/pagamento/**", "/resources/**", "/static/**", "/templates/**",
		        "classpath:/resources/**", "classpath:/static/**", "classpath:/templates/**"
		     ).permitAll()
		    
		    .antMatchers(
			    HttpMethod.GET, "/pagamento/**", "/resources/**", "/static/**", "/templates/**",
			    "classpath:/resources/**", "classpath:/static/**", "classpath:/templates/**"
			).permitAll()
		    
		    .antMatchers(
		        HttpMethod.POST, "/pagamento/**", "/resources/**", "/static/**", "/templates/**",
		        "classpath:/resources/**", "classpath:/static/**", "classpath:/templates/**"
		     ).permitAll()
		    
		    .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
			.anyRequest().authenticated()

		    /* Redireciona para o index quando desloga */
		    .and()
		    .logout().logoutSuccessUrl("/index")

		    /* Mapeia o logout do sistema */
		    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))

		    /* Filtra as requisições para usarem a autenticação JWT */
		    .and()
		    .addFilterAfter(
		    		new JWTLoginFilter(
		    				"/login", authenticationManager()), 
		    		        UsernamePasswordAuthenticationFilter.class)
		    .addFilterBefore(
		    		new JWTApiAutenticacaoFilter(), UsernamePasswordAuthenticationFilter.class);
	}
	
	/* Irá consultar o usuário no banco atráves do login e a senha criptografada 
	 * utilizando o Spring Security */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(implementacaoUserDetailsService)
		        .passwordEncoder(new BCryptPasswordEncoder());
	}

	/**
	 * Configuração para permitir acesso não autenticado a recursos estáticos e
	 * operações específicas.
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
	    web.ignoring() 
    	    .antMatchers(
    	        HttpMethod.GET, "/buscarAcessoPorId/**", "/buscarAcessoPorNome/**", "/pagamento/**",
    	        "/resources/**", "/static/**", "/templates/**", "classpath:/resources/**",
    	        "classpath:/static/**", "classpath:/templates/**", "/webjars/**", 
    	        "/WEB-INF/classes/static/**"
    	    )
    	    
            .antMatchers(
                HttpMethod.POST, "/salvarAcesso", "/buscarAcessoPorId/**", "/buscarAcessoPorNome/**", "/pagamento/**",
    	        "/resources/**", "/static/**", "/templates/**", "classpath:/resources/**",
    	        "classpath:/static/**", "classpath:/templates/**", "/webjars/**", 
    	        "/WEB-INF/classes/static/**"
             )
            
		    .antMatchers(HttpMethod.DELETE, "/deletarAcessoPorId/**");
	    
	}

}
