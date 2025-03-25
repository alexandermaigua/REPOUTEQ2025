package com.example.ojsmobileapp.adaptadores;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ojsmobileapp.R;
import com.example.ojsmobileapp.ViewPDFActivity;
import com.example.ojsmobileapp.modelos.ArticuloModelo;
import com.example.ojsmobileapp.modelos.GaleysModelo;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class ArticulosAdaptador extends RecyclerView.Adapter<ArticulosAdaptador.ViewHolder> {

    private ArrayList<ArticuloModelo> articulosList;
    private String idioma;
    private String id_articulos = "";
    private JSONArray lstgaleyArray;
    private String urlpdf = "";

    public ArticulosAdaptador(ArrayList<ArticuloModelo> articulosList, String idioma) {
        this.articulosList = articulosList;
        this.idioma = idioma;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.crdwarticulos, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ArticuloModelo articulo = articulosList.get(position);
        holder.txtNombre.setText(articulo.getTitulo());
        holder.txtDoi.setText(articulo.getDoi());
        holder.txtFecha.setText(articulo.getFecha());
        lstgaleyArray = articulo.getGaleys();
        id_articulos = articulo.getId_ediciones();
    }

    @Override
    public int getItemCount() {
        return articulosList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtNombre;
        public TextView txtDoi;
        public TextView txtFecha;

        public ViewHolder(View itemView) {
            super(itemView);

            txtNombre = itemView.findViewById(R.id.txtNombreArticulo);
            txtDoi = itemView.findViewById(R.id.txtDoiArticulo);
            txtFecha = itemView.findViewById(R.id.txtFechaArticulo);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                ArticuloModelo articulo = articulosList.get(position);
                id_articulos = articulo.getId_ediciones();
                lstgaleyArray = articulo.getGaleys();

                ArrayList<GaleysModelo> lstgaley = null;
                try {
                    lstgaley = GaleysModelo.JsonObjectsBuild(lstgaleyArray);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                for (GaleysModelo g : lstgaley) {
                    if ("PDF".equals(g.getLabel())) {
                        urlpdf = g.getUrlViewGalley();
                    }
                }

                Snackbar.make(v, "Item Seleccionado " + id_articulos, Snackbar.LENGTH_LONG)
                        .setAction("Acción", null).show();

                Context context = v.getContext();
                Intent intent = new Intent(context, ViewPDFActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("idioma", idioma);
                bundle.putString("id", id_articulos);
                bundle.putString("title", txtNombre.getText().toString());
                bundle.putString("url", urlpdf);
                intent.putExtras(bundle);
                ContextCompat.startActivity(context, intent, null);
            });
        }
    }
}
