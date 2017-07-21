package com.basic.prevoz.Models;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.basic.prevoz.Helper.DateConverter;
import com.basic.prevoz.Helper.MyAnimations;
import com.basic.prevoz.R;
import com.basic.prevoz.Utils.Sesija;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Developer on 07.07.2017..
 */

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<PorukeVM> mPoruke;
    private static final int TYPE_LEFT=0;
    private static final int TYPE_RIGHT=1;
    public static final int NOT_SENT=2;
    public static final int SENT=0;
    public static final int SEEN=1;
    private static final float ALPHA_VALUE_ACTIVE_ICON= (float) 0.87;
    ChatAdapterOnClickHandler onClickHandler;

    private Context context;

    public  ChatAdapter(ChatAdapterOnClickHandler clickHandler){
        onClickHandler=clickHandler;
        mPoruke=new ArrayList<>();

    }

    public interface ChatAdapterOnClickHandler{

        void OnClick(PorukeVM poruka);

    }
    @Override
    public int getItemViewType(int position) {
        if(mPoruke.get(position).KorisnikPoslaoId== Sesija.GetSignInUser().Id){
            return  TYPE_RIGHT;
        }
        else {
            return TYPE_LEFT;

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context=parent.getContext();
        LayoutInflater inflater=LayoutInflater.from(context);
        View view;

        switch (viewType) {
            default:
                view = inflater.inflate(R.layout.chat_right_list_item, parent, false);
                return new ChatAdapterRightViewHolder(view);
            case TYPE_LEFT:
                view = inflater.inflate(R.layout.chat_left_list_item, parent, false);
                return new ChatAdapterLeftViewHolder(view);


        }


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PorukeVM poruka=mPoruke.get(position);
        if(holder instanceof ChatAdapterLeftViewHolder) {
            ChatAdapterLeftViewHolder chatHolder=(ChatAdapterLeftViewHolder) holder;
            chatHolder.mPoruka.setText(poruka.Text);
            chatHolder.mDatum.setText(DateConverter.to_hh_mm(poruka.DatumKreiranja));
        }
        else{
            ChatAdapterRightViewHolder chatHolder=(ChatAdapterRightViewHolder) holder;
            chatHolder.mPoruka.setText(poruka.Text);

            switch (poruka.Status){

                case NOT_SENT:
                    chatHolder.mErrorNotSent.setVisibility(View.VISIBLE);
                    chatHolder.mReadCheck.setVisibility(View.GONE);
                    chatHolder.mSendCheck.setVisibility(View.GONE);
                    chatHolder.mDatum.setVisibility(View.GONE);
                    break;


                case SENT:
                    chatHolder.mSendCheck.setAlpha(ALPHA_VALUE_ACTIVE_ICON);
                    chatHolder.mDatum.setText(DateConverter.to_hh_mm(poruka.DatumKreiranja));
                    chatHolder.mDatum.setVisibility(View.VISIBLE);
                    break;


                case SEEN:
                    chatHolder.mReadCheck.setAlpha(ALPHA_VALUE_ACTIVE_ICON);
                    chatHolder.mSendCheck.setAlpha(ALPHA_VALUE_ACTIVE_ICON);
                    chatHolder.mDatum.setText(DateConverter.to_hh_mm(poruka.DatumKreiranja));
                    break;
                default:
            }




        }






    }

    @Override
    public int getItemCount() {
        return mPoruke.size();
    }



    public class ChatAdapterRightViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final public   TextView mPoruka;
        final public   TextView mDatum;
        final public   TextView mErrorNotSent;
        final public ImageView mSendCheck;
        final public ImageView mReadCheck;
        public ChatAdapterRightViewHolder(View itemView) {
            super(itemView);
            this.mPoruka = (TextView) itemView.findViewById(R.id.tv_poruka);
            this.mDatum = (TextView) itemView.findViewById(R.id.tv_datum);
            this.mErrorNotSent = (TextView) itemView.findViewById(R.id.tv_message_error);
            this.mSendCheck=(ImageView) itemView.findViewById(R.id.image_check_send);
            this.mReadCheck=(ImageView) itemView.findViewById(R.id.image_check_read);

         itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
           doOnClickMessage(v);


        }

    }
    public class ChatAdapterLeftViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final public   TextView mPoruka;
        final public   TextView mDatum;
        public ChatAdapterLeftViewHolder(View itemView) {
            super(itemView);
            this.mPoruka = (TextView) itemView.findViewById(R.id.tv_poruka);
            this.mDatum = (TextView) itemView.findViewById(R.id.tv_datum);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            doOnClickMessage(v);


        }

    }

    private void doOnClickMessage(View v) {
    }




    public void setPorukeData(List<PorukeVM> data){
        mPoruke.clear();
        mPoruke=data;
        notifyDataSetChanged();


    }
    public void addPorukeDataa(List<PorukeVM> data){

        mPoruke.addAll(data);
        int addedSize = data.size();
        int oldSize = mPoruke.size() - addedSize;
        notifyItemRangeInserted(oldSize, addedSize);
    }
    public void addLastPoruka(PorukeVM data){
        mPoruke.add(0, data);
        notifyItemInserted(0);
    }

    public void addNewPorukeDataa(List<PorukeVM> data){

        mPoruke.addAll(0,data);
        int addedSize = data.size();
        notifyItemRangeInserted(0, addedSize);
    }
}
