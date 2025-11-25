package com.example.buchladen.Configuration;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.rmi.ServerException;
import java.util.Collection;

@Component
public class AfterLoginForAdminAndUser implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication auth) throws IOException, ServerException {

        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();

        for(GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();
             if (role.equals("ROLE_ADMIN")) {
                 response.sendRedirect("/admin/dashboard");
                 return;
             } else if (role.equals("ROLE_USER")) {
                 response.sendRedirect("/home");
                 return;
             }
        }

    }

}
