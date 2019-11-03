package com.quemepongo.api;

import com.quemepongo.domain.User;
import com.quemepongo.persistence.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Base64;
import java.util.Optional;

import static org.springframework.util.Base64Utils.decode;
import static org.springframework.util.Base64Utils.encode;

public class AbstractController {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractController.class);

    protected UserRepository userRepository;

    protected class NotFoundException extends RuntimeException { }

    protected class UnauthorizedException extends RuntimeException { }

    @Autowired
    public AbstractController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    protected User getUserFromToken(String token){ //TODO Use a real token encode-decode strategy and interceptors
        LOG.info("Authorizing user with token: {}", token);
        Optional<User> potentialUser;
        try {
             potentialUser = userRepository.findById(decodeToken(token));
             return potentialUser.orElseThrow(UnauthorizedException::new);
        } catch (Exception e){
            throw new UnauthorizedException();
        }
    }

    protected String generateTokenFor(User user){
        return encodeToken(user.getId());
    }

    private Long decodeToken(String token){
        byte[] decodedBytes = decode(decode(decode(token.getBytes())));
        return Long.valueOf(new String(decodedBytes));
    }

    private String encodeToken(Long id){
        return new String(encode(encode(encode(id.toString().getBytes()))));
    }

    @ExceptionHandler({ DataIntegrityViolationException.class })
    public ResponseEntity handleException(DataIntegrityViolationException e) {
        LOG.error("Constraint violated",e);
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler({ NotFoundException.class })
    public ResponseEntity handleException(NotFoundException e) {
        LOG.error("Entity instance not found",e);
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler({ UnauthorizedException.class })
    public ResponseEntity handleException(UnauthorizedException e) {
        LOG.error("Unauthorized request",e);
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }

}
