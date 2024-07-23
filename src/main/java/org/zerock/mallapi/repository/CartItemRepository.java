package org.zerock.mallapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.mallapi.domain.CartItem;
import org.zerock.mallapi.dto.CartItemDto;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    //특정 사용자의 모든 장바구니 아이템을 가져옴

}
