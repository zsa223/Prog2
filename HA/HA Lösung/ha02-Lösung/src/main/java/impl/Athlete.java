package impl;

import util.AbstractAthlete;
import util.AbstractWeight;
import util.GymMetrics;

/**
 * DO NOT CHANGE THE SIGNATURE OF THIS CLASS!
 * <p>
 * Represents an athlete in our gym. Extends AbstractAthlete, which in turn extends Thread.
 * * 체육관의 운동선수를 나타냅니다. AbstractAthlete를 확장하고, 이는 다시 Thread를 확장합니다.
 * An athlete performs a number of training cycles. In each cycle, the following sequence is performed:
 * 운동선수는 여러 훈련 주기를 수행합니다. 각 사이클에서 다음과 같은 순서가 수행됩니다:
 * <p>
 * - the athlete stretches,
 * - picks up both weights
 * - exercises,
 * - and puts down both weights.
 * - 선수가 스트레칭을 합니다,
 * * - 두 웨이트를 모두 들어 올립니다.
 * * - 운동,
 * * - 두 웨이트를 모두 내려놓습니다.
 */
public class Athlete extends AbstractAthlete {

    /**
     * DO NOT CHANGE THE SIGNATURE OF THE CONSTRUCTOR!
     *
     * @param id          The identifier of this athlete. Must be unique among all athletes.
     * @param cycles      Specifies how many times the athlete should perform the training sequence.
     * @param leftWeight  The weight to the left of the athlete.
     * @param rightWeight The weight to the right of the athlete.
     */
    public Athlete(int id, int cycles, AbstractWeight leftWeight, AbstractWeight rightWeight) {
        super(id, cycles, leftWeight, rightWeight);
    }

    /**
     * Implements the athlete's training cycle. The following sequence is performed @cycle times:
     * 1. stretch - call super.stretch()
     * 2. pick up both weights - call pickUp() on both
     * 3. exercise - call super.exercise()
     * 4. put down both weights - call putDown() on both
     * * 선수의 훈련 사이클을 구현합니다. 사이클 시간마다 다음 순서가 수행됩니다:
     * * 1. 스트레칭 - super.stretch() 호출
     * * 2. 두 웨이트를 모두 들어 올리기 - 양쪽 모두에서 pickUp()을 호출합니다.
     * * 3. 운동 - super.exercise() 호출
     * * 4. 두 웨이트를 모두 내려놓기 - 둘 다 putDown()을 호출합니다.
     * <p>
     * Ensure the following:
     * - no starvation: every athlete gets perform their full training cycle
     * - no deadlocks: the program always completes successfully
     * - no concurrent access: a weight is only ever held by up to one athlete at any given time
     * 다음 사항을 확인하세요:
     * * - 굶주림 없음: 모든 선수가 전체 훈련 주기를 수행합니다.
     * * - 교착 상태 없음: 프로그램이 항상 성공적으로 완료됩니다.
     * * 동시 접속 금지: 한 번에 최대 한 명의 선수만 웨이트를 들 수 있습니다.
     * <p>
     * For maximum points:
     * - on step 2 of the sequence, the athlete calls pickUp() exactly once on each weight
     * - on step 4 of the sequence, the athlete calls putDown() exactly once on each weight
     * * 최대 점수를 얻으려면:
     * * - 시퀀스의 2단계에서 선수는 각 웨이트에서 정확히 한 번씩 pickUp()을 호출합니다.
     * * - 시퀀스의 4단계에서 선수는 각 웨이트에서 정확히 한 번씩 putDown()을 호출합니다.
     */
    @Override
    public void run() {
        // 지정된 횟수만큼 'cycles'만큼 반복.
        for (int i = 0; i < cycles; i++) {
//            System.out.println("사이클 : " + (i+1));

            // 사용가능 여부 확인 : 왼쪽 덤벨이 사용가능한지 확인.
            if (leftWeight.isAvailable()) {
                // 트라이캐치는 acquire() 메서드를 사용하면 자동으로 감싸짐
                // 먼저 없이 써보고 그 후를 공략해보자.
                try {

                    // 형변환 : leftWeight와 rightWeigt는 AbsatractWeigt 타입이다.
                    // AbstractWeigt 클래스에는 semaphore 필드가 없기 때문에 Weight 클래스의
                    // semaphore 필드에 접근하려면 AbstractWeigt타입의 객체를 Weight 타입으로 형변환 해야한다.
                    ((Weight) leftWeight).semaphore.acquire();
//                    System.out.println("왼쪽 허가 : " + getAthleteId());

                }
                catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try {
                    // Wie oben.
                    ((Weight) rightWeight).semaphore.acquire();
//                    System.out.println("우측 허가 : " + getAthleteId());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                // 그리고 이제 문제에서 스트레칭 하라고 했음. 부모클래스인 AbstractAthlete에 구현되어있고
                // 선수가 운동을 수행한다. 최대 100ms 동안 멈추게 된다.
                stretch();
//                System.out.println("스트레칭 성공");

                // 왼/오른쪽 덤벨을 집고
                leftWeight.pickUp(this);
//                System.out.println("왼쪽 잡기 성공");
                rightWeight.pickUp(this);
//                System.out.println("우측 잡기 성공");

                //운동하고
                exercise();
//                System.out.println("운동 완료 ");
                //왼/오 덤벨 내려놓고
                leftWeight.putDown(this);
                rightWeight.putDown(this);

                // 세마포어 헤제 : 덤벨을 내려놓고 난 뒤, 세마포어를 헤제하여 다른 선수가 덤벨을 사용하도록 한다.
                // 마찬가지로 세마포어는 Weight에 있기때문에 형변환 필수
                ((Weight) rightWeight).semaphore.release();
//                System.out.println("오른쪽 내리기성공");

                ((Weight) leftWeight).semaphore.release();
//                System.out.println("왼쪽 내리기 성공 ");
            }
        }
    }
}

// TODO : was ist acquire() ?
// 획득: 세마포어를 획득 시도한다. 허가를 얻을 수 있을 때까지 현재 쓰레드를 차단한다.
// 특징 : try - catch 블록 필수. Interrupt() 상황을 복구해야하므로.

