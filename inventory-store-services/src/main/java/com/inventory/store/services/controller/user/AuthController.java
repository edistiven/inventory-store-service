package com.inventory.store.services.controller.user;

import com.inventory.store.core.service.AuthService;
import com.inventory.store.response.ApiResponse;
import com.inventory.store.response.AuthResponse;
import com.inventory.store.vo.UserVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"})
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Intento de login para usuario: {}", loginRequest.getUsername());

        try {
            AuthResponse authResponse = authService.authenticate(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
            );

            if (authResponse.getToken() != null && !authResponse.getToken().isEmpty()) {
                return ResponseEntity.ok(ApiResponse.success(authResponse, "Login exitoso"));
            } else {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Credenciales inválidas"));
            }
        } catch (Exception e) {
            log.error("Error durante el login", e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Error en el servidor"));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserVO>> getCurrentUser(@RequestHeader("Authorization") String authorization) {
        try {
            String token = extractTokenFromHeader(authorization);

            if (token == null || token.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Token no proporcionado"));
            }

            if (!authService.validateToken(token)) {
                return ResponseEntity.status(401)
                        .body(ApiResponse.error("Token inválido o expirado"));
            }

            UserVO currentUser = authService.getCurrentUser(token)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            return ResponseEntity.ok(ApiResponse.success(currentUser));
        } catch (Exception e) {
            log.error("Error al obtener usuario actual", e);
            return ResponseEntity.status(401)
                    .body(ApiResponse.error("Token inválido"));
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<ApiResponse<Boolean>> validateToken(@RequestHeader("Authorization") String authorization) {
        try {
            String token = extractTokenFromHeader(authorization);

            if (token == null || token.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.success(false, "Token no proporcionado"));
            }

            boolean isValid = authService.validateToken(token);

            return ResponseEntity.ok(ApiResponse.success(isValid,
                    isValid ? "Token válido" : "Token inválido"));
        } catch (Exception e) {
            log.error("Error al validar token", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.success(false, "Error al validar token"));
        }
    }

    @PostMapping("/create-test-user")
    public ResponseEntity<ApiResponse<String>> createTestUser() {
        try {
            authService.createUsuario("admin", "password01.", "admin", "Administrador"); // crear usuario administrador
            authService.createUsuario("user", "user123", "usuario", "usuario"); // crear usuario normal
            return ResponseEntity.ok(ApiResponse.success("Usuario de prueba creado: admin/secret"));
        } catch (Exception e) {
            log.error("Error al crear usuario de prueba", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error al crear usuario: " + e.getMessage()));
        }
    }

    private String extractTokenFromHeader(String authorization) {
        if (authorization != null && authorization.startsWith("Bearer ")) {
            return authorization.substring(7);
        }
        return null;
    }

    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class LoginRequest {
        private String username;
        private String password;
    }
}
