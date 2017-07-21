package com.basic.prevoz.Models;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.basic.prevoz.BuildConfig;
import com.basic.prevoz.Helper.DateConverter;
import com.basic.prevoz.Helper.ImageConverter;
import com.basic.prevoz.Helper.MD5;
import com.basic.prevoz.Helper.MyApp;
import com.basic.prevoz.MainActivity;
import com.basic.prevoz.Utils.NetworkUtils;
import com.basic.prevoz.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Developer on 29.04.2017..
 */

public class PrevozAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

   private Context context;
   private List<PrevozVM>  mPrevozData;
    private int LastVisibleItemPosition;
    private static final int NATIV_ADD_VIEW_TYPE=1;
    private static final int DEFAULT_VIEW_TYPE=0;


private static int PIXEL_SIZE=135;
    PrevozAdapterOnClickHandler onClickHandler;

    public  PrevozAdapter(PrevozAdapterOnClickHandler clickHandler){
        mPrevozData=new ArrayList<>();
            onClickHandler=clickHandler;
    }
    public interface  PrevozAdapterOnClickHandler{
           void OnClick(PrevozVM prevoz);


    }

    @Override
    public int getItemViewType(int position) {
       if(position>1&&position%5==0){
           return NATIV_ADD_VIEW_TYPE;
       }
   return DEFAULT_VIEW_TYPE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context =parent.getContext();
        View view;
        LayoutInflater inflater=LayoutInflater.from(context);

        switch (viewType) {
            default:

            view = inflater.inflate(R.layout.prevoz_list_item, parent, false);
            return new PrevozAdapterViewHolder(view);
            case NATIV_ADD_VIEW_TYPE:
                view = inflater.inflate(R.layout.nativ_ads, parent, false);
                return new NativeAdViewHolder(view);


        }



    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (!(holder instanceof PrevozAdapterViewHolder)) {
            return;
        }
        final PrevozAdapterViewHolder prevozViewHolder = (PrevozAdapterViewHolder) holder;
        final PrevozVM mPrevoz=mPrevozData.get(position);
      if(mPrevoz.Korisnik.photoUrl!=null) {
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
              protected void onPostExecute(Bitmap result) {
                  super.onPostExecute(result);
                  if(result!=null) {
                      Bitmap circularBitmap = ImageConverter.getRoundedCornerBitmap(result, PIXEL_SIZE);

                      prevozViewHolder.mUserImage.setImageBitmap(circularBitmap);

                  }
              }
          }.execute(Uri.parse(mPrevoz.Korisnik.photoUrl).buildUpon().appendQueryParameter("sz", String.valueOf(PIXEL_SIZE)).build());

      }
      else{


           /*TODO Napravit da ucita neku sliku ako korisnik nema slike*/

      }


        prevozViewHolder.mUsernameTextView.setText(mPrevoz.Korisnik.ImePrezime);

if(mPrevoz.Cijena==0){
    prevozViewHolder.mCijena.setText(MyApp.getContext().getString(R.string.free));
}
else {
    prevozViewHolder.mCijena.setText(String.valueOf(mPrevoz.Cijena));
}
        prevozViewHolder.mBrojOsoba.setText(String.valueOf(mPrevoz.BrojMjesta));
        prevozViewHolder.mDatumKretanja.setText(DateConverter.to_dd_mm_yyyy_hh_mm(mPrevoz.DatumKretanja));



        String mStaniceKraj=mPrevoz.Stanice.get((mPrevoz.Stanice.size())-1).Grad;
        String mStanicaStart=mPrevoz.Stanice.get(0).Grad;
        prevozViewHolder.mRelacijaTextView.setText(mStanicaStart+" - "+mStaniceKraj);

    }

    @Override
    public int getItemCount() {
        return mPrevozData.size();
    }



    public class PrevozAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


       public TextView mRelacijaTextView;
       public TextView mUsernameTextView;
       public ImageView mUserImage;
       public TextView mCijena;
       public TextView mBrojOsoba;
       public TextView mDatumKretanja;

    public PrevozAdapterViewHolder(final View itemView) {
        super(itemView);

        mRelacijaTextView = (TextView) itemView.findViewById(R.id.tv_relacija);
        mUsernameTextView= (TextView) itemView.findViewById(R.id.tv_username);
        mUserImage= (ImageView) itemView.findViewById(R.id.image_user);
        mCijena= (TextView) itemView.findViewById(R.id.tv_cijena);
        mBrojOsoba= (TextView) itemView.findViewById(R.id.tv_brojOsoba);
        mDatumKretanja= (TextView) itemView.findViewById(R.id.tv_datum);

        itemView.setOnClickListener(this);




    }



        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            final PrevozVM prevoz = mPrevozData.get(adapterPosition);



                    onClickHandler.OnClick(prevoz);



        }
    }

    public class NativeAdViewHolder extends RecyclerView.ViewHolder {

        private final NativeExpressAdView mNativeAd;

        public NativeAdViewHolder(View itemView) {
            super(itemView);
            mNativeAd = (NativeExpressAdView) itemView.findViewById(R.id.nativeAd);
            mNativeAd.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();

                }

                @Override
                public void onAdClosed() {
                    super.onAdClosed();

                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    super.onAdFailedToLoad(errorCode);

                }

                @Override
                public void onAdLeftApplication() {
                    super.onAdLeftApplication();

                }

                @Override
                public void onAdOpened() {
                    super.onAdOpened();

                }
            });
            AdRequest.Builder builder = new AdRequest.Builder();
            if(BuildConfig.DEBUG){

                String android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                String deviceId = MD5.MD5(android_id);
                builder.addTestDevice(deviceId);


            }
            AdRequest adRequest = builder.build();

            //You can add the following code if you are testing in an emulator
            /*AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();*/
            mNativeAd.loadAd(adRequest);
        }
    }
public void setPrevozData(List<PrevozVM> data){
    mPrevozData.clear();
    mPrevozData=data;
    notifyDataSetChanged();


}
    public void addPrevozDataa(List<PrevozVM> data){

        mPrevozData.addAll(data);
        int addedSize = data.size();
        int oldSize = mPrevozData.size() - addedSize;
        notifyItemRangeInserted(oldSize, addedSize);
    }

}
