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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

import br.edu.utfpr.heliobrandao.viniveritas.utils.UtilsAlert;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_VINHO_NOME = "KEY_VINHO_NOME";
    public static final String KEY_VINHO_TIPO_VINHO = "KEY_VINHO_TIPO_VINHO";
    public static final String KEY_SAFRA = "KEY_SAFRA";
    public static final String KEY_PAIS = "KEY_PAIS";
    public static final String KEY_CARACTERISTICAS = "KEY_CARACTERISTICAS";
    public static final String KEY_COMENTARIOS = "KEY_COMENTARIOS";
    public static final String KEY_MODO = "MODO";
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
                String nome = bundle.getString(KEY_VINHO_NOME);
                String safra = bundle.getString(KEY_SAFRA);
                String pais = bundle.getString(KEY_PAIS);
                String tipoString = bundle.getString(KEY_VINHO_TIPO_VINHO);
                ArrayList<String> caracteristicas = bundle.getStringArrayList(KEY_CARACTERISTICAS);
                String comentarios = bundle.getString(KEY_COMENTARIOS);

                Tipo tipoVinho = Tipo.valueOf(tipoString);

                editTextNomeVinho.setText(nome);
                editTextSafra.setText(safra);
                editTextComentarios.setText(comentarios);
                spinnerPais.setSelection(((ArrayAdapter)spinnerPais.getAdapter()).getPosition(pais));
                if (tipoVinho == Tipo.Tinto) {
                    radioButtonTinto.setChecked(true);
                } else if (tipoVinho == Tipo.Branco) {
                    radioButtonBranco.setChecked(true);
                } else if (tipoVinho == Tipo.Rosé) {
                    radioButtonRose.setChecked(true);
                } else if (tipoVinho == Tipo.Espumante) {
                    radioButtonEspumante.setChecked(true);
                }
                checkBoxSeco.setChecked(caracteristicas.contains(getString(R.string.caracteristica_seco)));
                checkBoxDoce.setChecked(caracteristicas.contains(getString(R.string.caracteristica_doce)));
                checkBoxFrutado.setChecked(caracteristicas.contains(getString(R.string.caracteristica_frutado)));
                checkBoxTanico.setChecked(caracteristicas.contains(getString(R.string.caracteristica_tanico)));
                editTextComentarios.setText(comentarios);
            }
        }
    }

    private void limparFormulario() {
        editTextNomeVinho.setText("");
        editTextSafra.setText("");
        editTextComentarios.setText("");
        spinnerPais.setSelection(0);
        radioGroupTipo.clearCheck();
        checkBoxSeco.setChecked(false);
        checkBoxDoce.setChecked(false);
        checkBoxFrutado.setChecked(false);
        checkBoxTanico.setChecked(false);

        Toast.makeText(
                this,
                getString(R.string.formulario_limpo),
                Toast.LENGTH_LONG).show();
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

        salvarUltimoPaisSelecionado(spinnerPais.getSelectedItemPosition());

        Intent intentResposta = new Intent();
        intentResposta.putExtra(KEY_VINHO_NOME, nomeVinho);
        intentResposta.putExtra(KEY_SAFRA, safra);
        intentResposta.putExtra(KEY_PAIS, paisSelecionado);
//        intentResposta.putExtra(KEY_VINHO_TIPO_VINHO, tipoVinho);
        intentResposta.putExtra(KEY_VINHO_TIPO_VINHO, tipoEnum.name());
        intentResposta.putStringArrayListExtra(KEY_CARACTERISTICAS, (ArrayList<String>) caracteristicas);
        intentResposta.putExtra(KEY_COMENTARIOS, comentarios);

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