package com.piotrglazar.webs.converters;

import junitparams.converters.ConversionFailedException;
import junitparams.converters.ParamConverter;

import java.math.BigDecimal;

public class BigDecimalConverter implements ParamConverter<BigDecimal> {
    @Override
    public BigDecimal convert(final Object param, final String options) throws ConversionFailedException {
        return BigDecimal.valueOf(Double.parseDouble(param.toString()));
    }
}
