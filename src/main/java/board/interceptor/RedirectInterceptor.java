package board.interceptor;

import board.common.UserSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Slf4j
public class RedirectInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean returnVal = Optional.ofNullable(request)
                                    .map(o -> o.getSession(false))
                                    .map(o -> o.getAttribute("userSession"))
                                    .filter(o -> o instanceof UserSession)
                                    .isPresent();
        if(!returnVal)
            response.sendRedirect(request.getContextPath() + "/index.do");
        return returnVal;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request,response,handler,modelAndView);
    }
}