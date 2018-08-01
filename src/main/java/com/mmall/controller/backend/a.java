package com.mmall.controller.backend;

/**
 * Created by 12996 on 2018/7/31.
 */
public class a {
    public static void getAns(int n) {
        int min = 1;
        int sum = 1;
        int max = 1;
        while(min <= n/2+1) {
            if(sum == n) {
                for(int k = min; k<=max; k++) {
                    System.out.println(k+"");
                }
                System.out.println();
                sum = sum - min;
                min ++;
                max ++;
                sum = sum+max;
            }
            if(sum > n) {
                sum = sum - min;
                min ++;
            } else {
                max ++;
                sum = sum + max;
            }
        }
    }
    public static void main(String[] args) {
        getAns(15);
    }

}
