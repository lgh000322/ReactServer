package org.zerock.mallapi.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.mallapi.domain.Cart;
import org.zerock.mallapi.domain.CartItem;
import org.zerock.mallapi.domain.Member;
import org.zerock.mallapi.domain.Product;
import org.zerock.mallapi.dto.CartItemListDto;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class CartItemRepositoryTest {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Transactional
    @Commit
    @Test
    public void testInsertByProduct() throws Exception {
        String email = "user1@aaa.com";
        Long pno = 6L;
        int qty = 1;


        CartItem cartItem = cartItemRepository.getItemOfPno(email, pno);

        if (cartItem != null) {
            cartItem.changeQty(qty);
            cartItemRepository.save(cartItem);
            return;
        }

        Optional<Cart> result = cartRepository.getCartOfMember(email);

        Cart cart = null;

        if (result.isEmpty()) {
            Member member = Member.builder()
                    .email(email)
                    .build();

            Cart tempCart = Cart.builder()
                    .owner(member)
                    .build();

            cart = cartRepository.save(tempCart);
        } else {
            cart = result.get();
        }

        Product product = Product.builder()
                .pno(pno)
                .build();

        cartItem = CartItem.builder()
                .cart(cart)
                .product(product)
                .qty(qty)
                .build();

        cartItemRepository.save(cartItem);

    }

    @Test
    public void testListOfMember() throws Exception {
        String email = "user1@aaa.com";

        List<CartItemListDto> itemsOfCartDTOByEmail = cartItemRepository.getItemsOfCartDTOByEmail(email);

        for (CartItemListDto cartItemListDto : itemsOfCartDTOByEmail) {
            log.info("cartItemListDto:={}", cartItemListDto.toString());
        }
    }

    @Transactional
    @Commit
    @Test
    public void testUpdateByCino() throws Exception{
        Long cino = 1L;
        int qty=4;

        Optional<CartItem> result = cartItemRepository.findById(cino);

        CartItem cartItem = result.orElseThrow();

        cartItem.changeQty(qty);

        cartItemRepository.save(cartItem);
    }

    @Test
    public void testDeleteThenList() throws Exception {
        Long cino=1L;
        Long cno = cartItemRepository.getCartFromItem(cino);

        cartItemRepository.deleteById(cno);

        List<CartItemListDto> itemsOfCartDtoByCart = cartItemRepository.getItemsOfCartDtoByCart(cno);
        for (CartItemListDto cartItemListDto : itemsOfCartDtoByCart) {
            log.info("cartItemListDto={}", cartItemListDto.toString());
        }
    }
}