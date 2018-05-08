package chat.firebase.com.firebasechatapplication.util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import chat.firebase.com.firebasechatapplication.ChatMessagesActivity;
import chat.firebase.com.firebasechatapplication.R;
import chat.firebase.com.firebasechatapplication.UpdateProfileActivity;
import chat.firebase.com.firebasechatapplication.model.ChatMessage;
import chat.firebase.com.firebasechatapplication.model.FileModel;


public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {
    public static final int ITEM_TYPE_SENT = 0;
    public static final int ITEM_TYPE_RECEIVED = 1;

    private List<ChatMessage> mMessagesList=new ArrayList<>();
    private Context mContext;



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView messageTextView;
        ImageView imageView;
        LinearLayout msgLayout;

        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            messageTextView = (TextView) v.findViewById(R.id.chatMsgTextView);
            imageView = (ImageView) v.findViewById(R.id.image);
            msgLayout = (LinearLayout) v.findViewById(R.id.layout);


            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(mContext,"Removed "+getAdapterPosition(),Toast.LENGTH_SHORT).show();
                    ((ChatMessagesActivity)mContext).removeSenderMessage(getAdapterPosition());
                    return true;
                }
            });
        }
    }

    public class ViewHolder2 extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView messageTextView;
        ImageView imageView;
        LinearLayout msgLayout;

        public View layout;

        public ViewHolder2(View v) {
            super(v);
            layout = v;
            messageTextView = (TextView) v.findViewById(R.id.chatMsgTextView);
            imageView = (ImageView) v.findViewById(R.id.image);
            msgLayout = (LinearLayout) v.findViewById(R.id.layout);

        }
    }


    public void add(int position, ChatMessage message) {
        mMessagesList.add(position, message);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        mMessagesList.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MessagesAdapter(List<ChatMessage> myDataset, Context context) {
        mMessagesList = myDataset;
        mContext = context;

    }

//    public MessagesAdapter(Context mContext) {
//        this.mContext = mContext;
//    }

    @Override
    public int getItemViewType(int position) {
        if (mMessagesList.get(position).getSenderId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            return ITEM_TYPE_SENT;
        } else {
            return ITEM_TYPE_RECEIVED;
        }
    }



    // Create new views (invoked by the layout manager)
    @Override
    public MessagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        View v = null;
        if (viewType == ITEM_TYPE_SENT) {
            v = LayoutInflater.from(mContext).inflate(R.layout.sent_msg_row, null);
        } else if (viewType == ITEM_TYPE_RECEIVED) {
            v = LayoutInflater.from(mContext).inflate(R.layout.received_msg_row, null);
        }
        return new ViewHolder(v); // view holder for header items
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        ChatMessage msg = mMessagesList.get(position);

//        if (msg.getFile()!=null) {
//            holder.imageView.setVisibility(View.VISIBLE);
//            holder.msgLayout.setVisibility(View.GONE);
//            FileModel fileModel = msg.getFile();
//            if (fileModel.getUrl_file() != null)
//                Picasso.with(mContext).load(fileModel.getUrl_file()).placeholder(R.mipmap.ic_launcher).into(holder.imageView);
//        }else {
//            holder.imageView.setVisibility(View.GONE);
            holder.msgLayout.setVisibility(View.VISIBLE);
            holder.messageTextView.setText(msg.getMessage());

//        }





    }
    public void addAll(List<ChatMessage> newMessages) {

        mMessagesList.addAll(newMessages);
        int initialSize = mMessagesList.size()-1;
        notifyItemInserted(initialSize);
//        notifyItemRangeInserted(initialSize, newMessages.size());
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mMessagesList.size();
    }

    public String getLastItemId() {
        return mMessagesList.get(mMessagesList.size() - 1).getMessageId();
    }
    public String getFirstItemId() {
        return mMessagesList.get(0).getMessageId();
    }

}