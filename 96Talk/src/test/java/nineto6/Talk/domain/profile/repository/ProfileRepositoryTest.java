package nineto6.Talk.domain.profile.repository;

import lombok.extern.slf4j.Slf4j;
import nineto6.Talk.domain.friend.repository.FriendRepository;
import nineto6.Talk.domain.member.repository.MemberRepository;
import nineto6.Talk.domain.profile.repository.ProfileRepository;
import nineto6.Talk.domain.friend.domain.Friend;
import nineto6.Talk.domain.member.domain.Member;
import nineto6.Talk.domain.profile.domain.ProfileMember;
import nineto6.Talk.domain.profile.domain.Profile;
import nineto6.Talk.global.common.pagination.Pagination;
import nineto6.Talk.domain.profile.dto.ProfileSearchDto;
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
public class ProfileRepositoryTest {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private FriendRepository friendRepository;
    private static Member member;
    private static Profile profile;
    @BeforeEach
    void setup() {
        member = Member.builder()
                .memberEmail("hello@naver.com")
                .memberPwd("123123")
                .memberNickname("한국")
                .build();
        memberRepository.save(member);

        profile = Profile.builder()
                .memberId(member.getMemberId())
                .build();
    }

    @Test
    void saveDefault() {
        // given

        // when
        profileRepository.saveDefault(profile);

        // then
        Optional<Profile> ProfileOptional = profileRepository.findByMemberId(member.getMemberId());
        Profile findProfile = ProfileOptional.orElse(null);
        Assertions.assertThat(findProfile).isNotNull();
        Assertions.assertThat(findProfile.getMemberId()).isEqualTo(member.getMemberId());
    }

    @Test
    void updateFileByMemberId() {
        // given
        profileRepository.saveDefault(profile);

        // when
        profileRepository.updateFileByMemberId(member.getMemberId(), "사진.jpg", "dsgsd.jpg");

        // then
        Optional<Profile> profileOptional = profileRepository.findByMemberId(member.getMemberId());
        Profile findProfile = profileOptional.orElse(null);
        Assertions.assertThat(findProfile).isNotNull();
        Assertions.assertThat(findProfile.getProfileUploadFileName()).isEqualTo("사진.jpg");
        Assertions.assertThat(findProfile.getProfileStoreFileName()).isEqualTo("dsgsd.jpg");
    }

    @Test
    void updateStateMessageByMemberId() {
        // given
        profileRepository.saveDefault(profile);

        // when
        profileRepository.updateStateMessageByMemberId(member.getMemberId(), "상태메세지");

        // then
        Optional<Profile> profileOptional = profileRepository.findByMemberId(member.getMemberId());
        Profile findProfile = profileOptional.orElse(null);
        Assertions.assertThat(findProfile).isNotNull();
        Assertions.assertThat(findProfile.getProfileStateMessage()).isEqualTo("상태메세지");
    }

    @Test
    void findByMemberId() {
        // given
        profileRepository.saveDefault(profile);

        // when
        Optional<Profile> profileOptional = profileRepository.findByMemberId(member.getMemberId());

        // then
        Profile findProfile = profileOptional.orElse(null);
        Assertions.assertThat(findProfile).isNotNull();
        Assertions.assertThat(findProfile.getMemberId()).isEqualTo(member.getMemberId());
    }

    @Test
    void findByMemberNickname() {
        // given
        profileRepository.saveDefault(profile);

        // when
        Optional<ProfileMember> memberProfileOptional = profileRepository.findByMemberNickname(member.getMemberNickname());

        // then
        ProfileMember findProfileMember = memberProfileOptional.orElse(null);
        Assertions.assertThat(findProfileMember).isNotNull();
        Assertions.assertThat(findProfileMember.getProfile().getMemberId()).isEqualTo(member.getMemberId());
        Assertions.assertThat(findProfileMember.getMemberNickname()).isEqualTo("한국");
    }

    @Test
    void findByStoreFileName() {
        // given
        profileRepository.saveDefault(profile);
        profileRepository.updateFileByMemberId(member.getMemberId(), "uploadFileName", "storeFileName");

        // when
        Optional<Profile> profileOptional = profileRepository.findByStoreFileName("storeFileName");

        // then
        Profile findProfile = profileOptional.orElse(null);
        Assertions.assertThat(findProfile).isNotNull();
        Assertions.assertThat(findProfile.getMemberId()).isEqualTo(member.getMemberId());
    }

