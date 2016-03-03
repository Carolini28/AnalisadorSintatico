/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package analisadorlexico;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Carolini
 */
public class AnalisadorLexico {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        //tabela de simbolos
        List<String> list = Arrays.asList(
                "program" , "begin"    , "var", "then",
                "real"   , "integer", "procedure", "end", "if", "else",
                "read"   , "write"  , "while"    ,  "repeat" , "until", "do");
        
        //Inicia a interface gráfica
        new Principal(list).setVisible(true);
    }
}
