package br.edu.utfpr.heliobrandao.viniveritas;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
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
import androidx.appcompat.view.ActionMode;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.edu.utfpr.heliobrandao.viniveritas.modelo.Tipo;
import br.edu.utfpr.heliobrandao.viniveritas.modelo.Vinho;
import br.edu.utfpr.heliobrandao.viniveritas.persistencia.VinhosDatabase;
import br.edu.utfpr.heliobrandao.viniveritas.utils.UtilsAlert;

public class VinhosActivity extends AppCompatActivity {

    private RecyclerView recyclerViewVinhos;
    private RecyclerView.LayoutManager layoutManager;
    private VinhoRecyclerViewAdapter vinhoRecyclerViewAdapter;
    private List<Vinho> listaVinhos;
    private int posicaoVinhoSelecionado = -1;
    private ActionMode actionMode;

    private View viewSelecionada;
    private Drawable backgroundDrawable;


    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflate = mode.getMenuInflater();
            inflate.inflate(R.menu.vinhos_item_selecionado, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int idMenuItem = item.getItemId();

            if (idMenuItem == R.id.menuItemEditar) {
                editarVinho();
                return true;
            }else
                if (idMenuItem == R.id.menuItemExcluir) {
                    excluirVinho();
                    return true;
                }else{
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

            if (viewSelecionada != null) {
                viewSelecionada.setBackground(backgroundDrawable);
            }
            actionMode = null;
            viewSelecionada = null;
            posicaoVinhoSelecionado = -1;

            recyclerViewVinhos.setEnabled(true);
        }
    };

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

        VinhosDatabase database = VinhosDatabase.getInstance(this);

        if (ordenacaoCrescente) {
            listaVinhos = database.getVinhoDao().queryAllAscending();
        } else {
            listaVinhos = database.getVinhoDao().queryAllDownward();
        }

        Vinho vinho;
        vinhoRecyclerViewAdapter = new VinhoRecyclerViewAdapter(this, listaVinhos);

        vinhoRecyclerViewAdapter.setOnItemClickListener(new VinhoRecyclerViewAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View v, int position) {

                posicaoVinhoSelecionado = position;
                editarVinho();
            }
        });

        vinhoRecyclerViewAdapter.setOnItemLongClickListener(new VinhoRecyclerViewAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View v, int position) {
                if (actionMode != null) {
                    return false;
                }
                posicaoVinhoSelecionado = position;
                viewSelecionada = v;
                backgroundDrawable = v.getBackground();

                v.setBackgroundColor(getColor(R.color.corSelecionado));

                recyclerViewVinhos.setEnabled(false);

                actionMode = startSupportActionMode(actionModeCallback);

                return true;
            }
        });

        recyclerViewVinhos.setAdapter(vinhoRecyclerViewAdapter);
    }

    public void abrirSobre() {

        Intent intentAbertura = new Intent(this, SobreActivity.class);
        startActivity(intentAbertura);
    }

    private void excluirVinho() {
        if (posicaoVinhoSelecionado < 0 || posicaoVinhoSelecionado >= listaVinhos.size()) {
            return; // Posição inválida, não faz nada
        }

        final Vinho vinho = listaVinhos.get(posicaoVinhoSelecionado);

        String mensagem = getString(R.string.deseja_apagar, vinho.getNome());

        DialogInterface.OnClickListener listenerSim = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                VinhosDatabase database = VinhosDatabase.getInstance(VinhosActivity.this);

                int quantidadeAlterada = database.getVinhoDao().delete(vinho);

                if (quantidadeAlterada != 1) {
                    UtilsAlert.mostrarAviso(VinhosActivity.this,
                                                    R.string.erro_ao_tentar_excluir);
                    return;
                }

                listaVinhos.remove(posicaoVinhoSelecionado);
                vinhoRecyclerViewAdapter.notifyItemRemoved(posicaoVinhoSelecionado);
                Toast.makeText(VinhosActivity.this, R.string.vinho_excluído, Toast.LENGTH_LONG).show();
                if (actionMode != null) {
                    actionMode.finish();
                }
            }
        };
        UtilsAlert.confirmarAcao(this, mensagem, listenerSim, null);
    }

    ActivityResultLauncher<Intent> laucherEditarVinho = registerForActivityResult(  new ActivityResultContracts.StartActivityForResult(),
                                                                                    new ActivityResultCallback<ActivityResult>() {
    @Override
    public void onActivityResult(ActivityResult result) {

        if(result.getResultCode() == VinhosActivity.RESULT_OK){

            Intent intent = result.getData();

            Bundle bundle = intent.getExtras();

            if (bundle != null) {

                final Vinho vinhoOriginal = listaVinhos.get(posicaoVinhoSelecionado);

                long id = bundle.getLong(MainActivity.KEY_ID);
                final VinhosDatabase database = VinhosDatabase.getInstance(VinhosActivity.this);
                final Vinho vinhoEditado = database.getVinhoDao().queryForId(id);

                listaVinhos.set(posicaoVinhoSelecionado, vinhoEditado);

                ordenarListaVinhos();

                final ConstraintLayout constraintLayout = findViewById(R.id.main);

                Snackbar snackbar = Snackbar.make(constraintLayout,
                                                    R.string.alteracao_realizada,
                                                    Snackbar.LENGTH_LONG);

                snackbar.setAction(R.string.desfazer, new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        int quantidadeAlterada = database.getVinhoDao().update(vinhoOriginal);

                        if (quantidadeAlterada != 1) {
                            UtilsAlert.mostrarAviso(VinhosActivity.this,
                                    R.string.erro_ao_tentar_alterar);
                            return;
                        }

                        listaVinhos.remove(vinhoEditado);;
                        listaVinhos.add(vinhoOriginal);;

                        ordenarListaVinhos();
                    }

                });

                snackbar.show();
            }
        }
        posicaoVinhoSelecionado = -1;

        if (actionMode != null) {
            actionMode.finish();
        }

    }});
    private void editarVinho() {
        Vinho vinho = listaVinhos.get(posicaoVinhoSelecionado);
        Intent intentAbertura = new Intent(this, MainActivity.class);
        intentAbertura.putExtra(MainActivity.KEY_MODO, MainActivity.MODO_EDITAR);

        intentAbertura.putExtra(MainActivity.KEY_ID, vinho.getId());


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
                            long id = bundle.getLong(MainActivity.KEY_ID);

                            VinhosDatabase database = VinhosDatabase.getInstance(VinhosActivity.this);

                            Vinho vinho = database.getVinhoDao().queryForId(id);

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
                        confirmarRestaurarPadroes();
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

    private void confirmarRestaurarPadroes() {
        String mensagem = getString(R.string.deseja_restaurar_padroes_originais);

        DialogInterface.OnClickListener listenerSim = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                restaurarPadroes();
                atualizarIconeOrdenacao();
                ordenarListaVinhos();

                Toast.makeText(VinhosActivity.this,
                                        R.string.as_configuracoes_foram_restaurada,
                                        Toast.LENGTH_LONG).show();
            }
        };
        UtilsAlert.confirmarAcao(this, mensagem, listenerSim, null);
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
