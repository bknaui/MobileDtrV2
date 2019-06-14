package com.dohro7.mobiledtrv2;

import android.util.Log;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, (3%5)+1);
    }



    @Test
    public void josephus() {



        int[] data = new int[]{1, 2, 3, 4, 5};
        int counter = 3;

        int deleted_count = 0;
        int iterator = 0;
        int index = 0;

         // iterator = 0 ;index = 3 ; output = 1 2 0 4 5 ,


        while (data.length - deleted_count != 2) {

            while (iterator != counter) {
                if (data[index] == 0) {
                    index = (index % data.length) + 1;
                    continue;
                }
                index++;
                iterator++;
            }

            data[index-1] = 0;
            iterator = 0;
            deleted_count++;
        }



        String output = "";
        for (int a : data) {
            output += output + " " + a;
        }

        assertEquals(4, output);

    }
}