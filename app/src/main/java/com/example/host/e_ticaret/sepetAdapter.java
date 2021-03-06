package com.example.host.e_ticaret;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.List;

/**
 * Created by Java3 on 8/16/2016.
 */
public class sepetAdapter extends BaseAdapter {
    private LayoutInflater inf;
    private List<urun> urls;
    private Activity ac;

    public sepetAdapter(Activity ac, List<urun> urls) {
        inf = (LayoutInflater) ac.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.urls = urls;
        this.ac = ac;
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public Object getItem(int i) {
        return urls.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public static int tikla = -1;

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view1;
        tikla = i;
        view1 = inf.inflate(R.layout.sepetrow, null);
        TextView sepeturunbaslik = (TextView) view1.findViewById(R.id.sepeturunbasliktxt);
        TextView sepeturunfiyat = (TextView) view1.findViewById(R.id.sepeturunfiyattxt);
        ImageView vresim = (ImageView) view1.findViewById(R.id.sepetresimImageView);
        final Button sepettenSilButonu = (Button) view1.findViewById(R.id.sepettensilbtn);
        sepettenSilButonu.setTag(tikla);
        sepettenSilButonu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ac);
                builder.setTitle("Sepetten Ürün Silme İşlemi");
                builder.setMessage("Seçilen Ürünü Sepetten Silmek İstediğinizden Emin misiniz ?");
                builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Yes Durumunda
                        tikla = Integer.valueOf(sepettenSilButonu.getTag().toString());
                        DB db = new DB(ac);
                        try {
                            int sonuc = db.sil("sepet", "sepet_id", sepet.sepetID.get(tikla).toString());
                            if (sonuc > 0) {
                                ac.finish();
                                Toast.makeText(ac, "Sepetten Ürün Silindi ...", Toast.LENGTH_SHORT).show();
                                Intent git = new Intent(ac, sepet.class);
                                ac.startActivity(git);
                            } else {
                                Toast.makeText(ac, "Sepetten Ürün Silinemedi ...", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception ex) {
                            Log.d("Sepet Silme Hatası : ", ex.toString());
                        }
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
            }
        });
        urun ul = urls.get(i);
        sepeturunbaslik.setText(ul.getBaslik());
        sepeturunfiyat.setText(ul.getVidID());
        Picasso.with(ac).load(ul.getResimUrl()).into(vresim);
        return view1;
    }

}
