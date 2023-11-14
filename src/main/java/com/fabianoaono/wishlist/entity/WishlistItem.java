package com.fabianoaono.wishlist.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Document(collection = "wishlistItems")
public class WishlistItem {

    @Id
    private String id;

    private String clientId;

    private String productId;

}
