package org.practice.SpringRestdemo.util;

public enum Authority {
    READ,
    WRITE,
    UPDATE,
    USER, // USER can update & delete self object also read anything
    ADMIN // ADMIN can read, delete & update anything

}
