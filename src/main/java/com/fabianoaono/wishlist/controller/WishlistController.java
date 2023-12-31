package com.fabianoaono.wishlist.controller;

import com.fabianoaono.wishlist.entity.WishlistItem;
import com.fabianoaono.wishlist.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    @Autowired
    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @GetMapping("/items/{clientId}")
    public ResponseEntity<List<WishlistItem>> getWishlistItems(@PathVariable String clientId) {

        return ResponseEntity.ok(wishlistService.getWishlistItems(clientId));
    }

    @GetMapping("/items/{clientId}/contains/{productId}")
    public ResponseEntity<Boolean> checkProductInWishlist(
            @PathVariable String clientId,
            @PathVariable String productId
    ) {

        return ResponseEntity.ok(wishlistService.hasWishlistItem(clientId, productId));
    }

    @PostMapping("/items/{clientId}")
    public ResponseEntity<Object> createWishlistItem(
            @PathVariable String clientId,
            @RequestBody WishlistItem wishlistItem
    ) {

        wishlistItem.setClientId(clientId);
        return new ResponseEntity(wishlistService.createWishlistItem(wishlistItem), HttpStatus.CREATED);
    }

    @DeleteMapping("/items/{clientId}/product/{productId}")
    public ResponseEntity<Object> removeWishlistItemByProduct(
            @PathVariable String clientId,
            @PathVariable String productId
    ) {
        wishlistService.removeWishlistItemByProduct(clientId, productId);
        return ResponseEntity.noContent().build();
    }
}
