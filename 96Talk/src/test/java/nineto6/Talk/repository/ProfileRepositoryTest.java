package nineto6.Talk.repository;

import lombok.extern.slf4j.Slf4j;
import nineto6.Talk.domain.Member;
import nineto6.Talk.domain.Profile;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@SpringBootTest
@Transactional
public class ProfileRepositoryTest {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Test
    void saveDefault() {
        // given
        Member member = Member.builder()
                .memberEmail("hello@naver.com")
                .memberPwd("123123")
                .memberNm("한국")
                .build();
        memberRepository.save(member);
        Profile profile = Profile.builder()
                .memberId(member.getMemberId())
                .build();

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
        Member member = Member.builder()
                .memberEmail("hello@naver.com")
                .memberPwd("123123")
                .memberNm("한국")
                .build();
        memberRepository.save(member);

        Profile profile = Profile.builder()
                .memberId(member.getMemberId())
                .build();
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
        Member member = Member.builder()
                .memberEmail("hello@naver.com")
                .memberPwd("123123")
                .memberNm("한국")
                .build();
        memberRepository.save(member);

        Profile profile = Profile.builder()
                .memberId(member.getMemberId())
                .build();
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
        Member member = Member.builder()
                .memberEmail("hello@naver.com")
                .memberPwd("123123")
                .memberNm("한국")
                .build();
        memberRepository.save(member);

        Profile profile = Profile.builder()
                .memberId(member.getMemberId())
                .build();
        profileRepository.saveDefault(profile);

        // when
        Optional<Profile> profileOptional = profileRepository.findByMemberId(member.getMemberId());

        // then
        Profile findProfile = profileOptional.orElse(null);
        Assertions.assertThat(findProfile).isNotNull();
        Assertions.assertThat(findProfile.getMemberId()).isEqualTo(member.getMemberId());
    }
}
