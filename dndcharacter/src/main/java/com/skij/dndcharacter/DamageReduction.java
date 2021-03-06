package com.skij.dndcharacter;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import core.DnDCharacter;


public class DamageReduction extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_damage_reduction);

        setOriginalValues();
    }

    private void setOriginalValues() {
        setEditTextContent(R.id.damage_reduction_damage_reduction, character.getDamageReduction() + "");
        setEditTextContent(R.id.damage_reduction_spell_resist, character.getSpellResist() + "");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_damage_reduction, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public void apply(View view) {
        Integer tmp;

        try {
            if ((tmp = getEditTextContentAsInteger(R.id.damage_reduction_damage_reduction)) != null) {
                character.setDamageReduction(tmp);
            }
            if ((tmp = getEditTextContentAsInteger(R.id.damage_reduction_spell_resist)) != null) {
                character.setSpellResist(tmp);
            }
        } catch (DnDCharacter.InvalidCharacterException e) {
            Toast.makeText(getApplicationContext(), "Invalid Parameters", Toast.LENGTH_LONG).show();
            return;
        }

        Toast.makeText(getApplicationContext(), "Applying Changes", Toast.LENGTH_LONG).show();
        Utils.editCharacter(character, posInArray, this);
        finish();
    }

}