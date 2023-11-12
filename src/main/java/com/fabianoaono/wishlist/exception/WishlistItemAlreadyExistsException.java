package com.fabianoaono.wishlist.exception;

public class WishlistItemAlreadyExistsException extends RuntimeException {
    public WishlistItemAlreadyExistsException(String s) {
        super(s);
    }
}
