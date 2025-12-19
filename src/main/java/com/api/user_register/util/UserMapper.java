package com.api.user_register.util;

import com.api.user_register.dto.UserRequestDTO;
import com.api.user_register.dto.UserResponseDTO;
import com.api.user_register.model.Phone;
import com.api.user_register.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    // Hacemos el cambio de DTO entrada a entidad (Base de Datos) asi no metemos mas codigo spaghetti dentro del Service
    // De DTO (Entrada) a Entidad (Base de Datos)
    public User toEntity(UserRequestDTO dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());

        // Mapeamos los teléfonos manualmente
        if (dto.getPhones() != null) {
            List<Phone> phoneEntities = dto.getPhones().stream().map(p -> {
                Phone phone = new Phone();
                phone.setNumber(p.getNumber());
                phone.setCityCode(p.getCitycode());
                phone.setCountryCode(p.getContrycode());
                phone.setUser(user); // Vinculamos bidireccionalmente
                return phone;
            }).collect(Collectors.toList());
            user.setPhones(phoneEntities);
        }

        return user;
    }

    // Hacemos el cambio de Entidad (Base de Datos) a DTO de salida
    public UserResponseDTO toResponse(User user) {
        UserResponseDTO response = new UserResponseDTO();
        response.setId(user.getId());
        response.setCreated(user.getCreated());
        response.setModified(user.getUpdated());
        response.setLastLogin(user.getLastLogin());
        response.setToken(user.getToken());
        response.setIs_active(user.is_active());
        return response;
    }
}
