package com.fiap.cp5pt.dto.response;

public record AuthResponse(
        Long id,
        String nome,
        String email,
        String papel
) {}