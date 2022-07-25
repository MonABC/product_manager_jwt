package com.example.product_manager.configuration.security;

import com.example.product_manager.configuration.CusTomAccessDeniedHandler;
import com.example.product_manager.configuration.JwtAuthFilter;
import com.example.product_manager.configuration.JwtProvider;
import com.example.product_manager.configuration.RestAuthEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private RestAuthEntryPoint restAuthEntryPoint;
    @Autowired
    private JwtProvider jwtProvider;

    @Bean
    public PasswordEncoder passwordEncoder() { //bean mã hóa pass người dùng
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CusTomAccessDeniedHandler cusTomAccessDeniedHandler() { //cấu hình lại lỗi không có quyền truy cập
        return new CusTomAccessDeniedHandler();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().ignoringAntMatchers("/**");// vô hiệu hóa csrf cho một số đường dẫn nhất định
        http.httpBasic().authenticationEntryPoint(restAuthEntryPoint);//tùy chỉnh lại thông báo 401 thông qua class restEntryPoint
        http.authorizeRequests()
                .antMatchers("/login",
                        "/register", "/**").permitAll() // tất cả truy cập được
                .anyRequest().authenticated()  // các request còn lại cần xác thực
                .and().csrf().disable(); // vô hiệu hóa của csrf (kiểm soát quyền truy cập)
        http.addFilterBefore(new JwtAuthFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class) //lớp finter kiểm tra chuỗi jwt
                .exceptionHandling().accessDeniedHandler(cusTomAccessDeniedHandler()); //xử lí lí ngoại lệ khi không có quyền truy cập
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//http.cors(); // ngăn chặn truy cập từ miền khác
    }

}
