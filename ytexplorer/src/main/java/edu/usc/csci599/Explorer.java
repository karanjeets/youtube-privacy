package edu.usc.csci599;

import java.util.HashSet;
import java.util.Set;

import edu.usc.csci599.model.Query;
import edu.usc.csci599.model.QueryResult;
import edu.usc.csci599.model.Video;
import edu.usc.csci599.util.Constants;
import edu.usc.csci599.util.FileUtil;
import edu.usc.csci599.util.SearchUtil;
import edu.usc.csci599.util.YoutubePlayer;

import java.io.IOException;
/**
 * Created by ksingh on 2/16/17.
 */
public class Explorer {

    // Initialize Variables & Configuration
    private void init() {
        System.setProperty("webdriver.firefox.bin", Constants.FFX_BIN_PATH);
    }

    public static void main(String[] args) throws IOException{

    	// WE will explore everything.
    	
    	Set<String> setA = new HashSet<String>();
    	Set<String> setB = new HashSet<String>();
    	
    	setA = FileUtil.readLinesInSet(Explorer.class.getClassLoader().getResourceAsStream("data/a-search-queries.txt"));
    	//setB = FileUtil.readLinesInSet("b-search-queries.txt");
    	
    	for(String element : setA)
    	{
    		Query query = new Query(element);
    		QueryResult response = SearchUtil.queryAndFetch(query, 2);
    		for(Video video : response.getVideos())
    		{
    			YoutubePlayer.playVideo(video);
    		}
    	}
    	
    }
}
