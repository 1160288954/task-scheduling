package commmon;

public interface SchedulerInterface {

    /**
     * initial parameters
     * @param job
     */
    public void initialParameter(Job job);

    /**
     * iteration
     * @param job
     */
    public abstract double train(Job job);

}
