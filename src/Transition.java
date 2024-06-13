package FTC_TP2.src;

public class Transition {
    private String symbol;
    private State initialState;
    private State finalState;

    public Transition(String symbol) {
        this.symbol = symbol;
        this.initialState = null; // Definido como null para ser configurado posteriormente
        this.finalState = null; // Definido como null para ser configurado posteriormente
    }

    public Transition(String symbol, State initialState, State finalState) {
        this.symbol = symbol;
        this.initialState = initialState;
        this.finalState = finalState;
    }

    // Getters e Setters
    public String getSymbol() {
        return this.symbol;
    }

    public State getInitialState() {
        return this.initialState;
    }

    public void setInitialState(State initialState) {
        this.initialState = initialState;
    }

    public State getFinalState() {
        return this.finalState;
    }

    public void setFinalState(State finalState) {
        this.finalState = finalState;
    }
}
