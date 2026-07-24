package com.toolkit.pagination.util;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PageRequestUtilsTest {

    @Test
    void shouldApplyDefaultsWhenParamsAreNull() {
        Pageable pageable = PageRequestUtils.toPageable(null, null, null);

        assertThat(pageable.getPageNumber()).isZero();
        assertThat(pageable.getPageSize()).isEqualTo(PageRequestUtils.DEFAULT_SIZE);
        assertThat(pageable.getSort().isUnsorted()).isTrue();
    }

    @Test
    void shouldCapSizeAtMaximum() {
        Pageable pageable = PageRequestUtils.toPageable(0, 500, null);

        assertThat(pageable.getPageSize()).isEqualTo(PageRequestUtils.MAX_SIZE);
    }

    @Test
    void shouldTreatNegativePageAsDefault() {
        Pageable pageable = PageRequestUtils.toPageable(-5, 10, null);

        assertThat(pageable.getPageNumber()).isZero();
    }

    @Test
    void shouldParseMultiFieldSort() {
        Sort sort = PageRequestUtils.parseSort(List.of("lastName,asc", "createdAt,desc"));

        assertThat(sort.getOrderFor("lastName").getDirection()).isEqualTo(Sort.Direction.ASC);
        assertThat(sort.getOrderFor("createdAt").getDirection()).isEqualTo(Sort.Direction.DESC);
    }

    @Test
    void shouldIgnoreBlankSortEntries() {
        Sort sort = PageRequestUtils.parseSort(List.of("", "   "));

        assertThat(sort.isUnsorted()).isTrue();
    }

    @Test
    void shouldDefaultToAscendingWhenDirectionMissing() {
        Sort sort = PageRequestUtils.parseSort(List.of("email"));

        assertThat(sort.getOrderFor("email").getDirection()).isEqualTo(Sort.Direction.ASC);
    }
}
