package com.fabianoaono.wishlist.controller;

import com.fabianoaono.wishlist.entity.WishlistItem;
import com.fabianoaono.wishlist.exception.MaxWishlistItemsExceededException;
import com.fabianoaono.wishlist.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @GetMapping("/items/{clientId}")
    public ResponseEntity<List<WishlistItem>> getWishlistItems(@PathVariable String clientId) {

        return ResponseEntity.ok(wishlistService.getWishlistItems(clientId));
    }

    @GetMapping("/items/{clientId}/product/{productId}")
    public ResponseEntity<Boolean> getWishlistItemByProduct(
            @PathVariable String clientId,
            @PathVariable String productId
    ) {

        return ResponseEntity.ok(wishlistService.hasWishlistItem(clientId, productId));
    }

    @PostMapping("/items/{clientId}")
    public ResponseEntity<Object> createWishlistItem(@PathVariable String clientId, @RequestBody WishlistItem wishlistItem) {

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
