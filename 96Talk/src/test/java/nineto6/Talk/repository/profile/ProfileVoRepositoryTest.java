package nineto6.Talk.repository.profile;

import lombok.extern.slf4j.Slf4j;
import nineto6.Talk.repository.profile.ProfileRepository;
import nineto6.Talk.vo.ProfileVo;
import nineto6.Talk.vo.MemberVo;
import nineto6.Talk.vo.FriendVo;
import nineto6.Talk.repository.friend.FriendRepository;
import nineto6.Talk.repository.member.MemberRepository;
import nineto6.Talk.dto.profile.ProfileMemberDto;
import nineto6.Talk.global.common.pagination.Pagination;
import nineto6.Talk.controller.profile.request.ProfileSearchRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@SpringBootTest
@Transactional
public class ProfileVoRepositoryTest {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private FriendRepository friendRepository;
    private static MemberVo memberVo;
    private static ProfileVo profileVo;
    @BeforeEach
    void setup() {
        memberVo = MemberVo.builder()
                .memberEmail("hello@naver.com")
                .memberPwd("123123")
                .memberNickname("한국")
                .build();
        memberRepository.save(memberVo);

        profileVo = ProfileVo.builder()
                .memberId(memberVo.getMemberId())
                .build();
    }

    @Test
    void saveDefault() {
        // given

        // when
        profileRepository.saveDefault(profileVo);

        // then
        Optional<ProfileVo> ProfileOptional = profileRepository.findByMemberId(memberVo.getMemberId());
        ProfileVo findProfileVo = ProfileOptional.orElse(null);
        Assertions.assertThat(findProfileVo).isNotNull();
        Assertions.assertThat(findProfileVo.getMemberId()).isEqualTo(memberVo.getMemberId());
    }

    @Test
    void updateFileByMemberId() {
        // given
        profileRepository.saveDefault(profileVo);

        // when
        profileRepository.updateFileByMemberId(memberVo.getMemberId(), "사진.jpg", "dsgsd.jpg");

        // then
        Optional<ProfileVo> profileOptional = profileRepository.findByMemberId(memberVo.getMemberId());
        ProfileVo findProfileVo = profileOptional.orElse(null);
        Assertions.assertThat(findProfileVo).isNotNull();
        Assertions.assertThat(findProfileVo.getProfileUploadFileName()).isEqualTo("사진.jpg");
        Assertions.assertThat(findProfileVo.getProfileStoreFileName()).isEqualTo("dsgsd.jpg");
    }

    @Test
    void updateStateMessageByMemberId() {
        // given
        profileRepository.saveDefault(profileVo);

        // when
        profileRepository.updateStateMessageByMemberId(memberVo.getMemberId(), "상태메세지");

        // then
        Optional<ProfileVo> profileOptional = profileRepository.findByMemberId(memberVo.getMemberId());
        ProfileVo findProfileVo = profileOptional.orElse(null);
        Assertions.assertThat(findProfileVo).isNotNull();
        Assertions.assertThat(findProfileVo.getProfileStateMessage()).isEqualTo("상태메세지");
    }

    @Test
    void findByMemberId() {
        // given
        profileRepository.saveDefault(profileVo);

        // when
        Optional<ProfileVo> profileOptional = profileRepository.findByMemberId(memberVo.getMemberId());

        // then
        ProfileVo findProfileVo = profileOptional.orElse(null);
        Assertions.assertThat(findProfileVo).isNotNull();
        Assertions.assertThat(findProfileVo.getMemberId()).isEqualTo(memberVo.getMemberId());
    }

    @Test
    void findByMemberNickname() {
        // given
        profileRepository.saveDefault(profileVo);

        // when
        Optional<ProfileMemberDto> memberProfileOptional = profileRepository.findProfileMemberDtoByMemberNickname(memberVo.getMemberNickname());

        // then
        ProfileMemberDto findProfileMemberDto = memberProfileOptional.orElse(null);
        Assertions.assertThat(findProfileMemberDto).isNotNull();
        Assertions.assertThat(findProfileMemberDto.getProfileVo().getMemberId()).isEqualTo(memberVo.getMemberId());
        Assertions.assertThat(findProfileMemberDto.getMemberNickname()).isEqualTo("한국");
    }

    @Test
    void findByStoreFileName() {
        // given
        profileRepository.saveDefault(profileVo);
        profileRepository.updateFileByMemberId(memberVo.getMemberId(), "uploadFileName", "storeFileName");

        // when
        Optional<ProfileVo> profileOptional = profileRepository.findByStoreFileName("storeFileName");

        // then
        ProfileVo findProfileVo = profileOptional.orElse(null);
        Assertions.assertThat(findProfileVo).isNotNull();
        Assertions.assertThat(findProfileVo.getMemberId()).isEqualTo(memberVo.getMemberId());
    }

