package com.business.common.Entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBaseDataEntity is a Querydsl query type for BaseDataEntity
 */
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QBaseDataEntity extends EntityPathBase<BaseDataEntity> {

    private static final long serialVersionUID = -121414585L;

    public static final QBaseDataEntity baseDataEntity = new QBaseDataEntity("baseDataEntity");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Integer> createdBy = createNumber("createdBy", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final NumberPath<Integer> deletedBy = createNumber("deletedBy", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final NumberPath<Integer> updatedBy = createNumber("updatedBy", Integer.class);

    public QBaseDataEntity(String variable) {
        super(BaseDataEntity.class, forVariable(variable));
    }

    public QBaseDataEntity(Path<? extends BaseDataEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBaseDataEntity(PathMetadata metadata) {
        super(BaseDataEntity.class, metadata);
    }

}

