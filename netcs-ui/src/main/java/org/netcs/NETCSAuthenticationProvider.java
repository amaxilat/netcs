package org.netcs;


import org.apache.log4j.Logger;
import org.netcs.model.sql.User;
import org.netcs.model.sql.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashSet;

/**
 * The authentication provider looks up the user data from the neo4j repository.
 */
@Component
public class NETCSAuthenticationProvider
        implements AuthenticationProvider {

    private static final Logger LOGGER = Logger.getLogger(NETCSAuthenticationProvider.class);


    protected final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    protected UserRepository userRepository;

    public NETCSAuthenticationProvider() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Performs authentication with the same contract as {@link org.springframework.security.authentication.AuthenticationManager#authenticate(Authentication)}.
     *
     * @param authentication the authentication request object.
     * @return a fully authenticated object including credentials. May return <code>null</code> if the
     * <code>AuthenticationProvider</code> is unable to support authentication of the passed <code>Authentication</code>
     * object. In such a case, the next <code>AuthenticationProvider</code> that supports the presented
     * <code>Authentication</code> class will be tried.
     * @throws AuthenticationException if authentication fails.
     */
    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {

        final UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;
        final String username = String.valueOf(auth.getPrincipal());
        final String password = String.valueOf(auth.getCredentials());
        final User user;

        //1. Check if we are connected to Neo4J
        if (userRepository == null) {
            LOGGER.error("Couldn't connect to userRepository. Aborting.");
            throw new BadCredentialsException("Internal server error. Unable to connect to the database.");
        }

        //2. Check if the username and password are both filled in
        checkUsernameAndPassword(username, password);

        user = userRepository.findByEmail(username);

        //3. Try and get the User from the UserRepository and check if such a User exists
        checkUserExists(user, username);

        //4. Check if the passwords match.
        checkPassword(password, user.getPasswordHash(), user.getId());

        //7. Return an authenticated token, containing user data and authorities
        HashSet authorities = new HashSet();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new UsernamePasswordAuthenticationToken(user, null, authorities);
    }

    private void checkUsernameAndPassword(final String username, final String password) throws BadCredentialsException {
        if (username.length() == 0 && password.length() == 0) {
            LOGGER.warn("Empty username and password received. Aborting.");
            throw new BadCredentialsException("Please fill in all the fields. Both username and password are required.");

        } else if (username.length() == 0) {
            LOGGER.warn("Empty username received. Aborting.");
            throw new BadCredentialsException("Please fill in all the fields. Username is required.");

        } else if (password.length() == 0) {
            LOGGER.warn("Empty password received. Aborting.");
            throw new BadCredentialsException("Please fill in all the fields. Password is required.");
        }

    }

    private void checkUserExists(final User user, final String username) throws BadCredentialsException {
        if (user == null) {
            LOGGER.warn(String.format("User %s doesn't exist.", username));
            throw new BadCredentialsException("Either username or password is incorrect. Please try again.");
        }

    }


    private void checkPassword(final String password, final String password1, final Long userId) throws BadCredentialsException {
        if (!passwordEncoder.matches(password, password1)) {
            throw new BadCredentialsException("incorrect username or password");
        } else {
        }
    }


    /**
     * Returns <code>true</code> if this <Code>AuthenticationProvider</code> supports the indicated
     * <Code>Authentication</code> object. <p> Returning <code>true</code> does not guarantee an
     * <code>AuthenticationProvider</code> will be able to authenticate the presented instance of the
     * <code>Authentication</code> class. It simply indicates it can support closer evaluation of it. An
     * <code>AuthenticationProvider</code> can still return <code>null</code> from the {@link
     * #authenticate(Authentication)} method to indicate another <code>AuthenticationProvider</code> should be tried.
     * </p> <p>Selection of an <code>AuthenticationProvider</code> capable of performing authentication is conducted at
     * runtime the <code>ProviderManager</code>.</p>
     *
     * @return <code>true</code> if the implementation can more closely evaluate the <code>Authentication</code> class
     * presented
     */
    @Override
    public boolean supports(final Class<?> authentication) {
        return true;
    }

}