    @Test
    void updateFileToNull() {
        // given
        profileRepository.saveDefault(profileVo);
        profileRepository.updateFileByMemberId(memberVo.getMemberId(), "uploadFileName", "storeFileName");

        // when
        profileRepository.updateFileToNull(memberVo.getMemberId());

        // then
        Optional<ProfileVo> profileOptional = profileRepository.findByMemberId(memberVo.getMemberId());
        ProfileVo findProfileVo = profileOptional.orElse(null);
        Assertions.assertThat(findProfileVo).isNotNull();
        Assertions.assertThat(findProfileVo.getProfileUploadFileName()).isNull();
        Assertions.assertThat(findProfileVo.getProfileStoreFileName()).isNull();
    }

    @Test
    void updateStateMessageToNull() {
        // given
        profileRepository.saveDefault(profileVo);
        profileRepository.updateStateMessageByMemberId(memberVo.getMemberId(), "프로필메세지");

        // when
        profileRepository.updateStateMessageToNull(memberVo.getMemberId());

        // then
        Optional<ProfileVo> profileOptional = profileRepository.findByMemberId(memberVo.getMemberId());
        ProfileVo findProfileVo = profileOptional.orElse(null);
        Assertions.assertThat(findProfileVo).isNotNull();
        Assertions.assertThat(findProfileVo.getProfileStateMessage()).isNull();
    }

    @Test
    void findFriendProfileListByMemberId() {
        // given
        MemberVo memberVo1 = MemberVo.builder()
                .memberEmail("hello1@naver.com")
                .memberPwd("123123")
                .memberNickname("주인공")
                .build();
        memberRepository.save(memberVo1);

        ProfileVo profileVo1 = ProfileVo.builder()
                .memberId(memberVo1.getMemberId())
                .build();
        profileRepository.saveDefault(profileVo1);

        MemberVo memberVo2 = MemberVo.builder()
                .memberEmail("hello2@naver.com")
                .memberPwd("asdasd")
                .memberNickname("친구")
                .build();
        memberRepository.save(memberVo2);

        ProfileVo profileVo2 = ProfileVo.builder()
                .memberId(memberVo2.getMemberId())
                .build();
        profileRepository.saveDefault(profileVo2);

        FriendVo friendVo = FriendVo.builder()
                .memberId(memberVo1.getMemberId())
                .friendMemberId(memberVo2.getMemberId())
                .build();
        friendRepository.save(friendVo);

        // when
        List<ProfileMemberDto> profileList = profileRepository.findProfileMemberDtoByMemberId(memberVo1.getMemberId());

        // then
        ProfileMemberDto profileMemberDto = profileList.get(0);
        Assertions.assertThat(profileMemberDto.getMemberNickname()).isEqualTo("친구");
        Assertions.assertThat(profileList.size()).isEqualTo(1);
    }

    @Test
    void findSearchProfileByKeyword() {
        // given
        for (int i = 0; i < 10; i++) {
            MemberVo memberVo = MemberVo.builder()
                    .memberEmail("hello1" + i + "@naver.com")
                    .memberPwd("123123")
                    .memberNickname("주인공" + i)
                    .build();
            memberRepository.save(memberVo);

            ProfileVo profileVo = ProfileVo.builder()
                    .memberId(memberVo.getMemberId())
                    .build();
            profileRepository.saveDefault(profileVo);
        }

        ProfileSearchRequest search = new ProfileSearchRequest();
        search.setKeyword("");
        Pagination pagination = new Pagination(profileRepository.getMaxCount(search), search);
        search.setPagination(pagination);

        // when
        List<ProfileMemberDto> searchProfile = profileRepository.findProfileMemberDtoByProfileSearchRequest(search);

        // then
        Assertions.assertThat(searchProfile.size()).isEqualTo(4);
    }

    @Test
    void getMaxCount() {
        // given
        for (int i = 0; i < 10; i++) {
            MemberVo memberVo = MemberVo.builder()
                    .memberEmail("hello1" + i + "@naver.com")
                    .memberPwd("123123")
                    .memberNickname("주인공" + i)
                    .build();
            memberRepository.save(memberVo);
        }

        ProfileSearchRequest search = new ProfileSearchRequest();
        search.setKeyword("");
        Pagination pagination = new Pagination(profileRepository.getMaxCount(search), search);
        search.setPagination(pagination);

        // when
        Integer maxCount = profileRepository.getMaxCount(search);

        // then
        Assertions.assertThat(maxCount).isEqualTo(10 + 1);
    }
}
