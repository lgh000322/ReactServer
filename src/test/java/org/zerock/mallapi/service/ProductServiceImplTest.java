package org.zerock.mallapi.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.mallapi.dto.PageRequestDto;
import org.zerock.mallapi.dto.PageResponseDto;
import org.zerock.mallapi.dto.ProductDto;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class ProductServiceImplTest {

    @Autowired
    private ProductService productService;


    @Test
    public void testList() throws Exception{
        PageRequestDto pageRequestDto = PageRequestDto.builder().build();

        PageResponseDto<ProductDto> responseDto = productService.getList(pageRequestDto);

        log.info(responseDto.getDtoList().toString());

    }

    @Test
    public void testRegister() throws Exception{
        ProductDto productDto = ProductDto.builder()
                .pname("새로운 상품")
                .pdesc("신규 추가 상품입니다.")
                .price(10000)
                .build();

        productDto.setUploadedFileNames(
                java.util.List.of(
                        UUID.randomUUID()+"-"+"Test1.JPG",
                        UUID.randomUUID()+"-"+"Test2.JPG"
                )
        );

        productService.register(productDto);
    }



}