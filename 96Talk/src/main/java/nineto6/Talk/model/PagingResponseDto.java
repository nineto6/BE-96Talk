package nineto6.Talk.model;

import lombok.Getter;

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
