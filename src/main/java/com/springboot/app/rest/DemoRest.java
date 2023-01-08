package com.springboot.app.rest;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.app.models.AuthenticationReq;
import com.springboot.app.models.TokenInfo;
import com.springboot.app.service.JwtUtilService;

@RestController
public class DemoRest {
	
	@Autowired
	JwtUtilService jwtUilService;
	
	@Autowired
	UserDetailsService usuarioDetailsService;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	  private static final Logger logger = LoggerFactory.getLogger(DemoRest.class);

	  @GetMapping("/mensaje")
	  public ResponseEntity<?> getMensaje() {
	    var auth =  SecurityContextHolder.getContext().getAuthentication();
	    logger.info("Datos del Usuario: {}", auth.getPrincipal());
	    logger.info("Datos de los Permisos {}", auth.getAuthorities());
	    logger.info("Esta autenticado {}", auth.isAuthenticated());

	    Map<String, String> mensaje = new HashMap<>();
	    mensaje.put("contenido", "Hola Erik");
	    return ResponseEntity.ok(mensaje);
	  }
	  
	  @PostMapping("/authenticate")
	  public ResponseEntity<TokenInfo> authenticate(@RequestBody AuthenticationReq authenticationReq) {
		  //Ir a BD y autenticar que el usuario y password son incorrectos
		  
		  logger.info("Usuario {}", authenticationReq.getUsuario());
		  logger.info("Password {}", authenticationReq.getClave());
		  
		  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationReq.getUsuario(), authenticationReq.getClave()));
		  final UserDetails userDetails = usuarioDetailsService.loadUserByUsername(authenticationReq.getUsuario());
		  final String jwt = jwtUilService.generateToken(userDetails);
		  TokenInfo tokenInfo = new TokenInfo(jwt);
		  return ResponseEntity.ok(tokenInfo);
	  }
}
