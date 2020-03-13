package mx.uagro.preeclampsia;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.ArrayList;
import weka.classifiers.lazy.IBk;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnDiagnostico;
    EditText editTextEdad;
    EditText editTextIMCMat;
    EditText editTextTas;
    EditText editTextGestacion;
    EditText editTextVpm;
    EditText editTextNeutrofilo;
    EditText editTextEosinofilo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        btnDiagnostico = findViewById(R.id.btnDiagnostico);
        editTextEdad = findViewById(R.id.etxtEdad);
        editTextIMCMat = findViewById(R.id.etxtIMCMat);
        editTextTas = findViewById(R.id.etxtTAS);
        editTextGestacion = findViewById(R.id.etxtGesta);
        editTextVpm = findViewById(R.id.etxtVpm);
        editTextNeutrofilo = findViewById(R.id.etxtNeutrofilo);
        editTextEosinofilo = findViewById(R.id.etxtEosinofilo);

        btnDiagnostico.setOnClickListener(this);

    }


    private void classify() throws Exception {
        AssetManager assetManager = getAssets();
        RandomForest randomForest = (RandomForest)
                SerializationHelper.read(assetManager.open("RandomForest.model"));

        IBk iBk = (IBk) SerializationHelper.read(assetManager.open("IBk.model"));
        J48 j48 = (J48) SerializationHelper.read(assetManager.open("J48.model"));

        ArrayList<Attribute> attributes = new ArrayList<>();
        ArrayList <String> classes = new ArrayList<>();
        attributes.add(new Attribute("edad"));
        attributes.add(new Attribute("IMCMat"));
        attributes.add(new Attribute("TAS"));
        attributes.add(new Attribute("totGestas"));
        attributes.add(new Attribute("vpm"));
        attributes.add(new Attribute("neutrofilo"));
        attributes.add(new Attribute("eosinofilo "));
        classes.add("1");
        classes.add("0");
        attributes.add(new Attribute("prediccion",classes));

        Instances data = new Instances("MIIDTWEKA-Preeclampsia-weka."
                + "filters.unsupervised.attribute.Remove-R2-9,12,14-24,26,"
                + "28-29,31-38",attributes,0);

        double instance [] = new double[data.numAttributes()];
        instance[0] = Double.valueOf(editTextEdad.getText().toString());
        instance[1] = Double.valueOf(editTextIMCMat.getText().toString());
        instance[2] = Double.valueOf(editTextTas.getText().toString());
        instance[3] = Double.valueOf(editTextGestacion.getText().toString());
        instance[4] = Double.valueOf(editTextVpm.getText().toString());
        instance[5] = Double.valueOf(editTextNeutrofilo.getText().toString());
        instance[6] = Double.valueOf(editTextEosinofilo.getText().toString());

        data.add(new DenseInstance(1.0,instance));
        data.setClassIndex(data.numAttributes()-1);
        Instance inst = data.instance(0);
        System.out.println("instancia "+inst.toString());
        /*double predRForest = randomForest.classifyInstance(inst);
        double predIbk = iBk.classifyInstance(inst);
        double predJ48 = j48.classifyInstance(inst);*/
        String predRForest = (randomForest.classifyInstance(inst)==0.0)?"Clase: 1":"Clase: 0";
        String predIbk = (iBk.classifyInstance(inst)==0.0)?"Clase: 1":"Clase: 0";
        String predJ48 = (j48.classifyInstance(inst)==0.0)?"Clase: 1":"Clase: 0";
        /*/System.out.println("predicciones:");
        System.out.println("RandomForest: "+predRForest);
        System.out.println("IBk: "+predIbk);
        System.out.println("J48: "+predJ48);*/
        AlertDialog.Builder preDiag = new AlertDialog.Builder(this);
        preDiag.setCancelable(true);
        preDiag.setPositiveButton(
                "Aceptar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        editTextEdad.setText("");
        editTextIMCMat.setText("");
        editTextTas.setText("");
        editTextGestacion.setText("");
        editTextVpm.setText("");
        editTextNeutrofilo.setText("");
        editTextEosinofilo.setText("");

        preDiag.setMessage("RandomForest: "+ predRForest +"\n"+"IBk: "+predIbk+"\n"+"J48: "+predJ48);

        AlertDialog alertDiag = preDiag.create();
        alertDiag.show();
    }

    private void checkAll() throws Exception {

        AlertDialog.Builder verificar = new AlertDialog.Builder(this);
        verificar.setCancelable(true);
        verificar.setPositiveButton(
                "Aceptar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        if(editTextEdad.length()==0){

            verificar.setMessage("Favor de Ingresar el Valor de Edad");
            AlertDialog alertDiag = verificar.create();
            alertDiag.show();

        }else if (editTextIMCMat.length()==0){

            verificar.setMessage("Favor de Ingresar el Valor de IMCMat");
            AlertDialog alertDiag = verificar.create();
            alertDiag.show();
        }else if (editTextTas.length()==0){

            verificar.setMessage("Favor de Ingresar el Valor de TAS");
            AlertDialog alertDiag = verificar.create();
            alertDiag.show();

        }else if (editTextGestacion.length()==0){

            verificar.setMessage("Favor de Ingresar la Gestación");
            AlertDialog alertDiag = verificar.create();
            alertDiag.show();

        }else if (editTextVpm.length()==0){

        verificar.setMessage("Favor de Ingresar el Valor de Vpm");
        AlertDialog alertDiag = verificar.create();
        alertDiag.show();

    }else if (editTextNeutrofilo.length()==0){

            verificar.setMessage("Favor de Ingresar el Valor de Neutrófilo");
            AlertDialog alertDiag = verificar.create();
            alertDiag.show();

        }else if (editTextEosinofilo.length()==0){

            verificar.setMessage("Favor de Ingresar el Valor de Eosinófilo");
            AlertDialog alertDiag = verificar.create();
            alertDiag.show();

        }else {

            classify();

        }

    }


    @Override
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnDiagnostico:
                    try {
                        checkAll();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                default:
                    break;
            }

    }
}
