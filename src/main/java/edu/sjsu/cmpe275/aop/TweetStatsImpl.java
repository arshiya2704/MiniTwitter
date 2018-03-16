package edu.sjsu.cmpe275.aop;
import java.util.Map;
import java.util.Set;

import edu.sjsu.cmpe275.aop.aspect.StatsAspect;
import org.springframework.beans.factory.annotation.Autowired;

public class TweetStatsImpl implements TweetStats {
    /***
     * Following is a dummy implementation.
     * You are expected to provide an actual implementation based on the requirements.
     */

	@Autowired
	private StatsAspect statsObj;

	public void resetStatsAndSystem() {
		// TODO Auto-generated method stub
		statsObj.setLongestTweetAttempted(0);
		statsObj.followerUserMap.clear();
		statsObj.productiveUserMap.clear();
		statsObj.blockedUserMap.clear();

	}

	public int getLengthOfLongestTweetAttempted() {
		// TODO Auto-generated method stub
		return statsObj.getLongestTweetAttempted();
	}

	public String getMostFollowedUser() {
		// TODO Auto-generated method stub
		int maxFollowedUser = 0;
		String user = null;
		/* If size of map is 0 then return null as no user is there in map */
		if (statsObj.followerUserMap.size() != 0)
		{
			for (Map.Entry<String, Set<String>> entry : statsObj.followerUserMap.entrySet())
			{
				String currUser = entry.getKey();
				int currSize = entry.getValue().size();
				if (currSize > maxFollowedUser)
				{
					user = currUser;
					maxFollowedUser = currSize;
				}
			}
			return user;
		}
		return null;
	}

	public String getMostProductiveUser() {
		// TODO Auto-generated method stub
		int maxProductiveUser = 0;
		String user = null;
		if (statsObj.productiveUserMap.size() != 0)
		{
			for (Map.Entry<String, Integer> entry : statsObj.productiveUserMap.entrySet())
			{
				String currUser = entry.getKey();
				int currSize = entry.getValue();
				if (currSize > maxProductiveUser)
				{
					user = currUser;
					maxProductiveUser = currSize;
				}
			}
			return user;
		}
		return null;
	}

	public String getMostBlockedFollower() {
		// TODO Auto-generated method stub
		int maxBlockedUser=0;
		String user = null;
		if(statsObj.blockedUserMap.size() !=0)
		{
			for (Map.Entry<String, Set<String>> entry : statsObj.blockedUserMap.entrySet())
			{
				String currUser = entry.getKey();
				int currSize = entry.getValue().size();
				if (currSize > maxBlockedUser)
				{
					user = currUser;
					maxBlockedUser = currSize;
				}
			}
			return user;
		}
		return null;
	}

	public void addTweetDetails(String user, int length){
		Integer exist_tweet_len = statsObj.productiveUserMap.get(user);
		if(exist_tweet_len== null)
		{
			exist_tweet_len=0;
		}
		else{
			exist_tweet_len = exist_tweet_len;
		}
		exist_tweet_len = exist_tweet_len+length;
		statsObj.productiveUserMap.put(user, exist_tweet_len);
		System.out.println("p_user_map"+statsObj.productiveUserMap);
	}

}




