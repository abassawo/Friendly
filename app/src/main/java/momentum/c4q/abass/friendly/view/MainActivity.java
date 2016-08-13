package momentum.c4q.abass.friendly.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import momentum.c4q.abass.friendly.R;
import momentum.c4q.abass.friendly.controller.WidgetController;
import momentum.c4q.abass.friendly.model.Contact;
import momentum.c4q.abass.friendly.model.Prefs;

/**
 * Created by Abass on 7/12/16.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.activity_add_btn)
    Button addContactBtn;
    @BindView(R.id.name_field)
    EditText nameField;
    @BindView(R.id.number_field)
    EditText numberField;
    private WidgetController controller;
    private Contact contact;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        if (Prefs.isFirstRun(this)) {
            showWelcomeDialog();
        }
        controller = new WidgetController(getApplicationContext());
        ButterKnife.bind(MainActivity.this);
        addContactBtn.setOnClickListener(this);
    }

    public void showWelcomeDialog() {
        Log.d(TAG, "Welcome");
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Enter your first name")
                .setMessage("it will be included in the automated text message");
        final EditText edittext = new EditText(this);
        builder.setView(edittext);


        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, int whichButton) {
                if(TextUtils.isEmpty(edittext.getText())){
                    Toast.makeText(MainActivity.this, "Enter a name", Toast.LENGTH_SHORT).show();
                }
                Prefs.setOwnerName(MainActivity.this, edittext.getText().toString());
                Prefs.disableWelcome(MainActivity.this);
                dialog.dismiss();

            }
        });
        builder.show();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_add_btn:
                contact = controller.createContact(nameField, numberField);
                controller.processContact(contact);
                break;
        }

    }

}
