package com.example.host.e_ticaret;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.ArrayList;

public class altKategoriler extends AppCompatActivity {
SharedPreferences sha;
SharedPreferences.Editor edit;
ListView list;

ArrayList<String> kategoriID=new ArrayList<String>();
ArrayList<String> kategoriAdi=new ArrayList<String>();
static ArrayList<String> categoryIDs=new ArrayList<String>();
    String altkategoriID="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ana_kategoriler);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        list=(ListView)findViewById(R.id.listView);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sha=getSharedPreferences("KullaniciCerezler",MODE_PRIVATE);
        edit=sha.edit();


        String url = "http://jsonbulut.com/json/companyCategory.php?ref=7b7392076900968d8e4ad78351ad55d3";
        new anakategori(url,this).execute();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent git=new Intent(altKategoriler.this,urunListesi.class);
                git.putExtra("altkategoriID",categoryIDs.get(i));
                Log.d("altkategoriID",categoryIDs.get(i));
                startActivity(git);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case android.R.id.home:
                if (getParentActivityIntent()==null){
                    finish();
                }else{
                    NavUtils.navigateUpFromSameTask(this);
                }


        }
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.sepetim) {
            Intent git3=new Intent(altKategoriler.this,sepet.class);
            startActivity(git3);
            return true;
        }
        if (id == R.id.adreslerim) {
            Intent git=new Intent(altKategoriler.this,adreslerim.class);
            startActivity(git);
            return true;
        }
        if (id == R.id.ayarlarim) {
            Intent git=new Intent(altKategoriler.this,ayarlarim.class);
            startActivity(git);
            return true;
        }
        if (id == R.id.cikis) {
            AlertDialog.Builder builder = new AlertDialog.Builder(altKategoriler.this);
            builder.setTitle("Çıkış İşlemi");
            builder.setMessage("Çıkış Yapmak İstediğinizden Emin misiniz ?");
            builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Yes Durumunda
                    edit.remove("ID");
                    edit.remove("Adi");
                    edit.remove("Soyadi");
                    edit.remove("Mail");
                    edit.remove("Telefon");
                    edit.commit();

                    Intent git = new Intent(altKategoriler.this, girisEkrani.class);
                    finish();
                    startActivity(git);
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // No Durumunda

                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class anakategori extends AsyncTask<Void,Void,Void> {
        private ProgressDialog pro;
        String data = "";
        String url = "";
        public anakategori(String url, Activity ac) {
            this.url = url;
            pro = new ProgressDialog(ac);
            pro.setMessage("Lütfen Bekleyiniz !");
            pro.show();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... param) {
            try {
                data = Jsoup.connect(url).ignoreContentType(true).execute().body();
            }catch (Exception ex) {
                Log.d("Json Hatası ", ex.toString());
            }finally {
                pro.dismiss();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void o) {
            super.onPostExecute(o);
            String gelen=getIntent().getExtras().getString("altkategoriID","");
            try {
                /*ls.clear();*/
                JSONObject obj = new JSONObject(data);
                JSONArray kategoriler=obj.getJSONArray("Kategoriler");
                JSONObject acategories=kategoriler.getJSONObject(0);
                JSONArray categories=acategories.getJSONArray("Categories");
                for (int j=0;j<categories.length();j++){
                String duzAnaKategori=categories.getJSONObject(j).getString("TopCatogryId");
                String anaKategoriAdi=categories.getJSONObject(j).getString("CatogryName");
                String kategoriIDss=categories.getJSONObject(j).getString("CatogryId");

                    if (duzAnaKategori.equals(gelen)){
                        kategoriAdi.add(anaKategoriAdi);
                        altkategoriID=categories.getJSONObject(j).getString("CatogryId");
                        categoryIDs.add(kategoriIDss);
                    }
                }

                ArrayAdapter<String> adp=new ArrayAdapter<String>(altKategoriler.this,R.layout.altkategorirow,R.id.altkategoritxt,kategoriAdi);
                list.setAdapter(adp);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
