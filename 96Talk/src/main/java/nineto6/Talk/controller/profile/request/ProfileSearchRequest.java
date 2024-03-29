package nineto6.Talk.controller.profile.request;

import lombok.Getter;
import lombok.Setter;
import nineto6.Talk.global.common.pagination.Search;

@Getter @Setter
public class ProfileSearchRequest extends Search {
    private String keyword; // 검색 키워드

    // 객체가 생성되는 시점에 현재 페이지 번호는 1로, 페이지당 출력할 데이터 개수와
    // 하단에 출력할 페이지 개수를 10으로 초기화 합니다.
    public ProfileSearchRequest() {
        this.setPage(1);
        this.setRecordSize(4);
        this.setPageSize(5);
    }
}
