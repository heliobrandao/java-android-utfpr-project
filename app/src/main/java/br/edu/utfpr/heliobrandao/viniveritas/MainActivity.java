package br.edu.utfpr.heliobrandao.viniveritas;

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

        inicializarComponentes();
        configurarSpinner();
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

    private void configurarSpinner() {
        String[] paises = {
            "Selecione um país",
            "Brasil",
            "França",
            "Itália",
            "Espanha",
            "Portugal",
            "Argentina",
            "Chile",
            "Estados Unidos",
            "Alemanha",
            "Austrália"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
            android.R.layout.simple_spinner_item, paises);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPais.setAdapter(adapter);
    }

    private void configurarBotoes() {
        buttonLimpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limparFormulario();
            }
        });

        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarFormulario();
            }
        });
    }

    private void limparFormulario() {
        // Limpar EditTexts
        editTextNomeVinho.setText("");
        editTextSafra.setText("");
        editTextComentarios.setText("");

        // Resetar Spinner para primeira posição
        spinnerPais.setSelection(0);

        // Desmarcar RadioButtons
        radioGroupTipo.clearCheck();

        // Desmarcar CheckBoxes
        checkBoxSeco.setChecked(false);
        checkBoxDoce.setChecked(false);
        checkBoxFrutado.setChecked(false);
        checkBoxTanico.setChecked(false);

        // Mostrar Toast de confirmação
        Toast.makeText(this, "Formulário limpo com sucesso!", Toast.LENGTH_SHORT).show();
    }

    private void salvarFormulario() {
        String nomeVinho = editTextNomeVinho.getText().toString().trim();
        String safra = editTextSafra.getText().toString().trim();

        if (nomeVinho.isEmpty()) {
            Toast.makeText(this, "Por favor, informe o nome do vinho!", Toast.LENGTH_SHORT).show();
            editTextNomeVinho.requestFocus();
            return;
        }

        if (safra.isEmpty()) {
            Toast.makeText(this, "Por favor, informe a safra!", Toast.LENGTH_SHORT).show();
            editTextSafra.requestFocus();
            return;
        }

        if (spinnerPais.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Por favor, selecione um país!", Toast.LENGTH_SHORT).show();
            return;
        }

        int radioSelecionado = radioGroupTipo.getCheckedRadioButtonId();
        if (radioSelecionado == -1) {
            Toast.makeText(this, "Por favor, selecione o tipo de vinho!", Toast.LENGTH_SHORT).show();
            return;
        }

        String paisSelecionado = spinnerPais.getSelectedItem().toString();

        RadioButton radioButtonSelecionado = findViewById(radioSelecionado);
        String tipoVinho = radioButtonSelecionado.getText().toString();

        List<String> caracteristicas = new ArrayList<>();
        if (checkBoxSeco.isChecked()) caracteristicas.add("Seco");
        if (checkBoxDoce.isChecked()) caracteristicas.add("Doce");
        if (checkBoxFrutado.isChecked()) caracteristicas.add("Frutado");
        if (checkBoxTanico.isChecked()) caracteristicas.add("Tânico");

        String comentarios = editTextComentarios.getText().toString().trim();

        StringBuilder mensagem = new StringBuilder();
        mensagem.append("Vinho: ").append(nomeVinho).append("\n");
        mensagem.append("Safra: ").append(safra).append("\n");
        mensagem.append("País: ").append(paisSelecionado).append("\n");
        mensagem.append("Tipo: ").append(tipoVinho);

        if (!caracteristicas.isEmpty()) {
            mensagem.append("\nCaracterísticas: ");
            for (int i = 0; i < caracteristicas.size(); i++) {
                mensagem.append(caracteristicas.get(i));
                if (i < caracteristicas.size() - 1) {
                    mensagem.append(", ");
                }
            }
        }

        if (!comentarios.isEmpty()) {
            mensagem.append("\nComentários: ").append(comentarios);
        }

        // Mostrar Toast com os dados salvos (limitado a 2 linhas)
        Toast.makeText(this, "Vinho salvo: " + nomeVinho + " (" + tipoVinho + ")",
                      Toast.LENGTH_LONG).show();
    }
}