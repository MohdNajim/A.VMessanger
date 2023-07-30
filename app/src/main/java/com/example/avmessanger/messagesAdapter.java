package com.example.avmessanger;

import static com.example.avmessanger.chatwin.reciverIImg;
import static com.example.avmessanger.chatwin.senderImg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class messagesAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<msgmodelclass> messagesAdapterArrayList;
    int item_SEND=1;
    int item_RECIVE=2;

    public messagesAdapter(Context context, ArrayList<msgmodelclass> messagesAdapterArrayList) {
        this.context = context;
        this.messagesAdapterArrayList = messagesAdapterArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==item_SEND){
            View view= LayoutInflater.from(context).inflate(R.layout.sender_layout,parent,false);
            return new senderViewHolder(view);
        }
        else {
            View view=LayoutInflater.from(context).inflate(R.layout.reciver_layout,parent,false);
            return new reciverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        msgmodelclass messages =messagesAdapterArrayList.get(position);

        if (holder.getClass()==senderViewHolder.class){
            senderViewHolder viewHolder=(senderViewHolder) holder;
            viewHolder.msgtxt.setText(messages.getMessage());
            Picasso.get().load(senderImg).into(viewHolder.circleImageView);
        }
        else {
            reciverViewHolder viewHolder=(reciverViewHolder) holder;
            viewHolder.msgtxt.setText(messages.getMessage());
            Picasso.get().load(reciverIImg).into(viewHolder.circleImageView);
        }

    }

    @Override
    public int getItemCount() {

        return messagesAdapterArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        msgmodelclass messages=messagesAdapterArrayList.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.senderid)){
            return item_SEND;
        }
        else {
            return item_RECIVE;
        }
    }

    class senderViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView msgtxt;

        public senderViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView =itemView.findViewById(R.id.profilerggg);
            msgtxt=itemView.findViewById(R.id.msgsendertyp);
        }
    }

    class reciverViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView msgtxt;

        public reciverViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView =itemView.findViewById(R.id.pro);
            msgtxt=itemView.findViewById(R.id.recivertextset);
        }
    }

}
