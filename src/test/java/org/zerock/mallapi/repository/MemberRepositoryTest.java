package org.zerock.mallapi.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.mallapi.domain.Member;
import org.zerock.mallapi.domain.MemberRole;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
@Transactional(readOnly = true)
@Commit
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    @Test
    public void testInsertMember() throws Exception{

        for (int i = 0; i < 10; i++) {
            Member member = Member.builder()
                    .email("user" + i + "@aaa.com")
                    .pw(passwordEncoder.encode("1111"))
                    .nickname("USER" + i)
                    .build();

            member.addRole(MemberRole.USER);

            if (i > 5) {
                member.addRole(MemberRole.MANAGER);
            }


            if (i >= 8) {
                member.addRole(MemberRole.ADMIN);
            }

            memberRepository.save(member);
        }
    }


    @Test
    public void testRead() throws Exception {
        String email = "user9@aaa.com";

        Optional<Member> result = memberRepository.getWithRoles(email);

        Member member = result.orElseThrow();

        log.info("==================================");
        log.info(member.toString());
        log.info(member.getMemberRoleList().toString());

    }

}