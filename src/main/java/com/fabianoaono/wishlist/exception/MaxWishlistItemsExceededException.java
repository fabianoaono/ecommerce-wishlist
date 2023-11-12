package com.fabianoaono.wishlist.exception;

public class MaxWishlistItemsExceededException extends RuntimeException {
    public MaxWishlistItemsExceededException(String s) {
        super(s);
    }
}
