package KoreatechJinJunGun.Win_SpringProject.member.repository;

import KoreatechJinJunGun.Win_SpringProject.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByUsername(String string);
}
