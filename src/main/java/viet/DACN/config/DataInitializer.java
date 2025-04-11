package viet.DACN.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import viet.DACN.model.Role;
import viet.DACN.model.User;
import viet.DACN.repo.RoleRepository;
import viet.DACN.repo.UserRepository;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        if(userRepository.count() == 0 && roleRepository.count() == 0) {
            Role userRole = new Role();
            userRole.setName("ROLE_USER");
            roleRepository.save(userRole);

            Role adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            roleRepository.save(adminRole);

            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);
            roles.add(userRole);

            User user = User.builder()
                .first_name("viet")
                .last_name("nguyen")
                .username("viet1234")
                .password(passwordEncoder.encode("1234"))
                .roles(roles)
                .build();

            userRepository.save(user);
        }
    }
}