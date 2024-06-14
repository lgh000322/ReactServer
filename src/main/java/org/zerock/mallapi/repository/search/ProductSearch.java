package org.zerock.mallapi.repository.search;

import org.zerock.mallapi.dto.PageRequestDto;
import org.zerock.mallapi.dto.PageResponseDto;
import org.zerock.mallapi.dto.ProductDto;

public interface ProductSearch {

    PageResponseDto<ProductDto> searchList(PageRequestDto pageRequestDto);
}
