package RU.MEPHI.ICIS.C17501.messenger.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class MainController {
    @GetMapping
    public ResponseEntity<TestControllerEntity> main() {
        return new ResponseEntity<>(new TestControllerEntity("it is the Id", "It is the name"), HttpStatus.OK);
    }

}
