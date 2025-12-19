package com.api.user_register.service;

import com.api.user_register.dto.UserRequestDTO;
import com.api.user_register.dto.UserResponseDTO;
import com.api.user_register.model.User;
import com.api.user_register.repository.UserRepository;
import com.api.user_register.util.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper; // Mockeamos también el mapper

    @InjectMocks
    private UserService userService;

    private UserRequestDTO requestDTO;
    private User user;
    private UserResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        // Datos de prueba (Entrada)
        requestDTO = new UserRequestDTO();
        requestDTO.setName("Juan Test");
        requestDTO.setEmail("juan@test.org");
        requestDTO.setPassword("Hunter2");

        // Datos de prueba (Intermedio - Entidad)
        user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("juan@test.org");
        user.setPassword("Hunter2");
        user.setToken("token-dummy");

        // Datos de prueba (Salida)
        responseDTO = new UserResponseDTO();
        responseDTO.setId(user.getId());
        responseDTO.setToken("token-dummy");

        // Inyectamos la regex manualmente porque no cargamos el application.properties en test unitario ya que es una mala practica
        ReflectionTestUtils.setField(userService, "passwordRegex", "^(?=.*[A-Z])(?=.*[0-9]).{7,}$");
    }

    @Test
    void registerUser_Success() {
        // Simulamos el comportamiento de las dependencias
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty()); // Correo no existe
        when(userMapper.toEntity(any(UserRequestDTO.class))).thenReturn(user); // Mapper funciona
        when(userRepository.save(any(User.class))).thenReturn(user); // Guardado OK
        when(userMapper.toResponse(any(User.class))).thenReturn(responseDTO); // Mapper vuelta funciona

        // Ejecutamos el metodo registerUser
        UserResponseDTO result = userService.registerUser(requestDTO);

        // Verificacion de los resultados
        assertNotNull(result);
        assertNotNull(result.getToken());
        assertEquals(responseDTO.getId(), result.getId());

        // Verificamos que se llamo a guardar 1 vez
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registerUser_ExistingEmail() {
        // El repositorio dice que el email ya existe
        when(userRepository.findByEmail(requestDTO.getEmail())).thenReturn(Optional.of(user));

        // Esperamos que levante la excepcion
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.registerUser(requestDTO);
        });

        assertEquals("El correo ya registrado", exception.getMessage());

        // Aseguramos que nunca se intento guardar
        verify(userRepository, never()).save(any());
    }
}
