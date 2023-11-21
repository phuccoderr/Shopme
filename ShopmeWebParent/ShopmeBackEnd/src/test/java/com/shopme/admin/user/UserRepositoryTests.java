package com.shopme.admin.user;
import static org.assertj.core.api.Assertions.assertThat;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.config.Elements;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;

import java.util.HashSet;
import java.util.Set;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {

    @Autowired private UserRepository repo;

    @Autowired private TestEntityManager entityManager;

    @Test
    public void testCreateUser() {
        User user = new User();
        Role roleAdmin = entityManager.find(Role.class,1);
        user.setEmail("sale@gmail.com");
        user.setFirstName("Nguyen Hoang");
        user.setLastName("Phuc");
        user.setPassword("0123");
        user.setEnabled(true);
        user.addRoles(roleAdmin);

        User saved = repo.save(user);

        assertThat(saved.getId()).isGreaterThan(0);
    }

    @Test
    public void testGetUser() {
        User user = repo.findById(3).get();
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(new Role(2));
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String passEncode = passwordEncoder.encode("0123456789");
        user.setEnabled(true);
        user.setRoles(roleSet);
        user.setPassword(passEncode);
        User saved = repo.save(user);
        assertThat(saved).isNotNull();
    }


}
