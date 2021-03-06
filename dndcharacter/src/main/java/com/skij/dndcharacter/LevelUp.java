package com.skij.dndcharacter;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import core.DnDCharacter;


public class LevelUp extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_up);

        int[] lays = new int[]{R.id.level_up_customclassdice_lay, R.id.level_up_customclasslay};
        setClassSpinner(R.id.level_up_class_spinner, lays);
        setSpinner(DnDCharacter.STATS.values(), R.id.level_up_new_stat_spinner);
        setOriginalValues();
    }

    private void setOriginalValues() {
        setEditTextContent(R.id.level_up_atk1, character.getBasicAttackBonuses().get(0) + "");
        //noinspection EmptyCatchBlock
        try {
            setEditTextContent(R.id.level_up_atk2, character.getBasicAttackBonuses().get(1) + "");
            setEditTextContent(R.id.level_up_atk3, character.getBasicAttackBonuses().get(2) + "");
            setEditTextContent(R.id.level_up_atk4, character.getBasicAttackBonuses().get(3) + "");
            setEditTextContent(R.id.level_up_atk5, character.getBasicAttackBonuses().get(4) + "");
        } catch (IndexOutOfBoundsException e) {
        }
        setEditTextContent(R.id.level_up_for, character.getSavingthrowsbases()[DnDCharacter.SAVING.FORTITUDE.ordinal()] + "");
        setEditTextContent(R.id.level_up_ref, character.getSavingthrowsbases()[DnDCharacter.SAVING.REFLEX.ordinal()] + "");
        setEditTextContent(R.id.level_up_wil, character.getSavingthrowsbases()[DnDCharacter.SAVING.WILL.ordinal()] + "");
    }

    private <T> void setSpinner(T[] array, int resourceId) {
        Spinner s = (Spinner) findViewById(resourceId);
        s.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, array));
    }

    private void setClassSpinner(int resourceId, final int[] layoutsToShow) {
        ArrayList<String> res = character.getKnownClasses();
        res.add("Custom Class");
        setSpinner(res.toArray(), resourceId);

        //showcustomclassname
        final Spinner s = (Spinner) findViewById(resourceId);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                       long arg3) {
                int x = s.getSelectedItemPosition();
                if (x >= character.getKnownClasses().size()) { //is not default class
                    for (int layoutToShow : layoutsToShow) {
                        LinearLayout l = (LinearLayout) findViewById(layoutToShow);
                        l.setVisibility(View.VISIBLE);
                    }
                } else {
                    for (int layoutToShow : layoutsToShow) {
                        LinearLayout l = (LinearLayout) findViewById(layoutToShow);
                        l.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                for (int layoutToShow : layoutsToShow) {
                    LinearLayout l = (LinearLayout) findViewById(layoutToShow);
                    l.setVisibility(View.GONE);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_level_up, menu);
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
        int liferoll;
        try {
            liferoll = Integer.parseInt(((EditText) findViewById(R.id.level_up_liferoll)).getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(), "Missing required parameters", Toast.LENGTH_LONG).show();
            return;
        }

        int[] saving = new int[3];
        try {
            saving[DnDCharacter.SAVING.FORTITUDE.ordinal()] = Integer.parseInt(((EditText) findViewById(R.id.level_up_for)).getText().toString());
            saving[DnDCharacter.SAVING.REFLEX.ordinal()] = Integer.parseInt(((EditText) findViewById(R.id.level_up_ref)).getText().toString());
            saving[DnDCharacter.SAVING.WILL.ordinal()] = Integer.parseInt(((EditText) findViewById(R.id.level_up_wil)).getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(), "Missing required parameters", Toast.LENGTH_LONG).show();
            return;
        }

        ArrayList<Integer> atkbonus = getAtkBonus();
        if (atkbonus == null) {
            Toast.makeText(getApplicationContext(), "Missing required parameters", Toast.LENGTH_LONG).show();
            return;
        }

        int pos = (((Spinner) findViewById(R.id.level_up_class_spinner)).getSelectedItemPosition());

        DnDCharacter.STATS s;
        int x = (((Spinner) findViewById(R.id.level_up_new_stat_spinner)).getSelectedItemPosition());
        try {
            s = DnDCharacter.STATS.values()[x];
        } catch (ArrayIndexOutOfBoundsException e) {
            Toast.makeText(getApplicationContext(), "Missing required parameters", Toast.LENGTH_LONG).show();
            return;
        }

        int newstatdelta;
        try {
            newstatdelta = Integer.parseInt(((EditText) findViewById(R.id.level_up_new_stat_delta)).getText().toString());
        } catch (NumberFormatException e) {
            newstatdelta = 0;
        }

        //applychanges
        try {
            if (pos < character.getKnownClasses().size()) {
                String dndclass;
                try {
                    dndclass = (String) character.getKnownClasses().toArray()[pos];
                } catch (ArrayIndexOutOfBoundsException e) {
                    Toast.makeText(getApplicationContext(), "Missing required parameters", Toast.LENGTH_LONG).show();
                    return;
                }
                character.levelup(dndclass, liferoll, atkbonus, s, newstatdelta, saving);
            }
            else {//customclass
                String customclassname;
                customclassname = ((EditText) findViewById(R.id.level_up_customclass)).getText().toString();

                if (customclassname.equals("")) {
                    Toast.makeText(getApplicationContext(), "Missing required parameters", Toast.LENGTH_LONG).show();
                    return;
                }

                Integer maxlifedice = getEditTextContentAsInteger(R.id.level_up_customclassdice);
                if (maxlifedice==null) {
                    Toast.makeText(getApplicationContext(), "Missing required parameters", Toast.LENGTH_LONG).show();
                    return;
                }
                character.levelup(customclassname, maxlifedice, liferoll, atkbonus, s, newstatdelta, saving);
            }
        } catch (DnDCharacter.InvalidCharacterException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Invalid Values", Toast.LENGTH_LONG).show();
            return;
        }

        Utils.editCharacter(character, posInArray, this);
        finish();
    }

    private ArrayList<Integer> getAtkBonus() {

        int atkbonus;
        ArrayList<Integer> res = new ArrayList<>();
        try {
            atkbonus = Integer.parseInt(((EditText) findViewById(R.id.level_up_atk1)).getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(), "Missing required parameters", Toast.LENGTH_LONG).show();
            return null;
        }
        res.add(atkbonus);
        try {
            atkbonus = Integer.parseInt(((EditText) findViewById(R.id.level_up_atk2)).getText().toString());
            res.add(atkbonus);
        } catch (NumberFormatException e) {
            return res;
        }
        try {
            atkbonus = Integer.parseInt(((EditText) findViewById(R.id.level_up_atk3)).getText().toString());
            res.add(atkbonus);
        } catch (NumberFormatException e) {
            return res;
        }
        try {
            atkbonus = Integer.parseInt(((EditText) findViewById(R.id.level_up_atk4)).getText().toString());
            res.add(atkbonus);
        } catch (NumberFormatException e) {
            return res;
        }
        try {
            atkbonus = Integer.parseInt(((EditText) findViewById(R.id.level_up_atk5)).getText().toString());
            res.add(atkbonus);
        } catch (NumberFormatException e) {
            return res;
        }
        return res;
    }
}
