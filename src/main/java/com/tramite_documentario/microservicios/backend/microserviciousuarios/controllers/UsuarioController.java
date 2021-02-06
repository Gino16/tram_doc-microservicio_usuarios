package com.tramite_documentario.microservicios.backend.microserviciousuarios.controllers;

import com.tramite_documentario.microservicios.backend.commonusuarios.models.entity.Usuario;
import com.tramite_documentario.microservicios.backend.microserviciousuarios.services.UsuarioService;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@RestController
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JavaMailSender mailSender;

    @PostMapping("/forgot_password")
    public ResponseEntity<?> procesarForgotPasswordForm(HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
        String correo = request.getParameter("correo");
        String token = RandomString.make(32);
        usuarioService.updateResetPassword(correo, token);

        //String resetPasswordLink = "/reset_password?token=" + token;
        String resetPasswordLink = "http://localhost:3000/cambiar.contrasenia?token=" + token;

        sendMail(correo, resetPasswordLink);

        return ResponseEntity.ok(resetPasswordLink);
    }

    @PostMapping("/cambiar_contrasenia")
    public ResponseEntity<?> showResetPasswordForm(HttpServletRequest request) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");
        String repeatPassword = request.getParameter("repeat-password");

        Usuario usuario = usuarioService.findByResetPassword(token);

        if (usuario == null || !password.equals(repeatPassword)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.updatePassword(usuario, password));

    }

    private void sendMail(String correo, String resetPasswordLink) throws UnsupportedEncodingException, MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("ginoag7@gmail.com", "Mikasa Support");
        helper.setTo(correo);

        String mensaje = "Aca esta el link olvidadizo, restablece tu contra please";

        String contenido = "<p>Hola Usuario,</p> " +
                "<p>Te paso link para restablecer tu contra, <strong>no te olvides pa la proxima.</strong></p>" +
                "<p>Click en el link para restablecer contraseña</p>" +
                "<a href=\"" + resetPasswordLink + "\" >Cambiar contraseña</a>";

        helper.setSubject(mensaje);
        helper.setText(contenido, true);

        mailSender.send(message);
    }


}
