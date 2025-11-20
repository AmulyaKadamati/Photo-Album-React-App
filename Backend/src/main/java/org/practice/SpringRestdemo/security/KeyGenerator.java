package org.practice.SpringRestdemo.security;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

import org.springframework.stereotype.Component;

@Component
public class KeyGenerator {

    static KeyPair generateRsaKeyPair() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();

        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return keyPair;
    }

}
