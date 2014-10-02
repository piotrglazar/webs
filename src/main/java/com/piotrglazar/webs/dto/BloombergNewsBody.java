package com.piotrglazar.webs.dto;

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.Objects;

public class BloombergNewsBody {

    private final String name;
    private final String growth;
    private final boolean up;
    private final String price;

    public BloombergNewsBody(final String name, final String growth, final boolean up, final String price) {
        this.name = name;
        this.growth = growth;
        this.up = up;
        this.price = price;
    }

    public Map<String, Object> asMap() {
        return ImmutableMap.of("name", name, "growth", growth, "up", up, "price", price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, growth, up, price);
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
        return Objects.equals(this.name, other.name) &&
                Objects.equals(this.growth, other.growth) &&
                Objects.equals(this.up, other.up) &&
                Objects.equals(this.price, other.price);
    }
}
