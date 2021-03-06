package com.skij.dndcharacter;

import android.content.Intent;
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

import core.DnDCharacterManipulator;
import core.DnDClassManager;


public class NewCharacter extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_character);
        int[] lays = new int[]{R.id.new_char_customclassdice_lay, R.id.new_char_customclassname};
        setClassSpinner(R.id.new_char_class_spinner, lays);

    }

    private <T> void setSpinner(T[] array, int resourceId) {
        Spinner s = (Spinner) findViewById(resourceId);
        s.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, array));
    }

    private void setClassSpinner(int resourceId, final int[] layoutsToShow) {
        ArrayList<String> res = DnDClassManager.getDefaultClasses();
        res.add("Custom Class");
        setSpinner(res.toArray(), resourceId);

        //showcustomclassname
        final Spinner s = (Spinner) findViewById(resourceId);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                       long arg3) {
                int x = s.getSelectedItemPosition();
                if (x >= DnDClassManager.getDefaultClasses().size()) { //is not default class
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
        getMenuInflater().inflate(R.menu.menu_new_character, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    public void createNewCharacter(View view) {
        int str, dex, con, inte, wis, cha, fort, ref, wil, runspeed;
        try {
            str = Integer.parseInt(((EditText) findViewById(R.id.new_char_str)).getText().toString());
            dex = Integer.parseInt(((EditText) findViewById(R.id.new_char_dex)).getText().toString());
            con = Integer.parseInt(((EditText) findViewById(R.id.new_char_con)).getText().toString());
            inte = Integer.parseInt(((EditText) findViewById(R.id.new_char_int)).getText().toString());
            wis = Integer.parseInt(((EditText) findViewById(R.id.new_char_wis)).getText().toString());
            cha = Integer.parseInt(((EditText) findViewById(R.id.new_char_cha)).getText().toString());
            fort = Integer.parseInt(((EditText) findViewById(R.id.new_char_for)).getText().toString());
            ref = Integer.parseInt(((EditText) findViewById(R.id.new_char_ref)).getText().toString());
            wil = Integer.parseInt(((EditText) findViewById(R.id.new_char_wil)).getText().toString());
            runspeed = Integer.parseInt(((EditText) findViewById(R.id.new_char_runspeed)).getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(), "Missing required parameters", Toast.LENGTH_LONG).show();
            return;
        }
        int[] stats = {str, dex, con, inte, wis, cha};
        int[] sav = {fort, ref, wil};

        String name, race;
        name = ((EditText) findViewById(R.id.new_char_name)).getText().toString();
        race = ((EditText) findViewById(R.id.new_char_race)).getText().toString();
        if (name.equals("") || race.equals("")) {
            Toast.makeText(getApplicationContext(), "Missing required parameters", Toast.LENGTH_LONG).show();
            return;
        }

        //class
        int pos = (((Spinner) findViewById(R.id.new_char_class_spinner)).getSelectedItemPosition());


        DnDCharacterManipulator newChar;

        if (pos < DnDClassManager.getDefaultClasses().size()) {
            String dndclass;
            try {
                dndclass = DnDClassManager.getDefaultClasses().get(pos);
            } catch (ArrayIndexOutOfBoundsException e) {
                Toast.makeText(getApplicationContext(), "Missing required parameters", Toast.LENGTH_LONG).show();
                return;
            }

            newChar = new DnDCharacterManipulator(name, race, dndclass, stats, runspeed, sav);
        } else {//customclass
            String customclassname;
            customclassname = ((EditText) findViewById(R.id.new_char_customclass)).getText().toString();

            if (customclassname.equals("")) {
                Toast.makeText(getApplicationContext(), "Missing required parameters", Toast.LENGTH_LONG).show();
                return;
            }
            Integer lifedice = getEditTextContentAsInteger(R.id.new_char_customclassdice);
            if (lifedice == null) {
                Toast.makeText(getApplicationContext(), "Missing required parameters", Toast.LENGTH_LONG).show();
                return;
            }

            newChar = new DnDCharacterManipulator(name, race, customclassname, lifedice, stats, runspeed, sav);
        }

        Utils.addCharacter(newChar, this);

        finish();
        Intent i;
        i = new Intent(this, HomeScreen.class);
        startActivity(i);
    }
}
