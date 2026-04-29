package com.inventory.store.core.service;

import com.inventory.store.client.entity.UserEntity;
import com.inventory.store.client.repository.IUserRepository;
import com.inventory.store.response.AuthResponse;
import com.inventory.store.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthService {

    private final IUserRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse authenticate(String username, String password) {
        log.info("Intentando autenticar usuario: {}", username);

        Optional<UserEntity> usuarioOpt = usuarioRepository.findByUsernameAndActivo(username, true);

        if (usuarioOpt.isEmpty()) {
            log.warn("Usuario no encontrado o inactivo: {}", username);
            return AuthResponse.builder()
                    .token("")
                    .user(null)
                    .message("Credenciales inválidas")
                    .build();
        }

        UserEntity usuario = usuarioOpt.get();

        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            log.warn("Contraseña incorrecta para usuario: {}", username);
            return AuthResponse.builder()
                    .token("")
                    .user(null)
                    .message("Credenciales inválidas")
                    .build();
        }

        String token = jwtService.generateToken(usuario);
        UserVO userVO = convertToDTO(usuario);

        log.info("Usuario autenticado exitosamente: {}", username);

        return AuthResponse.builder()
                .token(token)
                .user(userVO)
                .message("Autenticación exitosa")
                .build();
    }

    public Optional<UserVO> getCurrentUser(String token) {
        try {
            String username = jwtService.extractUsername(token);
            log.info("Obteniendo información del usuario actual: {}", username);

            return usuarioRepository.findByUsernameAndActivo(username, true)
                    .map(this::convertToDTO);
        } catch (Exception e) {
            log.error("Error al obtener usuario actual del token", e);
            return Optional.empty();
        }
    }

    public void createUsuario(String username, String password, String role, String nombre) {
        log.info("Creando nuevo usuario: {}", username);

        if (usuarioRepository.existsByUsername(username)) {
            throw new RuntimeException("El nombre de usuario ya existe");
        }

        UserEntity usuario = UserEntity.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .rol(role)
                .userCompleteName("PRUEBA")
                .status('1')
                .registerUserDate("user01")
                .registerDate(new Date())
                .build();

        usuarioRepository.save(usuario);
    }

    public boolean validateToken(String token) {
        try {
            String username = jwtService.extractUsername(token);
            Optional<UserEntity> usuario = usuarioRepository.findByUsernameAndActivo(username, true);

            if (usuario.isEmpty()) {
                return false;
            }

            return jwtService.isTokenValid(token, usuario.get());
        } catch (Exception e) {
            log.error("Error al validar token", e);
            return false;
        }
    }

    private UserVO convertToDTO(UserEntity usuario) {
        return UserVO.builder()
                .id(usuario.getUuid())
                .username(usuario.getUsername())
                .role(usuario.getRol())
                .nombre(usuario.getUsername())
                .build();
    }
}
