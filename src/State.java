package FTC_TP2.src;

import java.util.ArrayList;
import java.util.HashMap;

public class State {
    private String name;
    private boolean isInitial;
    private boolean isFinal;
    private HashMap<String, ArrayList<State>> transitions;

    public void setAcceptance(boolean isFinal) {
        this.isFinal = isFinal;
    }

    public boolean getAcceptance() {
        return isFinal;
    }
    

    public State(int stateCount) {
        this.name = "q" + stateCount;
        this.transitions = new HashMap<>();
    }

    public State(String name) {
        this.name = name;
        this.transitions = new HashMap<>();
    }

    public String getName() {
        return this.name;
    }

    public boolean isInitial() {
        return isInitial;
    }

    public void setInitial(boolean isInitial) {
        this.isInitial = isInitial;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean isFinal) {
        this.isFinal = isFinal;
    }

    public HashMap<String, ArrayList<State>> getTransitions() {
        return this.transitions;
    }

    public void setTransitions(HashMap<String, ArrayList<State>> transitions) {
        this.transitions = transitions;
    }

    public void addTransition(String symbol, State state) {
        ArrayList<State> states = transitions.getOrDefault(symbol, new ArrayList<>());
        states.add(state);
        transitions.put(symbol, states);
    }

    @Override
    public String toString() {
        return name;
    }
}