    @Test
    void updateFileToNull() {
        // given
        profileRepository.saveDefault(profile);
        profileRepository.updateFileByMemberId(member.getMemberId(), "uploadFileName", "storeFileName");

        // when
        profileRepository.updateFileToNull(member.getMemberId());

        // then
        Optional<Profile> profileOptional = profileRepository.findByMemberId(member.getMemberId());
        Profile findProfile = profileOptional.orElse(null);
        Assertions.assertThat(findProfile).isNotNull();
        Assertions.assertThat(findProfile.getProfileUploadFileName()).isNull();
        Assertions.assertThat(findProfile.getProfileStoreFileName()).isNull();
    }

    @Test
    void updateStateMessageToNull() {
        // given
        profileRepository.saveDefault(profile);
        profileRepository.updateStateMessageByMemberId(member.getMemberId(), "프로필메세지");

        // when
        profileRepository.updateStateMessageToNull(member.getMemberId());

        // then
        Optional<Profile> profileOptional = profileRepository.findByMemberId(member.getMemberId());
        Profile findProfile = profileOptional.orElse(null);
        Assertions.assertThat(findProfile).isNotNull();
        Assertions.assertThat(findProfile.getProfileStateMessage()).isNull();
    }

    @Test
    void findFriendProfileListByMemberId() {
        // given
        Member member1 = Member.builder()
                .memberEmail("hello1@naver.com")
                .memberPwd("123123")
                .memberNickname("주인공")
                .build();
        memberRepository.save(member1);

        Profile profile1 = Profile.builder()
                .memberId(member1.getMemberId())
                .build();
        profileRepository.saveDefault(profile1);

        Member member2 = Member.builder()
                .memberEmail("hello2@naver.com")
                .memberPwd("asdasd")
                .memberNickname("친구")
                .build();
        memberRepository.save(member2);

        Profile profile2 = Profile.builder()
                .memberId(member2.getMemberId())
                .build();
        profileRepository.saveDefault(profile2);

        Friend friend = Friend.builder()
                .memberId(member1.getMemberId())
                .friendMemberId(member2.getMemberId())
                .build();
        friendRepository.save(friend);

        // when
        List<ProfileMember> profileList = profileRepository.findFriendProfileListByMemberId(member1.getMemberId());

        // then
        ProfileMember profileMember = profileList.get(0);
        Assertions.assertThat(profileMember.getMemberNickname()).isEqualTo("친구");
        Assertions.assertThat(profileList.size()).isEqualTo(1);
    }

    @Test
    void findSearchProfileByKeyword() {
        // given
        for (int i = 0; i < 10; i++) {
            Member member = Member.builder()
                    .memberEmail("hello1" + i + "@naver.com")
                    .memberPwd("123123")
                    .memberNickname("주인공" + i)
                    .build();
            memberRepository.save(member);

            Profile profile = Profile.builder()
                    .memberId(member.getMemberId())
                    .build();
            profileRepository.saveDefault(profile);
        }

        ProfileSearchDto search = new ProfileSearchDto();
        search.setKeyword("");
        Pagination pagination = new Pagination(profileRepository.getMaxCount(search), search);
        search.setPagination(pagination);

        // when
        List<ProfileMember> searchProfile = profileRepository.findSearchProfileByKeyword(search);

        // then
        Assertions.assertThat(searchProfile.size()).isEqualTo(4);
    }

    @Test
    void getMaxCount() {
        // given
        for (int i = 0; i < 10; i++) {
            Member member = Member.builder()
                    .memberEmail("hello1" + i + "@naver.com")
                    .memberPwd("123123")
                    .memberNickname("주인공" + i)
                    .build();
            memberRepository.save(member);
        }

        ProfileSearchDto search = new ProfileSearchDto();
        search.setKeyword("");
        Pagination pagination = new Pagination(profileRepository.getMaxCount(search), search);
        search.setPagination(pagination);

        // when
        Integer maxCount = profileRepository.getMaxCount(search);

        // then
        Assertions.assertThat(maxCount).isEqualTo(10 + 1);
    }
}
