package org.zerock.mallapi.repository.search;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.mallapi.domain.Product;
import org.zerock.mallapi.domain.QProduct;
import org.zerock.mallapi.domain.QProductImage;
import org.zerock.mallapi.dto.PageRequestDto;
import org.zerock.mallapi.dto.PageResponseDto;
import org.zerock.mallapi.dto.ProductDto;

import java.util.List;

import static org.zerock.mallapi.domain.QProduct.*;
import static org.zerock.mallapi.domain.QProductImage.*;


@Slf4j
public class ProductSearchImpl extends QuerydslRepositorySupport implements ProductSearch {

    private JPAQueryFactory queryFactory;
    public ProductSearchImpl(EntityManager em) {
        super(Product.class);
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public PageResponseDto<ProductDto> searchList(PageRequestDto pageRequestDto) {
        log.info("===========================searchList====================================");

        Pageable pageable = PageRequest.of(
                pageRequestDto.getPage() - 1,
                pageRequestDto.getSize(),
                Sort.by("pno").descending());

        JPAQuery<Product> query = queryFactory
                .selectFrom(product)
                .leftJoin(product.imageList, productImage)
                .where(productImage.ord.eq(0));

        this.getQuerydsl().applyPagination(pageable, query);

        List<Product> productList = query.fetch();

        Long count = queryFactory
                .select(product.count())
                .from(product)
                .leftJoin(product.imageList, productImage)
                .where(productImage.ord.eq(0))
                .fetchOne();

        log.info("===============================================================================");
        log.info(productList.toString());
        return null;
    }
}
