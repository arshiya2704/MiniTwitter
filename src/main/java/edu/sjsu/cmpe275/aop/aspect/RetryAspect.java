package edu.sjsu.cmpe275.aop.aspect;

import java.io.IOException;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.aspectj.lang.annotation.Around;



@Aspect
@Order(1)
public class RetryAspect {
    /***
     * Following is a dummy implementation of this aspect.
     * You are expected to provide an actual implementation based on the requirements, including adding/removing advices as needed.
     */

	@Around("execution(public void edu.sjsu.cmpe275.aop.TweetService.*(..))")
	public void networkFailAdvice(ProceedingJoinPoint jp) throws Throwable{
		System.out.printf("In Around advice for the function: %s ", jp.getSignature().getName());
		int attempts=0;
		IOException networkFailureException=null;
		do{
			attempts++;
			try{
				jp.proceed();
				System.out.println("In 'try' block of retry aspect attempts " +attempts);
				break;
			}
			catch(IOException ex){
				System.out.println("In 'catch' block of retry aspect attempts " +attempts);
				networkFailureException= ex;
				if (attempts ==4){
					System.out.println("explicit throw");
					throw networkFailureException;
				}
			}

		}while(attempts <= 3);
	}
}



