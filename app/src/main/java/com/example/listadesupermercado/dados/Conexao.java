package com.example.listadesupermercado.dados;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Conexao extends SQLiteOpenHelper {


    /**
     * Informações do Banco de Dados
     */
    private static final String name = "banco.db";
    //incrementar sempre que houver mudança mo banco
    private static final int version = 3;

    /**
     * Tabela Produto
     */
    private static final String TABLE = "produto";
    // Campos da tabela produto
    private static final String ID = "id";
    private static final String NOME = "nome";
    private static final String PRECO = "preco";
    private static final String STATUS = "status";
    private static final String CATEGORIA = "categoria";
    private static final String UNIDADE = "unidade";
    private static final String QUANTIDADE = "quantidade";
    private static final String CUSTO = "custo";

    public Conexao(Context context){
        super(context, name, null, version);
    }

    /*
        Criará as tabelas no banco de dados
     */
    @Override
    public void onCreate (SQLiteDatabase db){

        String sql = "CREATE TABLE " + TABLE + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NOME + " VARCHAR(50), "
                + PRECO + " DECIMAL(4,2), "
                + STATUS + " BOOLEAN, "
                + CATEGORIA + " VARCHAR(20), "
                + UNIDADE + " VARCHAR(4), "
                + QUANTIDADE + " DECIMAL(3,3), "
                + CUSTO + " DECIMAL(3,3) "
                + ")";

        db.execSQL(sql);

    }


    public void apagarTabela (SQLiteDatabase db, String TABLE){

        String sql = "DROP TABLE IF EXISTS " + TABLE;

        db.execSQL(sql);

        onCreate(db);

    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int i, int i1){
        // APAGA A ANTIGA TABELA
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE);
        // RECRIA A TABELA COM AS AUTERAÇÕES
        onCreate(db);
    }

}
