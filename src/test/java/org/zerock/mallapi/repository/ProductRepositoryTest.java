package org.zerock.mallapi.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.mallapi.domain.Product;

import java.util.UUID;

@SpringBootTest
@Slf4j
@Transactional(readOnly = true)
@Commit
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;
    
    @Test
    @Transactional
    public void testInsert() throws Exception{
        Product product = Product.builder()
                .pname("Test")
                .pdesc("Test Desc")
                .price(1000)
                .build();

        product.addImageString(UUID.randomUUID() + "_" + "IMAGE1.JPG");
        product.addImageString(UUID.randomUUID() + "_" + "IMAGE2.JPG");

        productRepository.save(product);
    }   



}
