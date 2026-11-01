package com.example.ecomerce.service;

import com.example.ecomerce.model.User;
import com.example.ecomerce.model.VerificationToken;
import com.example.ecomerce.repository.TokenRepository;
import com.example.ecomerce.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

@Service
public class AuthService {

    private static final Duration SESSION_LENGTH = Duration.ofDays(7);
    private static final String BAD_CREDENTIALS = "Incorrect email or password.";

    private final UserRepository users;
    private final TokenRepository tokens;
    private final PasswordEncoder encoder;
    private final JwtEncoder jwtEncoder;
    private final MailService mail;

    public AuthService(UserRepository users, TokenRepository tokens, PasswordEncoder encoder,
                       JwtEncoder jwtEncoder, MailService mail) {
        this.users = users;
        this.tokens = tokens;
        this.encoder = encoder;
        this.jwtEncoder = jwtEncoder;
        this.mail = mail;
    }

    @Transactional
    public User register(String name, String lastname, String email, String password) {
        String normalized = email.trim().toLowerCase(Locale.ROOT);
        if (users.findFirstByEmailIgnoreCase(normalized).isPresent()) {
            throw new ApiException(HttpStatus.CONFLICT, "An account with this email already exists.");
        }
        User user = new User();
        user.setName(name.trim());
        user.setLastname(lastname.trim());
        user.setEmail(normalized);
        user.setPassword(encoder.encode(password));
        user.setAktivan(!mail.isEnabled());
        users.save(user);
        if (mail.isEnabled()) {
            issueVerification(user);
        }
        return user;
    }

    /**
     * noRollbackFor keeps the token renewed when an unverified user is refused, so the
     * fresh link emailed to them actually works.
     */
    @Transactional(noRollbackFor = ApiException.class)
    public User login(String email, String password) {
        User user = users.findFirstByEmailIgnoreCase(email.trim())
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, BAD_CREDENTIALS));
        if (!passwordMatches(user, password)) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, BAD_CREDENTIALS);
        }
        if (mail.isEnabled() && !Boolean.TRUE.equals(user.getAktivan())) {
            issueVerification(user);
            throw new ApiException(HttpStatus.FORBIDDEN,
                    "Please confirm your email first. We just sent you a new link.");
        }
        return user;
    }

    @Transactional
    public void confirm(String token) {
        VerificationToken stored = tokens.findByToken(token)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "This confirmation link isn't valid."));
        if (stored.isExpired()) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "This confirmation link has expired. Sign in to get a new one.");
        }
        stored.getUser().setAktivan(true);
    }

    public User get(Long id) {
        return users.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Please sign in again."));
    }

    public String createSessionToken(User user) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(String.valueOf(user.getId()))
                .issuedAt(now)
                .expiresAt(now.plus(SESSION_LENGTH))
                .build();
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

    private boolean passwordMatches(User user, String password) {
        String stored = user.getPassword();
        if (stored == null) {
            return false;
        }
        if (stored.startsWith("$2")) {
            return encoder.matches(password, stored);
        }
        // Accounts from before passwords were hashed: check once, then store a hash.
        if (stored.equals(password)) {
            user.setPassword(encoder.encode(password));
            return true;
        }
        return false;
    }

    private void issueVerification(User user) {
        String value = UUID.randomUUID().toString();
        VerificationToken token = tokens.findByUser(user).orElse(null);
        if (token == null) {
            tokens.save(new VerificationToken(value, user));
        } else {
            token.renew(value);
        }
        mail.sendVerification(user.getEmail(), user.getName(), value);
    }
}
