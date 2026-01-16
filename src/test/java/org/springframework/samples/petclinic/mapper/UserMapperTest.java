package org.springframework.samples.petclinic.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.rest.dto.UserDto;

@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void shouldMapUserToDtoAndBack() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("pass");
        user.setEnabled(true);

        UserDto dto = userMapper.toUserDto(user);
        assertNotNull(dto);
        assertEquals("admin", dto.getUsername());

        User back = userMapper.toUser(dto);
        assertNotNull(back);
        assertEquals("admin", back.getUsername());
    }
}
