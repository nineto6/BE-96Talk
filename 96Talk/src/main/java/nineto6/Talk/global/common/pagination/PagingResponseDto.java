package nineto6.Talk.global.common.pagination;

import lombok.Getter;
import nineto6.Talk.global.common.pagination.Pagination;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PagingResponseDto<T> {
    private List<T> list = new ArrayList<>();
    private Pagination pagination;

    public PagingResponseDto(List<T> list, Pagination pagination) {
        this.list.addAll(list);
        this.pagination = pagination;
    }
}
