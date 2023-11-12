package com.fabianoaono.wishlist.repository;

import com.fabianoaono.wishlist.entity.WishlistItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface WishlistItemRepository extends MongoRepository<WishlistItem, String> {

    List<WishlistItem> findByClientId(String clientId);

    boolean existsByClientIdAndProductId(String clientId, String productId);

    void deleteByClientIdAndProductId(String clientId, String productId);
}
