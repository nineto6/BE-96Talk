package nineto6.Talk.model.member;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
public class MemberDto {
    private Long memberId;
    private String memberEmail;
    private String memberPwd;
    private String memberNm;
    private LocalDateTime memberRegdate;
    private List<String> roleList;
    @Builder
    public MemberDto(Long memberId, String memberEmail, String memberPwd, String memberNm, LocalDateTime memberRegdate, List<String> roleList) {
        this.memberId = memberId;
        this.memberEmail = memberEmail;
        this.memberPwd = memberPwd;
        this.memberNm = memberNm;
        this.memberRegdate = memberRegdate;
        this.roleList = roleList;
    }
}
