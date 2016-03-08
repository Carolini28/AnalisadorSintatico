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
public class segundo {
 
    private static HashMap hashmap = new HashMap<String, List<String>>();
    
    static void init() {
        adiciona("program",null);
        adiciona("corpo",Arrays.asList("."));
        adiciona("comandos",Arrays.asList("end"));
        adiciona("dc_p",Arrays.asList("begin"));
        adiciona("dc_v",Arrays.asList("procedure","begin"));
    }
    
    public static List<String> get(String chave){
        return(List<String>)
        hashmap.get(chave);
    }
    
    public static void adiciona(String chave, List<String> list){
        hashmap.put(chave, list);
    }
    
}
