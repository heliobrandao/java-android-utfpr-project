package br.edu.utfpr.heliobrandao.viniveritas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Import adicionado para o Enum Tipo
import br.edu.utfpr.heliobrandao.viniveritas.Tipo;

public class VinhosActivity extends AppCompatActivity {

    private RecyclerView recyclerViewVinhos;
    private RecyclerView.LayoutManager layoutManager;
    private VinhoRecyclerViewAdapter vinhoRecyclerViewAdapter;
    private VinhoRecyclerViewAdapter.OnItemClickListener onItemClickListener;

    private List<Vinho> listaVinhos;

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

        onItemClickListener = new VinhoRecyclerViewAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {

                Vinho vinho = listaVinhos.get(position);

                Toast.makeText(getApplicationContext(),
                        "Vinho selecionado: " + vinho.getNome() + " (Safra " + vinho.getSafra() + ")",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {

                Vinho vinho = listaVinhos.get(position);

                Toast.makeText(getApplicationContext(),
                        "Vinho " + vinho.getNome() + " recebeu um clique longo",
                        Toast.LENGTH_LONG).show();
            }
        };

        carregarDadosVinhos();
    }

    private void carregarDadosVinhos() {

//        String[] nomes = getResources().getStringArray(R.array.nomes_vinhos);
//        String[] safras = getResources().getStringArray(R.array.safras_vinhos);
//        String[] paises = getResources().getStringArray(R.array.paises_vinhos);
//        String[] tipos = getResources().getStringArray(R.array.tipos_vinhos);
//        String[] caracteristicasArray = getResources().getStringArray(R.array.caracteristicas_vinhos);
//        String[] comentarios = getResources().getStringArray(R.array.comentarios_vinhos);

        listaVinhos = new ArrayList<>();

        Vinho vinho;

//        for (int cont = 0; cont < nomes.length; cont++) {
//
//            String nome = nomes[cont];
//            int safra = Integer.parseInt(safras[cont]);
//            String pais = paises[cont];
//            // Corrigido para usar o Tipo importado
//            Tipo tipo = Tipo.valueOf(tipos[cont]);
//
//            // Processa caracteristicas e separa por |
//            List<String> caracteristicas = new ArrayList<>();
//            if (caracteristicasArray[cont] != null && !caracteristicasArray[cont].isEmpty()) {
//                String[] caracs = caracteristicasArray[cont].split("\\|");
//                caracteristicas = Arrays.asList(caracs);
//            }
//
//            String comentario = comentarios[cont];
//
//            vinho = new Vinho(nome, safra, pais, tipo, caracteristicas, comentario);
//
//            listaVinhos.add(vinho);
//        }

        vinhoRecyclerViewAdapter = new VinhoRecyclerViewAdapter(this, listaVinhos, onItemClickListener);

        recyclerViewVinhos.setAdapter(vinhoRecyclerViewAdapter);
    }

    public void abrirSobre(View view) {

        Intent intentAbertura = new Intent(this, SobreActivity.class);
        startActivity(intentAbertura);
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
                            vinhoRecyclerViewAdapter.notifyDataSetChanged();
                        }

                }
            }});
    public void abrirNovoVinho(View view) {

        Intent intentAbertura = new Intent(this, MainActivity.class);
        laucherNovoVinho.launch(intentAbertura);

    }
}
