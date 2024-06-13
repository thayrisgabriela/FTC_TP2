package FTC_TP2.src;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        Gerador_XML gerador = new Gerador_XML();
        String pathExpressaoRegular = "input/expressao_regular.txt";
        String pathSentencas = "input/sentencas.txt";

        // Leitura da expressão regular do arquivo
        String expressaoRegular = lerExpressaoRegular(pathExpressaoRegular);

        // Leitura das sentenças do arquivo
        List<String> sentencas = lerSentencas(pathSentencas);

        // Criação do AFN com a expressão regular
        AFN afn = new AFN(expressaoRegular);
        HashMap<String, State> afnMap = afn.removeTransicoesLambda(afn.getStateMap());
        ArrayList<State> newstatesList = new ArrayList<>(afn.getStatesList());
        HashMap<String, State> afd = afn.converteEmAfd(afnMap, newstatesList);

        // Geração do arquivo XML de saída
        String pathSaida = "output/afd_resultado.jff";
        gerador.escrever_xml(afd, pathSaida);

        // Lista de estados finais para simulação do AFD
        List<String> finalStateNames = new ArrayList<>();
        for (State state : afn.getFinalStates()) {
            finalStateNames.add(state.getName());
        }

        // Simulação do AFD com as sentenças
        for (String sentenca : sentencas) {
            boolean pertence = afn.simularAFD(sentenca, afd, afn.getInitialState().get(0), finalStateNames);
            System.out.println("Sentença: " + sentenca + " - Pertence à linguagem: " + pertence);
        }
    }

    // Método para ler a expressão regular do arquivo
    private static String lerExpressaoRegular(String filePath) throws IOException {
        try (BufferedReader expressaoReader = new BufferedReader(new FileReader(filePath))) {
            return expressaoReader.readLine();
        } catch (FileNotFoundException e) {
            System.err.println("Arquivo não encontrado: " + e.getMessage());
            throw new FileNotFoundException("Erro ao ler expressão regular");
        }
    }

    // Método para ler as sentenças do arquivo
    private static List<String> lerSentencas(String filePath) throws IOException {
        List<String> sentencas = new ArrayList<>();
        try (BufferedReader sentencasReader = new BufferedReader(new FileReader(filePath))) {
            String sentenca;
            while ((sentenca = sentencasReader.readLine()) != null) {
                sentencas.add(sentenca);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Arquivo não encontrado: " + e.getMessage());
            throw new FileNotFoundException("Erro ao ler sentenças");
        }
        return sentencas;
    }
}
