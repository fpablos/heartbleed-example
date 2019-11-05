package com.quemepongo.api;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.quemepongo.api.dto.AuthorizationResponse;
import com.quemepongo.domain.User;
import com.quemepongo.persistence.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
@RequestMapping("/session")
@JsonSerialize(using = LocalDateSerializer.class)
@Transactional
public class SessionController extends AbstractController{

    private static final Logger LOG = LoggerFactory.getLogger(SessionController.class);

    public SessionController(UserRepository userRepository) {
        super(userRepository);
    }

    @PostMapping("login")
    public ResponseEntity logUserIn(@RequestBody User login){//TODO make this more secure
            User user = getUser(login.getEmail());
            return user.hasPassword(login.getPasswordHash()) ?
                    ResponseEntity.ok().body(new AuthorizationResponse(generateTokenFor(user))) : new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("register")
    public ResponseEntity createUser(@RequestBody User user){
        if(!user.isValid()) return ResponseEntity.badRequest().build();
        userRepository.save(user);
        return ResponseEntity.ok().body(new AuthorizationResponse(generateTokenFor(user)));
    }

    private User getUser(String email) {
        List<User> users = userRepository.findByEmail(email);
        if(users.size() < 1){
            throw new NotFoundException();
        }
        return users.get(0);
    }
}
