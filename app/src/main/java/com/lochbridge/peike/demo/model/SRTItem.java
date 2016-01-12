package com.lochbridge.peike.demo.model;

/**
 * Created by Peike on 12/6/2015.
 */
public class SRTItem {
    public SRTItem next;
    public SRTItem previous;

    public int number;
    public int startTimeMilli;
    public int endTimeMilli;
    public String text;
}
