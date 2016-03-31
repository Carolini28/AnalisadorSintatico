/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analisadorlexico;

import java.io.IOException;
import java.util.Vector;

/**
 *
 * @author Carolini
 */
public class sintatico {
    
    Analisador analise; //analise lexica 
    Tipo token; //ultimo token
    Vector<Erros> erros; //erros q foram encontrados
    boolean finalAnalise; // flag q indica termino de analise
    private boolean listaArq = false;
    private boolean listaExp = false;
    
    
    public sintatico(Analisador analise){
    
        this.analise = analise;
        token = null;
        erros = new Vector<>();
        primeiro.init();
        segundo.init();
       
        
    }
    //pega proximo token 
    private void proxToken() throws Exception{
    
        if(!analise.isTerminate()){
            token = analise.analise();
                 if(analise.hasErro()){
                     ErroLexico();
                 }         
        }
        if(analise.isTerminate() || token == null){
            ErroSintatico("Fim inesperado do arquivo");
            throw new IOException("Fim inesperado do arquivo!");
        }
    }
    
    public void analisar() throws Exception{
    
    
    }

    private void ErroLexico() {
        Erros erro = analise.getErro();
        erros.add(erro);
    }

    private void ErroSintatico(String info) {
        Erros erro;
        if(token != null){
            erro = new Erros(info,token.getCode(),token.getLine(),"Sintático");
        }else{
            erro = new Erros(info,"",analise.getLine(),"Sintático");
        }
        erros.add(erro);
    }
    
    
    
}
