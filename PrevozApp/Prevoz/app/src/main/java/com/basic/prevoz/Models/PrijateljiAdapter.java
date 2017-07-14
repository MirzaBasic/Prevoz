package com.basic.prevoz.Models;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.basic.prevoz.Helper.ImageConverter;
import com.basic.prevoz.R;
import com.basic.prevoz.Utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Developer on 28.06.2017..
 */

public class PrijateljiAdapter extends RecyclerView.Adapter<PrijateljiAdapter.PrijateljiAdapterViewHolder> {

    private static int PENDING = 0;
    private static int ACCEPTED = 1;
    private static int DECLINED = 2;
    private static int BLOCKED = 3;
    private static int PIXEL_SIZE=135;
    private Context context;
    private List<KorisniciVM> mPrijatelji;
   private PrijateljiAdapterOnClickHandler onClickHandler;


    public PrijateljiAdapter(PrijateljiAdapterOnClickHandler clickHandler){
        mPrijatelji=new ArrayList<>();
        onClickHandler=clickHandler;

    }
public interface PrijateljiAdapterOnClickHandler{

    void OnClick(KorisniciVM prijatelj);

}
    @Override
    public PrijateljiAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context=parent.getContext();
        int layoutIdFromItemlist=R.layout.prijatelji_list_item;

        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(layoutIdFromItemlist,parent,false);

        return new PrijateljiAdapterViewHolder(view);


    }

    @Override
    public void onBindViewHolder(final PrijateljiAdapterViewHolder holder, int position) {
        KorisniciVM prijatelj=mPrijatelji.get(position);

        holder.mImeKorisnika.setText(prijatelj.ImePrezime);
        holder.mEmail.setText(prijatelj.Email);


        new AsyncTask<Uri, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Uri... params) {
                try {
                    return NetworkUtils.getBitmapImageFromUri(params[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                if(bitmap!=null){

                    Bitmap circularBitmap= ImageConverter.getRoundedCornerBitmap(bitmap,PIXEL_SIZE);
                    holder.mSlikaKorisnika.setImageBitmap(circularBitmap);
                }
                else{


                }
            }
        }.execute(Uri.parse(prijatelj.photoUrl).buildUpon().appendQueryParameter("sz",String.valueOf(PIXEL_SIZE)).build());

    }

    @Override
    public int getItemCount() {
        return mPrijatelji.size();
    }

    public class   PrijateljiAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mSlikaKorisnika;
        TextView mImeKorisnika;
        TextView mEmail;

        public PrijateljiAdapterViewHolder(View itemView) {
            super(itemView);

            mSlikaKorisnika= (ImageView) itemView.findViewById(R.id.image_user);
            mImeKorisnika= (TextView) itemView.findViewById(R.id.tv_username);
            mEmail= (TextView) itemView.findViewById(R.id.tv_email);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
int adapterPosition=getAdapterPosition();
            KorisniciVM prijatelj= mPrijatelji.get(adapterPosition);
            onClickHandler.OnClick(prijatelj);
        }
    }
    public  void setPrijateljiData(List<KorisniciVM> prijatelji){

        mPrijatelji=prijatelji;
    }
}
