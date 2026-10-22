package com.example.studentmanagement.config;

import com.example.studentmanagement.repository.TaiKhoanRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService(TaiKhoanRepository taiKhoanRepository) {
        return tenDangNhap -> taiKhoanRepository.findByTenDangNhap(tenDangNhap)
                .map(taiKhoan -> User.withUsername(taiKhoan.getTenDangNhap())
                        .password(taiKhoan.getMatKhau())
                        .roles(taiKhoan.getVaiTro())
                        .disabled(!taiKhoan.isKichHoat())
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản"));
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider(
            UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http,
            DaoAuthenticationProvider authenticationProvider) throws Exception {
        http.authenticationProvider(authenticationProvider)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/dang-nhap", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/quan-tri/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .formLogin(form -> form.loginPage("/dang-nhap")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/dang-nhap?loi")
                        .permitAll())
                .logout(logout -> logout.logoutSuccessUrl("/dang-nhap?da-dang-xuat"));
        return http.build();
    }
}
