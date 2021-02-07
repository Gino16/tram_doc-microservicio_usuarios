package com.tramite_documentario.microservicios.backend.microserviciousuarios.services;

import com.tramite_documentario.microservicios.backend.commonusuarios.models.entity.Usuario;
import org.springframework.scheduling.annotation.Async;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface UsuarioService {
    public Usuario findByUsername(String username);

    public Usuario findByEmail(String correo);

    public Usuario findByResetPassword(String token);

    public Usuario updateResetPassword(String correo, String token);

    public Usuario updatePassword(Usuario usuario, String newPassword);

    @Async
    public void sendMail(String correo, String resetPasswordLink) throws UnsupportedEncodingException, MessagingException;
}
