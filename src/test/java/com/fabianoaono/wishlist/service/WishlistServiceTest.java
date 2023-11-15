package com.fabianoaono.wishlist.service;

import com.fabianoaono.wishlist.entity.WishlistItem;
import com.fabianoaono.wishlist.exception.MaxWishlistItemsExceededException;
import com.fabianoaono.wishlist.exception.WishlistItemAlreadyExistsException;
import com.fabianoaono.wishlist.exception.WishlistItemNotFoundException;
import com.fabianoaono.wishlist.repository.WishlistItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WishlistServiceTest {

    @Mock
    private WishlistItemRepository wishlistItemRepository;

    @InjectMocks
    private WishlistService wishlistService;

    @Test
    void createWishlistItem_Successful() {

        String itemIdCreated = "1";
        String clientId = "1";
        String productId = "1";
        WishlistItem wishlistItem = WishlistItem.builder()
                .clientId(clientId)
                .productId(productId)
                .build();

        when(wishlistItemRepository.findByClientId(clientId)).thenReturn(Collections.emptyList());
        when(wishlistItemRepository.save(wishlistItem)).thenReturn(new WishlistItem(itemIdCreated, clientId, productId));

        WishlistItem createdItem = wishlistService.createWishlistItem(wishlistItem);

        assertNotNull(createdItem);
        assertEquals(wishlistItem.getClientId(), createdItem.getClientId());
        assertEquals(wishlistItem.getProductId(), createdItem.getProductId());

        verifyNoMoreInteractions(wishlistItemRepository);
    }

    @Test
    void createWishlistItem_MaxWishlistItemsExceededException() {
        String clientId = "1";
        String productId = "1";
        WishlistItem wishlistItem = WishlistItem.builder()
                .clientId(clientId)
                .productId(productId)
                .build();
        
        List<WishlistItem> existingWishlistItems = new ArrayList<>();
        
        for (int i = 1; i <= WishlistService.MAX_WISHLIST_ITEMS; i++) {
            existingWishlistItems.add(new WishlistItem("id" + i, clientId, "productId" + i));
        }

        when(wishlistItemRepository.findByClientId(clientId)).thenReturn(existingWishlistItems);

        MaxWishlistItemsExceededException exception = assertThrows(MaxWishlistItemsExceededException.class,
                () -> wishlistService.createWishlistItem(wishlistItem));

        verifyNoMoreInteractions(wishlistItemRepository);
    }

    @Test
    void createWishlistItem_ItemAlreadyExists() {

        String existingItemId = "1";
        String clientId = "1";
        String productId = "1";
        WishlistItem wishlistItem = WishlistItem.builder()
                .clientId(clientId)
                .productId(productId)
                .build();

        when(wishlistItemRepository.findByClientId(clientId)).thenReturn(Arrays.asList(
                new WishlistItem(existingItemId, clientId, productId)
        ));

        WishlistItemAlreadyExistsException exception = assertThrows(WishlistItemAlreadyExistsException.class,
                () -> wishlistService.createWishlistItem(wishlistItem));

        verifyNoMoreInteractions(wishlistItemRepository);
    }

    @Test
    void getWishlistItems_EmptyWishlist() {

        String clientId = "1";

        when(wishlistItemRepository.findByClientId(clientId)).thenReturn(Collections.emptyList());

        List<WishlistItem> wishlistItems = wishlistService.getWishlistItems(clientId);

        assertTrue(wishlistItems.isEmpty());

        verifyNoMoreInteractions(wishlistItemRepository);
    }

    @Test
    void getWishlistItems_NonEmptyWishlist() {

        String clientId = "1";

        List<WishlistItem> mockWishlist = Arrays.asList(
                new WishlistItem("id1", clientId, "product1"),
                new WishlistItem("id2", clientId, "product2")
        );

        when(wishlistItemRepository.findByClientId(clientId)).thenReturn(mockWishlist);

        List<WishlistItem> wishlistItems = wishlistService.getWishlistItems(clientId);

        assertFalse(wishlistItems.isEmpty());
        assertEquals(mockWishlist.size(), wishlistItems.size());
        assertTrue(wishlistItems.containsAll(mockWishlist));

        verifyNoMoreInteractions(wishlistItemRepository);
    }

    @Test
    void hasWishlistItem_ExistingProduct() {

        String clientId = "1";
        String existingProductId = "1";

        when(wishlistItemRepository.existsByClientIdAndProductId(clientId, existingProductId)).thenReturn(true);

        assertTrue(wishlistService.hasWishlistItem(clientId, existingProductId));

        verifyNoMoreInteractions(wishlistItemRepository);
    }

    @Test
    void hasWishlistItem_NonExistingProduct() {

        String clientId = "1";
        String nonExistingProductId = "1";

        when(wishlistItemRepository.existsByClientIdAndProductId(clientId, nonExistingProductId)).thenReturn(false);

        assertFalse(wishlistService.hasWishlistItem(clientId, nonExistingProductId));

        verifyNoMoreInteractions(wishlistItemRepository);
    }

    @Test
    void removeWishlistItemByProduct_ExistingProduct() {
        String clientId = "1";
        String existingProductId = "1";

        when(wishlistItemRepository.existsByClientIdAndProductId(clientId, existingProductId)).thenReturn(true);

        wishlistService.removeWishlistItemByProduct(clientId, existingProductId);

        verify(wishlistItemRepository, times(1)).deleteByClientIdAndProductId(clientId, existingProductId);

        verifyNoMoreInteractions(wishlistItemRepository);
    }

    @Test
    void removeWishlistItemByProduct_NonExistingProduct() {
        String clientId = "1";
        String nonExistingProductId = "1";

        when(wishlistItemRepository.existsByClientIdAndProductId(clientId, nonExistingProductId)).thenReturn(false);

        WishlistItemNotFoundException exception = assertThrows(WishlistItemNotFoundException.class,
                () -> wishlistService.removeWishlistItemByProduct(clientId, nonExistingProductId));

        verifyNoMoreInteractions(wishlistItemRepository);
    }
}