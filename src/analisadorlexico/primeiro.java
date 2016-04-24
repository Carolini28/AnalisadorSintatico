/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package analisadorlexico;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author ra143759
 */
public class primeiro {
    
    private static HashMap hashmap = new HashMap<String, List>();
    
    static void init() {
       adiciona1("programa", Arrays.asList("program"));
       adiciona1("corpo", Arrays.asList("var","procedure","begin"));
       adiciona1("dc_v", Arrays.asList("var"));
       adiciona1("dc_p", Arrays.asList("procedure"));
       adiciona1("tipo_var", Arrays.asList("real","integer"));
       
       adiciona1("variaveis", Arrays.asList("ID"));
       adiciona1("lista_par", primeiro.get("variaveis"));
       adiciona1("lista_arg", Arrays.asList("("));
       adiciona1("cmd", Arrays.asList("read","write","while","if","begin","ID"));
       adiciona1("comandos", primeiro.get("cmd"));
       
       adiciona1("expressao", Arrays.asList("+","-", "op_igual", "integer", "real", "ID", "("));
       adiciona1("condicao", primeiro.get("expressao"));
       adiciona1("termo", primeiro.get("expressao"));
       adiciona1("fator", Arrays.asList("ID","integer","real","("));
       adiciona1("mais_fatores", Arrays.asList("op_mult","op_div"));
       adiciona1("relacao", Arrays.asList("op_maior","op_menor","op_maior_igual","op_menor_igual", "op_diferente", "op_igual"));
       
       
    }
    
    public static List<String> get(String chave){
        return (List<String>) hashmap.get(chave);
    }
    
    //chave a qual a lista será adicionada e list de terminais e não terminais primeiro(chave)
    public static void adiciona1(String chave, List<String> list){
        hashmap.put(chave, list);
    }
    
    public static  HashMap imprime(){
        return hashmap;
    }
    
}
