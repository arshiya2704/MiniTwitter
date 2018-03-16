package edu.sjsu.cmpe275.aop.aspect;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import edu.sjsu.cmpe275.aop.TweetStatsImpl;

@Aspect
@Order(0)
public class StatsAspect {
    /***
     * Following is a dummy implementation of this aspect.
     * You are expected to provide an actual implementation based on the requirements, including adding/removing advices as needed.
     */

	private HashMap<String, Integer> UserTweetStore=new HashMap<String, Integer>();

	public static TreeMap<String, Integer> productiveUserMap = new TreeMap<String, Integer>();

	public static TreeMap<String, Set<String>> followerUserMap = new TreeMap<String, Set<String>>();

	public static TreeMap<String, Set<String>> blockedUserMap = new TreeMap<String, Set<String>>();

	private int longestTweetAttempted=0;
	private String mostProductiveUser;
	private String mostFollowedUser;
	private String mostBlockedUser;

	public int getLongestTweetAttempted() {
		return longestTweetAttempted;
	}

	public void setLongestTweetAttempted(int longestTweetAttempted) {
		this.longestTweetAttempted = longestTweetAttempted;
	}

	public String getMostFollowedUser() {
		return mostFollowedUser;
	}

	public void setMostFollowedUser(String mostFollowedUser) {
		this.mostFollowedUser = mostFollowedUser;
	}

	public String getMostProductiveUser() {
		return mostProductiveUser;
	}

	public void setMostProductiveUser(String mostProductiveUser) {
		this.mostProductiveUser = mostProductiveUser;
	}

	public String getMostBlockedUser() {
		return mostBlockedUser;
	}


	public void setMostBlockedUser(String mostBlockedUser) {
		this.mostBlockedUser = mostBlockedUser;
	}


	@Autowired TweetStatsImpl stats;

	@Before("execution(public void edu.sjsu.cmpe275.aop.TweetService.tweet(..)) && args(user, tweet)")
	public void tweetAdvice(JoinPoint joinPoint, String user, String tweet)throws IllegalArgumentException{
		System.out.println("Before tweet call");
		Integer lengthOfTweet=tweet.length();
		System.out.printf("The user message is:  %s",tweet);
		System.out.println("");
		System.out.printf("The length of user message is %d", lengthOfTweet );
		System.out.println("");

		if(lengthOfTweet>this.getLongestTweetAttempted()){
			this.setLongestTweetAttempted(lengthOfTweet);
		}
		if(lengthOfTweet<=140){
			System.out.printf("The tweet is inserted");
			System.out.println("");
			if(!UserTweetStore.containsKey(user)){
				UserTweetStore.put(user,lengthOfTweet);
			}
			else{

				int temp=UserTweetStore.get(user).intValue();
				temp+=lengthOfTweet;
				UserTweetStore.put(user, temp);
			}
		}
		else{
			System.out.printf("The Tweet: %s is not inserted as it's length is greater than 140: ",tweet);
			System.out.println("");
			throw new IllegalArgumentException("Message Length Exceeded");
		}
	}

	@AfterReturning("execution(public void edu.sjsu.cmpe275.aop.TweetService.tweet(..)) && args(user, tweet)")
	public void dummyAfterAdvice(JoinPoint joinPoint,String user, String tweet) {
		System.out.printf("After the execution of the method %s\n", joinPoint.getSignature().getName());
		System.out.println("");
		for (Object o : joinPoint.getArgs()){
			System.out.println((String)o);
		}
		stats.addTweetDetails((String)joinPoint.getArgs()[0], ((String)joinPoint.getArgs()[1]).length());
	}

	@AfterReturning("execution(public void edu.sjsu.cmpe275.aop.TweetService.follow(..)) && args(follower, followee)")
	public void followAdvice1(JoinPoint joinPoint, String follower, String followee){
		System.out.printf("After the execution of the method %s\n", joinPoint.getSignature().getName());
		System.out.println("");
		Set<String> temp= followerUserMap.get(followee);
		if(temp == null){
			temp= new HashSet<String>();
			temp.add(follower);
			followerUserMap.put(followee, temp);
			System.out.println(temp);
		}else{
			temp.add(follower);
		}
	}

	@AfterReturning("execution(public void edu.sjsu.cmpe275.aop.TweetService.block(..)) && args(user, followee)")
	public void followAdvice2(JoinPoint joinPoint, String user, String followee){
		System.out.printf("After the execution of the method %s\n", joinPoint.getSignature().getName());
		System.out.println("");
		Set<String> temp= blockedUserMap.get(followee);
		if(temp == null){
			temp= new HashSet<String>();
			temp.add(user);
			blockedUserMap.put(followee, temp);
			System.out.println(temp);
		}else{
			temp.add(user);
		}
	}
}
