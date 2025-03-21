package com.business.product.product.infrastructure.repository;

import com.business.common.infrastructure.util.QueryDslUtil;
import com.business.product.product.domain.entity.Product;
import static com.business.product.product.domain.entity.QProduct.product;
import com.business.product.product.domain.repository.ProductRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Product save(Product product) {
        return productJpaRepository.save(product);
    }

    @Override
    public Page<Product> search(String companyName, UUID companyId, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        if (companyName != null && !companyName.isBlank()) {
            builder.and(product.productName.containsIgnoreCase(companyName));
        }

        if (companyId != null) {
            builder.and(product.companyId.eq(companyId));
        }

        List<Product> results = queryFactory
                .selectFrom(product)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(QueryDslUtil.getAllOrderSpecifierArr(pageable, product))
                .fetch();

        long total = queryFactory
                .selectFrom(product)
                .where(builder)
                .fetchCount();

        return PageableExecutionUtils.getPage(results, pageable, () -> total);
    }
}