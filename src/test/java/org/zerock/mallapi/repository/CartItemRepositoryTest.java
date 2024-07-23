package org.zerock.mallapi.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class CartItemRepositoryTest {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;
    
    @Test
    public void testListOfMember() throws Exception{
        //given
        String email = "user1@aaa.com";

        //when
        cartItemRepository.getItemsOfCartDTOByEmail(email);
        //then
    }   

}