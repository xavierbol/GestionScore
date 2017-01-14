package bol.xavier.gestionscore.screen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import bol.xavier.gestionscore.R;
import bol.xavier.gestionscore.asynchronoustask.Async_top10;

public class ListGameActivity extends AppCompatActivity {
    private static int cpt = 0, max;
    private TextView[] tab_tv_game;
    private TextView[] tab_tv_player;
    private Button btn_previous = null;
    private Button btn_next = null;
    private Button[] btn_top10;
    private String[] tabJeu, tabJoueur;

    private View.OnClickListener onClickPrevious = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int taille;

            cpt -= 1;

            for (int i = 0; i < 5; i++) {
                tab_tv_game[i].setText("");
                tab_tv_player[i].setText("");
                btn_top10[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {}
                });
            }

            for(int i = cpt * 5, j = 0; i < tabJeu.length && j < 5; i++, j++) {
                final int y = j;
                tab_tv_game[j].setText(tabJeu[i]);
                tab_tv_player[j].setText(tabJoueur[i]);
                btn_top10[j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Async_top10(ListGameActivity.this).execute(tab_tv_game[y].getText().toString());
                    }
                });
            }

            if(cpt == 0) {
                btn_previous.setEnabled(false);
            }
            if(cpt < max) {
                btn_next.setEnabled(true);
            }
        }
    };

    private View.OnClickListener onClickNext = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int taille;
            cpt += 1;

            for (int i = 0; i < 5; i++) {
                tab_tv_game[i].setText("");
                tab_tv_player[i].setText("");
                btn_top10[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {}
                });
            }

            for(int i = cpt * 5, j = 0; i < tabJeu.length && j < 5; i++, j++) {
                final int y = j;
                tab_tv_game[j].setText(tabJeu[i]);
                tab_tv_player[j].setText(tabJoueur[i]);
                btn_top10[j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Async_top10(ListGameActivity.this).execute(tab_tv_game[y].getText().toString());
                    }
                });
            }


            if(cpt > 0) {
                btn_previous.setEnabled(true);
            }
            if(cpt == max - 1) {
                btn_next.setEnabled(false);
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_game);

        Intent intent_return = getIntent();
        String listGame_bestPlayer = intent_return.getStringExtra("listjeux");
        String[] tabJeu_bestPlayer, tabJeu_player;

        tabJeu_bestPlayer = listGame_bestPlayer.split(";");
        tabJeu = new String[tabJeu_bestPlayer.length];
        tabJoueur = new String[tabJeu_bestPlayer.length];

        for (int j = 0; j < tabJeu_bestPlayer.length; j++) {
            tabJeu_player = tabJeu_bestPlayer[j].split(",");
            tabJeu[j] = tabJeu_player[0];
            tabJoueur[j] = tabJeu_player[1];
        }

        btn_previous = (Button)findViewById(R.id.btn_previous);
        btn_next = (Button) findViewById(R.id.btn_next);
        btn_top10 = new Button[5];
        tab_tv_game = new TextView[5];
        tab_tv_player = new TextView[5];
        btn_top10[0] = (Button) findViewById(R.id.but_top10_1);
        btn_top10[1] = (Button) findViewById(R.id.but_top10_2);
        btn_top10[2] = (Button) findViewById(R.id.but_top10_3);
        btn_top10[3] = (Button) findViewById(R.id.but_top10_4);
        btn_top10[4] = (Button) findViewById(R.id.but_top10_5);
        tab_tv_game[0] = (TextView) findViewById(R.id.tv_jeu1);
        tab_tv_game[1] = (TextView) findViewById(R.id.tv_jeu2);
        tab_tv_game[2] = (TextView) findViewById(R.id.tv_jeu3);
        tab_tv_game[3] = (TextView) findViewById(R.id.tv_jeu4);
        tab_tv_game[4] = (TextView) findViewById(R.id.tv_jeu5);
        tab_tv_player[0] = (TextView) findViewById(R.id.tv_pseudo_best_joueur1);
        tab_tv_player[1] = (TextView) findViewById(R.id.tv_pseudo_best_joueur2);
        tab_tv_player[2] = (TextView) findViewById(R.id.tv_pseudo_best_joueur3);
        tab_tv_player[3] = (TextView) findViewById(R.id.tv_pseudo_best_joueur4);
        tab_tv_player[4] = (TextView) findViewById(R.id.tv_pseudo_best_joueur5);

        btn_previous.setOnClickListener(onClickPrevious);
        btn_next.setOnClickListener(onClickNext);

        max = tabJeu_bestPlayer.length / 5;
        if (tabJeu_bestPlayer.length % 5 > 0)
            max += 1;

        int taille = 5;
        if(tabJeu_bestPlayer.length < taille)
            taille = tabJeu_bestPlayer.length;

        for (int i = 0; i < taille; i++) {
            final int y = i;
            tab_tv_game[i].setText(tabJeu[i]);
            tab_tv_player[i].setText(tabJoueur[i]);
            btn_top10[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Async_top10(ListGameActivity.this).execute(tab_tv_game[y].getText().toString());
                }
            });
        }

        if (max == 1) {
            btn_previous.setEnabled(false);
            btn_next.setEnabled(false);
        }
        else
            btn_previous.setEnabled(false);
    }

    public void populate_top10 (String[] result) {
        int code = -1;
        String joueurs = "";
        LinearLayout lay_main = (LinearLayout)findViewById(R.id.activity_list_game);
        TextView tv_error = new TextView(this);

        code = Integer.parseInt(result[0]);
        joueurs = result[1];

        switch (code)
        {
            case 0:
                if (!joueurs.equals(""))
                {
                    String[] top10 = rechercherTop10(joueurs);

                    Intent intent = new Intent(ListGameActivity.this, AfficherTop10Activity.class);
                    intent.putExtra("pseudo", top10[0]);
                    intent.putExtra("score", top10[1]);
                    startActivity(intent);
                }
                else
                {
                    tv_error.setText("Aucun joueur trouvé pour pouvoir faire un top");
                }
                break;
            case 100:
                tv_error.setText(R.string.afficher_top_code_100);
                break;
            case 500:
                tv_error.setText(R.string.afficher_top_code_500);
                break;
            case 1000:
                tv_error.setText(R.string.afficher_top_code_1000);
                break;
            default:
                tv_error.setText(R.string.afficher_top_code_2000);
                break;
        }

        lay_main.addView(tv_error);
    }

    private String[] rechercherTop10 (String listJoueurs) {
        String pseudo, scores, pseudos;
        int scoreMax = 0, i = 0, tailleTop = 10, index = 0;
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
            i = i + 1;
        }

        tabTop10[0] = pseudos;
        tabTop10[1] = scores;

        return tabTop10;
    }
}
