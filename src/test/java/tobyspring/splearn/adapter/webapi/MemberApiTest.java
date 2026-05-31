package tobyspring.splearn.adapter.webapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;
import tobyspring.splearn.adapter.webapi.dto.MemberRegisterResponse;
import tobyspring.splearn.application.member.provided.MemberRegister;
import tobyspring.splearn.application.member.required.MemberRepository;
import tobyspring.splearn.domain.MemberFixture;
import tobyspring.splearn.domain.member.Member;
import tobyspring.splearn.domain.MemberRegisterRequest;

import java.io.UnsupportedEncodingException;

import static org.assertj.core.api.Assertions.assertThat;
import static tobyspring.splearn.AssertUtils.equalsTo;
import static tobyspring.splearn.AssertUtils.notnull;

// 데이터베이스까지 갔다가 돌아오는 테스트를 함
// webmvc테스트보다 이걸 추천함. webmvc테스트는 모킹을 하기때문에 정확하지 않음
// API 호출되고 서버 상태가 어떻게 바뀌었나도 테스트 해야함
@SpringBootTest
@AutoConfigureMockMvc
@Transactional // 테스트가 끝나면 롤백
public class MemberApiTest {
    @Autowired
    MockMvcTester mvcTester;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberRegister memberRegister;



    @Test
    void register() throws JsonProcessingException, UnsupportedEncodingException {
        MemberRegisterRequest request = MemberFixture.createMemberRegisterRequest();
        String requestJson = objectMapper.writeValueAsString(request);

        MvcTestResult result = mvcTester.post().uri("/api/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson).exchange();

        assertThat(result)
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.memberId", notnull())
                .hasPathSatisfying("$.email", equalsTo(request));

        MemberRegisterResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), MemberRegisterResponse.class);
        Member member = memberRepository.findById(response.memberId()).orElseThrow();

        assertThat(member.getEmail().address()).isEqualTo(request.email());
        assertThat(member.getNickname()).isEqualTo(request.nickname());

    }

    @Test
    void registerFail() throws JsonProcessingException {
        ;
        memberRegister.register(MemberFixture.createMemberRegisterRequest());

        MemberRegisterRequest request = MemberFixture.createMemberRegisterRequest();
        String requestJson = objectMapper.writeValueAsString(request);

        MvcTestResult result = mvcTester.post().uri("/api/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson).exchange();

        assertThat(result)
                .apply(MockMvcResultHandlers.print())
                .hasStatus(HttpStatus.CONFLICT);

    }


}
