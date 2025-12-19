package com.api.user_register.service;

import java.util.UUID;
import java.util.regex.Pattern;

import com.api.user_register.dto.UserRequestDTO;
import com.api.user_register.dto.UserResponseDTO;
import com.api.user_register.util.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.api.user_register.model.User;
import com.api.user_register.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Value("${user.password.regex}")
    private String passwordRegex;

    private static final String EMAIL_REGEX = "^[\\w\\-\\.]+@([\\w\\-]+\\.)+[\\w\\-]{2,4}$";

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserResponseDTO registerUser(UserRequestDTO request) {
        // Validamos el email
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("El correo ya registrado");
        }

        if (!Pattern.matches(EMAIL_REGEX, request.getEmail())) {
            throw new RuntimeException("El formato del correo es inválido");
        }

        if (!Pattern.matches(passwordRegex, request.getPassword())) {
            throw new RuntimeException("La contraseña no cumple con los requisitos de seguridad");
        }

        // Convertimos el DTO a Entidad
        User user = userMapper.toEntity(request);

        // Generar el UUII
        user.setToken(UUID.randomUUID().toString());
        // Aquí podrías agregar la encriptación de clave si decides implementarla

        // Guardamos el dato
        User userSaved = userRepository.save(user);

        // Convertimos a DTO la respuesta y retornamos los datos
        return userMapper.toResponse(userSaved);
    }

}
