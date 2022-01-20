package pl.patrykzajac.digitalcolliers.utils;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import pl.patrykzajac.digitalcolliers.configuration.ApplicationProperties;

import java.util.Collections;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final ApplicationProperties applicationProperties;

    @Override
    public Authentication authenticate(Authentication auth)
            throws AuthenticationException {
        String username = auth.getName();
        String password = auth.getCredentials()
                .toString();

        if (applicationProperties.getSecurity().containsKey(username) && applicationProperties.getSecurity().get(username).equals(password)) {
            return new UsernamePasswordAuthenticationToken
                    (username, password, Collections.emptyList());
        } else {
            throw new
                    BadCredentialsException("External system authentication failed");
        }
    }

    @Override
    public boolean supports(Class<?> auth) {
        return auth.equals(UsernamePasswordAuthenticationToken.class);
    }
}
