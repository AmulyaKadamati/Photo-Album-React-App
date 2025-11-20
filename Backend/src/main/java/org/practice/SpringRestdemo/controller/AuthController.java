package org.practice.SpringRestdemo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.practice.SpringRestdemo.Models.Account;
import org.practice.SpringRestdemo.payload.auth.AccountDTO;
import org.practice.SpringRestdemo.payload.auth.AccountViewDTO;
import org.practice.SpringRestdemo.payload.auth.AuthoritiesDTO;
import org.practice.SpringRestdemo.payload.auth.PasswordDTO;
import org.practice.SpringRestdemo.payload.auth.ProfileDTO;
import org.practice.SpringRestdemo.payload.auth.TokenDTO;
import org.practice.SpringRestdemo.payload.auth.UserLoginDTO;
import org.practice.SpringRestdemo.service.AccountService;
import org.practice.SpringRestdemo.service.TokenService;
import org.practice.SpringRestdemo.util.AccountError;
import org.practice.SpringRestdemo.util.AccountSuccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth Controller", description = "Controller for Account management") // for displaying about api
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @Autowired
    private AccountService accountService;

    public AuthController(TokenService tokenService, AuthenticationManager authenticationManager) {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }

    // This method is used to generate a token for the user
    // It requires the user's email and password to authenticate
    // If authentication is successful, it generates a token and returns it
    // If authentication fails, it returns a bad request response with an error
    // message
    @PostMapping("/token")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TokenDTO> token(@Valid @RequestBody UserLoginDTO userLogin) throws AuthenticationException {
        try {
            // Authenticate the user using the provided email and password
            Authentication authentication = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(userLogin.getEmail(), userLogin.getPassword()));
            return ResponseEntity.ok(new TokenDTO(tokenService.generateToken(authentication)));
        } catch (Exception e) {
            log.debug(AccountError.TOKEN_GENERATION_ERROR.toString() + " " + e.getMessage());
            return new ResponseEntity<>(new TokenDTO(null), HttpStatus.BAD_REQUEST);

        }
    }

    // This method is used to add a new user
    // It requires the user's email and password to create a new account
    // If the account is created successfully, it returns a success message
    // If there is an error, it returns a bad request response with an error message
    @PostMapping(value = "/users/add", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "400", description = "Please enter a valid email and Password length between 6 to 20 characters")
    @ApiResponse(responseCode = "200", description = "Account Added")
    @Operation(summary = "Add a new user")
    public ResponseEntity<String> addUser(@Valid @RequestBody AccountDTO accountDTO) {
        try {
            Account account = new Account();
            account.setEmail(accountDTO.getEmail());
            account.setPassword(accountDTO.getPassword());

            accountService.save(account);
            return ResponseEntity.ok(AccountSuccess.ACCOUNT_ADDED.toString());

        } catch (Exception e) {
            log.debug(AccountError.ADD_ACCOUNT_ERROR.toString() + " " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        }
    }

    // This method is used to list all users
    // It requires authentication to access this endpoint
    // It returns a list of users with their details (excluding passwords)
    // If the token is missing or invalid, it returns an unauthorized response
    // If the user does not have permission, it returns a forbidden response
    @GetMapping(value = "/users", produces = "application/json")
    @SecurityRequirement(name = "studyeasy-demo-api")
    @ApiResponse(responseCode = "200", description = "List of Users")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token Error")
    @Operation(summary = "List of users")
    public List<Account> users() {
        // we use AccountViewDTO so that pw will not be visible
        List<AccountViewDTO> accounts = new ArrayList<>();
        for (Account account : accountService.findall()) {
            accounts.add(new AccountViewDTO(account.getId(), account.getEmail(), account.getAuthorities()));
        }
        return accountService.findall();
    }

    // This method is used to update the authorities of a user
    // It requires authentication to access this endpoint
    // It takes the user ID and the new authorities as input
    @PutMapping(value = "/users/{user_id}/update-authorities", produces = "application/json", consumes = "application/json")
    @SecurityRequirement(name = "studyeasy-demo-api")
    @ApiResponse(responseCode = "200", description = "Update authorities")
    @ApiResponse(responseCode = "400", description = "Invalid User ")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token Error")
    @Operation(summary = "Update Authorities")
    public ResponseEntity<AccountViewDTO> update_authorities(@Valid @RequestBody AuthoritiesDTO authoritiesDTO,
            @PathVariable long user_id) {
        Optional<Account> optionalAccount = accountService.findById(user_id);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            account.setAuthorities(authoritiesDTO.getAuthorities());
            accountService.save(account);
            AccountViewDTO accountViewDTO = new AccountViewDTO(account.getId(), account.getEmail(),
                    account.getAuthorities());
            return ResponseEntity.ok(accountViewDTO);
        }
        return new ResponseEntity<AccountViewDTO>(new AccountViewDTO(), HttpStatus.BAD_REQUEST);
    }

    // This method is used to view the profile of the authenticated user
    // It requires authentication to access this endpoint
    // It returns the profile details of the user (ID, email, and authorities)
    // If the token is missing or invalid, it returns an unauthorized response
    // If the user does not have permission, it returns a forbidden response
    @GetMapping(value = "/profile", produces = "application/json")
    @SecurityRequirement(name = "studyeasy-demo-api")
    @ApiResponse(responseCode = "200", description = "Profile of User")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token Error")
    @Operation(summary = "View Profile")
    public ProfileDTO profile(Authentication authentication) {
        // when user genrates token using email, pw authentication will check token.
        String email = authentication.getName();
        Optional<Account> optionalAccount = accountService.findByEmail(email);
        Account account = optionalAccount.get();
        ProfileDTO profileDTO = new ProfileDTO(account.getId(), account.getEmail(), account.getAuthorities());
        return profileDTO;
    }

    // This method is used to update the password of the authenticated user
    // It requires authentication to access this endpoint
    // It takes the new password as input and updates the user's password
    // If the token is missing or invalid, it returns an unauthorized response
    // If the user does not have permission, it returns a forbidden response
    @PutMapping(value = "/profile/update-password", produces = "application/json", consumes = "application/json")
    @SecurityRequirement(name = "studyeasy-demo-api")
    @ApiResponse(responseCode = "200", description = "Update Password")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token Error")
    @Operation(summary = "Update Password")
    public AccountViewDTO update_password(@Valid @RequestBody PasswordDTO passwordDTO, Authentication authentication) {
        String email = authentication.getName();
        Optional<Account> optionalAccount = accountService.findByEmail(email);
        Account account = optionalAccount.get();
        account.setPassword(passwordDTO.getPassword());
        accountService.save(account);
        AccountViewDTO accountViewDTO = new AccountViewDTO(account.getId(), account.getEmail(),
                account.getAuthorities());
        return accountViewDTO;
    }

    // This method is used to delete the authenticated user's profile
    // It requires authentication to access this endpoint
    // If the user is found, it deletes the user and returns a success message
    @DeleteMapping(value = "/profile/delete")
    @SecurityRequirement(name = "studyeasy-demo-api")
    @ApiResponse(responseCode = "200", description = "Deleting User")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token Error")
    @Operation(summary = "Delete User Profile")
    public ResponseEntity<String> delete_profile(Authentication authentication) {
        String email = authentication.getName();
        Optional<Account> optionalAccount = accountService.findByEmail(email);
        if (optionalAccount.isPresent()) {

            accountService.deleteById(optionalAccount.get().getId());
            return ResponseEntity.ok("User deleted");

        }
        return new ResponseEntity<String>("Bad request", HttpStatus.BAD_REQUEST);
    }

}
