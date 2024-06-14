package FTC_TP2.src;

import java.util.*;

public class AFN {
    private String traducaoExpressaoRegular;
    private List<Character> symbolList;
    private List<State> finalStates;
    private List<String> initialState;
    private List<State> states;

    // Construtor
    public AFN(String expressaoRegular) {
        traducaoExpressaoRegular = expressaoRegular;
        symbolList = new ArrayList<>();
        finalStates = new ArrayList<>();
        initialState = new ArrayList<>();
        states = new ArrayList<>();
        adicionaSimbolos();
        criadorAFN();
        adicionaEstados();
    }

    // Método para adicionar símbolos à lista symbolList
    private void adicionaSimbolos() {
        Set<Character> symbolsSet = new HashSet<>();
        for (char c : traducaoExpressaoRegular.toCharArray()) {
            if (Character.isLetterOrDigit(c)) { // Adiciona apenas letras e dígitos
                symbolsSet.add(c);
            }
        }
        symbolList.addAll(symbolsSet);
    }
    
// Método para criar o AFN básico
private void criadorAFN() {
    State initial = new State("Initial");
    State finalState = new State("Final");

    // Criar transições usando símbolos e estados
    initial.addTransition("a", finalState);
    initial.addTransition("b", finalState);

    // Adicionar estados inicial e final às listas
    initialState.add(initial.getName());
    finalStates.add(finalState); // Adicionar o objeto State, não apenas o nome

    // Adicionar estados às listas de estados
    states.add(initial);
    states.add(finalState); // Adicionar apenas o nome do estado final
}


// Método para adicionar estados ao AFN
private void adicionaEstados() {
    State state1 = new State("State1");
    State state2 = new State("State2");

    initialState.add(state1.getName()); // Se initialState é List<String>, mantenha assim para os nomes
    finalStates.add(state2); // Se finalStates é List<State>, adicione o objeto State diretamente

    states.add(state1); // Adiciona o objeto State state1 à lista states
    states.add(state2); // Adiciona o objeto State state2 à lista states
}



 // Método para combinar transições de estados
private State combinaTransicoes(State s1, State s2) {
    HashMap<String, ArrayList<State>> s1Transitions = s1.getTransitions();
    HashMap<String, ArrayList<State>> s2Transitions = s2.getTransitions();

    for (String key : s2Transitions.keySet()) {
        ArrayList<State> transitionsInS1 = s1Transitions.getOrDefault(key, new ArrayList<>());
        for (State x : s2Transitions.get(key)) {
            if (!transitionsInS1.contains(x)) {
                transitionsInS1.add(x);
            }
        }
        s1Transitions.put(key, transitionsInS1);
    }

    s1.setTransitions(s1Transitions);
    return s1;
}
    // Método para remover transições lambda
    public HashMap<String, State> removeTransicoesLambda(HashMap<String, State> afnFromFile) {
        HashMap<String, State> AFN = new HashMap<>();

        for (String key : afnFromFile.keySet()) {
            State state = afnFromFile.get(key);
            HashMap<String, ArrayList<State>> transitions = state.getTransitions();

            if (transitions.containsKey("!")) {
                for (State s : transitions.get("!")) {
                    state = combinaTransicoes(state, s);
                }
                state.getTransitions().remove("!");
            }

            AFN.put(key, state);
        }

        return AFN;
    }


     // Método para converter AFN em AFD
    public HashMap<String, State> converteEmAfd(HashMap<String, State> AFN, List<State> newStatesList) {
        HashMap<String, State> AFD = new HashMap<>();
        Queue<String> queue = new LinkedList<>();
        State startState = null;

        for (String key : AFN.keySet()) {
            if (AFN.get(key).isInitial()) {
                startState = AFN.get(key);
                break;
            }
        }

        if (startState != null) {
            queue.add(startState.getName());

            while (!queue.isEmpty()) {
                String currentStateName = queue.remove();
                State currentState = AFN.get(currentStateName);

                if (!AFD.containsKey(currentStateName)) {
                    AFD.put(currentStateName, currentState);
                    HashMap<String, ArrayList<State>> transitions = currentState.getTransitions();

                    for (String transitionKey : transitions.keySet()) {
                        List<State> nextStates = transitions.get(transitionKey);
                        String nextStateNames = getStateNames(nextStates);

                        if (!AFD.containsKey(nextStateNames)) {
                            State newState = new State(nextStateNames);
                            newState.setAcceptance(checkAcceptance(nextStates));
                            AFD.put(nextStateNames, newState);
                            queue.add(nextStateNames);
                            newStatesList.add(newState);
                        }

                        // Usando ArrayList<State> explicitamente
                        AFD.get(currentStateName).getTransitions().put(transitionKey, new ArrayList<>(Collections.singletonList(AFD.get(nextStateNames))));
                    }
                }
            }
        }

        return AFD;
    }

    public HashMap<String, State> getStateMap() {
        HashMap<String, State> stateMap = new HashMap<>();
        // Itera sobre a lista de estados e adiciona ao mapa
        for (State state : states) {
            stateMap.put(state.getName(), state);
        }
        return stateMap;
    }
    
    public List<State> getStatesList() {
        // Retorna a lista de estados
        return states;
    }
    
    // Método auxiliar para verificar se um conjunto de estados é de aceitação
    private boolean checkAcceptance(List<State> states) {
        for (State state : states) {
            if (state.isFinal()) {
                return true;
            }
        }
        return false;
    }

    // Método auxiliar para obter os nomes dos estados de uma lista de estados
    private String getStateNames(List<State> states) {
        StringBuilder sb = new StringBuilder();
        for (State state : states) {
            sb.append(state.getName());
        }
        char[] stateNamesArray = sb.toString().toCharArray();
        Arrays.sort(stateNamesArray);
        return new String(stateNamesArray);
    }

    // Método para simular o AFD
    public boolean simularAFD(String sentenca, HashMap<String, State> afd, String initialState, List<String> finalStates) {
        String estadoAtual = initialState;
        for (char simbolo : sentenca.toCharArray()) {
            State estado = afd.getOrDefault(estadoAtual, null);
            if (estado == null) {
                return false;
            }
            List<State> proximosEstados = estado.getTransitions().get(String.valueOf(simbolo));
            if (proximosEstados == null || proximosEstados.isEmpty()) {
                return false;
            }
            estadoAtual = proximosEstados.get(0).getName();
        }
        return finalStates.contains(estadoAtual);
    }

    // Métodos getters para obter informações do autômato
    public List<Character> getSymbolList() {
        return symbolList;
    }

    public List<State> getFinalStates() {
        return finalStates;
    }

    public List<String> getInitialState() {
        return initialState;
    }

    public List<State> getStates() {
        return states;
    }
    
}
