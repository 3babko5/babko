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

import java.util.Optional;
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
    public Optional<Product> findById(UUID productId) {
        return productJpaRepository.findById(productId);
    }

    @Override
    public Page<Product> search(String productName, UUID companyId, UUID productId, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        if (productName != null && !productName.isBlank()) {
            builder.and(product.productName.containsIgnoreCase(productName));
        }

        if (companyId != null) {
            builder.and(product.companyId.eq(companyId));
        }

        if (productId != null) {
            builder.and(product.productId.eq(productId));
        }

        List<Product> results = queryFactory
                .selectFrom(product)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(QueryDslUtil.getAllOrderSpecifierArr(pageable, product))
                .fetch();

        Long total = queryFactory
                .select(product.count())
                .from(product)
                .where(builder)
                .fetchOne();

        return PageableExecutionUtils.getPage(results, pageable, () -> total != null ? total : 0L);
    }
}