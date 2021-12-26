package RU.MEPHI.ICIS.C17501.messenger.controller;

import RU.MEPHI.ICIS.C17501.messenger.responce.Response;
import RU.MEPHI.ICIS.C17501.messenger.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    UserService userService;

    @GetMapping
    public Response getAllUsers(@RequestHeader("requester_authorization_number") String requesterTelephoneNumber,
                                @RequestHeader("offsetPages") Integer offsetPages,
                                @RequestHeader("sizeOfPage") Integer sizeOfPage,
                                @RequestHeader("pass") String password) {
        return userService.getAllUsers(requesterTelephoneNumber, offsetPages, sizeOfPage, password);
    }

    @GetMapping("/{telephoneNumber}")
    public Response getUserByTelephoneNumber(@PathVariable String telephoneNumber,
                                             @RequestHeader("requester_authorization_number") String requesterTelephoneNumber,
                                             @RequestHeader("pass") String password) {
        return userService.getUserByTelephoneNumber(telephoneNumber, requesterTelephoneNumber, password);
    }

    @GetMapping("/byLogin")
    public Response getAllUsersByLogin(@RequestHeader("login") String login,
                                       @RequestHeader("requester_authorization_number") String requesterTelephoneNumber,
                                       @RequestHeader("pass") String password) {
        return userService.getAllUsersByLogin(requesterTelephoneNumber, login, password);
    }


    @PostMapping("/{telephoneNumber}/block")
    public Response blockUserByTelephoneNumber(@PathVariable String telephoneNumber,
                                               @RequestHeader("requester_authorization_number") String requesterTelephoneNumber,
                                               @RequestHeader("pass") String password) {
        return userService.blockUserByTelephoneNumber(telephoneNumber, requesterTelephoneNumber, password);
    }

    @PostMapping("/register")
    public Response registerUser(@RequestHeader("telephone_number") String telephoneNumber,
                                 @RequestHeader("login") String login,
                                 @RequestHeader("first_name") String firstName,
                                 @RequestHeader("second_name") String secondName,
                                 @RequestHeader("date_of_birth") Date dateOfBirth,
                                 @RequestHeader("gender") String gender,
                                 @RequestHeader("pass") String password) {
        return userService.createNewUser(telephoneNumber, login, firstName,
                secondName, dateOfBirth, gender, password);
    }

    @PostMapping("/authorize")
    public Response checkCredentials(@RequestHeader("telephone_number") String telephoneNumber,
                                     @RequestHeader("pass") String password) {
        return userService.checkCredentials(telephoneNumber, password);
    }

    @PostMapping(value = "/upload/avatar",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public Response uploadAvatar(@RequestParam(value = "file") MultipartFile avatarImageFile) {

        logger.info("Received request that contains file: " + avatarImageFile.getOriginalFilename());
        boolean isImageSuccessfullyUploaded = false;

        /*if (isImageSuccessfullyUploaded) {
            // TODO: тут интегрируемся с AmazonS3
        } else {
            return new Response("", errorMessage);
        }*/

        return new Response("", Response.successMessage);
    }

    /*@GetMapping
    public ResponseEntity<Resource> getAvatarImage() {
        return ResponseEntity.ok()
                .header()
    }*/
}

