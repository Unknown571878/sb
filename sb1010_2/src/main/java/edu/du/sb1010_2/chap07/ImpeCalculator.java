package edu.du.sb1010_2.chap07;

public class ImpeCalculator implements Calculator{
    @Override
    public long factorial(long num) {
//        long start = System.nanoTime();
        long result = 1;
        for(long i = 1; i <= num; i++){
            result *= i;
        }
        long end = System.currentTimeMillis();
//        System.out.println("Factorial of " + num + " took " + (end - start) + " ns");
        return result;
    }
}
