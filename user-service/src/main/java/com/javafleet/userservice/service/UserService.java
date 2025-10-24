package com.javafleet.userservice.service;
import com.javafleet.userservice.model.User;
import net.datafaker.Faker;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
@Service
public class UserService {
    private final Faker faker = new Faker(Locale.GERMAN);
    private final List<User> users;
    public UserService() {
        this.users = generateUsers(20);
    }
    public List<User> getAllUsers() {
        return users;
    }
    public User getUserById(Long id) {
        return users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    private List<User> generateUsers(int count) {
        List<User> list = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            User user = new User();
            user.setId((long) i);
            user.setUsername(faker.internet().username());
            user.setEmail(faker.internet().emailAddress());
            user.setFirstName(faker.name().firstName());
            user.setLastName(faker.name().lastName());
            user.setCity(faker.address().city());
            list.add(user);
        }
        return list;
    }
}
