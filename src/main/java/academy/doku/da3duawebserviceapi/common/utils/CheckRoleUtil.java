package academy.doku.da3duawebserviceapi.common.utils;

import academy.doku.da3duawebserviceapi.common.exception.role.CustomerForbiddenException;
import academy.doku.da3duawebserviceapi.common.exception.role.MerchantOnlyException;
import academy.doku.da3duawebserviceapi.common.exception.role.SuperAdminOnlyException;
import academy.doku.da3duawebserviceapi.mekaniku.user.entity.UserRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.security.Principal;

public final class CheckRoleUtil {
    
    public static void checkCustomerForbiddenAccess(Authentication authentication) {
        Jwt token = (Jwt) authentication.getPrincipal();


        if (token.getClaim("role").equals(UserRole.CUSTOMER.name())) {
            throw new CustomerForbiddenException();
        }
    }


    public static void checkMerchantOnlyAccess(Authentication authentication) {
        Jwt token = (Jwt) authentication.getPrincipal();

        if (!token.getClaim("role").equals(UserRole.WORKSHOP.name())) {
            throw new MerchantOnlyException();
        }
    }

    public static void checkSuperAdminOnlyAccess(Authentication authentication) {
        Jwt token = (Jwt) authentication.getPrincipal();

        if (!token.getClaim("role").equals(UserRole.SUPERADMIN.name())) {
            throw new SuperAdminOnlyException();
        }
    }
}
