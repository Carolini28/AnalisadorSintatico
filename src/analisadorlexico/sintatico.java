/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analisadorlexico;

import java.util.Vector;

/**
 *
 * @author Carolini
 */
public class sintatico {
    
    Analisador analise;
    Tipo token;
    Vector<Erros> erros;
    
    public sintatico(Analisador analise){
    
        this.analise = analise;
        token = null;
        erros = new Vector<>();
        
    }
    
    
}
