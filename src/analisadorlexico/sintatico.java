/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analisadorlexico;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
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
        
        try{
            finalAnalise = false;
            proxToken();
            inicio(null);
        }catch(IOException e){
            System.out.println("TESTE");
        }
    
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
    
    //Fazer Tratamento de erro espe4cifico do trabalho

    /**
     * Método que implementa o modo pânico de tratamento de erro, ou seja,
     * fica procurando por algum token de sincronização para poder retornar
     * à analise sintática
     * @param s lista com token de sincronização
     * @throws Exception
     */
    private void TrataErro(List<String> s) throws Exception{
    
        while(!s.contains(token.getCode())){
                proxToken();
        }
    
    }
    
    
    //Retorna todos os erros resultantes da analise
    public Vector<String> getErro(){
    
        Vector<String> vetor = new Vector<String>();
        
            for(int i=0; i<erros.size(); i++){
                vetor.add(erros.elementAt(i).toString2());
            }
        
        return  vetor;
        
    }
   //Metodo de união de listas
    private List<String> UnirListas(List<String> lista1, List<String> lista2){
        Vector uniao = new Vector();
        
        if(lista1 != null){
            for(String i:lista1){
                uniao.add(i);
            }
        }
        
        if(lista2 != null){
            for(String i:lista2){
            
                if(!uniao.isEmpty()){
                    if(!uniao.contains(i)){
                        uniao.add(i);
                    }
                }else{
                    uniao.add(i);
                }
            }
        }
        
        return uniao.subList(0, uniao.size());
    }
    
    //criar classe para variáveis
    private void variaveis(List<String> s) throws Exception{
    
        while(true){
            if(token.getCode().equals("ID")){
                 proxToken();
                 if(token.getCode().equals(",")){
                     proxToken();
                 }else
                     break;
            }else{
                //erro sintático
                ErroSintatico("Esperado ID, mas "+token.getToken()+" encontrado");
                TrataErro(s);
                break;
            }
        }
    
    }
    
    //Criar para tipo de variáveis
    private void tipo_variavel(List<String> s)throws Exception {
    
        if(token.getCode().equals("real") || token.getCode().equals("integer")){
              proxToken(); 
        }else{
              ErroSintatico("Variável deve ser tipo real ou integer.");
              TrataErro(s);
        }
    
    }
    
    //Inicio do programa
    private void inicio(List<String> s) throws Exception {
        
        if(token.getCode().equals("program")){
            proxToken();
        }else{
            ErroSintatico("Foi encontrado "+token.getToken()+" ao invés de program");
            TrataErro(UnirListas(s, Arrays.asList("ID")));
        }
        
        if(token.getCode().equals("ID")){
            proxToken();
        }else{
            ErroSintatico("Foi encontrado "+token.getToken()+" ao invés de ID");
            TrataErro(UnirListas(s, Arrays.asList(";")));
        }
        
        if(token.getCode().equals(";")){
            proxToken();
        }else{
            ErroSintatico("Foi encontrado "+token.getToken()+" ao invés de ;");
            TrataErro(UnirListas(s, primeiro.get("corpo")));
        }
        
        //corpo
        corpo(UnirListas(s, segundo.get("corpo")));
        
        if(!token.getCode().equals(".")){
            ErroSintatico("Foi encontrado "+token.getToken()+" ao invés de .");
        }
        
        if(!analise.isTerminate()){
            token = analise.analise();
            if(token != null){
               ErroSintatico("Existem simbolos após termino");
            }
        }else{
            throw new IOException("Final inesperado");
        }
    }
    
    private void corpo(List<String> s) throws Exception{
    
        //dc_v
        dc_v(UnirListas(s, segundo.get("dc_v")));
        //dc_p
        dc_p(UnirListas(s, segundo.get("dc_p")));
        
        if(token.getCode().equals("Begin")){
            proxToken();
        }else{
            ErroSintatico("Foi encontrado "+token.getToken()+" ao invés de begin");
            TrataErro(primeiro.get("comandos"));
        }
        
        //comandos entre begin e end
        
        comandos(UnirListas(s, segundo.get("comandos")));
        
        if(token.getCode().equals("end")){
            proxToken();
        }else{
            ErroSintatico("Foi encontrado "+token.getToken()+" ao invés de end");
            TrataErro(s);
        }
    }
    
    private void dc_v(List<String> s) throws Exception{
        while(token.getCode().equals("var")){
              proxToken();
              variaveis(UnirListas(s, segundo.get("variaveis")));
              
              if(token.getCode().equals(":")){
                  proxToken();
              }else{
                  ErroSintatico("É esperado :, mas "+token.getToken()+" encontrado");
                  TrataErro(UnirListas(s, primeiro.get("tipo_var")));
              }
              
              tipo_variavel(UnirListas(s, segundo.get("tipo_var")));
              
              if(token.getCode().equals(";")){
                   proxToken();
              }else{
                  ErroSintatico("É esperado ;, mas "+token.getToken()+" encontrado");
                  TrataErro(UnirListas(s, primeiro.get("dc_v")));
              }
        }
    }
    
    private void dc_p(List<String> s) throws Exception{
    
            if(token.getCode().equals("procedure")){
                proxToken();
                if(token.getCode().equals("ID")){
                     proxToken();
                }else{
                     ErroSintatico("Esperado ID, mas"+token.getToken()+"encontrado");
                     TrataErro(UnirListas(s, Arrays.asList("(",";")));
                }
                
                if(token.getCode().equals("(")){
                     proxToken();
                     par_list(UnirListas(s, segundo.get("lista_par")));
                        if(token.getCode().equals(")")){
                            proxToken();
                        }else{
                            ErroSintatico("É esperado ), mas "+token.getToken()+" encontrado");
                            TrataErro(UnirListas(s, Arrays.asList(";")));
                        }
                }
                if(token.getCode().equals(";")){
                    proxToken();
                }else{
                    ErroSintatico("É esperado ; ou (, mas "+token.getToken()+" encontrado");
                    TrataErro(UnirListas(s, primeiro.get("dc_v")));
                }
                
                dc_v(UnirListas(s, segundo.get("dc_v")));
                
                if(token.getCode().equals("begin")){
                    proxToken();
                }else{
                    ErroSintatico("É esperado begin, mas "+token.getToken()+" encontrado");
                    TrataErro(UnirListas(s, primeiro.get("comandos")));
                }
                
                comandos(UnirListas(s, segundo.get("comandos")));
                
                if(token.getCode().equals("end")){
                    proxToken();
                }else{
                    ErroSintatico("É esperado end, mas "+token.getToken()+" encontrado");
                    TrataErro(UnirListas(s, Arrays.asList(";")));
                }
                
                if(token.getCode().equals(";")){
                    proxToken();
                }else{
                    ErroSintatico("É esperado ;, mas "+token.getToken()+" encontrado");
                    TrataErro(UnirListas(s, primeiro.get("dc_p")));
                }
                
                dc_p(s);
            }
    }
    
    private void comandos(List<String> s) throws Exception{
        if(primeiro.get("cmd").contains(token.getCode())){
             cmd(UnirListas(s, segundo.get("cmd")));
             if(token.getCode().equals(";")){
                 proxToken();
             }else{
                 if(listaArq || listaExp){
                     ErroSintatico("É esperado ; ou :=, mas "+token.getToken()+" encontrado"); 
                     listaArq = false;
                     listaExp = false;
                     
                 }else{
                     ErroSintatico("É esperado ;, mas "+token.getToken()+" encontrado");
                     TrataErro(UnirListas(s, primeiro.get("comandos")));
                 }
             }
             comandos(s);
        }
    }
    
    private void par_list(List<String> s) throws Exception{
        
        variaveis(UnirListas(s, segundo.get("variaveis")));
        if(token.getCode().equals(":")){
           proxToken();
        }else{
            ErroSintatico("É esperado :, mas"+token.getToken()+"encontrado");
            TrataErro(UnirListas(s, primeiro.get("tipo_var")));
        }
        
        tipo_variavel(UnirListas(s, segundo.get("tipo_var")));
        if(token.getCode().equals(";")){
              proxToken();
              par_list(s);
        }
    }
    //<fator> ::= ident | numero_int | numero_real | ( <expressao> ) 
    private void fator(List<String> s) throws Exception {
        if(token.getCode().equals("ID") || token.getCode().equals("integer") || token.getCode().equals("real")){
            proxToken();
        }else{
            if(token.getCode().equals("(")){ 
               proxToken();
               expressao(UnirListas(s, segundo.get("expressao")));
               
                 if(token.getCode().equals(")")){
                    proxToken();
                 }else{
                     ErroSintatico("É esperado ), mas "+token.getToken()+" encontrado");
                     TrataErro(s);
                 }
            }else{
                 ErroSintatico("É esperado ID, numero inteiro, numero real ou (, mas" +token.getToken()+"encontrado");
                 TrataErro(UnirListas(s, primeiro.get("expressao")));
            }
        }
    }
    //<mais_fatores> ::= <op_mul> <fator> <mais_fatores> | λ 
    private void mais_fatores(List<String> s) throws Exception{
        if(token.getCode().equals("op_mult") || token.getCode().equals("op_div")){
           listaExp = true; //não haverá o operador ":=" na msg de erro
           proxToken();
           fator(UnirListas(s, segundo.get("fator")));
           mais_fatores(UnirListas(s, segundo.get("mais_fatores"))); 
        }
    }
    
    //<expressao> ::= <termo> <outros_termos> 
    private void expressao(List<String> s) throws Exception{
        if(token.getCode().equals("+") || token.getCode().equals("-")){
           listaExp = true;
           proxToken();
        }
        while(true){
            fator(UnirListas(s, segundo.get("fator")));
            mais_fatores(UnirListas(s, segundo.get("mais_fatores")));
            if(token.getCode().equals("+") || token.getCode().equals("-")){
                listaExp = true;
                proxToken();
                if(token.getCode().equals("+") || token.getCode().equals("-")){
                   proxToken();
                }
            }else{
                break;
            }
        }
    }
    /*<cmd> ::= read ( <variaveis> ) |
      write ( <variaveis> ) |
      while <condicao> do <cmd> |
      if <condicao> then <cmd> <pfalsa> |
      ident := <expressao> |
      ident <lista_arg> |
      begin <comandos> end */
    private void cmd (List<String> s) throws Exception{
        if(token.getCode().equals("read") || token.getCode().equals("write")){
            proxToken();
            if(token.getCode().equals("(")){
                proxToken();
            }else{
                ErroSintatico("É esperado (, mas "+token.getToken()+" encontrado");
                TrataErro(UnirListas(s, primeiro.get("variaveis")));
            }
            
            variaveis(UnirListas(s, segundo.get("variaveis")));
            
            if(token.getCode().equals(")")){
                proxToken();
            }else{
                ErroSintatico("É esperado , ou ), mas "+token.getToken()+" encontrado");
                TrataErro(s);
            }
        }else{
            if(token.getCode().equals("while")){
                proxToken();
                condicao(UnirListas(s, segundo.get("condicao")));
                if(token.getCode().equals("do")){ 
                    proxToken();
                }else{
                    ErroSintatico("É esperado do, mas"+token.getToken()+" encontrado");
                    TrataErro(UnirListas(s, primeiro.get("cmd")));
                }
                cmd(s);
            }else{
                if(token.getCode().equals("if")){ 
                    proxToken();
                    condicao(UnirListas(s, segundo.get("condicao"))); 
                    if(token.getCode().equals("then")){ 
                        proxToken();
                    }else{
                        ErroSintatico("É esperado then, mas "+token.getToken()+" encontrado");  
                        TrataErro(UnirListas(s, primeiro.get("cmd")));
                    }
                    
                    cmd(UnirListas(s, segundo.get("cmd")));
                    
                    if(token.getCode().equals("else")){
                        proxToken();
                        cmd(UnirListas(s, segundo.get("cmd"))); 
                    }
                }else{
                    if(token.getCode().equals("ID")){
                        proxToken();
                        if(token.getCode().equals("atribuicao")){
                            proxToken();
                            expressao(UnirListas(s, segundo.get("expressao"))); 
                        }else{
                            listaArq = true;
                            lista_arg(UnirListas(s, segundo.get("lista_arg"))); 
                        }
                    }else{
                        if(token.getCode().equals("begin")){
                            proxToken();
                            comandos(UnirListas(s, segundo.get("comandos"))); 
                            if(token.getCode().equals("end")){
                                proxToken();
                            }else{
                                ErroSintatico("É esperado end, mas "+token.getToken()+" encontrado"); 
                                TrataErro(s);
                            } 
                        }else{
                            if(token.getCode().equals("repeat")){ 
                                proxToken();
                                cmd(UnirListas(s, segundo.get("cmd"))); 
                                if(token.getCode().equals("until")){ 
                                    proxToken();                             
                                }else{
                                    ErroSintatico("É esperado until, mas "+token.getToken()+"encontrado"); 
                                    TrataErro(UnirListas(s, primeiro.get("condicao")));
                                }
                                condicao(UnirListas(s, segundo.get("condicao"))); 
                            }else{
                                ErroSintatico("É esperado read, write, while, if, ID, begin ou reapeat, mas " +token.getToken()+" encontrado"); 
                                TrataErro(s);
                            }
                        }
                    }
                }
            }
        
        }
    }
    
    //<lista_arg> ::= ( <argumentos> ) | λ
    private void lista_arg(List<String> s) throws Exception{
        if(token.getCode().equals("(")){
            proxToken();
            while(true){
                if(token.getCode().equals("ID")){
                    proxToken();
                }else{
                    ErroSintatico("É esperado ID, mas "+token.getCode()+" encontrado");
                    TrataErro(UnirListas(s, Arrays.asList(";",")")));
                }
                
                if(token.getCode().equals(";")){
                    proxToken();
                }else{
                    if(token.getCode().equals(")")){
                       break;
                    }else{
                        ErroSintatico("É esperado ;, mas "+token.getToken()+" encontrado");
                        TrataErro(UnirListas(s, Arrays.asList("ID")));
                        if(!token.getCode().equals("ID")){
                            break;
                        }
                    }
                }  
            }
                if(token.getCode().equals(")")){
                    proxToken();
                }else{
                    ErroSintatico("É esperado ), mas "+token.getToken()+" encontrado");
                    TrataErro(s);
                }
        }
    }
    
    //<condicao> ::= <expressao> <relacao> <expressao> 
    private void condicao(List<String> s) throws Exception{
        expressao(UnirListas(s, segundo.get("expressao"))); 
        if(token.getCode().equals("op_igual") || token.getCode().equals("op_diferente") ||token.getCode().equals("op_maior_igual") || token.getCode().equals("op_menor_igual") || token.getCode().equals("op_maior") || token.getCode().equals("op_menor")){
            proxToken();
            expressao(UnirListas(s, segundo.get("expressao")));
        }else{
            ErroSintatico("É esperado operador relacional, mas "+token.getToken()+" encontrado");
            TrataErro(s); 
        }
    }
   
}

