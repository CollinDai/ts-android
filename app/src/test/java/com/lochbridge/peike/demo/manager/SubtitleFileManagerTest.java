package com.lochbridge.peike.demo.manager;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.lochbridge.peike.demo.model.SRTItem;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.powermock.core.PowerMockUtils;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by Peike on 12/11/2015.
 */

@RunWith(MockitoJUnitRunner.class)
public class SubtitleFileManagerTest {

    @Mock
    Context mMockContext;
    FileInputStream fis;
    FileOutputStream fos;
    String pwd;

    @Before
    public void setUp() throws Exception {



    }

    @Test
    public void testGetSubtitle() throws Exception {

        String srtContent = SubtitleFileManager.getSubtitle(mMockContext, 12345);
        assertThat(srtContent, is(notNullValue()));
    }

    @Test
    public void testLongSrt() throws Exception {
        fis = new FileInputStream(new File("src/test/assets/test_long.srt"));
        when(mMockContext.openFileInput(anyString()))
                .thenReturn(fis);
        List<SRTItem> result = SubtitleFileManager.getSRTItem(mMockContext, 12345);
        System.out.println(result.get(0).text);
        System.out.println(result.get(0).number);
        assertThat(result.size(),equalTo(2015));
    }

    @Test
    public void testNormalSrt() throws Exception {
        fis = new FileInputStream(new File("src/test/assets/test_normal.srt"));
        when(mMockContext.openFileInput(anyString()))
                .thenReturn(fis);
        List<SRTItem> result = SubtitleFileManager.getSRTItem(mMockContext, 12345);
        System.out.println(result.get(0).text);
        System.out.println(result.get(0).number);
        assertThat(result.size(), equalTo(1274));
    }

    @Test
    public void testWriteSrtWithoutBOM() throws Exception {
        fos = new FileOutputStream(new File("src/test/assets/test_output.srt"));
        when(mMockContext.openFileOutput(anyString(), anyInt()))
                .thenReturn(fos);
        String content = "\uFEFF123445";
        SubtitleFileManager.putSubtitle(mMockContext, 12345, content);
    }
}