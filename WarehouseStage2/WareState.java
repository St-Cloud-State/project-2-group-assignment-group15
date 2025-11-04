public abstract class WareState {
    protected WareContext context;
    protected WareState() {
        //context = WareContext.instance();
    }
    public abstract void run();
}
