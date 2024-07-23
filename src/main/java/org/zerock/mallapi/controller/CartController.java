package org.zerock.mallapi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.zerock.mallapi.dto.CartItemDto;
import org.zerock.mallapi.dto.CartItemListDto;
import org.zerock.mallapi.service.CartService;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;


    @PreAuthorize("#cartItemDto.email == authentication.name")
    @PostMapping("/change")
    public List<CartItemListDto> changeCart(@RequestBody CartItemDto cartItemDto) {
        log.info(cartItemDto.toString());

        if (cartItemDto.getQty() <= 0) {
            return cartService.remove(cartItemDto.getCino());
        }

        return cartService.addOrModify(cartItemDto);
    }


    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/items")
    public List<CartItemListDto> getCartItems(Principal principal) {
        String email = principal.getName();
        return cartService.getCartItems(email);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/{cino}")
    public List<CartItemListDto> removeFromCart(@PathVariable(name = "cino") Long cino) {
        return cartService.remove(cino);
    }
}
