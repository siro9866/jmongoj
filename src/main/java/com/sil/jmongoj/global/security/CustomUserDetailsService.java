package com.sil.jmongoj.global.security;

import com.sil.jmongoj.domain.user.entity.Users;
import com.sil.jmongoj.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Users user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

		List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole()));

		return new org.springframework.security.core.userdetails.User(
				user.getUsername(),
				user.getPassword(),
				authorities
		);
	}
}

