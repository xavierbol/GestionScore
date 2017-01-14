package bol.xavier.gestionscore.screen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import bol.xavier.gestionscore.R;

public class ListUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent_recup = getIntent();
        String listUsers = intent_recup.getStringExtra("listUsers");

        ScrollView scroll = new ScrollView(this);
        TableLayout lay = new TableLayout(this);
        TableRow row;

        lay.setOrientation(TableLayout.VERTICAL);
        TextView tv_joueur;

        String[] tabUsers = listUsers.split(";");

        for(int i= 0; i < tabUsers.length; i++) {
            row = new TableRow(this);
            tv_joueur = new TextView(this);
            tv_joueur.setText(tabUsers[i]);
            row.addView(tv_joueur);
            lay.addView(row);
        }

        scroll.addView(lay);
        setContentView(scroll);
    }
}
