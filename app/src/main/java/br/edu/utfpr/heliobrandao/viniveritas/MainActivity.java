package br.edu.utfpr.heliobrandao.viniveritas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import br.edu.utfpr.heliobrandao.viniveritas.modelo.Tipo;
import br.edu.utfpr.heliobrandao.viniveritas.modelo.Vinho;
import br.edu.utfpr.heliobrandao.viniveritas.persistencia.VinhosDatabase;
import br.edu.utfpr.heliobrandao.viniveritas.utils.UtilsAlert;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_MODO = "MODO";
    public static final String KEY_ID = "ID";
    public final String KEY_SUGERIR_PAIS = "SUGERIR_PAIS";
    public final String KEY_ULTIMO_PAIS_SELECIONADO = "ULTIMO_PAIS_SELECIONADO";
    public static final int MODO_NOVO = 0;
    public static final int MODO_EDITAR = 1;

    private EditText editTextNomeVinho;
    private EditText editTextSafra;
    private EditText editTextComentarios;
    private Spinner spinnerPais;
    private RadioGroup radioGroupTipo;
    private RadioButton radioButtonTinto, radioButtonBranco, radioButtonRose, radioButtonEspumante;
    private CheckBox checkBoxSeco, checkBoxDoce, checkBoxFrutado, checkBoxTanico;
    private int modo;
    private Vinho vinhoOriginal;
    private boolean sugerirPais = false;
    private int ultimoPaisSelecionado = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.scrollView), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        editTextNomeVinho = findViewById(R.id.editTextNomeVinho);
        editTextSafra = findViewById(R.id.editTextSafra);
        editTextComentarios = findViewById(R.id.editTextComentarios);
        spinnerPais = findViewById(R.id.spinnerPais);
        radioGroupTipo = findViewById(R.id.radioGroupTipo);
        checkBoxSeco = findViewById(R.id.checkBoxSeco);
        checkBoxDoce = findViewById(R.id.checkBoxDoce);
        checkBoxFrutado = findViewById(R.id.checkBoxFrutado);
        checkBoxTanico = findViewById(R.id.checkBoxTanico);
        radioButtonTinto = findViewById(R.id.radioTinto);
        radioButtonBranco = findViewById(R.id.radioBranco);
        radioButtonRose = findViewById(R.id.radioRose);
        radioButtonEspumante = findViewById(R.id.radioEspumante);

        lerPreferencias();

        Intent intentAbertura = getIntent();
        Bundle bundle = intentAbertura.getExtras();

        if (bundle != null) {
            modo = bundle.getInt(KEY_MODO);

            if (modo == MODO_NOVO){
                setTitle(getString(R.string.novo_vinho));
                if (sugerirPais) {
                    spinnerPais.setSelection(ultimoPaisSelecionado);
                }
            } else {
                setTitle(getString(R.string.editar_vinho));

                long id = bundle.getLong(KEY_ID);

                VinhosDatabase database = VinhosDatabase.getInstance(this);
                vinhoOriginal = database.getVinhoDao().queryForId(id);


                editTextNomeVinho.setText(vinhoOriginal.getNome());
                editTextSafra.setText(String.valueOf(vinhoOriginal.getSafra()));;
                editTextComentarios.setText(vinhoOriginal.getComentarios());

                spinnerPais.setSelection(((ArrayAdapter)spinnerPais.getAdapter()).getPosition(vinhoOriginal.getPais()));

                Tipo tipoVinho = Tipo.valueOf(vinhoOriginal.getTipo().name());

                if (tipoVinho == Tipo.Tinto) {
                    radioButtonTinto.setChecked(true);
                } else if (tipoVinho == Tipo.Branco) {
                    radioButtonBranco.setChecked(true);
                } else if (tipoVinho == Tipo.Rosé) {
                    radioButtonRose.setChecked(true);
                } else if (tipoVinho == Tipo.Espumante) {
                    radioButtonEspumante.setChecked(true);
                }
                checkBoxSeco.setChecked(vinhoOriginal.getCaracteristicas().contains(getString(R.string.caracteristica_seco)));
                checkBoxDoce.setChecked(vinhoOriginal.getCaracteristicas().contains(getString(R.string.caracteristica_doce)));
                checkBoxFrutado.setChecked(vinhoOriginal.getCaracteristicas().contains(getString(R.string.caracteristica_frutado)));
                checkBoxTanico.setChecked(vinhoOriginal.getCaracteristicas().contains(getString(R.string.caracteristica_tanico)));

                editTextComentarios.setText(vinhoOriginal.getComentarios());

                editTextNomeVinho.requestFocus();
                editTextNomeVinho.setSelection(editTextNomeVinho.getText().length());

            }
        }
    }

    private void limparFormulario() {

        final String nomeVinho = editTextNomeVinho.getText().toString().trim();
        final String safra = editTextSafra.getText().toString().trim();
        final String comentarios = editTextComentarios.getText().toString().trim();
        final int paisSelecionado = spinnerPais.getSelectedItemPosition();
        final int radioSelecionado = radioGroupTipo.getCheckedRadioButtonId();
        final boolean seco = checkBoxSeco.isChecked();
        final boolean doce = checkBoxDoce.isChecked();
        final boolean frutado = checkBoxFrutado.isChecked();
        final boolean tanico = checkBoxTanico.isChecked();

        final ScrollView scrollView = findViewById(R.id.scrollView);
        final View viewComFoco = scrollView.findFocus();

        Snackbar snackbar = Snackbar.make(scrollView,
                                            R.string.formulario_limpo,
                                            Snackbar.LENGTH_LONG);
        editTextNomeVinho.setText("");
        editTextSafra.setText("");
        editTextComentarios.setText("");
        spinnerPais.setSelection(0);
        radioGroupTipo.clearCheck();
        checkBoxSeco.setChecked(false);
        checkBoxDoce.setChecked(false);
        checkBoxFrutado.setChecked(false);
        checkBoxTanico.setChecked(false);

        snackbar.setAction(R.string.desfazer, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editTextNomeVinho.setText(nomeVinho);
                editTextSafra.setText(safra);
                editTextComentarios.setText(comentarios);
                spinnerPais.setSelection(paisSelecionado);
                if (radioSelecionado == R.id.radioTinto) {
                    radioButtonTinto.setChecked(true);
                } else if (radioSelecionado == R.id.radioBranco) {
                    radioButtonBranco.setChecked(true);
                } else if (radioSelecionado == R.id.radioRose) {
                    radioButtonRose.setChecked(true);
                } else if (radioSelecionado == R.id.radioEspumante) {
                    radioButtonEspumante.setChecked(true);
                }
                checkBoxSeco.setChecked(seco);
                checkBoxDoce.setChecked(doce);
                checkBoxFrutado.setChecked(frutado);
                checkBoxTanico.setChecked(tanico);

                if (viewComFoco != null) {
                    viewComFoco.requestFocus();
                } else {
                    editTextNomeVinho.requestFocus();
                }
            }
        });
        snackbar.show();
    }

    private void salvarFormulario() {
        String nomeVinho = editTextNomeVinho.getText().toString().trim();
        String safra = editTextSafra.getText().toString().trim();

        if (nomeVinho.isEmpty()) {
            UtilsAlert.mostrarAviso(this, R.string.erro_nome_vazio);
            editTextNomeVinho.requestFocus();
            return;
        }

        if (safra.isEmpty()) {
            UtilsAlert.mostrarAviso(this, R.string.erro_safra_vazia);
            editTextSafra.requestFocus();
            return;
        }

        if (spinnerPais.getSelectedItemPosition() == 0) {
            UtilsAlert.mostrarAviso(this, R.string.erro_pais_nao_selecionado);
            return;
        }

        int radioSelecionado = radioGroupTipo.getCheckedRadioButtonId();


        Tipo tipoEnum;
        if (radioSelecionado == R.id.radioTinto) {
            tipoEnum = Tipo.Tinto;
        } else if (radioSelecionado == R.id.radioBranco) {
            tipoEnum = Tipo.Branco;
        } else if (radioSelecionado == R.id.radioRose) {
            tipoEnum = Tipo.Rosé;
        } else if (radioSelecionado == R.id.radioEspumante) {
            tipoEnum = Tipo.Espumante;
        } else {
            Log.d("DEBUG", "Nenhum tipo válido selecionado - ID: " + radioSelecionado);

            UtilsAlert.mostrarAviso(this, R.string.selecione_tipo_vinho);
            return;
        }


        String paisSelecionado = spinnerPais.getSelectedItem().toString();

        //aqui criamos uma lista de caracteristicas utilizando ArrayList e checkboxes
        //sem validacao nesse caso
        List<String> caracteristicas = new ArrayList<>();
        if (checkBoxSeco.isChecked()) caracteristicas.add(getString(R.string.caracteristica_seco));
        if (checkBoxDoce.isChecked()) caracteristicas.add(getString(R.string.caracteristica_doce));
        if (checkBoxFrutado.isChecked()) caracteristicas.add(getString(R.string.caracteristica_frutado));
        if (checkBoxTanico.isChecked()) caracteristicas.add(getString(R.string.caracteristica_tanico));
        if (caracteristicas.isEmpty()) {
            caracteristicas.add(getString(R.string.nenhuma_caracteristica_selecionada));
        }

        String comentarios = editTextComentarios.getText().toString().trim();

        StringBuilder mensagem = new StringBuilder();
        mensagem.append(getString(R.string.vinho_dados_nome)).append(nomeVinho).append("\n");
        mensagem.append(getString(R.string.vinho_dados_safra)).append(safra).append("\n");
        mensagem.append(getString(R.string.vinho_dados_pais)).append(paisSelecionado).append("\n");
        mensagem.append(getString(R.string.vinho_dados_tipo)).append(tipoEnum.toString());

        if (!caracteristicas.isEmpty()) {
            mensagem.append("\n").append(getString(R.string.vinho_dados_caracteristicas));
            for (int i = 0; i < caracteristicas.size(); i++) {
                mensagem.append(caracteristicas.get(i));
                if (i < caracteristicas.size() - 1) {
                    mensagem.append(", ");
                }
            }
        }

        if (!comentarios.isEmpty()) {
            mensagem.append("\n").append(getString(R.string.vinho_dados_comentarios)).append(comentarios);
        }

        Vinho vinho = new Vinho(nomeVinho, Integer.parseInt(safra), paisSelecionado, tipoEnum, caracteristicas, comentarios);

        if (vinho.equals(vinhoOriginal)) {
            setResult(MainActivity.RESULT_CANCELED);
            finish();
            return;
        };

        Intent intentResposta = new Intent();


        VinhosDatabase database = VinhosDatabase.getInstance(this);
        if (modo == MODO_NOVO) {

            long novoId = database.getVinhoDao().insert(vinho);
            if (novoId == -1) {
                UtilsAlert.mostrarAviso(this, R.string.erro_ao_tentar_salvar);
                return;
            }

            vinho.setId(novoId);

        } else {

            vinho.setId(vinhoOriginal.getId());
            int quantidadeAlterada = database.getVinhoDao().update(vinho);

            if (quantidadeAlterada == 0) {
                UtilsAlert.mostrarAviso(this, R.string.erro_ao_tentar_alterar);
                return;
            }

        }

        salvarUltimoPaisSelecionado(spinnerPais.getSelectedItemPosition());

        intentResposta.putExtra(KEY_ID, vinho.getId());

        setResult(MainActivity.RESULT_OK, intentResposta);

        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.vinho_opcoes, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem itemSugerirPais = menu.findItem(R.id.menuItemSugerirPais);
        itemSugerirPais.setChecked(sugerirPais);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int idMenuItem = item.getItemId();

        if (idMenuItem == R.id.menuItemSalvar) {
            salvarFormulario();
            return true;
        }else
            if (idMenuItem == R.id.menuItemLimpar) {
                limparFormulario();
                return true;
            }else
                if (idMenuItem == R.id.menuItemSugerirPais) {
                    boolean valor = !item.isChecked();

                    salvarSugerirPais(valor);
                    item.setChecked(valor);

                    if (sugerirPais){
                        spinnerPais.setSelection(ultimoPaisSelecionado);
                    }

                    return true;
                }else{
                    return super.onOptionsItemSelected(item);
                }
    }

    private void lerPreferencias() {
        SharedPreferences shared = getSharedPreferences(VinhosActivity.ARQUIVO_PREFERENCIAS, Context.MODE_PRIVATE);

        sugerirPais = shared.getBoolean(KEY_SUGERIR_PAIS, sugerirPais);
        ultimoPaisSelecionado = shared.getInt(KEY_ULTIMO_PAIS_SELECIONADO, ultimoPaisSelecionado);
    }

    private void salvarSugerirPais(boolean novoValor) {
        SharedPreferences shared = getSharedPreferences(VinhosActivity.ARQUIVO_PREFERENCIAS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();

        editor.putBoolean(KEY_SUGERIR_PAIS, novoValor);
        editor.commit();

        sugerirPais = novoValor;
    }

    private void salvarUltimoPaisSelecionado(int novoValor) {
        SharedPreferences shared = getSharedPreferences(VinhosActivity.ARQUIVO_PREFERENCIAS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();

        editor.putInt(KEY_ULTIMO_PAIS_SELECIONADO, novoValor);
        editor.commit();

        ultimoPaisSelecionado = novoValor;
    }
}