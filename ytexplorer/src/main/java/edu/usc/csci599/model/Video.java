package edu.usc.csci599.model;

public class Video {

	public Video(int duration, String title, String link)
	{
		this.duration = duration;
		this.title = title;
		this.link = link;
	}
	
	public int getTime()
	{
		return this.duration;
	}
	
	public String getUrl()
	{
		return this.link;
	}
	
	private int duration;
	private String title;
	private String link;
}
