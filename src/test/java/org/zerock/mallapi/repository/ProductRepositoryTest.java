package org.zerock.mallapi.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.mallapi.domain.Product;
import org.zerock.mallapi.dto.PageRequestDto;

import java.util.Arrays;
import java.util.Optional;
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

        for (int i = 0; i < 10; i++) {

            Product product = Product.builder()
                    .pname("Test"+i)
                    .pdesc("Test Desc"+i)
                    .price(1000+i)
                    .build();

            product.addImageString(UUID.randomUUID() + "_" + "IMAGE1.JPG"+i);
            product.addImageString(UUID.randomUUID() + "_" + "IMAGE2.JPG"+i);

            productRepository.save(product);
        }
    }

    @Test
    public void readOne() throws Exception{
        Long pno=1l;

        Optional<Product> product = productRepository.selectOne(pno);

        Product result = product.orElseThrow();

        log.info("result={}", result.toString());

        log.info("imageList={}", result.getImageList());
    }

    @Test
    @Transactional
    public void testDelete() throws Exception{
        Long pno=2L;

        productRepository.updateToDelete(true, pno);

    }


    @Test
    @Transactional
    public void testUpdate() throws Exception {
        Optional<Product> product = productRepository.selectOne(1L);

        Product result = product.orElseThrow();

        result.changePrice(3000);

        result.clearList();

        result.addImageString(UUID.randomUUID() + "_" + "PIMAGE1.JPG");
        result.addImageString(UUID.randomUUID() + "_" + "PIMAGE2.JPG");
        result.addImageString(UUID.randomUUID() + "_" + "PIMAGE3.JPG");

        productRepository.save(result);
    }
    
    @Test
    public void testList() throws Exception{
        Pageable pageable = PageRequest.of(0, 10, Sort.by("pno").descending());

        Page<Object[]> result = productRepository.selectList(pageable);

        result.getContent().forEach(arr -> log.info(Arrays.toString(arr)));
    }   

    
    @Test
    public void testSearch() throws Exception{
        PageRequestDto pageRequestDto = PageRequestDto.builder().build();

        productRepository.searchList(pageRequestDto);
    }   

}
