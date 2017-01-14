package bol.xavier.gestionscore.screen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import bol.xavier.gestionscore.R;
import bol.xavier.gestionscore.asynchronoustask.Async_AfficherJeux;
import bol.xavier.gestionscore.asynchronoustask.Async_addScore;

public class AjoutScoreActivity extends AppCompatActivity {
    private final int NUM_REQUETE = 1;
    private EditText et_nameGame = null;
    private TextView tv_error = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_score);

        Intent intent_recup = getIntent();
        final int id_user = intent_recup.getIntExtra("id_user", 0);

        final EditText et_nameGame;
        final EditText et_score;
        Button btn_wizard, btn_ajouterScore;

        et_nameGame = (EditText)findViewById(R.id.et_nameGame);
        et_score = (EditText)findViewById(R.id.et_score);
        btn_wizard = (Button)findViewById(R.id.btn_wizard);
        btn_ajouterScore = (Button)findViewById(R.id.btn_ajouterScore);

        btn_wizard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Async_AfficherJeux(AjoutScoreActivity.this).execute();
            }
        });

        btn_ajouterScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Async_addScore(AjoutScoreActivity.this).execute(et_score.getText().toString(), et_nameGame.getText().toString(), Integer.toString(id_user));
            }
        });
    }

    protected void onActivityResult(int num_requete, int code_retour, Intent intent) {
        et_nameGame = (EditText)findViewById(R.id.et_nameGame);
        if (num_requete == NUM_REQUETE)
        {
            if (code_retour == RESULT_OK)
            {
                String nom_jeu = intent.getStringExtra("nomJeu");
                et_nameGame.setText(nom_jeu);
            }
            else {
                et_nameGame.setText("Erreur avec le wizard");
            }
        }
    }

    public void populate (int[] result) {
        tv_error = (TextView)findViewById(R.id.tv_errorAjoutScore);
        switch (result[0])
        {
            case 0:
                Intent intent = new Intent(AjoutScoreActivity.this, HomepageActivity.class);
                intent.putExtra("id_user", result[1]);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case 100:
                tv_error.setText(R.string.ajout_score_code_100);
                break;
            case 110:
                tv_error.setText(R.string.ajout_score_code_110);
                break;
            case 120:
                tv_error.setText(R.string.ajout_score_code_120);
                break;
            case 1000:
                tv_error.setText(R.string.ajout_score_code_1000);
                break;
            default:
                tv_error.setText(R.string.ajout_score_code_2000);
        }
    }

    public void populate_wizard(String[] result)
    {
        tv_error = (TextView)findViewById(R.id.tv_errorAjoutScore);
        int code;
        String jeux;

        code = Integer.parseInt(result[0]);
        jeux = result[1];

        switch (code){
            case 0:
                if(!jeux.equals(""))
                {
                    Intent intent = new Intent(AjoutScoreActivity.this, WizardActivity.class);
                    intent.putExtra("listjeux", jeux);
                    startActivityForResult(intent, NUM_REQUETE);
                }
                else
                {
                    tv_error.setText("Il existe encore aucun jeu dans la base de données");
                }
                break;
            case 500:
                tv_error = new TextView(this);
                tv_error.setText(R.string.list_jeux_code_500);
                break;
            case 1000:
                tv_error = new TextView(this);
                tv_error.setText(R.string.list_jeux_code_1000);
                break;
            default:
                tv_error = new TextView(this);
                tv_error.setText(R.string.list_jeux_code_2000);
        }
    }
}
