package cifrado;

import java.util.ArrayList;
import java.util.List;

public class ZigZag {


    public ZigZag(int niveles, String texto) {
        this.niveles = niveles;
        Texto = texto;
    }

    public int niveles;
    public String Texto;

    public int TamañoOla(){
        int resultado=0;
        resultado = (niveles*2)-2;
        return resultado;
    }

    //devuelva la cantidad de olas que existen
    private int NumeroOlas(){
        int resultado=0;
        resultado = TamañoTexto()/TamañoOla();
        return resultado;

    }

    private int TamañoBloque(){
        int resultado=0;
        resultado = 2*NumeroOlas();
        return resultado;

    }

    private int TamañoTexto(){
        int resultado=0;
        char TamañoTexto[]=Texto.toCharArray();
        for (int i=0;i<TamañoTexto.length; i++)
        {
            resultado++;
        }
        return resultado;

    }

    private List<Character> ListaCaracteresTotales(){
        List<Character> lista = new ArrayList<Character>();
        char caracteres[]= Texto.toCharArray();

        for (int i=0;i<TamañoTexto(); i++){
            lista.add(caracteres[i]);
        }

        return  lista;
    }


    public List<List<Character>> ListasCifrado(){
        String TextoCompreso="";

        List<List<Character>> ListasOlas=new ArrayList<>();

        char TamañoText[]=Texto.toCharArray();
        char res=' ';

        List<Character> Temporal= ListaCaracteresTotales();

        for (int i=0; i<=NumeroOlas(); i++){ //5
            List<Character> ListaPrueba = new ArrayList<>();

            if (!Temporal.isEmpty()) {
                for (int j = 0; j <TamañoOla()  ; j++) {

                    res = Temporal.remove(0);
                    ListaPrueba.add(res);
                    if (Temporal.isEmpty()){
                        int tamaño=TamañoOla()-ListaPrueba.size();
                        for (int h=0; h < tamaño; h++){
                            ListaPrueba.add('|');

                        }
                        j=TamañoOla()+1;
                    }

                }

                ListasOlas.add(ListaPrueba);

            }
            else {

                ListaPrueba.add('1');
            }
        }

        return ListasOlas;
    }

    public String Cifrado(){


        String cifrado="";
        List<List<Character>> temporal=ListasCifrado();

        int ultimo=0;
        ultimo = TamañoOla()-3;
        for (int i=0; i <=ListasCifrado().get(0).size()-1; i++ ){

            //cifrado = cifrado + temporal.get(0).get(i).toString() + temporal.get(1).get(i).toString() +
            //temporal.get(2).get(i).toString() + temporal.get(3).get(i).toString()+
            //temporal.get(4).get(i).toString();


            for(int j=0; j<=NumeroOlas();j++){

                if (j==0 && i==0){
                    for (int x=0;x<=NumeroOlas();x++) {
                        cifrado = cifrado + temporal.get(x).remove(i).toString();
                    }
                    //i=i+1;
                }

               // ultimo = temporal.get(j).size()-1;
                if ( (temporal.get(j).size()-1) >=0){
                    cifrado = cifrado + temporal.get(j).remove(0).toString();//s
                }
                if ((temporal.get(j).size()-1)>0) {
                    cifrado = cifrado + temporal.get(j).remove(temporal.get(j).size() - 1).toString();
                }

            }

        }

        return cifrado;
    }



}


