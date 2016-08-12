package momentum.c4q.abass.friendly.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import momentum.c4q.abass.friendly.R;
import momentum.c4q.abass.friendly.controller.WidgetController;
import momentum.c4q.abass.friendly.model.Contact;

/**
 * Created by Abass on 7/12/16.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.activity_add_btn) Button addContactBtn;
    @BindView(R.id.name_field) EditText nameField;
    @BindView(R.id.number_field) EditText numberField;
    private WidgetController controller;
    private Contact contact;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        controller = new WidgetController(getApplicationContext());
        ButterKnife.bind(MainActivity.this);
        addContactBtn.setOnClickListener(this);
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
