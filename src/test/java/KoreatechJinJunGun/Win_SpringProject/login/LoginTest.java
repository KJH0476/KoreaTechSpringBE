package KoreatechJinJunGun.Win_SpringProject.login;

import KoreatechJinJunGun.Win_SpringProject.member.entity.Member;
import KoreatechJinJunGun.Win_SpringProject.member.entity.Role;
import KoreatechJinJunGun.Win_SpringProject.member.entity.Status;
import KoreatechJinJunGun.Win_SpringProject.member.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@SpringBootTest
@AutoConfigureMockMvc
public class LoginTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    MemberRepository memberRepository;
    ObjectMapper objectMapper = new ObjectMapper();

    @Value("${TEST_LOGIN_EMAIL}") String email;
    @Value("${TEST_LOGIN_PASSWORD}") String password;
    @Value("${TEST_LOGIN_WRONG_PASSWORD}") String wrong_password;

    @Test
    @DisplayName("로그인 성공 테스트")
    void 로그인_성공() throws Exception {
        //given
        Map<String, String> map = new HashMap<>();
        map.put("email", email);
        map.put("password", password);

        //when
        mvc.perform(MockMvcRequestBuilders.post("/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(map)))
                //then
                .andExpect(MockMvcResultMatchers.status().isOk())   //http 상태 200
                //성공 응답 바디 메시지 확인
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(Map.of("message", "success login"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").exists());
    }

    @Test
    @DisplayName("로그인 이메일 or 비밀번호 틀렸을 경우 테스트")
    void 로그인_이메일비밀번호오류() throws Exception {
        //given
        Map<String, String> map = new HashMap<>();
        map.put("email", email);
        map.put("password", wrong_password);

        //when
        mvc.perform(MockMvcRequestBuilders.post("/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(map)))
                //then
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())   //http 상태 401
                .andExpect(MockMvcResultMatchers.header().string("Error-Message", "UNAUTHORIZED"))  //헤더 사용자 지정 오류 메세지 확인
                //응답 바디 메시지 확인
                .andExpect(MockMvcResultMatchers.content().string("UNAUTHORIZED USER"));
    }

    @Test
    @DisplayName("로그인 이메일 or 비밀번호 null일 경우 테스트")
    void 로그인_null() throws Exception {
        //given
        Map<String, String> map = new HashMap<>();
        map.put("email", null);
        map.put("password", password);

        //when
        mvc.perform(MockMvcRequestBuilders.post("/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(map)))
                //then
                .andExpect(MockMvcResultMatchers.status().isBadRequest())   //http 상태 400
                //응답 바디 메시지 확인
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(Map.of("message", "email 입력해주세요."))));
    }

    @BeforeEach
    void setUp() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(password);

        Member testMember = Member.builder()
                .email(email)
                .password(encodedPassword) // 암호화된 비밀번호 저장
                .username("testUser")
                .nickname("testNick")
                .createdate(new Date())
                .birth("2000-01-01")
                .role(Role.USER)
                .status(Status.ONLINE)
                .build();
        memberRepository.save(testMember);
    }

    @AfterEach
    void afterexecute(){
        memberRepository.findByEmail(email)
                .ifPresent(member -> memberRepository.delete(member));
    }
}
