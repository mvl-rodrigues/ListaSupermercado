package com.example.listadesupermercado.ui.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.listadesupermercado.model.Produto;
import com.example.listadesupermercado.dao.ProdutoDAO;
import com.example.listadesupermercado.R;

import java.util.List;

public class FormularioActivity extends AppCompatActivity {

    private EditText nome;
    private EditText preco;
    private EditText quantidade;
    private RadioGroup unidade;
    private RadioButton radioButtonUnidade;
    private Spinner categoria;
    private Produto produto = null;
    private ProdutoDAO dao;
    private List<Produto> produtos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        inicializaCampos();

        mostrarActionBarCustomizado();

        dao = new ProdutoDAO(this);

        produtos = dao.listarTodos();

        Intent it = getIntent();
        if (it.hasExtra("produto")){
            setTitle("Editar Item");
            setCamposParaAtualizar(it);
        }

    }

    private void setCamposParaAtualizar(Intent it) {
        produto = (Produto)it.getSerializableExtra("produto");

        nome.setText(produto.getNome());
        preco.setText(String.valueOf(produto.getPreco()));
        quantidade.setText(String.valueOf(produto.getQuantidade()));

        //precisei criar um método para settar o valor que veio como string e na view é um spinner
        categoria.setSelection(spinnerSelectedByValue(categoria, produto.getCategoria()));

        unidade.check(percorreRadioGroup(produto.getUnidade()));
    }

    private void inicializaCampos() {
        nome = (EditText) findViewById(R.id.editNome);
        preco = (EditText) findViewById(R.id.editPreco);
        quantidade = (EditText) findViewById(R.id.editQuantidade);
        categoria = (Spinner)findViewById(R.id.spinnerCategoria);
        unidade = (RadioGroup)findViewById(R.id.radioUnidade);
    }

    private void mostrarActionBarCustomizado() {
        LayoutInflater inflator = (LayoutInflater)this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v = inflator.inflate(R.layout.activity_formulario_botao_voltar, null);

        ActionBar actionBar = getSupportActionBar();
        //posiciona o boão a esquerda
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("Novo Item");

        actionBar.setCustomView(v);

    }

    /**
     * Implementação das funções dos botões da tela formulário
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int activity_formulario_botao_voltar = 16908332; //16908332: id da view: menu_activity_formulario_botao_voltar

        switch (item.getItemId()) {
            case activity_formulario_botao_voltar:

                finish();

                return true;

            case R.id.menu_activity_formulario_botao_salvar:

                salvar();

            default:

                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * RETORNA A STRING DO RADIOBUTTON SELECIONADO P/ SALVAR
     */
    public String getStringRadioButton (){

        //identificando o id do radioButton
        int selectedId = unidade.getCheckedRadioButtonId();
        //linkando o radioButton selecionado a variável radioButtonTipo
        radioButtonUnidade = (RadioButton)findViewById(selectedId);
        //Convertendo o valor do radioButton para String
        String txt = (String) radioButtonUnidade.getText();

        return txt;

    }

    /**
     * PERCORRE OS BUTTONS DO RADIOGROUP
     */
    public int percorreRadioGroup(String string){

        //unidade.getChildCount(): quantidade de radiobuttons
        for (int i = 0; i < unidade.getChildCount() ; i++){

            //unidade.getChildAt(i).getId(): passa pelo id de todos os botões
            radioButtonUnidade = (RadioButton)findViewById(unidade.getChildAt(i).getId());

            if (radioButtonUnidade.getText().equals(string)){

                return unidade.getChildAt(i).getId();
            }
        }

        return 0;
    }

    /*
        MÉTODO PARA CRIAR (INFLAR) UM MENU na barra de menu
     */
    public boolean onCreateOptionsMenu (Menu menu){

        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.menu_activity_formulario, menu);

        return true;
    }

    //MÉTODO DO MENU VOLTAR
    public void voltar (MenuItem item){
        finish();
    }

    /*
        MÉTODO PARA SETTAR O VALOR DO SPINNER ATRAVÉS DE UMA STRING (EU CRIEI O MÉTODO!)
     */
    public int spinnerSelectedByValue (Spinner s, String value){
        for (int i = 0; i < s.getCount(); i++){
            if (s.getItemAtPosition(i).toString().equalsIgnoreCase(value)){
                return i;
            }
        }
        return 0;
    }

    /**
     * FUNÇÃO PARA VALIDAR CAMPOS
     */
    private boolean isCampoVazio (String campo) {
        /*
            TextUtils.isEmpty(campo): verifica se o campo está vazio. não considera espaço como vazio
            campo.trim().isEmpty(): considera espaço como vazio
         */
        boolean resultado = (TextUtils.isEmpty(campo) || campo.trim().isEmpty());

        return resultado;
    }

    private boolean verificaNomes (String verificaNome){

        if (isCampoVazio(verificaNome)){

            nome.requestFocus();

            AlertDialog aviso = new AlertDialog.Builder(this).
                    setTitle("Aviso").
                    setMessage("O campo nome está em branco!").
                    setNeutralButton("Ok", null).
                    create();
            aviso.show();

            return false;

        }else{

            for (Produto produtoAux : produtos){

                if (verificaNome.equalsIgnoreCase(produtoAux.getNome())){
                    AlertDialog aviso = new AlertDialog.Builder(this).
                            setTitle("Aviso").
                            setMessage("Você já tem este item na lista!").
                            setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    nome.requestFocus();
                                }
                            }).
                            create();
                    aviso.show();
                    return false;

                }
            }

            return true;
        }

    }
    /*
        METODO SALVAR
     */
    public void salvar (){

        produtos = dao.listarTodos();

        if (produto == null){

            Produto p = new Produto();

            p.setNome(nome.getText().toString());

            //Se não for preenchido, setto um vazor por padrão
            if (isCampoVazio(preco.getText().toString())){
                p.setPreco(0);
            }else{
                p.setPreco(Double.parseDouble(preco.getText().toString()));
            }

            //Se não for preenchido, setto um vazor por padrão
            if (isCampoVazio(quantidade.getText().toString())){
                p.setQuantidade(0);
            }else{
                p.setQuantidade(Double.parseDouble(quantidade.getText().toString()));
            }

            p.setStatus(false);
            p.setCusto(p.getPreco()*p.getQuantidade());
            p.setCategoria(categoria.getSelectedItem().toString());
            p.setUnidade(getStringRadioButton());

            if (verificaNomes(nome.getText().toString())) {
                try{
                    dao.salvar(p);
                    Toast.makeText(this, "Produto inserido na lista de compras!", Toast.LENGTH_SHORT).show();

                    nome.setText(null);
                    preco.setText(null);
                    quantidade.setText(null);
                    unidade.check(R.id.radioButtonUnidade);
                    categoria.setSelection(0);

                }catch (Exception e){
                    Toast.makeText(this, "Algo deu errado! Verifique se o formulários foi preenchido corretamente. \n" +
                            "Erro: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

        /*
            Caso seja para ATUALIZAR UM PRODUTO
         */
        }else{

            produto.setNome(nome.getText().toString());
            produto.setPreco(Double.parseDouble(preco.getText().toString()));
            produto.setQuantidade(Double.parseDouble(quantidade.getText().toString()));
            produto.setCategoria(categoria.getSelectedItem().toString());
            produto.setUnidade(getStringRadioButton());
            produto.setCusto(produto.getPreco() * produto.getQuantidade());

            try{
                dao.atualizar(produto);
                Toast.makeText(this, "Produto atualizado!" + String.valueOf(produto.getCusto()), Toast.LENGTH_SHORT).show();

                finish();

            }catch (Exception e){
                Toast.makeText(this, "Algo deu errado! Verifique se o formulários foi preenchido corretamente. \n" +
                        "Erro: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }

}
