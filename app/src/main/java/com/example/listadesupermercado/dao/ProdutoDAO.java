package com.example.listadesupermercado.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.listadesupermercado.dados.Conexao;
import com.example.listadesupermercado.model.Produto;

import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    private Conexao conexao;
    private SQLiteDatabase banco;

    public ProdutoDAO (Context context){
        conexao = new Conexao(context);
        banco = conexao.getWritableDatabase();
    }

    public void salvar (Produto p){

        ContentValues values = new ContentValues();
        //capturando os valores do produto a serem persistidos no banco de dados
        //o atributo id é auto incremento
        values.put("nome", p.getNome().toUpperCase());
        values.put("preco", p.getPreco());
        //valor boolean
        values.put("status", p.isStatus());
        values.put("categoria", p.getCategoria());
        values.put("unidade", p.getUnidade());
        values.put("quantidade", p.getQuantidade());
        values.put("custo", p.getCusto());

        banco.insert("produto", null, values);

    }

    public List<Produto> listarTodos (){

        //pesquisar sobre a relação do list e o arraylist
        List<Produto> produtos = new ArrayList<>();

        Cursor cursor = banco.query("produto", new String[]{"id","nome","preco","status",
                        "categoria","unidade","quantidade", "custo"},
                null, null, null, null, null);

        while (cursor.moveToNext()){

            Produto p = new Produto();

            p.setId(cursor.getLong(0));
            p.setNome(cursor.getString(1));
            p.setPreco(cursor.getDouble(2));
            /*
                SQLite trata boolean como inteiro (0 = false e 1 = true)
                por isso preciso converter a coluna status (inteiro que represento um boolean)
                para um Boolean usando o parseBoolean
             */
            p.setStatus(Boolean.parseBoolean(cursor.getString(3)));
            p.setCategoria(cursor.getString(4));
            p.setUnidade(cursor.getString(5));
            p.setQuantidade(cursor.getDouble(6));
            p.setCusto(cursor.getDouble(7));

            produtos.add(p);
        }

        return produtos;
    }

    public void deletar (Produto p){
        banco.delete("produto", "id = ?", new String[]{p.getId().toString()});
    }

    public void deletarLista (String lista){

        conexao.apagarTabela(banco, lista);

    }


    public void atualizar (Produto p){
        ContentValues values = new ContentValues();
        //capturando os valores do produto a serem persistidos no banco de dados
        //o atributo id é auto incremento
        values.put("nome", p.getNome().toUpperCase());
        values.put("preco", p.getPreco());
        //valor boolean para salvar no SQLite precisa converter para String, pois o SQLite não tem boolean
        values.put("status", String.valueOf(p.isStatus()));
        values.put("categoria", p.getCategoria());
        values.put("unidade", p.getUnidade());
        values.put("quantidade", p.getQuantidade());
        values.put("custo", p.getCusto());

        banco.update("produto", values, "id = ?", new String[]{p.getId().toString()});
    }

}
