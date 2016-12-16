package com.markit.pe.comparabledata.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QEVBData is a Querydsl query type for EVBData
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QEVBData extends EntityPathBase<EVBData> {

    private static final long serialVersionUID = -1695277493L;

    public static final QEVBData eVBData = new QEVBData("eVBData");

    public final NumberPath<java.math.BigDecimal> accruedInterest = createNumber("accruedInterest", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> askPrice = createNumber("askPrice", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> askYTM = createNumber("askYTM", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> bidAskPriceSpread = createNumber("bidAskPriceSpread", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> bidPrice = createNumber("bidPrice", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> bidYTM = createNumber("bidYTM", java.math.BigDecimal.class);

    public final StringPath classification = createString("classification");

    public final NumberPath<java.math.BigDecimal> coupon = createNumber("coupon", java.math.BigDecimal.class);

    public final StringPath currency = createString("currency");

    public final StringPath cusip = createString("cusip");

    public final DateTimePath<java.util.Date> date = createDateTime("date", java.util.Date.class);

    public final StringPath defaulted = createString("defaulted");

    public final NumberPath<java.math.BigDecimal> dirtyAskPrice = createNumber("dirtyAskPrice", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> dirtyBidPrice = createNumber("dirtyBidPrice", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> dirtyMidPrice = createNumber("dirtyMidPrice", java.math.BigDecimal.class);

    public final StringPath frequency = createString("frequency");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath isin = createString("isin");

    public final NumberPath<Integer> liquidityScore = createNumber("liquidityScore", Integer.class);

    public final DateTimePath<java.util.Date> maturityDate = createDateTime("maturityDate", java.util.Date.class);

    public final NumberPath<java.math.BigDecimal> midASWSpread = createNumber("midASWSpread", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> midModDuration = createNumber("midModDuration", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> midPrice = createNumber("midPrice", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> midYTM = createNumber("midYTM", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> midZSpread = createNumber("midZSpread", java.math.BigDecimal.class);

    public final StringPath region = createString("region");

    public final StringPath sectorLevel5 = createString("sectorLevel5");

    public final StringPath service = createString("service");

    public final StringPath shortName = createString("shortName");

    public final NumberPath<java.math.BigDecimal> spreadVsBenchmarkBid = createNumber("spreadVsBenchmarkBid", java.math.BigDecimal.class);

    public final StringPath ticker = createString("ticker");

    public final StringPath tier = createString("tier");

    public final StringPath type = createString("type");

    public QEVBData(String variable) {
        super(EVBData.class, forVariable(variable));
    }

    public QEVBData(Path<? extends EVBData> path) {
        super(path.getType(), path.getMetadata());
    }

    public QEVBData(PathMetadata metadata) {
        super(EVBData.class, metadata);
    }

}

