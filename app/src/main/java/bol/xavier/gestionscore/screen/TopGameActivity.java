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
import bol.xavier.gestionscore.asynchronoustask.Async_top10;

public class TopGameActivity extends AppCompatActivity {
    private final int NUM_REQUETE = 2;
    EditText et_nameGame = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_game);

        Button btn_wizard, btn_afficherTop;

        et_nameGame = (EditText)findViewById(R.id.et_nomJeu);
        btn_wizard = (Button)findViewById(R.id.btn_wizard2);
        btn_afficherTop = (Button)findViewById(R.id.btn_afficherTop10);

        btn_wizard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Async_AfficherJeux(TopGameActivity.this).execute();
            }
        });

        btn_afficherTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Async_top10(TopGameActivity.this).execute(et_nameGame.getText().toString());
            }
        });
    }

    protected void onActivityResult(int num_requete, int code_retour, Intent intent) {
        if (num_requete == NUM_REQUETE)
        {
            et_nameGame = (EditText)findViewById(R.id.et_nomJeu);
            if (code_retour == RESULT_OK)
            {
                et_nameGame.setText(intent.getStringExtra("nomJeu"));
            }
            else {
                et_nameGame.setText("Erreur avec le wizard");
            }
        }
    }

    public void populate_wizard(String[] result)
    {
        TextView tv_top = (TextView)findViewById(R.id.tv_errorTopGame);
        int code;
        String jeux;

        code = Integer.parseInt(result[0]);
        jeux = result[1];

        switch (code){
            case 0:
                if(!jeux.equals(""))
                {
                    Intent intent = new Intent(TopGameActivity.this, WizardActivity.class);
                    intent.putExtra("listjeux", jeux);
                    startActivityForResult(intent, NUM_REQUETE);
                }
                else
                {
                    tv_top.setText("Il existe encore aucun jeu dans la base de données");
                }
                break;
            case 500:
                tv_top = new TextView(this);
                tv_top.setText(R.string.list_jeux_code_500);
                break;
            case 1000:
                tv_top = new TextView(this);
                tv_top.setText(R.string.list_jeux_code_1000);
                break;
            default:
                tv_top = new TextView(this);
                tv_top.setText(R.string.list_jeux_code_2000);
        }
    }

    public void populate_top10 (String[] result) {
        int code = -1;
        String joueurs = "";
        TextView tv_top = (TextView)findViewById(R.id.tv_errorTopGame);

        code = Integer.parseInt(result[0]);
        joueurs = result[1];

        switch (code)
        {
            case 0:
                if (!joueurs.equals(""))
                {
                    String[] top10 = rechercherTop10(joueurs);
                    //tv_top.setText(top10[0] + " " + top10[1]);

                    Intent intent = new Intent(TopGameActivity.this, AfficherTop10Activity.class);
                    intent.putExtra("pseudo", top10[0]);
                    intent.putExtra("score", top10[1]);
                    startActivity(intent);
                }
                else
                {
                    tv_top.setText("Aucun joueur trouvé pour pouvoir faire un top");
                }
                break;
            case 100:
                tv_top.setText(R.string.afficher_top_code_100);
                break;
            case 500:
                tv_top.setText(R.string.afficher_top_code_500);
                break;
            case 1000:
                tv_top.setText(R.string.afficher_top_code_1000);
                break;
            default:
                tv_top.setText(R.string.afficher_top_code_2000);
                break;
        }
    }

    private String[] rechercherTop10 (String listJoueurs) {
        String pseudo, scores, pseudos;
        int scoreMax = 0, score, i = 0, tailleTop = 10, index = 0;
        int[] tabScores;
        String[] tabJoueurs, tabTop10, tabPseudos, tabJoueur;

        tabTop10 = new String[2];
        tabJoueurs = listJoueurs.split(";");
        tabPseudos = new String[tabJoueurs.length];
        tabScores = new int[tabJoueurs.length];

        for (int j = 0; j < tabJoueurs.length; j++) {
            tabJoueur = tabJoueurs[j].split(",");
            tabPseudos[j] = tabJoueur[0];
            tabScores[j] = Integer.parseInt(tabJoueur[1]);
        }

        pseudo = scores = pseudos = "";
        //List<Object[]> top10 = new ArrayList<>();

        if(tabJoueurs.length<10)
        {
            tailleTop = tabJoueurs.length;
        }
        while (i < tailleTop) {
            for (int j = 0; j < tabJoueurs.length; j++) {
                if (scoreMax < tabScores[j] && (!tabPseudos[j].equals(""))) {
                    pseudo = tabPseudos[j];
                    scoreMax = tabScores[j];
                    index = j;
                }
            }
            pseudos = pseudos + pseudo + ";";
            scores = scores + Integer.toString(scoreMax) + ";";
            tabPseudos[index] = "";
            tabScores[index] = 0;
            scoreMax = 0;
            //top10.add(new Object[] {pseudo, scoreMax});
            i = i + 1;
        }

        tabTop10[0] = pseudos;
        tabTop10[1] = scores;

        return tabTop10;
    }
}
