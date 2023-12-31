package com.fabianoaono.wishlist.controller;

import com.fabianoaono.wishlist.entity.WishlistItem;
import com.fabianoaono.wishlist.exception.GlobalExceptionHandler;
import com.fabianoaono.wishlist.exception.WishlistItemNotFoundException;
import com.fabianoaono.wishlist.service.WishlistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WishlistControllerTest {

    @Mock
    private WishlistService wishlistService;

    @InjectMocks
    private WishlistController wishlistController;

    @Test
    void getWishlistItems() {
        String clientId = "1";
        List<WishlistItem> mockWishlist = Arrays.asList(
                new WishlistItem("id1", "1", "1"),
                new WishlistItem("id2", "1", "2")
        );

        when(wishlistService.getWishlistItems(clientId)).thenReturn(mockWishlist);

        ResponseEntity<List<WishlistItem>> responseEntity = wishlistController.getWishlistItems(clientId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockWishlist, responseEntity.getBody());
    }

    @Test
    void checkProductInWishlist_ExistingProduct() {

        String clientId = "1";
        String existingProductId = "1";
        boolean wishlistContainsItem = true;

        when(wishlistService.hasWishlistItem(clientId, existingProductId)).thenReturn(wishlistContainsItem);

        ResponseEntity<Boolean> responseEntity = wishlistController.checkProductInWishlist(clientId, existingProductId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(wishlistContainsItem, responseEntity.getBody());

        verify(wishlistService).hasWishlistItem(clientId, existingProductId);

        verifyNoMoreInteractions(wishlistService);
    }

    @Test
    void checkProductInWishlist_NonExistingProduct() {

        String clientId = "1";
        String nonExistingProductId = "1";
        boolean wishlistDoesNotContainItem = false;

        when(wishlistService.hasWishlistItem(clientId, nonExistingProductId)).thenReturn(wishlistDoesNotContainItem);

        ResponseEntity<Boolean> nonExistingResponseEntity = wishlistController.checkProductInWishlist(clientId, nonExistingProductId);

        assertEquals(HttpStatus.OK, nonExistingResponseEntity.getStatusCode());
        assertEquals(wishlistDoesNotContainItem, nonExistingResponseEntity.getBody());

        verify(wishlistService).hasWishlistItem(clientId, nonExistingProductId);

        verifyNoMoreInteractions(wishlistService);
    }

    @Test
    void createWishlistItem() {

        String itemId = "1";
        String clientId = "1";
        String productId = "1";
        WishlistItem request = WishlistItem.builder()
                .clientId(clientId)
                .productId(productId)
                .build();
        WishlistItem createdWishlistItem = new WishlistItem(itemId, clientId, productId);

        when(wishlistService.createWishlistItem(request)).thenReturn(createdWishlistItem);

        ResponseEntity<Object> responseEntity = wishlistController.createWishlistItem(clientId, request);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(createdWishlistItem, responseEntity.getBody());

        verify(wishlistService).createWishlistItem(request);

        verifyNoMoreInteractions(wishlistService);
    }

    @Test
    void removeWishlistItemByProduct_ExistingProduct() {

        String clientId = "1";
        String productId = "1";

        ResponseEntity<Object> responseEntity = wishlistController.removeWishlistItemByProduct(clientId, productId);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());

        verify(wishlistService).removeWishlistItemByProduct(clientId, productId);

        verifyNoMoreInteractions(wishlistService);
    }

    @Test
    void removeWishlistItemByProduct_NonExistingProduct() {

        String clientId = "1";
        String nonExistingProductId = "1";

        doThrow(new WishlistItemNotFoundException("Wishlist item not found for clientId " + clientId + " and productId " + nonExistingProductId))
                .when(wishlistService)
                .removeWishlistItemByProduct(clientId, nonExistingProductId);

        WishlistItemNotFoundException exception = assertThrows(WishlistItemNotFoundException.class,
                () -> wishlistController.removeWishlistItemByProduct(clientId, nonExistingProductId));
    }
}