package com.toolkit.pagination.dto;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PagedResponseTest {

    @Test
    void shouldCopyMetadataFromSpringDataPage() {
        Page<Integer> page = new PageImpl<>(List.of(1, 2, 3), PageRequest.of(0, 3), 10);

        PagedResponse<Integer> response = PagedResponse.from(page);

        assertThat(response.data()).containsExactly(1, 2, 3);
        assertThat(response.page()).isZero();
        assertThat(response.size()).isEqualTo(3);
        assertThat(response.totalElements()).isEqualTo(10);
        assertThat(response.totalPages()).isEqualTo(4);
    }

    @Test
    void shouldMapContentWhilePreservingMetadata() {
        Page<Integer> page = new PageImpl<>(List.of(1, 2, 3), PageRequest.of(0, 3), 10);

        PagedResponse<String> response = PagedResponse.from(page, i -> "item-" + i);

        assertThat(response.data()).containsExactly("item-1", "item-2", "item-3");
        assertThat(response.totalElements()).isEqualTo(10);
        assertThat(response.totalPages()).isEqualTo(4);
    }
}
