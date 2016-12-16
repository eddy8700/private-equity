package com.markit.pe.comparabledata.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QEVBCompFilterRaw is a Querydsl query type for EVBCompFilterRaw
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QEVBCompFilterRaw extends EntityPathBase<EVBCompFilterRaw> {

    private static final long serialVersionUID = -1568945728L;

    public static final QEVBCompFilterRaw eVBCompFilterRaw = new QEVBCompFilterRaw("eVBCompFilterRaw");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final StringPath parent = createString("parent");

    public final NumberPath<Integer> referenceValue = createNumber("referenceValue", Integer.class);

    public final StringPath type = createString("type");

    public final StringPath value = createString("value");

    public QEVBCompFilterRaw(String variable) {
        super(EVBCompFilterRaw.class, forVariable(variable));
    }

    public QEVBCompFilterRaw(Path<? extends EVBCompFilterRaw> path) {
        super(path.getType(), path.getMetadata());
    }

    public QEVBCompFilterRaw(PathMetadata metadata) {
        super(EVBCompFilterRaw.class, metadata);
    }

}

