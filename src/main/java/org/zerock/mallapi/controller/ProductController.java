package org.zerock.mallapi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.mallapi.dto.ProductDto;
import org.zerock.mallapi.util.CustomFileUtil;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final CustomFileUtil fileUtil;

    @PostMapping("/")
    public Map<String, String> register(ProductDto productDto) {
        log.info("register: {}", productDto);

        List<MultipartFile> files = productDto.getFiles();
        List<String> uploadedFileNames = fileUtil.saveFiles(files);

        productDto.setUploadedFileNames(uploadedFileNames);

        log.info("uploadedFileName={}", uploadedFileNames);

        return Map.of("RESULT", "SUCCESS");
    }

    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGet(@PathVariable(name = "fileName") String fileName) {
        return fileUtil.getFile(fileName);
    }



}
