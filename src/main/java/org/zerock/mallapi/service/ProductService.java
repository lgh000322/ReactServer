package org.zerock.mallapi.service;

import org.zerock.mallapi.dto.PageRequestDto;
import org.zerock.mallapi.dto.PageResponseDto;
import org.zerock.mallapi.dto.ProductDto;

public interface ProductService {

    PageResponseDto<ProductDto> getList(PageRequestDto pageRequestDto);

    Long register(ProductDto productDto);

    ProductDto get(Long pno);

    void modify(ProductDto productDto);

    void remove(Long pno);
}
