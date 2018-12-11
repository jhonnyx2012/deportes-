package com.kaiman.sports.main.news.adapter;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.core.presentation.adapter.BaseListAdapter;
import com.core.presentation.adapter.holder.BaseViewHolder;
import com.kaiman.sports.R;
import com.kaiman.sports.databinding.ItemPublicationBinding;
import com.kaiman.sports.main.news.PublicationViewModel;

/**
 * Created by jhonnybarrios on 3/15/18
 */

public class PublicationAdapter extends BaseListAdapter<PublicationViewModel,PublicationAdapter.ViewHolder> {
    private LikeClickListener likeClickListener;
    private DeleteListener deleteListener;

    @Override
    protected RecyclerView.ViewHolder createViewHolder(int viewType, View v) {
        return new ViewHolder(v);
    }

    public void setLikeClickListener(LikeClickListener likeClickListener) {
        this.likeClickListener = likeClickListener;
    }

    public void setDeleteListener(DeleteListener deleteListener) {
        this.deleteListener = deleteListener;
    }

    @Override
    protected int getLayoutIdByType(int viewType) {
        return R.layout.item_publication;
    }

    public void updateItemLikes(int position, boolean liked) {
        PublicationViewModel item = getItem(position);
        item.likesCount = String.valueOf(liked ? Integer.valueOf(item.likesCount) + 1 : Integer.valueOf(item.likesCount) - 1);
        item.isUpdating = true;
        updateItem(item, position);
    }

    public void updateItem(int position, String newReactionId) {
        PublicationViewModel item = getItem(position);
        item.reactionId = newReactionId;
        item.isUpdating = false;
        updateItem(item, position);
    }

    class ViewHolder extends BaseViewHolder<PublicationViewModel, ItemPublicationBinding> implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
        ViewHolder(View itemView) {
            super(itemView);
            binder.imageLike.setOnCheckedChangeListener(this);
            binder.imageDelete.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    deleteListener.onDeleteItem(getItem(getAdapterPosition()), getAdapterPosition());
                }
            });
        }

        @Override
        public void bind(int position, PublicationViewModel item) {
            binder.imageLike.setEnabled(!item.isUpdating);
            changeChecked(binder.imageLike, item.iLikeIt());
            binder.textUsername.setText(item.userName);
            binder.textLikesCount.setText(item.likesCount);
            binder.textDescription.setText(item.description);
            binder.textTimeAgo.setReferenceTime(item.date.getTime());
            if (Integer.valueOf(item.commentsCount) == 1) {
                binder.textNComments.setText(getString(R.string.n_comment, item.commentsCount));
            } else {
                binder.textNComments.setText(getString(R.string.n_comments, item.commentsCount));
            }

            binder.imageDelete.setVisibility(item.canDelete ? View.VISIBLE : View.GONE);

            if (item.userPhotoUrl != null && !item.userPhotoUrl.isEmpty()) {
                Glide.with(getContext())
                        .load(item.userPhotoUrl)
                        .apply(new RequestOptions()
                                .error(buildInitialDrawable(item.userName))
                                .placeholder(buildInitialDrawable(item.userName))
                                .circleCrop())
                        .into(binder.imageUserPhoto);
            } else {
                binder.imageUserPhoto.setImageDrawable(buildInitialDrawable(item.userName));
            }

            Glide.with(getContext())
                    .load(item.photoUrl)
                    .into(binder.imagePhoto);

            binder.imageLike.setOnClickListener(this);
        }

        private void changeChecked(CheckBox checkBox, boolean checked) {
            checkBox.setOnCheckedChangeListener(null);
            checkBox.setChecked(checked);
            checkBox.setOnCheckedChangeListener(this);
        }

        private Drawable buildInitialDrawable(String text) {
            if (text != null && !text.isEmpty()) {
                return TextDrawable.builder().buildRound(text.charAt(0)+"", Color.GRAY);
            }
            return getDrawable(R.drawable.ic_dots);
        }

        private void toggleSelection(View view) {
            view.setSelected(!view.isSelected());
        }

        @Override
        public void onClick(View v) {
            toggleSelection(v);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            buttonView.setEnabled(false);
            PublicationViewModel item = getItem(getAdapterPosition());
            likeClickListener.onLikeClicked(getAdapterPosition(), item, isChecked);
        }
    }

    public interface LikeClickListener {
        void onLikeClicked(int position, PublicationViewModel item, boolean liked);
    }

    public interface DeleteListener {
        void onDeleteItem(PublicationViewModel item, int adapterPosition);
    }
}