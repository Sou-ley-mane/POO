package com.gie.fleet.tenant;

import com.gie.fleet.entity.enums.Role;

/**
 * Contexte courant de la requête (thread-local) : gieId et rôle de l'utilisateur authentifié.
 * Rempli par JwtAuthenticationFilter, nettoyé en fin de requête. Sert de source de vérité
 * pour le filtrage systématique par gieId dans les services.
 */
public final class TenantContext {

    private static final ThreadLocal<Long> CURRENT_GIE_ID = new ThreadLocal<>();
    private static final ThreadLocal<Role> CURRENT_ROLE = new ThreadLocal<>();
    private static final ThreadLocal<Long> CURRENT_USER_ID = new ThreadLocal<>();

    private TenantContext() {
    }

    public static void set(Long userId, Long gieId, Role role) {
        CURRENT_USER_ID.set(userId);
        CURRENT_GIE_ID.set(gieId);
        CURRENT_ROLE.set(role);
    }

    /** Nullable : le SUPER_ADMIN n'appartient à aucun GIE et n'est donc pas filtré. */
    public static Long getGieId() {
        return CURRENT_GIE_ID.get();
    }

    public static Role getRole() {
        return CURRENT_ROLE.get();
    }

    public static Long getUserId() {
        return CURRENT_USER_ID.get();
    }

    public static boolean isSuperAdmin() {
        return CURRENT_ROLE.get() == Role.SUPER_ADMIN;
    }

    public static void clear() {
        CURRENT_GIE_ID.remove();
        CURRENT_ROLE.remove();
        CURRENT_USER_ID.remove();
    }
}
