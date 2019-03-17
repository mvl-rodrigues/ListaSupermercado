package com.example.listadesupermercado.ui.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.listadesupermercado.util.ListaUtil;
import com.example.listadesupermercado.model.Produto;
import com.example.listadesupermercado.ui.adapter.ProdutoAdapter;
import com.example.listadesupermercado.dao.ProdutoDAO;
import com.example.listadesupermercado.R;

import java.util.ArrayList;
import java.util.List;

public class ListaActivity extends AppCompatActivity {

    private ListView listView;
    private ProdutoDAO dao;
    private List<Produto> produtos;
    private List<Produto> produtosAuxiliar = new ArrayList<>();
    private Produto produto = null;
    private TextView QtdItens;
    private TextView custoTotal;
    private Spinner ordenar;
    private ListaUtil listaUtil;
    private FloatingActionButton fab;
    private TextView custoSubtatol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);
        setTitle("Lista");

        inicializaCampos();

        setListaAdaptada();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                atualizaStatusCompra(position);
            }
        });

        /*
            Ação do spinner que ordena a lista pela categoria
         */
        ordenar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ordenaListaPorCategoria();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                novoItem();
            }
        });

        /*
            Verificando a quantidade de itens que ainda falta comprar
         */
        QtdItens.setText(String.valueOf(listaUtil.calcItens(produtosAuxiliar)));
        custoTotal.setText(listaUtil.calcValor(produtosAuxiliar));
        custoSubtatol.setText(listaUtil.calcValorSubtotaL(produtosAuxiliar));

        registerForContextMenu(listView);
    }

    private void novoItem() {
        startActivity(new Intent(ListaActivity.this, FormularioActivity.class));
    }

    private void ordenaListaPorCategoria() {
        if (!ordenar.getSelectedItem().toString().equalsIgnoreCase("Todos")){
            ordenarList(ordenar.getSelectedItem().toString());
            Toast.makeText(ListaActivity.this, "Categoria selecionada: " + ordenar.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
        }else{
            onResume();
        }
    }

    private void setListaAdaptada() {
        dao = new ProdutoDAO(this);

        produtos = dao.listarTodos();

        produtosAuxiliar.addAll(produtos);

        ProdutoAdapter adaptador = new ProdutoAdapter(this, produtosAuxiliar);

        listView.setAdapter(adaptador);
    }

    private void atualizaStatusCompra(int position) {
        //Produto atualizarStatus;
        if (produtosAuxiliar.get(position).isStatus()){
            produto = produtosAuxiliar.get(position);
            produto.setStatus(false);
            dao.atualizar(produto);
        }else{
            produto = produtosAuxiliar.get(position);
            produto.setStatus(true);
            dao.atualizar(produto);
        }

        onResume();
    }

    private void inicializaCampos() {
        listView = (ListView) findViewById(R.id.listProdutos);
        QtdItens = (TextView) findViewById(R.id.textQtdItens);
        custoTotal = (TextView) findViewById(R.id.textCustoTotal);
        ordenar = (Spinner) findViewById(R.id.spinnerOrdenar);
        custoSubtatol = (TextView) findViewById(R.id.textCustoSubtotal);

        fab = (FloatingActionButton)findViewById(R.id.activity_lista_fab_novo_item);
        listaUtil = new ListaUtil();
    }

    public void ordenarList (String valor){

        produtosAuxiliar.clear();

        for (Produto p: produtos){
            //contains(): compara para ver se acha nomes que contenham algo da string de busca passada pelo usuário
            if(p.getCategoria().contains(valor)){
                produtosAuxiliar.add(p);
            }
        }

        //invalidateView: para restartar o lista so com os resultados da busca
        listView.invalidateViews();

    }

    /*
        INFLATOR: "infla" o meu menu nessa activity
     */
    public boolean onCreateOptionsMenu (Menu menu){

        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.menu_activity_lista, menu);

        /*
            PESQUISA: implementação da função do menu de pesquisa (searchview)
         */
        SearchView sv = (SearchView)menu.findItem(R.id.menu_procurar).getActionView();

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                procurarProduto(s);

                return false;
            }
        });

        return true;
    }

    /*
        INFLATOR: "infla" um menu de contexto para cada item clickado da lista de produtos
     */
    public void onCreateContextMenu (ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflar = getMenuInflater();

        //passo o endereço do menu que será inflado: R.menu.menu_context
        inflar.inflate(R.menu.menu_context, menu);

    }

    /*
        METODO EXECUTADO NO MENU DE PROCURA (SEARCH)
     */
    public void procurarProduto (String nomeProduto){
        produtosAuxiliar.clear();

        for (Produto p: produtos){
            //contains(): compara para ver se acha nomes que contenham algo da string de busca passada pelo usuário
            if(p.getNome().toLowerCase().contains(nomeProduto.toLowerCase())){
                produtosAuxiliar.add(p);
            }
        }

        //invalidateView: para restartar o lista so com os resultados da busca
        listView.invalidateViews();
    }

    public void limparLista (MenuItem item){
        for (Produto p: produtosAuxiliar){
            p.setStatus(false);
            dao.atualizar(p);
        }

        onResume();
    }

    public void apagarLista (MenuItem item){

        AlertDialog ad = new AlertDialog.Builder(this).
                setTitle("Aviso!").
                setMessage("Você realmente deseja apagar toda a sua lista de compras?").
                setNegativeButton("Não", null).
                setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dao.deletarLista("produto");

                        onResume();
                    }
                }).create();

        ad.show();
    }

    /*
        METDOD ATUALIZAR
     */
    public  void atualizar (MenuItem menuItem){
        //cast de menuItem para MenuInfo
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo)menuItem.getMenuInfo();

        final Produto produtoAtualizar = produtosAuxiliar.get(menuInfo.position);

        //intent para ir para outra activity
        Intent it = new Intent(this, FormularioActivity.class);

        //incluir na intent os dados do produto a ser atualizado
        it.putExtra("produto", produtoAtualizar);

        //inicio a minha intent (intenção)
        startActivity(it);
    }

    /*
        MÉTODO EXCLUIR: cria um alarta antes de enviar para o DAO o produto a ser deletado
     */
    public void excluir (MenuItem item){
        //cast da item para contextMenuInfo, para poder pegar as informações do item selecionado
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        //pegando da lista o produto que vou deletar
        final Produto produtoExcluir = produtosAuxiliar.get(menuInfo.position);

        //alarta de confirmação para deletar
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Alerta!")
                .setMessage("Você realmente deseja excluir o item: " + produtoExcluir.getNome()+"?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setNegativeButton("Não", null)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //remover da lista
                        produtosAuxiliar.remove(produtoExcluir);
                        produtos.remove(produtoExcluir);

                        //excluir efetivamento
                        dao.deletar(produtoExcluir);

                        custoTotal.setText(String.valueOf(listaUtil.calcValor(produtosAuxiliar)));
                        //resetar a lista de produtos
                        listView.invalidateViews();
                    }
                    //para criar o alerta
                }).create();

        //mostrar o alertDialog configurado acima
        dialog.show();

    }

    /*
        onResume: reexecuta quando a activity está pausada (onPause) <<ciclo de vida da activity>>
        ou seja, neste método podemos atualizar alguns dados, como os da listView
     */
    @Override
    public void onResume(){
        super.onResume();

        //executa novamente o metodo listar para ver se teve alguma atualização
        produtos = dao.listarTodos();
        //limpa a lista antiga
        produtosAuxiliar.clear();
        //seta os novos valores da lista
        produtosAuxiliar.addAll(produtos);

        /*
            Verificando a quantidade de itens que ainda falta comprar
         */
        QtdItens.setText(String.valueOf(listaUtil.calcItens(produtosAuxiliar)));
        custoTotal.setText(listaUtil.calcValor(produtosAuxiliar));
        custoSubtatol.setText(listaUtil.calcValorSubtotaL(produtosAuxiliar));

        //"reseta" a listView
        listView.invalidateViews();

    }

}
