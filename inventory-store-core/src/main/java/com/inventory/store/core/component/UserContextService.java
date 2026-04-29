package com.inventory.store.core.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Componente utilitario para obtener información del contexto del usuario actual.
 * Proporciona métodos para acceder al usuario autenticado desde cualquier parte de la aplicación.
 */
@Slf4j
@Component
public class UserContextService {

    /**
     * Obtiene el usuario actualmente autenticado.
     *
     * @return nombre del usuario o "system" si no hay autenticación
     */
    public String getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                return authentication.getName(); // Retorna el username del usuario autenticado
            }
        } catch (Exception e) {
            log.warn("No se pudo obtener el usuario autenticado", e);
        }
        return "system"; // Valor por defecto si no hay usuario autenticado
    }

    /**
     * Verifica si hay un usuario autenticado.
     *
     * @return true si hay un usuario autenticado, false en caso contrario
     */
    public boolean isUserAuthenticated() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            return authentication != null && authentication.isAuthenticated()
                    && !"anonymousUser".equals(authentication.getName());
        } catch (Exception e) {
            log.warn("Error al verificar autenticación del usuario", e);
            return false;
        }
    }

    /**
     * Obtiene el contexto de autenticación completo.
     *
     * @return Authentication object o null si no hay autenticación
     */
    public Authentication getAuthentication() {
        try {
            return SecurityContextHolder.getContext().getAuthentication();
        } catch (Exception e) {
            log.warn("No se pudo obtener el contexto de autenticación", e);
            return null;
        }
    }
}
