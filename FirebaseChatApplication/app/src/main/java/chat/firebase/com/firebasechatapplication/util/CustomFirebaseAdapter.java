//package chat.firebase.com.firebasechatapplication.util;
//
//import android.app.Activity;
//import android.content.Context;
//import android.os.Build;
//import android.support.annotation.NonNull;
//import android.support.annotation.RequiresApi;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.firebase.ui.database.FirebaseRecyclerAdapter;
//import com.google.firebase.database.Query;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import chat.firebase.com.firebasechatapplication.MessageActivity;
//import chat.firebase.com.firebasechatapplication.R;
//import chat.firebase.com.firebasechatapplication.model.Message;
//import chat.firebase.com.firebasechatapplication.viewholder.LoadingViewHolder;
//import chat.firebase.com.firebasechatapplication.viewholder.MessageViewHolder;
//
//public class CustomFirebaseAdapter extends FirebaseRecyclerAdapter<Message,  RecyclerView.ViewHolder> {
//
//    private final int VIEW_TYPE_ITEM = 0;
//    private final int VIEW_TYPE_LOADING = 1;
//    private final Class<Message> modelClass;
//    private final int modelLayout;
//    private final Class<RecyclerView.ViewHolder> viewHolderClass;
//    private final Context context;
//
//    Query query;
//
//    public CustomFirebaseAdapter(Class<Message> modelClass, int modelLayout, Class<RecyclerView.ViewHolder> viewHolderClass, Query ref,Context context) {
//        super(modelClass, modelLayout, viewHolderClass, ref);
//        this.modelClass = modelClass;
//        this.modelLayout = modelLayout;
//        this.viewHolderClass = viewHolderClass;
//        query = ref;
//
//        this.context = context;
//    }
//
//
//
//    @NonNull
//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//// Create a new instance of the ViewHolder, in this case we are using a custom
//// layout called R.layout.message for each item
//        if (viewType == VIEW_TYPE_ITEM) {
//            View view = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false);
//            return new MessageViewHolder(view);
//        } else if (viewType == VIEW_TYPE_LOADING) {
//            View view = LayoutInflater.from(context).inflate(R.layout.item_loading, parent, false);
//            return new LoadingViewHolder(view);
//        }
//        return null;
//    }
//
//    @Override
//    protected void populateViewHolder(RecyclerView.ViewHolder viewHolder, Message model, int position) {
//
//        if (viewHolder instanceof MessageViewHolder) {
//            MessageViewHolder messageViewHolderViewHolder = (MessageViewHolder) viewHolder;
//            messageViewHolderViewHolder.authorView.setText(model.author);
//            messageViewHolderViewHolder.timeView.setText(model.time);
//            messageViewHolderViewHolder.bodyView.setText(model.body);
//        } else if (viewHolder instanceof LoadingViewHolder) {
//            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) viewHolder;
//            loadingViewHolder.progressBar.setIndeterminate(true);
//        }
//
//    }
//
//}
//
