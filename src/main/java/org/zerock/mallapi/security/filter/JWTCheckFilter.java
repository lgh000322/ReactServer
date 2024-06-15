package org.zerock.mallapi.security.filter;

import com.google.gson.Gson;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;
import org.zerock.mallapi.dto.MemberDto;
import org.zerock.mallapi.util.JWTUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@Slf4j
public class JWTCheckFilter extends OncePerRequestFilter {
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        //false => 체크 o true => 체크 x
        String path = request.getRequestURI();
        log.info("check uri------------------{}", path);

        if (path.startsWith("/api/member")) {
            return true;
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        log.info("===============================");

        log.info("===============================");

        log.info("===============================");

        String authenticationStr = request.getHeader("Authorization");

        try {
            String accessToken = authenticationStr.substring(7);
            Map<String, Object> claims = JWTUtil.validateToken(accessToken);

            log.info("claims={}", claims);
            /*filterChain.doFilter(request, response);*/

            String email = (String) claims.get("email");
            String pw = (String) claims.get("pw");
            String nickname = (String) claims.get("nickname");
            Boolean social = (Boolean) claims.get("social");
            List<String> roleNames = (List<String>) claims.get("roleNames");

            MemberDto memberDto = new MemberDto(email, pw, nickname, social.booleanValue(), roleNames);

            log.info("===================================");
            log.info(memberDto.toString());
            log.info("memberDto의 author={}", memberDto.getAuthorities());

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                    = new UsernamePasswordAuthenticationToken(memberDto, pw, memberDto.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.info("JWT Check Error....");
            log.error(e.getMessage());

            Gson gson = new Gson();
            String msg = gson.toJson(Map.of("error", "ERROR_ACCESS_TOKEN"));

            response.setContentType("application/json; charset=UTF-8");
            PrintWriter printWriter = response.getWriter();
            printWriter.println(msg);
            printWriter.close();

        }
    }
}
