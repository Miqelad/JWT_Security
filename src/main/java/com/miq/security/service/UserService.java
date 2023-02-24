/**
 * @author BomboRa
 */
package com.miq.security.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miq.security.dto.LoginCred;
import com.miq.security.dto.user.UserDetailsInfo;
import com.miq.security.dto.user.UserDetailsInfoHeader;
import com.miq.security.dto.user.UserDetailsInfoPayload;
import com.miq.security.entity.User;
import com.miq.security.jwt.JWTService;
import com.miq.security.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;
    private final JWTService jwtService;
    private final AuthenticationManager authManager;
    private final PasswordEncoder passwordEncoder;

    /**
     * Определение функции для регистрации пользователя
     * Кодирование пароля с использованием Bcrypt
     * Установка закодированного пароля
     * Сохранение сущности пользователя в базе данных
     * Генерация JWT
     *
     * @param user {@link User}
     * @return Ответ, какой JWT создан
     */
    public String register(User user) {
        String encodedPass = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPass);
        user = userRepo.save(user);
        String token = jwtService.generateToken(user.getEmail());
        return token;
    }

    /**
     * Вход пользователя
     * 1. Создание токена аутентификации,
     * который будет содержать учетные данные для аутентификации
     * Этот токен используется в качестве
     * входных данных для процесса аутентификации
     * 2. Проверка подлинности учетных данных для входа
     * 3. Если проверка пройдена, это означает, что аутентификация прошла успешно
     * Сгенерируйте JWT
     *
     * @param loginCred {@link LoginCred}
     * @return {@link String} предоставление JWT токена,
     * ранее созданного при регистрации
     */
    public UserDetailsInfo login(LoginCred loginCred) {
        try {
            UsernamePasswordAuthenticationToken authInputToken =
                    new UsernamePasswordAuthenticationToken(loginCred.getEmail(), loginCred.getPassword());
            authManager.authenticate(authInputToken);
            String token = jwtService.generateToken(loginCred.getEmail());

            String[] chunks = token.split("\\.");
            Base64.Decoder decoder = Base64.getUrlDecoder();
            String header = new String(decoder.decode(chunks[0]));
            String payload = new String(decoder.decode(chunks[1]));

            return new UserDetailsInfo(
                    new ObjectMapper().readValue(header, UserDetailsInfoHeader.class),
                    new ObjectMapper().readValue(payload, UserDetailsInfoPayload.class),
                    token);
        } catch (AuthenticationException authExc) {
            throw new RuntimeException("Invalid Login Credentials");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Определение функции для обработки маршрута GET для извлечения
     * пользовательской информации аутентифицированного пользователя
     * Дальше идет извлечение электронной почты из контекста безопасности
     *
     * @return {@link User} данные о пользователе
     */
    public User getUserInfo() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Not found user"));
    }
}
