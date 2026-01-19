package org.springframework.samples.petclinic.mapper;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.samples.petclinic.model.Role;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.rest.dto.RoleDto;
import org.springframework.samples.petclinic.rest.dto.UserDto;

class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    void shouldMapUserToUserDtoAndBack() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("pass123");
        user.setEnabled(true);

        UserDto dto = userMapper.toUserDto(user);

        assertNotNull(dto);
        assertEquals("admin", dto.getUsername());
        assertEquals("pass123", dto.getPassword());
        assertTrue(dto.getEnabled());

        User back = userMapper.toUser(dto);

        assertNotNull(back);
        assertEquals("admin", back.getUsername());
        assertEquals("pass123", back.getPassword());

        // ✅ correct getter in this project
        assertTrue(back.getEnabled());
    }

    @Test
    void shouldMapRoleDtoToRole_ignoreIdAndUser() {
        RoleDto dto = new RoleDto();
        dto.setName("ROLE_ADMIN");

        Role role = userMapper.toRole(dto);

        assertNotNull(role);
        assertNull(role.getId());
        assertNull(role.getUser());
        assertEquals("ROLE_ADMIN", role.getName());
    }

    @Test
    void shouldMapRoleToRoleDto() {
        Role role = new Role();
        role.setName("ROLE_OWNER_ADMIN");

        RoleDto dto = userMapper.toRoleDto(role);

        assertNotNull(dto);
        assertEquals("ROLE_OWNER_ADMIN", dto.getName());
    }

    @Test
    void shouldMapRoleCollectionToRoleDtoCollection() {
        Role r1 = new Role();
        r1.setName("ROLE_A");

        Role r2 = new Role();
        r2.setName("ROLE_B");

        Collection<RoleDto> result = userMapper.toRoleDtos(List.of(r1, r2));

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void shouldMapRoleDtoCollectionToRoleCollection() {
        RoleDto d1 = new RoleDto();
        d1.setName("ROLE_X");

        RoleDto d2 = new RoleDto();
        d2.setName("ROLE_Y");

        Collection<Role> roles = userMapper.toRoles(List.of(d1, d2));

        assertNotNull(roles);
        assertEquals(2, roles.size());
    }
}
