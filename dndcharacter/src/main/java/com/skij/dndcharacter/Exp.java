package com.skij.dndcharacter;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class Exp extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exp);

        setOriginalValues();
    }

    private void setOriginalValues() {
        ((TextView) findViewById(R.id.exp_value)).setText("Current exp:" + character.getExp());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exp, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    public void apply(View view) {
        Toast.makeText(getApplicationContext(), "Applying Changes", Toast.LENGTH_LONG).show();
        Utils.editCharacter(character, posInArray, this);
        finish();
    }


    public void plusExp(View view) {
        Integer i = getEditTextContentAsInteger(R.id.exp_new_value);
        if (i != null) {
            character.setExpDelta(i);
        }
        setEditTextContent(R.id.exp_new_value, "");
        setOriginalValues();
    }

    public void minusExp(View view) {
        Integer i = getEditTextContentAsInteger(R.id.exp_new_value);
        if (i != null) {
            character.setExpDelta(-i);
        }
        setEditTextContent(R.id.exp_new_value, "");
        setOriginalValues();
    }

    public void setExp(View view) {
        Integer i = getEditTextContentAsInteger(R.id.exp_new_value);
        if (i != null) {
            character.setExpDelta(i - character.getExp());
        }
        setEditTextContent(R.id.exp_new_value, "");
        setOriginalValues();
    }
}