package com.piotrglazar.webs.converters;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import junitparams.converters.ConversionFailedException;
import junitparams.converters.ParamConverter;

import java.util.List;
import java.util.stream.Collectors;

public class IntegerListConverter implements ParamConverter<List<Integer>> {

    @Override
    public List<Integer> convert(final Object param, final String options) throws ConversionFailedException {
        return Splitter
                .on(";")
                .trimResults(CharMatcher.anyOf("[]"))
                .splitToList(param.toString())
                .stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }
}
