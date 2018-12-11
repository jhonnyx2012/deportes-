package com.kaiman.sports.main.news.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.core.presentation.adapter.BaseListAdapter;
import com.core.presentation.adapter.holder.BaseViewHolder;
import com.kaiman.sports.R;
import com.kaiman.sports.databinding.ItemCommentBinding;
import com.kaiman.sports.main.news.CommentViewModel;

/**
 * Created by jhonnybarrios on 3/15/18
 */

public class CommentAdapter extends BaseListAdapter<CommentViewModel,CommentAdapter.ViewHolder> {
    private DeleteClickListener deleteListener;

    @Override
    protected RecyclerView.ViewHolder createViewHolder(int viewType, View v) {
        return new ViewHolder(v);
    }

    public void setDeleteClickListener(DeleteClickListener deleteListener) {
        this.deleteListener = deleteListener;
    }

    @Override
    protected int getLayoutIdByType(int viewType) {
        return R.layout.item_comment;
    }

    class ViewHolder extends BaseViewHolder<CommentViewModel, ItemCommentBinding> {
        ViewHolder(View itemView) {
            super(itemView);
            binder.imageDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommentViewModel item = getItem(getAdapterPosition());
                    deleteListener.onDeleteClicked(getAdapterPosition(), item);
                }
            });
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void bind(int position, CommentViewModel item) {
            binder.textComment.setText(item.commentary);
            binder.textName.setText(item.userName.trim() + ":");
            binder.textDate.setReferenceTime(item.date.getTime());

            if (item.userPhotoUrl != null && !item.userPhotoUrl.isEmpty()) {
                Glide.with(getContext())
                        .load(item.userPhotoUrl)
                        .apply(new RequestOptions()
                                .error(buildInitialDrawable(item.userName))
                                .placeholder(buildInitialDrawable(item.userName))
                                .circleCrop())
                        .into(binder.imageAvatar);
            } else {
                binder.imageAvatar.setImageDrawable(buildInitialDrawable(item.userName));
            }

            binder.imageDelete.setVisibility(item.canBeDeleted? View.VISIBLE : View.GONE);
        }

        private Drawable buildInitialDrawable(String text) {
            if (text != null && !text.isEmpty()) {
                return TextDrawable.builder().buildRound(text.charAt(0)+"", Color.GRAY);
            }
            return getDrawable(R.drawable.ic_dots);
        }
    }

    public interface DeleteClickListener {
        void onDeleteClicked(int position, CommentViewModel item);
    }
}