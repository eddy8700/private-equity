package com.markit.pe.comparabledata.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QEVBCompositeKey is a Querydsl query type for EVBCompositeKey
 */
@Generated("com.querydsl.codegen.EmbeddableSerializer")
public class QEVBCompositeKey extends BeanPath<EVBCompositeKey> {

    private static final long serialVersionUID = -2103390215L;

    public static final QEVBCompositeKey eVBCompositeKey = new QEVBCompositeKey("eVBCompositeKey");

    public final DateTimePath<java.util.Date> date = createDateTime("date", java.util.Date.class);

    public final StringPath isin = createString("isin");

    public QEVBCompositeKey(String variable) {
        super(EVBCompositeKey.class, forVariable(variable));
    }

    public QEVBCompositeKey(Path<? extends EVBCompositeKey> path) {
        super(path.getType(), path.getMetadata());
    }

    public QEVBCompositeKey(PathMetadata metadata) {
        super(EVBCompositeKey.class, metadata);
    }

}

