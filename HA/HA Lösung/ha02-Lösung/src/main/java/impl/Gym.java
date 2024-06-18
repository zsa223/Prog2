package impl;

import util.AbstractWeight;

public class Gym {

    /**
     * DO NOT CHANGE THE SIGNATURE OF THIS METHOD!
     * <p>
     * Sets up the gym with the specified number of athletes and prepares them for a specified number of training cycles.
     * If the specified number of athletes is smaller than 1, one athlete and two weights are created.
     * Otherwise, the number of weights is equal to the number of athletes. A weight is always shared by two athletes:
     * for the first athlete, it's the left-hand weight, for the second athlete, it's the right-hand weight.
     *
     * @param numAthletes Number of athletes to be created.
     * @param cycles      Number of training cycles, i.e. how many times each athlete should perform the training sequence.
     * @return Array of prepared athletes.
     * <p>
     * * 지정된 수의 선수로 체육관을 설정하고 지정된 수의 훈련 주기에 대비합니다.
     * * 지정 선수 수가 1명보다 작을 경우 선수 1명, 체급 2개가 생성됩니다.
     * * 그렇지 않은 경우, 중량의 수는 선수의 수와 동일합니다. 체중은 항상 두 선수가 공유합니다.
     * * 첫 번째 선수는 왼쪽 무게추, 두 번째 선수는 오른쪽 무게추입니다.
     * * @param numAthletes 생성할 선수 수입니다.
     * * @param Cycles 훈련 주기 수, 즉 각 선수가 훈련 순서를 몇 번 수행해야 하는지.
     * * @return 선수 배열이 준비되었습니다.
     */

    public static Athlete[] setup(int numAthletes, int cycles) {
        int athletesAmount;

        // 운동선수의 수가 1보다 작으면 1로 설정해야해
        if (numAthletes < 1  ){
            athletesAmount = 1;

        // 아니면 그냥 그 수겠지
        }else {
            athletesAmount = numAthletes;
        }

        // 훈련주기 수 설정
        int cyclesAmount;

        // 훈련 주기가 1보다 작을경우 다시 1로 설정
        if (cycles < 1) {
            cyclesAmount = 1;

        // 아니면 맞는거고
        }else {
            cyclesAmount = cycles;
        }
        // 운동선수 객체를 저장할 배열을 만들어줘야지.
        Athlete[] athletes = new Athlete[athletesAmount];

        // 첫번째 덤밸과 두번째 덤밸(좌,우)를 생성하고
        Weight leftWeight = new Weight(0);
        Weight rightWeight = new Weight(1);

        // 첫번째 운동선수 객체를 생성하여 배열로 저장한다.
        athletes[0] = new Athlete(0, cyclesAmount, leftWeight, rightWeight);

        // 첫번째 덤밸을 firstWeight로 생성하고, 왼쪽으로 설정
        Weight firstWeight = leftWeight;
        // 또한 우측도 들어야지
        leftWeight = rightWeight;

        // 운동선수가 한명일 경우 반환
        if (athletesAmount == 1) {
            return athletes;
        }

        // 운동선수 및 덤벨 설정
        // 운동선수가 두명 이상일 경우, 반복문으로, 생성과 설정.
        for (int i = 1; i < athletesAmount - 1; i++) {
            // 새로운 덤벨을 생성 및 설정
            Weight newWeight = new Weight(i + 1);
            athletes[i] = new Athlete(i, cyclesAmount, leftWeight, newWeight);
            // 두개 다 들게 만들어주고
            leftWeight = newWeight;
        }

        // 마지막 운동선수 객체를 생성하여 배열에 저장.
        // 마지막 운동선수의 오른쪽 덤벨은 첫번째 덤벨이다.
        // 우리는 원으로 하기로했으니
        athletes[athletes.length - 1] = new Athlete(athletes.length - 1, cyclesAmount, leftWeight, firstWeight);

        return athletes;
    }
}