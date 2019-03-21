package com.example.listadesupermercado.ui;

import com.example.listadesupermercado.model.Produto;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ListaProdutosViewUtil {

    public int ItensQueFalta(List<Produto> produtos){
        int quantidadeItens = 0;
        for (Produto p: produtos){
            if(!p.isStatus()){
                quantidadeItens++;
            }
        }
        return quantidadeItens;
    }

    public String calculaValorTotal(List<Produto> produtos){
        double valor = 0;
        for (Produto p: produtos){
            valor = p.getCusto() + valor;
        }
        return formatarEmReais(valor);
    }

    public String calculaValorSubtotaL(List<Produto> produtos){
        double valorTotal = 0;
        double valorSub = 0;
        double valor;
        for (Produto p: produtos){
            valorTotal = p.getCusto() + valorTotal;
            if (p.isStatus()){
                valorSub = valorSub + p.getCusto();
            }
        }
        valor = valorTotal - valorSub;
        return formatarEmReais(valor);
    }

    public String formatarEmReais(double valor) {
        Locale BRASIL = new Locale("pt", "BR");
        NumberFormat numeroFormatado = NumberFormat.getInstance(BRASIL);
        numeroFormatado.setMinimumFractionDigits(2);
        return numeroFormatado.format(valor);
    }
}