package org.zerock.mallapi.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.mallapi.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {

    @EntityGraph(attributePaths = "memberRoleList")
    @Query("select m from Member m where m.email = :email")
    Optional<Member> getWithRoles(@Param("email") String email);
}
