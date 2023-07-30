package upbrella.be.login.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import upbrella.be.user.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class OAuthLoginInterceptor implements HandlerInterceptor {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        HttpSession session = request.getSession();

        Long userId = (Long) session.getAttribute("userId");

        // 카카오 인증이 안되어 있는 경우, 세션이 없는 경우
        if (userId == null) {
            return false;
        }

        // 인증된 경우
        if (!userRepository.existsById(userId)) {
            return false;
        }

        return true;
    }
}

