package impl;

import util.AbstractAthlete;
import util.AbstractWeight;

import java.util.concurrent.Semaphore;

public class Weight extends AbstractWeight {

    //TODO : Implementiern
    // 덤벨이 현재 어느 선수에 의해 사용중인지 나타낸다. -1로 초기화 하여
    // 덤벨이 사용중이 아님을 표시한다.
    private int dumbel_erheben = -1;

    // TODO : 세마포어는 덤벨에 대한 접근을 제어하기 위해 사용. 초기값이 1은 한 번에 하나의 쓰레드만 사용하기 위함
    public Semaphore semaphore = new Semaphore(1);

    /**
     * DO NOT CHANGE THE SIGNATURE OF THE CONSTRUCTOR!
     *
     * @param id The identifier of this weight. Must be unique among all weights.
     */
    public Weight(int id) {
        super(id);
    }

    /**
     * Determines whether the weight is available to be picked up by an athlete.
     * This is the case if the weight is not currently held by any athlete.
     *
     * @return false if the weight is held by an athlete, true otherwise.
     */
    @Override
    // 선수가 덤밸을 들고있는지 없는지?
    public synchronized boolean isAvailable() {
        //TODO: implement
        // -1 이면 사용중이 아니지. 그럼 is Available
        return dumbel_erheben == -1;
    }

    /**
     * Called by super.pickUp(). Implements the logic of checking whether the weight is available for pick-up
     * by an athlete and marking it as held by the athlete if it was available.
     * super.pickUp()에 의해 호출됩니다. 선수가 웨이트를 픽업할 수 있는지 여부를 확인하는 로직을 구현합니다.
     * *할 수 있는지 확인하고 가능한 경우 선수가 들고 있는 것으로 표시하는 로직을 구현합니다.
     *
     * @param athlete The athlete who is attempting to pick up the weight.
     * @return true if the weight was successfully picked up, false otherwise.
     */
    @Override
    protected boolean pickUpImpl(AbstractAthlete athlete) {
        //TODO: implement
        // 덤벨을 잡을수 있는지 확인하고, 가능하면 dumbel_erheben을 업데이트 하여 잡혔음을 표시하자.

        // 선수가 가지고있는 왼쪽덤벨과 오른쪽 덤벨의 아이디를 가져오자.
        int left_Weight = athlete.getLeftWeight().getWeightId();
        int right_Weight = athlete.getRightWeight().getWeightId();

        // 현재 덤벨의 아이디가 선수가 들고있는 왼쪽 덤벨 또는 오른쪽 덤벨인지 확인하여,
        // 선수가 잡을라는 덤벨이 맞는지 검사한다. 이래야 선수가 자기의 덤벨만을 잡을수있다.
        if (this.id == left_Weight || this.id == right_Weight) {

            // is Available() 를 사용하면 덤벨의 상태를 알수있으니. 확인해보자.
            if (isAvailable()) {
                // 확인이고 자시고, 이 메서드는 덤벨 올리는 놈이니까. 잡은걸 가정하고 써보자
                // 잡았으며는 이제 거기다가 선수의 아이디를 부여하자
                dumbel_erheben = athlete.getAthleteId();

                // 트루는 잡았음을 알린다.
                return true;
            }
        }
        // 못잡았으면 false
        return false;
    }

    /**
     * Called by super.putDown(). Implements the logic of checking whether the weight may be put down by the
     * calling athlete (this is the case only if the weight is currently being held by this athlete) and releasing
     * the weight if applicable.
     *
     * @param athlete The athlete who is attempting to put down the weight.
     * @return true if the weight was successfully put down, false otherwise.
     * <p>
     * * super.putDown()에 의해 호출됩니다. 웨이트를 내려놓을 수 있는지 여부를 확인하는 로직을 구현합니다.
     * * 선수를 호출하고(현재 이 선수가 웨이트를 들고 있는 경우에만 해당) 해당되는 경우 웨이트를 해제하는
     * * 해당되는 경우 웨이트를 해제합니다.
     * * @param athlete 웨이트를 내려놓으려는 선수입니다.
     * * 웨이트를 성공적으로 내려놓으면 참을 반환하고, 그렇지 않으면 거짓을 반환합니다.
     */
    @Override
    protected boolean putDownImpl(AbstractAthlete athlete) {
        //TODO: implement

        // 선수가 가지고 있는 왼쪽과 오른쪽의 ID를 가져온다. 나중에 현재 덤벨의 ID와 비교하기 위해 필요하다.
        int left_WeightId = athlete.getLeftWeight().getWeightId();
        int right_WeightId = athlete.getRightWeight().getWeightId();

        // 현재 덤벨을 잡고있는 선수가 이 선수인지 확인한다.
        // dumbel_erheben은 현재 덤벨을 잡고있는 선수의 아이디를 저장해놨다. 그 선수가 현재 덤벨을 쥐고있는 것.
        if (this.dumbel_erheben == athlete.getAthleteId()) {

            // 현재 this.id선수가 들고있는 왼쪽의 덤벨 ID또는 오른쪽 아이디와 일치하는지 확인.
            // 이는 현재 덤벨이 선수의 덤벨 중 하나인지를 확인하기 위함.
            if (this.id == left_WeightId || this.id == right_WeightId) {

                // 덤벨이 사용중인지 확인. 사용중 x : true, 사용중 o : false
                if (!isAvailable()) {

                    // 덤벨을 사용가능하게 바꾸는거제
                    dumbel_erheben = -1;
                    // 그걸 문제에서 true로 바꾸래
                    return true;
                }
            }
        }

        return false;
    }
}
