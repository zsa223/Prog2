package util;

import impl.Athlete;
import impl.Gym;

public class Main {
    public static void main(String[] args) {
        Athlete[] athletes = Gym.setup(20, 3);

        if(athletes == null || athletes.length == 0) {
            System.out.println("The gym is empty :(");
            return;
        }

        for(Athlete a : athletes) {
            a.start();
        }
        for(Athlete a : athletes) {
            try {
                a.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("All Athletes are done.");
    }
}
