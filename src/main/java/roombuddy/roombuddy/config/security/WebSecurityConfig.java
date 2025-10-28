package roombuddy.roombuddy.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import roombuddy.roombuddy.config.jwt.JpaTokenAuthenticationFilter;
import roombuddy.roombuddy.config.jwt.JpaTokenProvider;
import roombuddy.roombuddy.config.jwt.TokenAuthenticationFilter;
import roombuddy.roombuddy.config.jwt.TokenProvider;
import roombuddy.roombuddy.jpaservice.token.JpaTokenBlackListService;
import roombuddy.roombuddy.jpaservice.userdetail.JpaCustomUserDetailsService;
import roombuddy.roombuddy.service.token.TokenBlackListService;
import roombuddy.roombuddy.service.userdetail.CustomUserDetailsService;

/**
 * 스프링 시큐리티 설정 클래스
 */
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig {

//    private final TokenBlackListService tokenBlackListService;
//    private final CustomUserDetailsService customUserDetailsService;
//    private final TokenProvider tokenProvider;

    private final JpaTokenBlackListService tokenBlackListService;
    private final JpaCustomUserDetailsService customUserDetailsService;
    private final JpaTokenProvider tokenProvider;


    /**
     *  패스워드 인코더로 사용할 Bean 등록
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * 인증 매니저 빈 등록 ,  Spring Security에서 커스텀 인증을 처리
     */
    @Bean
    public AuthenticationManager authenticationManager(BCryptPasswordEncoder bCryptPasswordEncoder) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
        return new ProviderManager(daoAuthenticationProvider);
    }

    /**
     * 특정 HTTP 요쳥에 대한 웹 기반 보안 구성
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // TokenAuthenticationFilter 생성 및 Security 필터 체인에 추가

        //토큰 인증 필터
//        TokenAuthenticationFilter tokenAuthenticationFilter = new TokenAuthenticationFilter(tokenBlackListService,tokenProvider);
        JpaTokenAuthenticationFilter tokenAuthenticationFilter = new JpaTokenAuthenticationFilter(tokenBlackListService,tokenProvider);


        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // Stateless 모드
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/roombuddy/auth/signup/**","/api/roombuddy/auth/login",
                                "/static/***","/profile-images/**","/test.html","/api/auth/**",
                                "/api/roombuddy/rooms"
                        )
                        .permitAll()

                        .requestMatchers(HttpMethod.GET,
                                "/api/roombuddy/reservation/**",
                                "/api/roombuddy/blackout/rooms/{id}/today"
                                )
                        .permitAll()

                        .requestMatchers("/api/roombuddy/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);  // JWT 필터 추가


        return http.build();

    }
}