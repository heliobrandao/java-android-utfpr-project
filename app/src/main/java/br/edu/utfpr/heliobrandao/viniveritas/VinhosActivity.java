package br.edu.utfpr.heliobrandao.viniveritas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VinhosActivity extends AppCompatActivity {

    private RecyclerView recyclerViewVinhos;
    private RecyclerView.LayoutManager layoutManager;
    private VinhoRecyclerViewAdapter vinhoRecyclerViewAdapter;
    private List<Vinho> listaVinhos;
    private int posicaoVinhoSelecionado = -1;
    public static final String ARQUIVO_PREFERENCIAS = "br.edu.utfpr.heliobrandao.viniveritas.PREFERENCIAS";
    public static final boolean DEFAULT_ORDEM_CRESCENTE = true;
    private boolean ordenacaoCrescente = DEFAULT_ORDEM_CRESCENTE;
    public static final String KEY_ORDEM_CRESCENTE = "ORDEM_CRESCENTE";
    private MenuItem menuItemOrdenacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vinhos);

        setTitle(getString(R.string.cat_logo_de_vinhos));

        recyclerViewVinhos = findViewById(R.id.recyclerViewVinhos);

        layoutManager = new LinearLayoutManager(this);

        recyclerViewVinhos.setLayoutManager(layoutManager);
        recyclerViewVinhos.setHasFixedSize(true);
        recyclerViewVinhos.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));

        lerPreferencias();
        carregarDadosVinhos();
    }

    private void carregarDadosVinhos() {

        listaVinhos = new ArrayList<>();
        Vinho vinho;
        vinhoRecyclerViewAdapter = new VinhoRecyclerViewAdapter(this, listaVinhos);

        vinhoRecyclerViewAdapter.setOnCreateContextMenu(new VinhoRecyclerViewAdapter.OnCreateContextMenu() {
            @Override
            public void onCreateContextMenu(ContextMenu menu,
                                            View v,
                                            ContextMenu.ContextMenuInfo menuInfo,
                                            int position,
                                            MenuItem.OnMenuItemClickListener onMenuItemClickListener) {
                getMenuInflater().inflate(R.menu.vinhos_item_selecionado, menu);

                for (int i = 0; i < menu.size(); i++) {
                    menu.getItem(i).setOnMenuItemClickListener(onMenuItemClickListener);
                }
            }
        });

        vinhoRecyclerViewAdapter.setOnContextMenuClickListener(new VinhoRecyclerViewAdapter.OnContextMenuClickListener() {
            @Override
            public boolean onContextMenuItemClick(MenuItem item, int position) {

                int idMenuItem = item.getItemId();

                if (idMenuItem == R.id.menuItemEditar) {
                    editarVinho(position);
                    return true;
                }else
                    if (idMenuItem == R.id.menuItemExcluir) {
                        excluirVinho(position);
                        return true;
                }else{
                    return false;
                }
            }
        });

        vinhoRecyclerViewAdapter.setOnItemClickListener(new VinhoRecyclerViewAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View v, int position) {
                editarVinho(position);
            }
        });

        recyclerViewVinhos.setAdapter(vinhoRecyclerViewAdapter);
    }

    public void abrirSobre() {

        Intent intentAbertura = new Intent(this, SobreActivity.class);
        startActivity(intentAbertura);
    }

    private void excluirVinho(int position) {
        if (position >= 0 && position < listaVinhos.size()) {
            listaVinhos.remove(position);
            vinhoRecyclerViewAdapter.notifyItemRemoved(position);
            Toast.makeText(this, "Vinho excluído", Toast.LENGTH_SHORT).show();
        }
    }

    ActivityResultLauncher<Intent> laucherEditarVinho = registerForActivityResult(  new ActivityResultContracts.StartActivityForResult(),
                                                                                    new ActivityResultCallback<ActivityResult>() {
    @Override
    public void onActivityResult(ActivityResult result) {

        if(result.getResultCode() == VinhosActivity.RESULT_OK){

            Intent intent = result.getData();

            Bundle bundle = intent.getExtras();

            if (bundle != null) {
                String nome = bundle.getString(MainActivity.KEY_VINHO_NOME);
                String safra = bundle.getString(MainActivity.KEY_SAFRA);
                int safraInt = Integer.parseInt(safra);
                String pais = bundle.getString(MainActivity.KEY_PAIS);
                String tipoString = bundle.getString(MainActivity.KEY_VINHO_TIPO_VINHO);
                ArrayList<String> caracteristicas = bundle.getStringArrayList(MainActivity.KEY_CARACTERISTICAS);
                String comentarios = bundle.getString(MainActivity.KEY_COMENTARIOS);

                Vinho vinho = listaVinhos.get(posicaoVinhoSelecionado);
                vinho.setNome(nome);
                vinho.setSafra(safraInt);
                vinho.setPais(pais);

                Tipo tipo = Tipo.valueOf(tipoString);
                vinho.setTipo(tipo);

                vinho.setCaracteristicas(caracteristicas);
                vinho.setComentarios(comentarios);

                ordenarListaVinhos();
            }
        }
        posicaoVinhoSelecionado = -1;

    }});
    private void editarVinho(int position) {
        posicaoVinhoSelecionado = position;
        Vinho vinho = listaVinhos.get(posicaoVinhoSelecionado);
        Intent intentAbertura = new Intent(this, MainActivity.class);
        intentAbertura.putExtra(MainActivity.KEY_MODO, MainActivity.MODO_EDITAR);
        intentAbertura.putExtra(MainActivity.KEY_VINHO_NOME, vinho.getNome());
        intentAbertura.putExtra(MainActivity.KEY_SAFRA, String.valueOf(vinho.getSafra()));
        intentAbertura.putExtra(MainActivity.KEY_PAIS, vinho.getPais());
        intentAbertura.putExtra(MainActivity.KEY_VINHO_TIPO_VINHO, vinho.getTipo().toString());
        intentAbertura.putStringArrayListExtra(MainActivity.KEY_CARACTERISTICAS, (ArrayList<String>) vinho.getCaracteristicas());
        intentAbertura.putExtra(MainActivity.KEY_COMENTARIOS, vinho.getComentarios());

        laucherEditarVinho.launch(intentAbertura);

    }

    ActivityResultLauncher<Intent> laucherNovoVinho = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if(result.getResultCode() == VinhosActivity.RESULT_OK){

                        Intent intent = result.getData();

                        Bundle bundle = intent.getExtras();

                        if (bundle != null) {
                            String nome = bundle.getString(MainActivity.KEY_VINHO_NOME);
                            String safra = bundle.getString(MainActivity.KEY_SAFRA);
                            int safraInt = Integer.parseInt(safra);
                            String pais = bundle.getString(MainActivity.KEY_PAIS);
                            String tipoString = bundle.getString(MainActivity.KEY_VINHO_TIPO_VINHO);
                            ArrayList<String> caracteristicas = bundle.getStringArrayList(MainActivity.KEY_CARACTERISTICAS);
                            String comentarios = bundle.getString(MainActivity.KEY_COMENTARIOS);


                            Vinho vinho = new Vinho(nome, safraInt, pais, Tipo.valueOf(tipoString), caracteristicas, comentarios);

                            listaVinhos.add(vinho);

                            ordenarListaVinhos();
                        }

                }
            }});
    public void abrirNovoVinho() {

        Intent intentAbertura = new Intent(this, MainActivity.class);
        intentAbertura.putExtra(MainActivity.KEY_MODO, MainActivity.MODO_NOVO);
        laucherNovoVinho.launch(intentAbertura);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.vinhos_opcoes, menu);
        menuItemOrdenacao = menu.findItem(R.id.menuItemOrdenacao);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        atualizarIconeOrdenacao();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int idMenuItem = item.getItemId();
        if (idMenuItem == R.id.menuItemAdicionar) {
            abrirNovoVinho();
            return true;
        }else
            if (idMenuItem == R.id.menuItemSobre) {
                abrirSobre();
                return true;
            }else
                if (idMenuItem == R.id.menuItemOrdenacao) {
                    salvarPreferencias(!ordenacaoCrescente);
                    atualizarIconeOrdenacao();
                    ordenarListaVinhos();
                    return true;
                }else
                    if (idMenuItem == R.id.menuItemRestaurar) {
                        restaurarPadroes();
                        atualizarIconeOrdenacao();
                        ordenarListaVinhos();
                        Toast.makeText(this, R.string.as_configuracoes_foram_restaurada, Toast.LENGTH_LONG).show();
                        return true;
                    }else
                {
                    return super.onOptionsItemSelected(item);
                }
    }

    private void ordenarListaVinhos() {
        if (ordenacaoCrescente) {
            Collections.sort(listaVinhos, Vinho.ordenacaoCrescente);
        } else {
            Collections.sort(listaVinhos, Vinho.ordenacaoDecrescente);
        }
        vinhoRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void atualizarIconeOrdenacao() {
        if (ordenacaoCrescente) {
            menuItemOrdenacao.setIcon(R.drawable.ic_action_ascending_order);
        } else {
            menuItemOrdenacao.setIcon(R.drawable.ic_action_descending_order);
        }
    }
    private void lerPreferencias() {
        SharedPreferences shared = getSharedPreferences(ARQUIVO_PREFERENCIAS, Context.MODE_PRIVATE);
        ordenacaoCrescente = shared.getBoolean(KEY_ORDEM_CRESCENTE, ordenacaoCrescente);
    }

    private void salvarPreferencias(boolean novoEstado) {
        SharedPreferences shared = getSharedPreferences(ARQUIVO_PREFERENCIAS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putBoolean(KEY_ORDEM_CRESCENTE, novoEstado);
        editor.commit();
        ordenacaoCrescente = novoEstado;
    }

    private void restaurarPadroes() {
        SharedPreferences shared = getSharedPreferences(ARQUIVO_PREFERENCIAS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.clear();
        editor.commit();
        ordenacaoCrescente = DEFAULT_ORDEM_CRESCENTE;
        atualizarIconeOrdenacao();
        ordenarListaVinhos();
    }

}
