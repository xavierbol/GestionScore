package bol.xavier.gestionscore.screen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import bol.xavier.gestionscore.R;

public class WizardActivity extends AppCompatActivity {
    //private LinearLayout lay = new LinearLayout(this);
    //private Button btn_jeu = null;
    //private TextView tv_error = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard);

        ScrollView scrollView = new ScrollView(this);
        Intent intent_return = getIntent();
        String listjeux = intent_return.getStringExtra("listjeux");

        LinearLayout lay = new LinearLayout(this);
        lay.setOrientation(LinearLayout.VERTICAL);

        final String[] tabJeux = listjeux.split(";");

        for (int i = 0; i < tabJeux.length; i++)
        {
            final String nom_jeu = tabJeux[i];
            Button btn_jeux = new Button(this);
            btn_jeux.setText(tabJeux[i]);
            btn_jeux.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent_return = new Intent(WizardActivity.this, AjoutScoreActivity.class);
                    intent_return.putExtra("nomJeu", nom_jeu);
                    setResult(RESULT_OK, intent_return);
                    finish();
                }
            });
            lay.addView(btn_jeux);
        }
        scrollView.addView(lay);
        setContentView(scrollView);
    }
}
