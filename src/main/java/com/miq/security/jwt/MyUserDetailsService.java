/**
 * @author BomboRa
 */
package com.miq.security.jwt;

import com.miq.security.entity.User;
import com.miq.security.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepo userRepo;

    /**
     * Извлекаем пользователя из бд,
     *
     * @param email
     * @return {@link UserDetails} Возвращает объект сведений о пользователе
     * используя полученную информацию о пользователе
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userRes = userRepo.findByEmail(email);
        if (userRes.isEmpty())
            throw new UsernameNotFoundException("Could not findUser with email = " + email);
        User user = userRes.get();
        return new org.springframework.security.core.userdetails.User(
                email,
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("USER")));
    }
}

