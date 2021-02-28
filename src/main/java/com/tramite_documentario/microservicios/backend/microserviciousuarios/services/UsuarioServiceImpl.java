package com.tramite_documentario.microservicios.backend.microserviciousuarios.services;

import com.tramite_documentario.microservicio.backend.commonpersonas.models.entity.Persona;
import com.tramite_documentario.microservicios.backend.commonusuarios.models.entity.Usuario;
import com.tramite_documentario.microservicios.backend.microserviciousuarios.clients.PersonaFeignClient;
import com.tramite_documentario.microservicios.backend.microserviciousuarios.models.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;


@Service
public class UsuarioServiceImpl implements UsuarioService {

    private Logger logger = LoggerFactory.getLogger(UsuarioServiceImpl.class);


    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PersonaFeignClient personaFeignClient;

    @Override
    public Usuario findByUsername(String username) {
        return repository.findByUsername(username);
    }



    public Usuario findByEmail(String correo) {
        Persona persona = personaFeignClient.findPersonaByCorreo(correo);
        if (persona == null) {
            return null;
        }
        return repository.findByDniRuc(persona.getDniRuc());
    }


    public Usuario updateResetPassword(String correo, String token) {
        Usuario usuario = findByEmail(correo);
        if (usuario != null) {
            usuario.setResetPasswordToken(token);
            return repository.save(usuario);
        } else {
            return null;
        }
    }

    public Usuario findByResetPassword(String token){
        return repository.findByResetPasswordToken(token);
    }

    public Usuario updatePassword(Usuario usuario, String newPassword){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String passwordEncoded = passwordEncoder.encode(newPassword);

        usuario.setPassword(passwordEncoded);
        usuario.setResetPasswordToken(null);

        return repository.save(usuario);
    }

    public void sendMail(String correo, String resetPasswordLink) throws UnsupportedEncodingException, MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("ginoag7@gmail.com", "Mikasa Support");
        helper.setTo(correo);

        String mensaje = "Recuperar contraseña del sistema";

        String contenido = "<h1>Hola Usuario,</h1> " +
                "<p>Le alcanzo link para restablecer tu contraseña</p>" +
                "<p><strong>Click en el link para restablecer contraseña</strong></p>" +
                "<a href=\"" + resetPasswordLink + "\" >Cambiar contraseña</a>" +
                "<strong>Por favor no responder a este mensaje</strong>";

        helper.setSubject(mensaje);
        helper.setText(contenido, true);

        mailSender.send(message);
    }


}
