import java.util.concurrent.Callable;

class GetBestNextMoveTask implements Callable<Integer> {
    private Algorithm m_alg;
    private CheckerState m_state;
    private int m_max_depth;
    private int m_maximizer;
    private int m_minimizer;

    GetBestNextMoveTask(Algorithm alg, CheckerState state, int max_depth, int maximizer, int minimizer) {
        m_alg = alg;
        m_state = state;
        m_max_depth = max_depth;
        m_minimizer = minimizer;
        m_maximizer = maximizer;
    }

    @Override
    public Integer call() throws Exception {
//            Thread.sleep(4000); // Just to demo a long running task of 4 seconds.
        m_alg.execute_iteratively(m_state, m_max_depth, m_maximizer, m_minimizer);
        return 0;
    }
}