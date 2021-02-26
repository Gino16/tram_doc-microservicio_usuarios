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
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@RestController
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;


    @PostMapping("/forgot_password")
    public ResponseEntity<?> procesarForgotPasswordForm(HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
        String correo = request.getParameter("correo");
        String token = RandomString.make(32);
        Usuario user = usuarioService.updateResetPassword(correo, token);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        //String resetPasswordLink = "/reset_password?token=" + token;
        String resetPasswordLink = "http://localhost:3000/cambiar.contrasenia?token=" + token;

        usuarioService.sendMail(correo, resetPasswordLink);

        return ResponseEntity.ok("Se reestablecio la contraseña correctamente");
    }

    @PostMapping("/cambiar_contrasenia")
    public ResponseEntity<?> showResetPasswordForm(HttpServletRequest request) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");
        String repeatPassword = request.getParameter("repeat-password");

        Usuario usuario = usuarioService.findByResetPassword(token);
        List<String> errorList = new ArrayList<>();
        if (isValid(password, repeatPassword, errorList)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorList);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.updatePassword(usuario, password));

    }

    private  boolean isValid(String password, String repeatPassword, List<String> errorList) {

        Pattern specailCharPatten = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Pattern UpperCasePatten = Pattern.compile("[A-Z ]");
        Pattern lowerCasePatten = Pattern.compile("[a-z ]");
        Pattern digitCasePatten = Pattern.compile("[0-9 ]");
        errorList.clear();

        boolean flag = true;

        if (!password.equals(repeatPassword)) {
            errorList.add("password and confirm password does not match");
            flag = false;
        }
        if (password.length() < 8) {
            errorList.add("Password lenght must have alleast 8 character !!");
            flag = false;
        }
        if (!specailCharPatten.matcher(password).find()) {
            errorList.add("Password must have atleast one specail character !!");
            flag = false;
        }
        if (!UpperCasePatten.matcher(password).find()) {
            errorList.add("Password must have atleast one uppercase character !!");
            flag = false;
        }
        if (!lowerCasePatten.matcher(password).find()) {
            errorList.add("Password must have atleast one lowercase character !!");
            flag = false;
        }
        if (!digitCasePatten.matcher(password).find()) {
            errorList.add("Password must have atleast one digit character !!");
            flag = false;
        }

        return flag;

    }
}
