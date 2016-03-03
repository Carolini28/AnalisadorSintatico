/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package analisadorlexico;

import java.util.HashMap;
import java.util.List;

/**
 *
 * @author ra143759
 */
public class primeiro {
    
    private static HashMap hashmap = new HashMap<String, List>();
    
    static void init() {
       adiciona1();
    }
    
    public void adiciona1(String chave, List<String> list){
        hashmap.put(chave, list);
    }
    
    public static  HashMap imprime(){
        return hashmap;
    }
    
}
