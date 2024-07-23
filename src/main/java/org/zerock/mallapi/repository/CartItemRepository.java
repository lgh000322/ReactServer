package org.zerock.mallapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.zerock.mallapi.domain.CartItem;
import org.zerock.mallapi.dto.CartItemDto;
import org.zerock.mallapi.dto.CartItemListDto;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    //특정 사용자의 모든 장바구니 아이템을 가져옴

    @Query("select new org.zerock.mallapi.dto.CartItemListDto(ci.cino, ci.qty, p.pname, p.price, pi.fileName) " +
            "from CartItem ci inner join Cart mc on ci.cart = mc " +
            "left join Product p on ci.product = p " +
            "left join p.imageList pi " +
            "where pi.ord = 0 and mc.owner.email = :email " +
            "order by ci.cino desc")
    List<CartItemListDto> getItemsOfCartDTOByEmail(@Param("email") String email);

    //이메일 상품번호로 해당 상품이 장바구니 아이템으로 있는지 확인
    @Query("select ci from CartItem ci left join Cart c on ci.cart = c " +
            "where c.owner.email = :email and ci.product.pno = :pno")
    CartItem getItemOfPno(@Param("email") String email, @Param("pno") Long pno);


    //장바구니 아이템 번호로 장바구니 번호를 얻어옴
    @Query("select c.cno from Cart c left join CartItem ci on ci.cart = c " +
            "where ci.cino = :cino")
    Long getCartFromItem(@Param("cino") Long cino);


    //장바구니 번호로 모든 장바구니 아이템 조회

    @Query("select new org.zerock.mallapi.dto.CartItemListDto(ci.cino, ci.qty, p.pname, p.price, pi.fileName) " +
            "from CartItem ci inner join Cart c on ci.cart = c " +
            "left join Product p on ci.product = p " +
            "left join p.imageList pi " +
            "where pi.ord = 0 and c.cno = :cno " +
            "order by ci.cino desc")
    List<CartItemListDto> getItemsOfCartDtoByCart(@Param("cno") Long cno);

}
