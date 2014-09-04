package com.piotrglazar.webs.dto;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class WebsPageableTest {

    @Mock
    Page<Integer> page;

    @Test
    public void shouldCopyParametersFromPage() {
        // given
        final List<Integer> ints = Lists.newArrayList(1, 2, 3);
        given(page.getContent()).willReturn(ints);
        given(page.isFirst()).willReturn(true);
        given(page.isLast()).willReturn(false);
        given(page.getTotalPages()).willReturn(5);
        given(page.getNumber()).willReturn(3);

        // when
        final WebsPageable<Integer> websPageable = new WebsPageable<>(page);

        // then
        assertThat(websPageable.getContent()).isEqualTo(ints);
        assertThat(websPageable.getPageCount()).isEqualTo(5);
        assertThat(websPageable.getPageNumber()).isEqualTo(3);
        assertThat(websPageable.isFirst()).isEqualTo(true);
        assertThat(websPageable.isLast()).isEqualTo(false);
    }

    @Test
    public void shouldTransformListOfIntsIntoListOfStrings() {
        // given
        final List<Integer> ints = Lists.newArrayList(1, 2, 3);
        given(page.getContent()).willReturn(ints);

        // when
        final WebsPageable<String> websPageable = new WebsPageable<>(page).transform(Object::toString);

        // then
        assertThat(websPageable.getContent()).containsOnly("1", "2", "3");
    }

    @Test
    public void shouldPerformSeveralTransformationsInChain() {
        // given
        final List<Integer> ints = Lists.newArrayList(1, 3, 5);
        given(page.getContent()).willReturn(ints);

        // when
        final WebsPageable<String> websPageable = new WebsPageable<>(page)
                                                    .transform(i -> i * Math.PI)
                                                    .transform(Math::round)
                                                    .transform(Object::toString);

        // then
        assertThat(websPageable.getContent()).containsOnly("3", "9", "16");
    }
}