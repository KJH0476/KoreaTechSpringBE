package KoreatechJinJunGun.Win_SpringProject.filter;

import KoreatechJinJunGun.Win_SpringProject.service.login.JwtTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

    private final JwtTokenService jwtTokenService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        //요청 헤더의 토큰 값을 가져온다.
        String jwt = getRequestAuthToken(httpServletRequest);
        String requestURI = httpServletRequest.getRequestURI();

        /**
         * 요청헤더의 토큰이 존재하고, 토큰의 유효성이 검증되면 권한 정보를 SpringContextHolder에 저장한다.
         * 모든 요청에 대해 JWT 토큰을 분석하고 인증 상태를 설정하는 역할, 주로 API 요청에서 사용자의 인증 상태를 관리하는 데 사용 (LoginService에서 인증 정보 저장하는 것과 다른 역할)
         * RESTful API와 같은 Stateless 환경에서는 각 요청이 독립적으로 처리되어야 하므로, 모든 요청에서 인증 정보를 검증하고 SecurityContext에 저장해야 함
         */
        if (StringUtils.hasText(jwt) && jwtTokenService.validateToken(jwt)) {
            Authentication authentication = jwtTokenService.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
        } else {
            log.info("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
        }

        //다음 필터를 실행해준다.
        filterChain.doFilter(servletRequest, servletResponse);
    }
    // Request Header 에서 토큰 정보를 꺼내오기 위한 메소드
    private String getRequestAuthToken(HttpServletRequest request) {
        //요청 헤더 'Authorization' 값
        String bearerToken = request.getHeader("Authorization");

        //bearerToken 이 존재하고, Bearer 로 시작하면, 토큰 값만 반환한다.
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }

}
