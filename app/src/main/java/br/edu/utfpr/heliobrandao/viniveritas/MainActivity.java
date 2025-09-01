package br.edu.utfpr.heliobrandao.viniveritas;

import android.content.Intent;
import android.os.Bundle;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_VINHO_NOME = "KEY_VINHO_NOME";
    public static final String KEY_VINHO_TIPO_VINHO = "KEY_VINHO_TIPO_VINHO";
    public static final String KEY_SAFRA = "KEY_SAFRA";
    public static final String KEY_PAIS = "KEY_PAIS";
    public static final String KEY_CARACTERISTICAS = "KEY_CARACTERISTICAS";
    public static final String KEY_COMENTARIOS = "KEY_COMENTARIOS";
    private EditText editTextNomeVinho;
    private EditText editTextSafra;
    private EditText editTextComentarios;
    private Spinner spinnerPais;
    private RadioGroup radioGroupTipo;
    private CheckBox checkBoxSeco;
    private CheckBox checkBoxDoce;
    private CheckBox checkBoxFrutado;
    private CheckBox checkBoxTanico;
    private Button buttonLimpar;
    private Button buttonSalvar;

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

        setTitle(getString(R.string.novo_vinho));

        inicializarComponentes();
        configurarBotoes();
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
        buttonLimpar = findViewById(R.id.buttonLimpar);
        buttonSalvar = findViewById(R.id.buttonSalvar);
    }

    private void configurarBotoes() {
        buttonLimpar.setOnClickListener(v -> limparFormulario());
        buttonSalvar.setOnClickListener(v -> salvarFormulario());
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
            Toast.makeText(
                    this,
                    getString(R.string.erro_nome_vazio),
                    Toast.LENGTH_LONG).show();
            editTextNomeVinho.requestFocus();
            return;
        }

        if (safra.isEmpty()) {
            Toast.makeText(
                    this,
                    getString(R.string.erro_safra_vazia),
                    Toast.LENGTH_LONG).show();
            editTextSafra.requestFocus();
            return;
        }

        if (spinnerPais.getSelectedItemPosition() == 0) {
            Toast.makeText(
                    this,
                    getString(R.string.erro_pais_nao_selecionado),
                    Toast.LENGTH_LONG).show();
            return;
        }

        int radioSelecionado = radioGroupTipo.getCheckedRadioButtonId();
        if (radioSelecionado == -1) {
            Toast.makeText(
                    this,
                    getString(R.string.erro_tipo_nao_selecionado),
                    Toast.LENGTH_LONG).show();
            return;
        }

        String paisSelecionado = spinnerPais.getSelectedItem().toString();

        RadioButton radioButtonSelecionado = findViewById(radioSelecionado);
        String tipoVinho = radioButtonSelecionado.getText().toString();

        //aqui criamos uma lista de caracteristicas utilizando ArrayList e checkboxes
        List<String> caracteristicas = new ArrayList<>();
        if (checkBoxSeco.isChecked()) caracteristicas.add(getString(R.string.caracteristica_seco));
        if (checkBoxDoce.isChecked()) caracteristicas.add(getString(R.string.caracteristica_doce));
        if (checkBoxFrutado.isChecked()) caracteristicas.add(getString(R.string.caracteristica_frutado));
        if (checkBoxTanico.isChecked()) caracteristicas.add(getString(R.string.caracteristica_tanico));

        String comentarios = editTextComentarios.getText().toString().trim();

        StringBuilder mensagem = new StringBuilder();
        mensagem.append(getString(R.string.vinho_dados_nome)).append(nomeVinho).append("\n");
        mensagem.append(getString(R.string.vinho_dados_safra)).append(safra).append("\n");
        mensagem.append(getString(R.string.vinho_dados_pais)).append(paisSelecionado).append("\n");
        mensagem.append(getString(R.string.vinho_dados_tipo)).append(tipoVinho);

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

        Intent intentResposta = new Intent();
        intentResposta.putExtra(KEY_VINHO_NOME, nomeVinho);
        intentResposta.putExtra(KEY_SAFRA, safra);
        intentResposta.putExtra(KEY_PAIS, paisSelecionado);
        intentResposta.putExtra(KEY_VINHO_TIPO_VINHO, tipoVinho);
        intentResposta.putStringArrayListExtra(KEY_CARACTERISTICAS, (ArrayList<String>) caracteristicas);
        intentResposta.putExtra(KEY_COMENTARIOS, comentarios);

        setResult(MainActivity.RESULT_OK, intentResposta);
        finish();

        //aqui demos preferencia em apenas mostrar o nome do vinho e o tipo do vinho ao salvar
//        String toastMessage = getString(R.string.vinho_salvo_prefixo) + nomeVinho + " (" + tipoVinho + ")";
//        Toast.makeText(
//                this,
//                toastMessage,
//                Toast.LENGTH_LONG).show();
    }
}