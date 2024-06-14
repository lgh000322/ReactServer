package org.zerock.mallapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.mallapi.domain.Product;
import org.zerock.mallapi.domain.ProductImage;
import org.zerock.mallapi.dto.PageRequestDto;
import org.zerock.mallapi.dto.PageResponseDto;
import org.zerock.mallapi.dto.ProductDto;
import org.zerock.mallapi.repository.ProductRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;


    @Override
    public PageResponseDto<ProductDto> getList(PageRequestDto pageRequestDto) {
        Pageable pageable = PageRequest.of(
                pageRequestDto.getPage() - 1,
                pageRequestDto.getSize(),
                Sort.by("pno").descending()
        );

        Page<Object[]> result = productRepository.selectList(pageable);

        List<ProductDto> dtoList = result.get().map(arr -> {
            ProductDto productDto = null;

            Product product = (Product) arr[0];
            ProductImage productImage = (ProductImage) arr[1];

            productDto=ProductDto.builder()
                    .pno(product.getPno())
                    .pname(product.getPname())
                    .pdesc(product.getPdesc())
                    .price(product.getPrice())
                    .build();

            String imageStr = productImage.getFileName();
            productDto.setUploadedFileNames(List.of(imageStr));

            return productDto;
        }).collect(Collectors.toList());

        Long totalCount = result.getTotalElements();

        return PageResponseDto.<ProductDto>withAll()
                .dtoList(dtoList)
                .total(totalCount)
                .pageRequestDto(pageRequestDto)
                .build();

    }

    @Transactional
    @Override
    public Long register(ProductDto productDto) {
        Product product = dtoToEntity(productDto);

        log.info("=======================================");
        log.info(product.toString());
        log.info(product.getImageList().toString());

        Long pno = productRepository.save(product).getPno();

        return pno;
    }

    @Override
    public ProductDto get(Long pno) {
        Optional<Product> result = productRepository.findById(pno);
        Product product = result.orElseThrow();
        return entityToDto(product);
    }

    @Transactional
    @Override
    public void modify(ProductDto productDto) {
        Optional<Product> result = productRepository.findById(productDto.getPno());
        Product product = result.orElseThrow();

        product.changePrice(productDto.getPrice());
        product.changeName(productDto.getPname());
        product.changeDesc(productDto.getPdesc());
        product.changeDel(productDto.isDelFlag());

        //이미지 처리
        List<String> uploadedFileNames = productDto.getUploadedFileNames();

        product.clearList();

        if (uploadedFileNames != null && !uploadedFileNames.isEmpty()) {
            uploadedFileNames.forEach(uploadName->{
                product.addImageString(uploadName);
            });
        }
    }

    @Transactional
    @Override
    public void remove(Long pno) {
        productRepository.deleteById(pno);

    }

    private ProductDto entityToDto(Product product) {
        ProductDto productDto = ProductDto.builder()
                .pno(product.getPno())
                .pname(product.getPname())
                .pdesc(product.getPdesc())
                .price(product.getPrice())
                .delFlag(product.isDelFlag())
                .build();

        List<ProductImage> imageList = product.getImageList();
        if (imageList == null || imageList.isEmpty()) {
            return productDto;
        }

        List<String> fileNameList = imageList.stream()
                .map(ProductImage::getFileName)
                .collect(Collectors.toList());

        productDto.setUploadedFileNames(fileNameList);

        return productDto;
    }

    private Product dtoToEntity(ProductDto productDto) {
        Product product = Product.builder()
                .pno(productDto.getPno())
                .pname(productDto.getPname())
                .pdesc(productDto.getPdesc())
                .price(productDto.getPrice())
                .build();

        List<String> uploadedFileNames = productDto.getUploadedFileNames();

        if (uploadedFileNames == null && uploadedFileNames.isEmpty()) {
            return product;
        }

        uploadedFileNames.forEach(fileName->{
            product.addImageString(fileName);
        });

        return product;
    }
}
