package com.example.listadesupermercado.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.listadesupermercado.R;
import com.example.listadesupermercado.model.Produto;
import com.example.listadesupermercado.ui.ListaProdutosViewUtil;

import java.util.List;

public class ProdutoAdapter extends BaseAdapter {

    private final Context context;
    private final List<Produto> produtos;

    private TextView nome;
    private TextView preco;
    private TextView quantidade;
    private TextView unidade;
    private TextView categoria;
    private TextView reais;

    public ProdutoAdapter(Context context, List<Produto> produtos) {
        this.context = context;
        this.produtos = produtos;
    }

    @Override
    public int getCount() {
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View view = criarView(viewGroup);
        Produto produto = produtos.get(position);
        vinculaProdutoComView(view, produto);
        if (produto.isStatus()) {
            marcarItensComprados(produto);
        }
        return view;
    }

    private void vinculaProdutoComView(View view, Produto produto) {

        String precoFormatado = new ListaProdutosViewUtil().formatarEmReais(produto.getCusto());

        nome = (TextView) view.findViewById(R.id.textNome);
        nome.setText(produto.getNome());

        preco = (TextView) view.findViewById(R.id.textPreco);
        preco.setText(precoFormatado);

        quantidade = (TextView) view.findViewById(R.id.textQuantidade);
        quantidade.setText(String.valueOf(produto.getQuantidade()));

        unidade = (TextView) view.findViewById(R.id.textUnidade);
        unidade.setText(String.valueOf(produto.getUnidade()));

        categoria = (TextView) view.findViewById(R.id.textCategoria);
        categoria.setText(produto.getCategoria());

        reais = (TextView) view.findViewById(R.id.textRs);

        CheckBox status = (CheckBox) view.findViewById(R.id.checkStatus);
        status.setChecked(produto.isStatus());
    }

    private void marcarItensComprados(Produto produto) {

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

    private View criarView(ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.item, viewGroup, false);
    }
}
