package com.example.listadesupermercado.util;

import com.example.listadesupermercado.model.Produto;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ListaUtil {
    /**
     * calcula a quantadade de itens que falta ser comprados
     */
    public int calcItens(List<Produto> produtosAuxiliar){

        int c= 0;
        for (Produto p: produtosAuxiliar){
            if(!p.isStatus()){
                c++;
            }
        }
        return c;
    }

    /**
     * calcula o custo total da lista de compras
     */
    public String calcValor(List<Produto> produtosAuxiliar){
        double valor = 0;
        for (Produto p: produtosAuxiliar){
            valor = p.getCusto() + valor;
        }

//        DecimalFormat valorFormat = new DecimalFormat("0.00");

        String valorFinal = getFormatoEmReais(valor);

        return valorFinal;
    }

    /**
     * calcula o Subtotal da lista de compras
     */
    public String calcValorSubtotaL(List<Produto> produtosAuxiliar){
        double valorTotal = 0;
        double valorSub = 0;
        double valor;

        for (Produto p: produtosAuxiliar){
            valorTotal = p.getCusto() + valorTotal;

            if (p.isStatus()){
                valorSub = valorSub + p.getCusto();
            }
        }

        valor = valorTotal - valorSub;

        String valorFinal = getFormatoEmReais(valor);

        return valorFinal;
    }

    public String getFormatoEmReais(double valor) {
        //Local para ter o tipo de número
        Locale BRASIL = new Locale("pt", "BR");
        //setando o formato do Brasil
        NumberFormat f = NumberFormat.getInstance(BRASIL);
        //setando o tamanho dos digitos decimais
        f.setMinimumFractionDigits(2);
        f.setMaximumFractionDigits(2);

        return f.format(valor);
    }

}
