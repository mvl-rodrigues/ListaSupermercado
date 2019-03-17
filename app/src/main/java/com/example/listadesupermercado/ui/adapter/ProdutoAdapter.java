package com.example.listadesupermercado.ui.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.listadesupermercado.model.Produto;
import com.example.listadesupermercado.R;
import com.example.listadesupermercado.util.ListaUtil;

import java.text.DecimalFormat;
import java.util.List;

public class ProdutoAdapter extends BaseAdapter {

    private Activity a;
    private List<Produto> produtos;

    public ProdutoAdapter(Activity context, List<Produto> produtos){
        this.a = context;
        this.produtos = produtos;
    }

    @Override
    public int getCount(){
        return produtos.size();
    }

    @Override
    public Object getItem(int position) {
        return produtos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return produtos.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = a.getLayoutInflater().inflate(R.layout.item, parent, false);

        TextView nome = (TextView)v.findViewById(R.id.textNome);
        TextView preco = (TextView)v.findViewById(R.id.textPreco);
        TextView quantidade = (TextView)v.findViewById(R.id.textQuantidade);
        TextView unidade = (TextView)v.findViewById(R.id.textUnidade);
        TextView categoria = (TextView)v.findViewById(R.id.textCategoria);
        TextView reais = (TextView)v.findViewById(R.id.textRs);

        CheckBox status = (CheckBox)v.findViewById(R.id.checkStatus);

        Produto p = produtos.get(position);

        ListaUtil util = new ListaUtil();
        //Formatando o proço em Reais
        String precoFormatado = util.getFormatoEmReais(p.getCusto());

        nome.setText(p.getNome());
        preco.setText(precoFormatado);
        quantidade.setText(String.valueOf(p.getQuantidade()));
        unidade.setText(String.valueOf(p.getUnidade()));
        categoria.setText(p.getCategoria());
        status.setChecked(p.isStatus());

        if (p.isStatus()){
            nome.setTextColor(Color.GRAY);
            nome.setPaintFlags(nome.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            preco.setTextColor(Color.GRAY);
            preco.setPaintFlags(preco.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            quantidade.setTextColor(Color.GRAY);
            quantidade.setPaintFlags(quantidade.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            unidade.setTextColor(Color.GRAY);
            unidade.setPaintFlags(unidade.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            categoria.setPaintFlags(categoria.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            categoria.setTextColor(Color.GRAY);

            reais.setPaintFlags(reais.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            reais.setTextColor(Color.GRAY);
        }

        return v;
    }
}
