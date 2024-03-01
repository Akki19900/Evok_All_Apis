package com.npst.evok.api.evok_apis.controller;

public class Test {

    public static void main(String[] args) {
        System.out.println("Going to debug our simple java application");
        int[] arr = {34, 3, 4, 3, 2, 3};
        int sum = getSum(arr);
        System.out.println(sum);

    }

    public static int getSum(int[] array) {

        int no = 0;
        for (int j : array) {
            no = no + j;
        }
        no = no + 45;
        return no;
    }
}
