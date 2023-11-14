package com.fabianoaono.wishlist.service;

import com.fabianoaono.wishlist.entity.WishlistItem;
import com.fabianoaono.wishlist.exception.MaxWishlistItemsExceededException;
import com.fabianoaono.wishlist.exception.WishlistItemAlreadyExistsException;
import com.fabianoaono.wishlist.exception.WishlistItemNotFoundException;
import com.fabianoaono.wishlist.repository.WishlistItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistService {

    private static final int MAX_WISHLIST_ITEMS = 20;

    private WishlistItemRepository wishlistItemRepository;

    @Autowired
    public WishlistService(WishlistItemRepository wishlistItemRepository) {
        this.wishlistItemRepository = wishlistItemRepository;
    }

    public WishlistItem createWishlistItem(WishlistItem wishlistItem) {

        List<WishlistItem> wishlistItems = wishlistItemRepository.findByClientId(wishlistItem.getClientId());
        if (wishlistItems.size() >= MAX_WISHLIST_ITEMS) {
            throw new MaxWishlistItemsExceededException("Maximum wishlist items limit exceeded.");
        }

        if (wishlistItemRepository.existsByClientIdAndProductId(wishlistItem.getClientId(), wishlistItem.getProductId())) {
            throw new WishlistItemAlreadyExistsException("Wishlist item already exists for the given client and product.");
        }

        return wishlistItemRepository.save(wishlistItem);
    }

    public List<WishlistItem> getWishlistItems(String clientId) {
        return wishlistItemRepository.findByClientId(clientId);
    }

    public boolean hasWishlistItem(String clientId, String productId) {
        return wishlistItemRepository.existsByClientIdAndProductId(clientId, productId);
    }

    public void removeWishlistItemByProduct(String clientId, String productId) {

        if (wishlistItemRepository.existsByClientIdAndProductId(clientId, productId)) {
            throw new WishlistItemNotFoundException("Wishlist item not found for clientId " + clientId + " and productId " + productId);
        }

        wishlistItemRepository.deleteByClientIdAndProductId(clientId, productId);
    }
}
