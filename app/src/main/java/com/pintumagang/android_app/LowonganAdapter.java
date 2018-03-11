package com.pintumagang.android_app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pintumagang.android_app.entity.Lowongan;

import java.util.List;

/**
 * Created by aribambang on 11/03/18.
 */

public class LowonganAdapter extends RecyclerView.Adapter<LowonganAdapter.LowonganViewHolder> {

    private Context mCtx;
    private List<Lowongan> lowonganList;

    public LowonganAdapter(Context mCtx, List<Lowongan> lowonganList) {
        this.mCtx = mCtx;
        this.lowonganList = lowonganList;
    }

    @Override
    public LowonganViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.lowongan_list, null);
        return new LowonganViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LowonganViewHolder holder, int position) {
        Lowongan lowongan = lowonganList.get(position);

        //loading the image
        Glide.with(mCtx)
                .load(lowongan.getLogo())
                .into(holder.imageView);

        holder.textViewNamaLowongan.setText(lowongan.getNama_lowongan());
        holder.textViewNamaPerusahaan.setText(lowongan.getNama_perusahaan());
        holder.textViewLokasi.setText(String.valueOf(lowongan.getLokasi()));
        holder.textViewWaktuInput.setText(String.valueOf(lowongan.getWaktu_input()));
    }

    @Override
    public int getItemCount() {
        return lowonganList.size();
    }

    class LowonganViewHolder extends RecyclerView.ViewHolder {

        TextView textViewNamaLowongan, textViewNamaPerusahaan, textViewLokasi, textViewWaktuInput;
        ImageView imageView;

        public LowonganViewHolder(View itemView) {
            super(itemView);

            textViewNamaLowongan = (TextView) itemView.findViewById(R.id.textViewNamaLowongan);
            textViewNamaPerusahaan = (TextView) itemView.findViewById(R.id.textViewNamaPerusahaan);
            textViewLokasi = (TextView) itemView.findViewById(R.id.textViewLokasi);
            textViewWaktuInput = (TextView) itemView.findViewById(R.id.textViewWaktuInput);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }
}

