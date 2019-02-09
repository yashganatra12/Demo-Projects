package vcard.application.android.com.vcard.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import vcard.application.android.com.vcard.R;
import vcard.application.android.com.vcard.Utility.CardItem;

public class ShowCard extends AppCompatActivity {

    TextView number, email,address,companyEmail,name,share;
    ImageButton message;
    CardItem cardItem;
    ImageView imageView;
    String cardId;
    private static final String VCF_DIRECTORY = "/vcf_demonuts";
    private File vcfFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_card);
        cardItem = new CardItem();
        imageView = findViewById(R.id.show_card_imageView);
        name = findViewById(R.id.show_card_company_name);
        number = findViewById(R.id.show_card_number);
        email = findViewById(R.id.show_card_email);
        message = findViewById(R.id.show_card_message);
        address = findViewById(R.id.show_card_company_address);
        share = findViewById(R.id.show_card_share);
        companyEmail = findViewById(R.id.show_card_company_email);
        cardId = FirebaseDatabase.getInstance().getReference().child("card").getKey();

        imageView.setImageURI(Uri.parse(getIntent().getStringExtra("image")));
        name.setText(getIntent().getStringExtra("name"));
        number.setText(getIntent().getStringExtra("number"));
        companyEmail.setText(getIntent().getStringExtra("email"));


        number.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + number.getText()));
                startActivity(intent);
            }
        });

        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://maps.google.com/maps?daddr="+address.getText();
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,  Uri.parse(url));
                startActivity(intent);
            }
        });
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW,Uri.fromParts("sms", String.valueOf(number.getText()),null)));
            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mailIntent = new Intent(Intent.ACTION_SENDTO);
                mailIntent.setData(Uri.parse("mailto:"+email.getText()));
                startActivity(mailIntent);
            }
        });
        companyEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mailIntent = new Intent(Intent.ACTION_SENDTO);
                mailIntent.setData(Uri.parse("mailto:"+companyEmail.getText()));
                startActivity(mailIntent);
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // File vcfFile = new File(this.getExternalFilesDir(null), "generated.vcf");
                    File vdfdirectory = new File(
                            Environment.getExternalStorageDirectory() + VCF_DIRECTORY);
                    // have the object build the directory structure, if needed.
                    if (!vdfdirectory.exists()) {
                        vdfdirectory.mkdirs();
                    }

                    vcfFile = new File(vdfdirectory, name+""+ Calendar.getInstance().getTimeInMillis() + ".vcf");

                    FileWriter fw = null;
                    fw = new FileWriter(vcfFile);
                    fw.write("BEGIN:VCARD\r\n");
                    fw.write("VERSION:3.0\r\n");
                    // fw.write("N:" + p.getSurname() + ";" + p.getFirstName() + "\r\n");
                    fw.write("FN:" + name.getText().toString() + "\r\n");
                    //  fw.write("ORG:" + p.getCompanyName() + "\r\n");
                    //  fw.write("TITLE:" + p.getTitle() + "\r\n");
                    fw.write("TEL;TYPE=WORK,VOICE:" + number.getText().toString() + "\r\n");
                    //   fw.write("TEL;TYPE=HOME,VOICE:" + p.getHomePhone() + "\r\n");
                    //   fw.write("ADR;TYPE=WORK:;;" + p.getStreet() + ";" + p.getCity() + ";" + p.getState() + ";" + p.getPostcode() + ";" + p.getCountry() + "\r\n");
                    fw.write("EMAIL;TYPE=PREF,INTERNET:" + email.getText().toString() + "\r\n");
                    fw.write("END:VCARD\r\n");
                    fw.close();

                   /* Intent i = new Intent(); //this will import vcf in contact list
                    i.setAction(android.content.Intent.ACTION_VIEW);
                    i.setDataAndType(Uri.fromFile(vcfFile), "text/x-vcard");
                    startActivity(i);*/

                    Toast.makeText(ShowCard.this, "Created!", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
