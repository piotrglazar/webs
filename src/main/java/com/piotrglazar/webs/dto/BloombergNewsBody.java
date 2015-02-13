package com.piotrglazar.webs.dto;

import com.google.common.collect.ImmutableMap;

import javax.annotation.concurrent.Immutable;
import java.util.Map;
import java.util.Objects;

@Immutable
public final class BloombergNewsBody {

    private final String name;
    private final String priceChange;
    private final boolean up;
    private final String price;
    private final String percentChange;

    public BloombergNewsBody(String name, String priceChange, boolean up, String price, String percentChange) {
        this.name = name;
        this.priceChange = priceChange;
        this.up = up;
        this.price = price;
        this.percentChange = percentChange;
    }

    public Map<String, Object> asMap() {
        return ImmutableMap.of("name", name, "priceChange", priceChange, "up", up, "price", price, "percentChange", percentChange);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, priceChange, up, price, percentChange);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final BloombergNewsBody other = (BloombergNewsBody) obj;
        return Objects.equals(this.name, other.name)
                && Objects.equals(this.priceChange, other.priceChange)
                && Objects.equals(this.up, other.up)
                && Objects.equals(this.price, other.price)
                && Objects.equals(this.percentChange, other.percentChange);
    }
}
