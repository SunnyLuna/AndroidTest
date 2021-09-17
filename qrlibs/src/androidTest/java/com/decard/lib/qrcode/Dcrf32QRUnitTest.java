package com.decard.lib.qrcode;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.decard.NDKMethod.BasicOper;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class Dcrf32QRUnitTest {


    @Test
    public void qrTest(){

        int ret = BasicOper.dc_open("COM",null,"/dev/ttyHSL1",115200);
        System.out.println("ret = "+ret);
        assertTrue(ret>0);

        String result = BasicOper.dc_Scan2DBarcodeStart(0);

        assertTrue(result.startsWith("0000"));

        long startTime = System.currentTimeMillis();

        do {

            result = BasicOper.dc_Scan2DBarcodeGetData();

            if(System.currentTimeMillis() - startTime >60*1000){
                break;
            }

            System.out.println("###########正在扫描###########");

        }while (!result.startsWith("0000"));
//        }while (true);

        BasicOper.dc_Scan2DBarcodeExit();
        BasicOper.dc_exit();
        assertTrue(result.startsWith("0000"));
        System.out.println("扫描成功 ： "+result);

    }

}
