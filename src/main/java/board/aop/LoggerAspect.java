package board.aop;

import board.common.UserSession;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Component
@Aspect
public class LoggerAspect {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Around("execution(* board..controller.*Controller.*(..)) || execution(* board..service.*Impl.*(..)) || execution(* board..mapper.*Mapper.*(..))")
    public Object logPrint(ProceedingJoinPoint joinPoint) throws Throwable {
        String type = "";
        String name = joinPoint.getSignature().getDeclaringTypeName();
        if(name.indexOf("Controller") > -1) {
            type = "Controller  \t:  ";
        } else if(name.indexOf("Service") > -1) {
            type = "ServiceImpl  \t:  ";
        } else if(name.indexOf("Mapper") > -1) {
            type = "Mapper  \t\t:  ";
        }
        log.debug(type + name + "." + joinPoint.getSignature().getName() + "()");
        for(Object o : joinPoint.getArgs()) {
            if(o instanceof HttpServletRequest) {

            }
        }
        return joinPoint.proceed();
    }

    /*      Controller에 있는 메서드의 정상적인 수행 여부와 상관없이 무조건 session 로그를 찍어주는 메서드를 만든다.
            userSession이 있을 경우 "sessionId : sessionId값, userSession : UserSession 객체의 내용" 프린트
            userSession이 없을 경우 "sessionId : sessionId값, userSession : no userSession" 프린트
         */
    @After("execution(* board..controller.*Controller.*(..))")
    public void sessionLogPrint(JoinPoint joinPoint) throws Throwable {
        HttpServletRequest req = null;
        StringBuilder logPrint = new StringBuilder();

        for(Object o : joinPoint.getArgs()) {
            if(o instanceof HttpServletRequest) {
                req = (HttpServletRequest) o;
                logPrint.append("sessionId : ")
                        .append(req.getRequestedSessionId())
                        .append(", userSession : ");
            }
        }

        Optional.ofNullable(req)
                .map(o -> o.getSession().getAttribute("userSession"))
                .filter(o -> o instanceof UserSession)
                .map(o -> (UserSession) o)
                .ifPresentOrElse(user -> log.debug(String.valueOf(logPrint.append(user))),
                        () -> log.debug(String.valueOf(logPrint.append("no userSession"))));
    }
}