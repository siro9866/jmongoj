package com.sil.jmongoj.global.component;

import com.sil.jmongoj.global.security.CustomUserDetails;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 등록/수정자정보 자동자정
 */
@Component
public class AuditAwareImpl implements AuditorAware<String> {
	@NotNull
	@Override
	public Optional<String> getCurrentAuditor() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (null == authentication || !authentication.isAuthenticated()) {
			return Optional.empty();
		}
		if ("anonymousUser".equalsIgnoreCase(authentication.getName())) {
			return Optional.empty();
		}
		return Optional.of(authentication.getName());
	}

}