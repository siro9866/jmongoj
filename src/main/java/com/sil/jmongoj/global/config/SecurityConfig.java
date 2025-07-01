package com.sil.jmongoj.global.config;

import com.sil.jmongoj.domain.user.repository.UserRepository;
import com.sil.jmongoj.global.security.UserAuthenticationFailureHandler;
import com.sil.jmongoj.global.security.UserAuthenticationSuccessHandler;
import com.sil.jmongoj.global.util.UtilMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final UserRepository userRepository;
	private final UtilMessage utilMessage;

    public SecurityConfig(UserRepository userRepository, UtilMessage utilMessage) {
        this.userRepository = userRepository;
        this.utilMessage = utilMessage;
    }


    @Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	UserAuthenticationSuccessHandler getSuccessHandler() {
		return new UserAuthenticationSuccessHandler(userRepository, utilMessage);
	}

	@Bean
	UserAuthenticationFailureHandler getFailureHandler() {
		return new UserAuthenticationFailureHandler(utilMessage);
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf(Customizer.withDefaults()) // CSRF 기본 활성화
//				.csrf(AbstractHttpConfigurer::disable)  // AJAX 요청을 위해 CSRF 비활성화
				// JSP 내부 경로는 보호 대상에서 제외
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/WEB-INF/jsp/**").permitAll()
						.requestMatchers(
								"/",
								"/auth/login",
								"/auth/signup",
								"/webjars/**",
								"/css/**",
								"/js/**",
								"/images/**",
								"/favicon.ico",
								"/resources/**",
								"/static/**"
						).permitAll()
						.requestMatchers("/admin/**").hasRole("ADMIN")
						.requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
						.anyRequest().authenticated()
				);

		http
				.formLogin(form -> form
						.loginPage("/auth/login")         // 로그인 페이지 경로
						.loginProcessingUrl("/auth/login")// 로그인 form action 경로
						.usernameParameter("username")    // 로그인 폼의 아이디 필드명
						.passwordParameter("password")    // 로그인 폼의 비밀번호 필드명
						.defaultSuccessUrl("/")          // 로그인 성공 시 리다이렉트 경로
						.successHandler(getSuccessHandler())
						.failureHandler(getFailureHandler())
						.permitAll()
				);

		http
				.logout(logout -> logout
						.logoutUrl("/auth/logout")
						.logoutSuccessUrl("/auth/login?logout=true")
						.invalidateHttpSession(true)
						.deleteCookies("JSESSIONID")
						.permitAll()
				);

		http
				.sessionManagement(session -> session
						.maximumSessions(1)
						.maxSessionsPreventsLogin(false)  // true에서 false로 변경
						.expiredUrl("/auth/login?expired")     // 세션 만료시 이동할 URL
				);

		return http.build();
	}

	// Spring Security 6.x에서는 AuthenticationManager 별도 등록 필요
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}

