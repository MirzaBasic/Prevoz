package com.basic.prevoz.Models;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
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
 * Created by Developer on 27.06.2017..
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageAdapterViewHolder> {

    private Context context;
    private List<PorukeVM> mMesseges;
    MessageAdapterOnClickHandler onClickHandler;
    private static int PIXEL_SIZE=135;

    public MessageAdapter(MessageAdapterOnClickHandler clickHandler){
        mMesseges=new ArrayList<>();
        onClickHandler=clickHandler;

    }
    public  interface MessageAdapterOnClickHandler{
        void OnClick(PorukeVM message);
    }

    @Override
    public MessageAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context=parent.getContext();
        int layoutIdFromItemlist= R.layout.message_list_item;
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(layoutIdFromItemlist,parent,false);
        return new MessageAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MessageAdapterViewHolder holder, int position) {
        PorukeVM message=mMesseges.get(position);

        holder.mImeKorisnika.setText(message.Korisnik.ImePrezime);
        holder.mLastMessage.setText(message.Text);
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

                    Bitmap circularBitmap=ImageConverter.getRoundedCornerBitmap(bitmap,PIXEL_SIZE);
                    holder.mSlikaKorisnika.setImageBitmap(circularBitmap);
                }
                else{


                }
            }
        }.execute(Uri.parse(message.Korisnik.photoUrl).buildUpon().appendQueryParameter("sz",String.valueOf(PIXEL_SIZE)).build());

    }


    @Override
    public int getItemCount() {
        return 0;
    }

  public class MessageAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

      ImageView mSlikaKorisnika;
      TextView mImeKorisnika;
      TextView mLastMessage;

      public MessageAdapterViewHolder(View itemView) {


          super(itemView);
          mSlikaKorisnika= (ImageView) itemView.findViewById(R.id.image_user);
          mImeKorisnika= (TextView) itemView.findViewById(R.id.tv_username);
          mLastMessage= (TextView) itemView.findViewById(R.id.tv_last_message);
          itemView.setOnClickListener(this);
      }


      @Override
      public void onClick(View v) {
int adapterPosition=getAdapterPosition();
          PorukeVM message=mMesseges.get(adapterPosition);
          onClickHandler.OnClick(message);
      }

  }
    public  void setMessagesData(List<PorukeVM> messages){

        mMesseges=messages;
    }
}
