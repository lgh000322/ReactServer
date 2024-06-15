package org.zerock.mallapi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.mallapi.dto.PageRequestDto;
import org.zerock.mallapi.dto.PageResponseDto;
import org.zerock.mallapi.dto.ProductDto;
import org.zerock.mallapi.service.ProductService;
import org.zerock.mallapi.util.CustomFileUtil;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final CustomFileUtil fileUtil;
    private final ProductService productService;

   /* @PostMapping("/")
    public Map<String, String> save(ProductDto productDto) {
        log.info("register: {}", productDto);

        List<MultipartFile> files = productDto.getFiles();
        List<String> uploadedFileNames = fileUtil.saveFiles(files);

        productDto.setUploadedFileNames(uploadedFileNames);

        log.info("uploadedFileName={}", uploadedFileNames);

        return Map.of("RESULT", "SUCCESS");
    }*/

    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGet(@PathVariable(name = "fileName") String fileName) {
        return fileUtil.getFile(fileName);
    }


    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @GetMapping("/list")
    public PageResponseDto<ProductDto> list(PageRequestDto pageRequestDto) {
        return productService.getList(pageRequestDto);
    }

    @PostMapping("/")
    public Map<String, Long> register(ProductDto productDto) {
        List<MultipartFile> files = productDto.getFiles();
        List<String> uploadFileNames = fileUtil.saveFiles(files);

        productDto.setUploadedFileNames(uploadFileNames);

        log.info(productDto.getUploadedFileNames().toString());

        Long pno = productService.register(productDto);

        return Map.of("result", pno);
    }

    @GetMapping("/{pno}")
    public ProductDto read(@PathVariable(name = "pno") Long pno) {
        return productService.get(pno);
    }

    @PutMapping("/{pno}")
    public Map<String, String> modify(@PathVariable(name = "pno") Long pno, ProductDto productDto) {

        productDto.setPno(pno);

        ProductDto oldProductDto = productService.get(pno);

        List<MultipartFile> files = productDto.getFiles();
        List<String> currentUploadFileName = fileUtil.saveFiles(files);

        List<String> uploadedFileNames = productDto.getUploadedFileNames();

        if (currentUploadFileName != null && !currentUploadFileName.isEmpty()) {
            uploadedFileNames.addAll(currentUploadFileName);
        }

        productService.modify(productDto);

        List<String> oldFileNames = oldProductDto.getUploadedFileNames();
        if (oldFileNames != null && !oldFileNames.isEmpty()) {
            List<String> removeFileNames = oldFileNames.stream().filter(fileName -> uploadedFileNames.indexOf(fileName) < 0).collect(Collectors.toList());
            fileUtil.deleteFiles(removeFileNames);
        }

        return Map.of("RESULT", "SUCCESS");
    }

    @DeleteMapping("/{pno}")
    public Map<String, String> remove(@PathVariable(name = "pno") Long pno) {
        List<String> oldFileNames = productService.get(pno).getUploadedFileNames();
        productService.remove(pno);

        fileUtil.deleteFiles(oldFileNames);

        return Map.of("RESULT", "SUCCESS");
    }
}
