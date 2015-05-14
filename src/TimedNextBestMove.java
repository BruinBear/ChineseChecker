import java.util.concurrent.Callable;

public class TimedNextBestMove implements Callable<Integer> {
    private SearchAlgorithm m_alg;
    private CheckerState m_state;

    TimedNextBestMove(SearchAlgorithm alg, CheckerState state) {
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
