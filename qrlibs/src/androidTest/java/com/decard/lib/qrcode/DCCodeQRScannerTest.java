package com.decard.lib.qrcode;

import android.content.Context;
import android.content.Intent;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.InstrumentationRegistry;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.decard.NDKMethod.BasicOper;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class DCCodeQRScannerTest {

//    @Rule
//    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Rule
    public ActivityTestRule<QRCodeTestActivity> activityActivityTestRule = new ActivityTestRule<>(QRCodeTestActivity.class,false,false);

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void test(){

        Context appContext = InstrumentationRegistry.getTargetContext();
        Intent intent = new Intent(appContext,QRCodeTestActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        appContext.startActivity(intent);
        activityActivityTestRule.launchActivity(intent);
        CountDownLatch countdown = new CountDownLatch(1);
        try {
            countdown.await(60, TimeUnit.SECONDS);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

}
