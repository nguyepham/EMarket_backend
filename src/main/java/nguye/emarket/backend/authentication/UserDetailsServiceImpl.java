package nguye.emarket.backend.authentication;

import nguye.emarket.backend.entity.PasswordEntity;
import nguye.emarket.backend.entity.ProfileEntity;
import nguye.emarket.backend.entity.UserEntity;
import nguye.emarket.backend.exception.BadCredentialsException;
import nguye.emarket.backend.repository.PasswordRepository;
import nguye.emarket.backend.repository.ProfileRepository;
import nguye.emarket.backend.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordRepository passwordRepository;
    private final ProfileRepository profileRepository;

    public UserDetailsServiceImpl(PasswordRepository passwordRepository, UserRepository userRepository, ProfileRepository profileRepository) {
        this.passwordRepository = passwordRepository;
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws BadCredentialsException {

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(BadCredentialsException::new);

        ProfileEntity profile = profileRepository.findById(user.getId())
                .orElseThrow(BadCredentialsException::new);

        PasswordEntity password = passwordRepository.findById(user.getId())
                .orElseThrow(BadCredentialsException::new);

        return new SecurityUser(user.getId(), user.getUsername(), password.getText(), profile.getAuthority());
    }
}
