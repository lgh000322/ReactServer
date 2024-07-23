package org.zerock.mallapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.mallapi.domain.Cart;
import org.zerock.mallapi.domain.CartItem;
import org.zerock.mallapi.domain.Member;
import org.zerock.mallapi.domain.Product;
import org.zerock.mallapi.dto.CartItemDto;
import org.zerock.mallapi.dto.CartItemListDto;
import org.zerock.mallapi.repository.CartItemRepository;
import org.zerock.mallapi.repository.CartRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Transactional
    @Override
    public List<CartItemListDto> addOrModify(CartItemDto cartItemDto) {
        String email = cartItemDto.getEmail();
        Long pno=cartItemDto.getPno();
        int qty = cartItemDto.getQty();
        Long cino = cartItemDto.getCino();

        if (cino != null) {
            Optional<CartItem> cartItemResult = cartItemRepository.findById(cino);
            CartItem cartItem = cartItemResult.orElseThrow();
            cartItem.changeQty(qty);
            cartItemRepository.save(cartItem);

            return getCartItems(email);
        }

        Cart cart = getCart(email);

        CartItem cartItem=null;

        cartItem = cartItemRepository.getItemOfPno(email, pno);

        if (cartItem == null) {
            Product product = Product.builder()
                    .pno(pno)
                    .build();

            cartItem = CartItem.builder()
                    .product(product)
                    .cart(cart)
                    .qty(qty)
                    .build();
        } else {
            cartItem.changeQty(qty);
        }

        cartItemRepository.save(cartItem);
        return getCartItems(email);
    }

    private Cart getCart(String email) {
        Cart cart=null;

        Optional<Cart> foundedCart = cartRepository.getCartOfMember(email);

        if (foundedCart.isEmpty()) {
            Member member = Member.builder()
                    .email(email)
                    .build();

            Cart tempCart = Cart.builder()
                    .owner(member)
                    .build();

            cart = cartRepository.save(tempCart);
        } else {
            cart = foundedCart.get();
        }

        return cart;
    }

    @Override
    public List<CartItemListDto> getCartItems(String email) {
        return cartItemRepository.getItemsOfCartDTOByEmail(email);
    }

    @Transactional
    @Override
    public List<CartItemListDto> remove(Long cino) {
        Long cno = cartItemRepository.getCartFromItem(cino);
        cartItemRepository.deleteById(cino);
        return cartItemRepository.getItemsOfCartDtoByCart(cno);
    }
}
