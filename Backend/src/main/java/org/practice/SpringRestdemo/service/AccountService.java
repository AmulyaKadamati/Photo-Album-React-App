package org.practice.SpringRestdemo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.practice.SpringRestdemo.Models.Account;
import org.practice.SpringRestdemo.Repository.AccountRepository;
import org.practice.SpringRestdemo.util.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Account save(Account account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        if (account.getAuthorities() == null) {
            account.setAuthorities(Authority.USER.toString());
        }
        return accountRepository.save(account);
    }

    public Optional<Account> findByEmail(String email) {

        return accountRepository.findByEmail(email);
    }

    public Optional<Account> findById(Long id) {

        return accountRepository.findById(id);
    }

    public void deleteById(Long id) {

        accountRepository.deleteById(id);
    }

    public List<Account> findall() {

        return accountRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Account> optionalAccount = accountRepository.findByEmail(email);
        if (!optionalAccount.isPresent()) {
            throw new UsernameNotFoundException("Account not found!");
        }
        Account account = optionalAccount.get();
        List<GrantedAuthority> grantedAuthority = new ArrayList<>();
        grantedAuthority.add(new SimpleGrantedAuthority(account.getAuthorities()));

        return new User(account.getEmail(), account.getPassword(), grantedAuthority);

    }

}
