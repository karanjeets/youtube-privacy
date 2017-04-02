package edu.usc.csci599.util;

import edu.usc.csci599.fetch.Fetcher;
import edu.usc.csci599.model.Video;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by ksingh on 2/16/17.
 */
public class YoutubePlayer {

    /**
     * Plays a Youtube video
     * @param url
     * @param start
     */
    public static void openVideo(WebDriver driver, String url, String start,int duration) {
        System.out.println("URL: " + url);

        //WebDriver driver = null;
        boolean badRequest = false;
        try {
            //driver = Fetcher.getSeleniumDriverInstance();
            driver.get(url + "&t=" + start);
        }
        catch(Exception e) {
            if(e instanceof TimeoutException) {
                System.out.println("Timeout Exception Raised. Processing whatever loaded so far...");
            }
            else {
                e.printStackTrace();
                badRequest = true;
            }
        }
        finally {
            try {
                if(!badRequest) {
                    // TODO: Add random function
                    Thread.sleep(duration*1000);

                    	likeVideo(driver);

                }
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            /*finally {
                if(driver != null) {
                    try {
                        driver.close();
                        driver.quit();
                    }
                    catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }*/
        }

        System.out.println("Video Completed");
    }


    /**
     * Plays a Youtube video
     * @param video
     */

    public static void playVideo(WebDriver driver, Video video)
    {
    	int duration = video.getTime();
    	String url = video.getUrl();
    	Random r = new Random();
    	System.out.println("Actual Duration of video is  "+ duration);
    	int playTime = r.nextInt(2*duration/5);
    	playTime+= (duration/5);
    	if(playTime>=180)
    	{
    		playTime = 180;
    	}
    	
    	int startTime = r.nextInt(duration/5);
    	startTime += duration/5;
    	System.out.println("Opening video starting from "+ startTime + " and will continue for "+ playTime + " seconds");
    	
    	openVideo(driver, url,Integer.toString(startTime),playTime);
    	
    }
    
    public static void likeVideo(WebDriver driver)
    {
    	List<WebElement> list = driver.findElements(By.className("yt-uix-button"));
    	for(WebElement e :list)
    	{
    		if(e.getAttribute("title").equals("I like this"))
    		{
    			System.out.println("Liked this video");
    			e.click();
    			break;
    		}
    	}
    }
    
    public static List<Video> getRecommendedVideos()
    {
        WebDriver driver = null;
        boolean badRequest = false;
        ArrayList<Video> recommendVideoList = new ArrayList<Video>();
        try {
            driver = Fetcher.getSeleniumDriverInstance();
            driver.get("https://www.youtube.com/feed/recommended");
        }
        catch(Exception e) {
            if(e instanceof TimeoutException) {
                System.out.println("Timeout Exception Raised. Processing whatever loaded so far...");
            }
            else {
                e.printStackTrace();
                badRequest = true;
            }
        }
        finally {
        	
        	//process page here. //feed-item-container
        	List<WebElement> videoList = driver.findElements(By.className("feed-item-dismissable"));
     	
        	for(WebElement video : videoList)
        	{	
        		
        		String url = "";
        		String title = "";
        		String duration = "";
        		
        		try {
        			url = video.findElement(By.className("yt-uix-tile-link")).getAttribute("href");
        			title = video.findElement(By.className("yt-uix-tile-link")).getAttribute("title");
            		duration = video.findElement(By.className("video-time")).getText();	
        		}
        		catch (Exception  e)
        		{
        			e.printStackTrace();
        			continue;
        		}
        		
        		int videoDuration = convertTime(duration);
        		System.out.println(videoDuration);
        		Video element = new Video(videoDuration,title,url);
        		recommendVideoList.add(element);
        	}
        	
            if(driver != null) {
                try {
                    driver.close();
                    driver.quit();
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
        	
        }
        return recommendVideoList;
    }
    
    public static int convertTime(String time)
    {
    	
    	String[] hhmmss = time.split(":");
    	int factor = 1;
    	int total_seconds = 0;
    	for(int i=(hhmmss.length-1);i>=0;i--)
    	{
    		total_seconds += (Integer.parseInt(hhmmss[i])*factor);
    		factor*= 60;
    	}
    	return total_seconds;
    }
    
    public static List<String> getChannels()
    {

        WebDriver driver = null;
        
        ArrayList<String> recommendChannels = new ArrayList<String>();
        try {
            driver = Fetcher.getSeleniumDriverInstance();
            driver.get("https://www.youtube.com");
        }
        catch(Exception e) {
            if(e instanceof TimeoutException) {
                System.out.println("Timeout Exception Raised. Processing whatever loaded so far...");
            }
            else {
                e.printStackTrace();
                //badRequest = true;
            }
        }
        finally {
        	
        	List<WebElement> feedList = driver.findElements(By.className("feed-item-dismissable"));
         	
        	for(WebElement feed : feedList)
        	{	
        		String channeltitle = "";
        		String isRecommended = "";
        			try {
        				channeltitle = feed.findElement(By.className("branded-page-module-title-text")).getText();
        				isRecommended = feed.findElement(By.className("shelf-annotation")).getText();
        			}
        			catch(Exception e)
        			{
        				//e.printStackTrace();
        				continue;
        			}
        			if(isRecommended.contains("Recommended"))
        			{
        				recommendChannels.add(channeltitle);
        				System.out.println(channeltitle);
        			}
        	}
        	
            if(driver != null) {
                try {
                    driver.close();
                    driver.quit();
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
        	
        }
        return recommendChannels;
    
    }
    
    
    public static void main(String[] args) {
    	
    	//getChannels();
    	//openVideo("https://www.youtube.com/watch?v=uQ763VvqiEM");
        /*for(String url: queryAndFetch(query, 30).getUrlContent().keySet()) {
            System.out.println(url);
        }*/
    }
    
    /* To be done
     * Recommendation videos and channels 
     * Duration extract after search query
     * Randomized function to play videos 
     * Add to Playlist
     * 
     * 
     * */
    
    
}
