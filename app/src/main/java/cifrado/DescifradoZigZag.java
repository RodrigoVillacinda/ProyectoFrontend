package cifrado;

import java.util.ArrayList;
import java.util.List;

public class DescifradoZigZag {

    public DescifradoZigZag(int niveles, String texto) {
        this.niveles = niveles;
        Texto = texto;
    }

    public int niveles;
    public String Texto;

    public int TamañoOla() {
        int resultado = 0;
        resultado = (niveles * 2) - 2;
        return resultado;
    }

    //devuelva la cantidad de olas que existen
    private int NumeroOlas() {
        int resultado = 0;
        resultado = TamañoTexto() / TamañoOla();
        return resultado;

    }

    private int TamañoBloque() {
        int resultado = 0;
        resultado = 2 * NumeroOlas();
        return resultado;

    }

    private int TamañoTexto() {
        int resultado = 0;
        char TamañoTexto[] = Texto.toCharArray();
        for (int i = 0; i < TamañoTexto.length; i++) {
            resultado++;
        }
        return resultado;

    }

    private List<Character> ListaCaracteresTotales() {
        List<Character> lista = new ArrayList<Character>();
        char caracteres[] = Texto.toCharArray();

        for (int i = 0; i < TamañoTexto(); i++) {
            lista.add(caracteres[i]);
        }

        return lista;
    }

    public List<List<Character>> ListaDescifrado() {
        List<List<Character>> ListasOlas = new ArrayList<>();
        List<Character> temporal = ListaCaracteresTotales();


        List<Character> ListaAuxiliar = new ArrayList<>();
        for (int j = 0; j < NumeroOlas(); j++) {
            ListaAuxiliar.add(temporal.remove(0));
        }
        ListasOlas.add(ListaAuxiliar);


        ListaAuxiliar = new ArrayList<>();
        int CantidadBloques = temporal.size() / TamañoBloque();
        for (int i = 0; i < CantidadBloques; i++) {
            ListaAuxiliar = new ArrayList<>();
            for (int j = 0; j < TamañoBloque(); j++) {
                ListaAuxiliar.add(temporal.remove(0));
            }
            ListasOlas.add(ListaAuxiliar);
        }


        ListaAuxiliar = new ArrayList<>();
        int Tamaño = temporal.size() - 1;
        for (int j = 0; j <= Tamaño; j++) {
            ListaAuxiliar.add(temporal.remove(0));
        }
        ListasOlas.add(ListaAuxiliar);


        return ListasOlas;
    }

    public String Descifrado() {

        String descifrado = "";
        List<List<Character>> temporal = ListaDescifrado();


        int comienzo=0;
        int ultimo=0;
        //int tamaño=temporal.get(0).size()-2;
        int tamaño=temporal.get(0).size()-1;

        tamaño=niveles;
        for (int i = 0; i < 1 ; i++) {


            if (comienzo == 1) {
                tamaño++;
            }

            for (int r = 0; r < NumeroOlas(); r++) {



                for (int j = comienzo; j < tamaño; j++) {

                    descifrado = descifrado + temporal.get(j).remove(0).toString();

                    ultimo = j;
                }
                if (i == 0) {
                    ultimo = ultimo - 1;
                }
                for (int x = ultimo; x >= 0; x--) {

                    if (temporal.get(x).size()!=0) {
                        descifrado = descifrado + temporal.get(x).remove(0).toString();
                        if (temporal.get(x).size() == 0) {
                            x = x - 1;
                        }

                    }else{
                        x = x - 1;
                    }
                }
                comienzo = 0;
                comienzo = comienzo + 1;

            }



        }


        return descifrado;
    }

}
