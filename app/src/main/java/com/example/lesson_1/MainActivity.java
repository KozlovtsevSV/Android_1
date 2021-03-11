package com.example.lesson_1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class MainActivity extends AppCompatActivity {

    private Button[] arrayNumberButton = new Button[10];
    private enum Operations {DIV, MULTIPLICATION, MINUS, PLUS, PERCENT, RESULT, C, CE, BACKSPACE}
    private Button[] arrayOperationButton = new Button[Operations.values().length];

    private double number;
    private double resultOperation;
    private Boolean flagNewNum = true;
    private Boolean flagDivBy_0 = false;
    private Boolean flagInfinite = false;

    private Operations currentOperation = Operations.RESULT;
    private final String POINT_REPRESENTATION = ".";
    private final String CLEAR_FIELD = "";
    private final int MAX_ACCURACY = 8;

    private String textWorkField = CLEAR_FIELD;
    private EditText mEditTextWorkField;
    private EditText mEditTextInfoField;
    private Button mbuttonPt;
    private SwitchMaterial mSwitchDarkTheme;

    private static final String NAME_KEY_NIGTH_THEME = "isNightTheme";

    private static final int RESULT_SAVE = 1;

    private static boolean isNightTheme = false;
    private static final String prefs = "prefs.xml";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        isNightTheme = getSharedPreferences(prefs, MODE_PRIVATE).
                getBoolean(NAME_KEY_NIGTH_THEME, false);
        if (isNightTheme) {
            setTheme(R.style.MyThemeDark);
        } else {
            setTheme(R.style.MyTheme);
        }

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.keyboard_frame);
        initButtonNumbers();
        mEditTextWorkField = findViewById(R.id.editTextWorkField);
        mEditTextInfoField = findViewById(R.id.editTextInfoField);

        MaterialButton buttonSettings = findViewById(R.id.buttonSettings);
        buttonSettings.setOnClickListener(view -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            intent.putExtra("isNightTheme", isNightTheme);
            startActivityForResult(intent, RESULT_SAVE);
        });

        upDateWorkField();
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent result) {
        super.onActivityResult(requestCode, responseCode, result);

        if (result != null && responseCode == RESULT_OK) {
            isNightTheme = result.getBooleanExtra(NAME_KEY_NIGTH_THEME, isNightTheme);
            SharedPreferences sharedPreferences = getSharedPreferences(prefs, MODE_PRIVATE);
            if (sharedPreferences.getBoolean(NAME_KEY_NIGTH_THEME, false) != isNightTheme) {
                sharedPreferences.edit().
                        putBoolean(NAME_KEY_NIGTH_THEME, isNightTheme).apply();
            }
            recreate();
        }
    }

    private String firstUpperCase(String word){
        if(word == null || word.isEmpty()) return "";
        return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
    }

    private void initButtonNumbers(){

        for (int i = 0; i < arrayNumberButton.length; i++){

            String buttonID = "button" + i;
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            arrayNumberButton[i] = findViewById(resID);
            int finalI = i;
            arrayNumberButton[i].setOnClickListener(view -> {
                clickNumberButton(String.valueOf(finalI));
            });
        }

        int i = 0;
        for (Operations operation : Operations.values()) {
            String buttonID = "button" + firstUpperCase(operation.name());
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            arrayOperationButton[i] = findViewById(resID);
            if(arrayOperationButton[i] == null){continue;}
            arrayOperationButton[i].setOnClickListener(view -> {
                clickOperationButton(operation);
            });
        }

        mbuttonPt = findViewById(R.id.buttonPt);
        mbuttonPt.setOnClickListener(view -> {
            clickNumberButton(POINT_REPRESENTATION);
        });
    }

    private void clickNumberButton(String arg){

        try {
            if(textWorkField.length() == 0 && arg.equals(POINT_REPRESENTATION)){
                textWorkField =  "0" + POINT_REPRESENTATION;
            }
            else{
                if(flagNewNum){
                    textWorkField = arg;
                }else{
                    textWorkField += arg;}
            }
            double dd = Double.parseDouble(textWorkField);
            number = Double.parseDouble(textWorkField);

        }catch (NumberFormatException e){
            textWorkField = textWorkField.substring(0, textWorkField.length() - 1);
        }
        if (number == 0 && textWorkField.indexOf(POINT_REPRESENTATION) == -1){
            textWorkField = CLEAR_FIELD;
        }
        flagNewNum = false;
        upDateWorkField();

    }

    private void clickOperationButton(Operations operation){

        if(Operations.RESULT == operation){
            paymentResult(false);
            return;
        }

        if(Operations.PERCENT == operation){
            paymentResult(true);
            return;
        }

        else if(Operations.BACKSPACE == operation){
            if(!flagNewNum){
                backspaceTextWorkField();}
            else{
                clearTextWorkField();
            }
            return;
        }
        else if(Operations.C == operation){
            resetAllData();
            return;
        }
        else if(Operations.CE == operation){
            resetAllData();
            return;
        }

        if(!flagNewNum){
            paymentResult(false);
        }
        currentOperation = operation;

        upDateInfoField();

    }

    private void paymentResult(Boolean isPersent){

        if(isPersent){
            number = resultOperation * number / 100;
        }

        switch (currentOperation){
            case DIV:
                if(number == 0){
                    flagDivBy_0 = true;
                    break;
                }
                resultOperation /= number;
                 break;
            case MULTIPLICATION:
                resultOperation *= number;
                break;
            case PLUS:
                resultOperation += number;
                break;
            case MINUS:
                 resultOperation -= number;
                 break;
            case PERCENT:
                resultOperation = resultOperation * number / resultOperation;
                break;
            case RESULT:
                resultOperation = number;
                number = 0d;
                break;
            }

        flagInfinite = Double.isInfinite(resultOperation);

        double scale = Math.pow(10, MAX_ACCURACY);
        resultOperation = Math.ceil(resultOperation * scale) / scale;
        flagNewNum = true;
        textWorkField = Double.toString(resultOperation);
        upDateWorkField();

    }

    private void upDateWorkField() {

        if(flagDivBy_0) {
            mEditTextWorkField.setText("деление на 0!");
            flagDivBy_0 = false;
            return;
        }

        if(flagInfinite) {
            mEditTextWorkField.setText("Переполнение!");
             return;
        }

         if(textWorkField.length() == 0){
            mEditTextWorkField.setText("0");
            return;
         }

         try {
            if(Float.parseFloat(textWorkField) % 1 == 0 && textWorkField.indexOf(POINT_REPRESENTATION + "0") >= 0 && flagNewNum){
                textWorkField = textWorkField.substring(0, textWorkField.indexOf(POINT_REPRESENTATION));
            }
        }catch (NumberFormatException e){
        }
        mEditTextWorkField.setText(textWorkField);
        upDateInfoField();

    }

    private void upDateInfoField() {
        if(resultOperation != 0) {
            mEditTextInfoField.setText(Double.toString(resultOperation) + getStringOperation() + (number != 0d ? Double.toString(number): ""));
        }
        else{
            mEditTextInfoField.setText("");
        }
    }

    private String getStringOperation(){

        String result = "";

        switch (currentOperation){
            case PLUS:
                result = "+";
                break;
            case MINUS:
                result = "-";
                break;
            case MULTIPLICATION:
                result = "*";
                break;
            case DIV:
                result = "/";
                break;
            default:
                result = "";
                break;
        }

        return result;
    }

    private void backspaceTextWorkField(){

        if (textWorkField.length() > 0) {
            textWorkField = textWorkField.substring(0, textWorkField.length() - 1);
            if (textWorkField.length() > 0) {
                try {
                    number = Float.parseFloat(textWorkField);
                }catch (NumberFormatException e){
                    Log.e("ERROR", "ошибка преобразования к Float");
                }
            }else{
                number = 0d;
            }
            upDateWorkField();
        }
    }

    private void clearTextWorkField(){
        textWorkField = CLEAR_FIELD;
        flagNewNum = true;
        upDateWorkField();
    }

    private void resetAllData(){
        textWorkField = CLEAR_FIELD;
        resultOperation = 0d;
        number = 0d;
        flagNewNum = true;
        currentOperation = Operations.RESULT;
        upDateWorkField();
        upDateInfoField();
    }

}