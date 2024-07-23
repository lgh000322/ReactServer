package org.zerock.mallapi.service;

import org.zerock.mallapi.dto.CartItemDto;
import org.zerock.mallapi.dto.CartItemListDto;

import java.util.List;

public interface CartService {
    List<CartItemListDto> addOrModify(CartItemDto cartItemDto);

    List<CartItemListDto> getCartItems(String email);

    List<CartItemListDto> remove(Long cino);
}
