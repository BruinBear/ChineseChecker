import java.util.concurrent.Callable;

public class GetBestNextMoveTask implements Callable<Integer> {
    private Minimax m_alg;
    private CheckerState m_state;

    GetBestNextMoveTask(Minimax alg, CheckerState state) {
        m_alg = alg;
        m_state = state;
    }

    @Override
    public Integer call() throws Exception {
//            Thread.sleep(4000); // Just to demo a long running task of 4 seconds.
        m_alg.execute_iteratively(m_state);
        return 0;
    }
}