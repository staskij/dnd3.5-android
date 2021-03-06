package com.skij.dndcharacter;

import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import core.DnDCharacter;


public class Temp extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);

        setOriginalValues();

        setScrollBehaviour();
    }

    private void setScrollBehaviour() {
        ScrollView parentScroll = (ScrollView) findViewById(R.id.scrollView);
        ListView childScroll = (ListView) findViewById(R.id.temp_status_list);
        parentScroll.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                Log.e("", "PARENT TOUCH");
                findViewById(R.id.temp_status_list).getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });
        childScroll.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                Log.e("", "PARENT TOUCH");
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }

    private void setOriginalValues() {
        setEditTextContent(R.id.temp_ac, character.getTempAC() + "");
        setEditTextContent(R.id.temp_attack_roll, character.getTempattackroll() + "");
        setEditTextContent(R.id.temp_max_hp, character.getTempHPMax() + "");
        setEditTextContent(R.id.temp_str, character.getTempstats(DnDCharacter.STATS.STR) + "");
        setEditTextContent(R.id.temp_dex, character.getTempstats(DnDCharacter.STATS.DEX) + "");
        setEditTextContent(R.id.temp_con, character.getTempstats(DnDCharacter.STATS.CON) + "");
        setEditTextContent(R.id.temp_int, character.getTempstats(DnDCharacter.STATS.INT) + "");
        setEditTextContent(R.id.temp_wis, character.getTempstats(DnDCharacter.STATS.WIS) + "");
        setEditTextContent(R.id.temp_cha, character.getTempstats(DnDCharacter.STATS.CHA) + "");
        setEditTextContent(R.id.temp_for, character.getTempsavingthrows(DnDCharacter.SAVING.FORTITUDE) + "");
        setEditTextContent(R.id.temp_ref, character.getTempsavingthrows(DnDCharacter.SAVING.REFLEX) + "");
        setEditTextContent(R.id.temp_wil, character.getTempsavingthrows(DnDCharacter.SAVING.WILL) + "");

        updateTempStatuses();
    }

    private void updateTempStatuses() {
        ArrayAdapter<String> arrayAdapter;
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, character.getTempstatuses());
        ListView statusListView = (ListView) findViewById(R.id.temp_status_list);
        statusListView.setAdapter(arrayAdapter);
        registerForContextMenu(statusListView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.misc_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.delete_entry:
                character.setTempStatus(character.getTempstatuses().get((int) info.id), true);
                updateTempStatuses();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_temp, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_clear_temp) {
            character.clearTemp();
            Toast.makeText(getApplicationContext(), "Temporary stats cleared", Toast.LENGTH_SHORT).show();
            Utils.editCharacter(character, posInArray, this);
            finish();
            startActivity(getIntent());
        }
        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    public void apply(View view) {
        Integer tmp;

        try {
            if ((tmp = getEditTextContentAsInteger(R.id.temp_ac)) != null) {
                character.setTempAC(tmp);
            }
            if ((tmp = getEditTextContentAsInteger(R.id.temp_attack_roll)) != null) {
                character.setTempAttackRoll(tmp);
            }
            if ((tmp = getEditTextContentAsInteger(R.id.temp_max_hp)) != null) {
                character.setTempHPMax(tmp);
            }
            if ((tmp = getEditTextContentAsInteger(R.id.temp_str)) != null) {
                character.setTempStat(DnDCharacter.STATS.STR, tmp);
            }
            if ((tmp = getEditTextContentAsInteger(R.id.temp_dex)) != null) {
                character.setTempStat(DnDCharacter.STATS.DEX, tmp);
            }
            if ((tmp = getEditTextContentAsInteger(R.id.temp_con)) != null) {
                character.setTempStat(DnDCharacter.STATS.CON, tmp);
            }
            if ((tmp = getEditTextContentAsInteger(R.id.temp_int)) != null) {
                character.setTempStat(DnDCharacter.STATS.INT, tmp);
            }
            if ((tmp = getEditTextContentAsInteger(R.id.temp_wis)) != null) {
                character.setTempStat(DnDCharacter.STATS.WIS, tmp);
            }
            if ((tmp = getEditTextContentAsInteger(R.id.temp_cha)) != null) {
                character.setTempStat(DnDCharacter.STATS.CHA, tmp);
            }
            if ((tmp = getEditTextContentAsInteger(R.id.temp_for)) != null) {
                character.setTempSaving(DnDCharacter.SAVING.FORTITUDE, tmp);
            }
            if ((tmp = getEditTextContentAsInteger(R.id.temp_ref)) != null) {
                character.setTempSaving(DnDCharacter.SAVING.REFLEX, tmp);
            }
            if ((tmp = getEditTextContentAsInteger(R.id.temp_wil)) != null) {
                character.setTempSaving(DnDCharacter.SAVING.WILL, tmp);
            }
        } catch (DnDCharacter.InvalidCharacterException e) {
            Toast.makeText(getApplicationContext(), "Invalid Parameters", Toast.LENGTH_LONG).show();
            return;
        }

        Toast.makeText(getApplicationContext(), "Applying Changes", Toast.LENGTH_LONG).show();
        Utils.editCharacter(character, posInArray, this);
        finish();
    }

    public void addStatus(View view) {
        String tmp = ((EditText) findViewById(R.id.temp_new_status)).getText().toString();
        if (!tmp.equals("")) {
            Log.d("Setting", "Setting new status " + tmp);
            ((EditText) findViewById(R.id.temp_new_status)).getText().clear();
            character.setTempStatus(tmp, false);
            updateTempStatuses();
        }
        Log.d("Setting", "Not Setting new status " + tmp);
    }
}
