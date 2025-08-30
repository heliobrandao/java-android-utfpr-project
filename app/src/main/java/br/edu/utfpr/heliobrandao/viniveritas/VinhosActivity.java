package br.edu.utfpr.heliobrandao.viniveritas;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VinhosActivity extends AppCompatActivity {

    private ListView listViewVinhos;
    private VinhoAdapter vinhoAdapter;
    private ArrayList<Vinho> listaVinhos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vinhos);

        // Inicializar a ListView
        listViewVinhos = findViewById(R.id.listViewVinhos);

        // Carregar os dados dos vinhos
        carregarDadosVinhos();

        // Configurar o adapter
        vinhoAdapter = new VinhoAdapter(this, listaVinhos);
        listViewVinhos.setAdapter(vinhoAdapter);

        // Configurar o clique nos itens da lista
        listViewVinhos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Vinho vinhoClicado = listaVinhos.get(position);
                String mensagem = "Vinho selecionado: " + vinhoClicado.getNome() +
                                " (Safra " + vinhoClicado.getSafra() + ")";
                Toast.makeText(VinhosActivity.this, mensagem, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void carregarDadosVinhos() {
        listaVinhos = new ArrayList<>();

        // Obter os arrays de recursos
        String[] nomes = getResources().getStringArray(R.array.nomes_vinhos);
        String[] safras = getResources().getStringArray(R.array.safras_vinhos);
        String[] paises = getResources().getStringArray(R.array.paises_vinhos);
        String[] tipos = getResources().getStringArray(R.array.tipos_vinhos);
        String[] caracteristicasArray = getResources().getStringArray(R.array.caracteristicas_vinhos);
        String[] comentarios = getResources().getStringArray(R.array.comentarios_vinhos);

        // Criar objetos Vinho com os dados dos arrays
        for (int i = 0; i < nomes.length; i++) {
            String nome = nomes[i];
            int safra = Integer.parseInt(safras[i]);
            String pais = paises[i];
            Vinho.Tipo tipo = Vinho.Tipo.valueOf(tipos[i]);

            // Processar características (separadas por |)
            List<String> caracteristicas = new ArrayList<>();
            if (caracteristicasArray[i] != null && !caracteristicasArray[i].isEmpty()) {
                String[] caracs = caracteristicasArray[i].split("\\|");
                caracteristicas = Arrays.asList(caracs);
            }

            String comentario = comentarios[i];

            // Criar e adicionar o vinho à lista
            Vinho vinho = new Vinho(nome, safra, pais, tipo, caracteristicas, comentario);
            listaVinhos.add(vinho);
        }
    }
}
