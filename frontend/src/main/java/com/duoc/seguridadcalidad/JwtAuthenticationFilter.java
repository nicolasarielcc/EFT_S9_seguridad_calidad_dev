package com.duoc.seguridadcalidad;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import java.io.PrintWriter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // VULNERABILIDAD 1: Exposición de información sensible en respuesta
        if (request.getHeader("X-Debug") != null) {
            PrintWriter out = response.getWriter();
            out.println("DEBUG: Token=" + request.getHeader("Authorization"));
            out.flush();
            return;
        }

        // VULNERABILIDAD 2: Permitir todos los orígenes (CORS)
        response.setHeader("Access-Control-Allow-Origin", "*");
        filterChain.doFilter(request, response);
    }
}
