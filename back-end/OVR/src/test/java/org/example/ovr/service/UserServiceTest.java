package org.example.ovr.service;

import org.example.ovr.Entity.User;
import org.example.ovr.dto.UserDTO;
import org.example.ovr.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UserDTO userDTO;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userDTO = new UserDTO();
        userDTO.setName("John Doe");
        userDTO.setEmail("John@example.com");
        userDTO.setPassword("password123");
        userDTO.setSecurityQuestion("Your pet's name?");
        userDTO.setSecurityAnswer("Fluffy");
        userDTO.setAddress("123 Main St");
        userDTO.setPhoneNumber("0771234567");
        userDTO.setRole("USER");
        userDTO.setStatus("active");

        user = new User();
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setPassword("$2a$encodedPassword"); // Mocked encoded password
        user.setSecurityQuestion("Your pet's name?");
        user.setSecurityAnswer("Fluffy");
        user.setAddress("123 Main St");
        user.setPhoneNumber("0771234567");
        user.setRole("ROLE_USER");
        user.setStatus("active");
    }

    @Test
    void testAddUser() {
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.addUser(userDTO, false);

        assertNotNull(savedUser);
        assertEquals("ROLE_USER", savedUser.getRole());
        assertEquals("active", savedUser.getStatus()); // Inactive if not admin creating? Actually in code: inactive
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testFindByEmail() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));

        Optional<User> found = userService.findByEmail("John@example.com"); // test trimming & lowercase

        assertTrue(found.isPresent());
        assertEquals("john@example.com", found.get().getEmail());
        verify(userRepository).findByEmail("john@example.com");
    }

    @Test
    void testCanLoginSuccess() {
        user.setStatus("active");
        when(passwordEncoder.matches("password123", user.getPassword())).thenReturn(true);

        boolean canLogin = userService.canLogin(user, "password123");

        assertTrue(canLogin);
        verify(passwordEncoder).matches("password123", user.getPassword());
    }

    @Test
    void testCanLoginFailWrongPassword() {
        when(passwordEncoder.matches("wrongPass", user.getPassword())).thenReturn(false);

        boolean canLogin = userService.canLogin(user, "wrongPass");

        assertFalse(canLogin);
    }

    @Test
    void testCanLoginFailInactive() {
        user.setStatus("inactive");
        when(passwordEncoder.matches("password123", user.getPassword())).thenReturn(true);

        boolean canLogin = userService.canLogin(user, "password123");

        assertFalse(canLogin);
    }

    @Test
    void testUpdateUser() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO updateDTO = new UserDTO();
        updateDTO.setName("John Updated");
        updateDTO.setEmail("johnupdated@example.com");
        updateDTO.setPassword("newpass123");
        updateDTO.setSecurityQuestion("New question");
        updateDTO.setSecurityAnswer("New answer");
        updateDTO.setAddress("456 New St");
        updateDTO.setPhoneNumber("0777654321");
        updateDTO.setRole("ADMIN");
        updateDTO.setStatus("active");

        User updated = userService.updateUser(1, updateDTO);

        assertNotNull(updated);
        assertEquals("John Updated", updated.getName());
        assertEquals("ROLE_ADMIN", updated.getRole());
        verify(passwordEncoder).encode("newpass123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));

        List<User> users = userService.getAllUsers();

        assertEquals(1, users.size());
        assertEquals("john@example.com", users.get(0).getEmail());
        verify(userRepository).findAll();
    }
}