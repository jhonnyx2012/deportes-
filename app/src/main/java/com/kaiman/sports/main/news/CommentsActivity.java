package com.kaiman.sports.main.news;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import com.core.presentation.activity.BaseRequestActivity;
import com.core.presentation.contract.BaseView;
import com.kaiman.sports.Injection;
import com.kaiman.sports.R;
import com.kaiman.sports.databinding.ActivityCommentsBinding;
import com.kaiman.sports.main.news.adapter.CommentAdapter;
import com.kaiman.sports.main.news.contract.Comments;
import com.kaiman.sports.utils.BottomSpaceItemDecoration;
import com.kaiman.sports.utils.Utils;
import java.util.ArrayList;
import java.util.List;

import static com.kaiman.sports.main.news.contract.Comments.NEWS_ID_KEY;

public class CommentsActivity extends BaseRequestActivity<ActivityCommentsBinding> implements Comments.View {

    Comments.Presenter presenter;
    private CommentAdapter adapter;
    private String newsId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        newsId = getIntent().getExtras().getString(NEWS_ID_KEY);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_comments;
    }

    @Override
    protected void initView() {
        showUpButton(true);
        setTitle(R.string.comments);
        presenter.initialize(this);
        binder.list.addItemDecoration(new BottomSpaceItemDecoration(Utils.dpToPx(74)));
        binder.buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onSendClicked(getComment());
            }
        });
        binder.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refreshContent();
            }
        });

        adapter = new CommentAdapter();

        binder.list.setAdapter(adapter);
        binder.list.setHasFixedSize(true);
    }

    private String getComment() {
        return binder.editTextComment.getText().toString().trim();
    }

    @Override
    protected void injectDependencies() {
        presenter = Injection.provideCommentsPresenter(newsId);
    }

    @Override
    public void setupList(List<CommentViewModel> items) {
        hideProgress();
        adapter.setList(items);
        adapter.setDeleteClickListener(new CommentAdapter.DeleteClickListener(){
            @Override
            public void onDeleteClicked(int position, CommentViewModel item) {
                presenter.onDeleteCommentClicked(position, item);
            }
        });
    }

    @Override
    public void hideProgress() {
        super.hideProgress();
        binder.swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected BaseView.Request provideRequestViewHelper() {
        return Injection.providePlaceholderHelper(binder.placeholderContainer);
    }

    @Override
    public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onRetryLoadingClicked() {
        presenter.onRetryClicked();
    }

   @Override
    public void showNoCommentsFound() {
        showPlaceholder(R.drawable.ic_sentiment_dissatisfied, R.string.no_comments_found_description, null);
    }

    @Override
    public void removeCommentByPosition(int position) {
        hideProgress();
        adapter.remove(position);
        if (adapter.getItemCount() == 0) {
            showNoCommentsFound();
        }
    }

    @Override
    public void showDeleteErrorMessage() {
        showMessage(R.string.app_name, getString(R.string.successfull_comment_deleted));
    }

    @Override
    public void addComment(CommentViewModel commentViewModel) {
        if (adapter.getItemCount() == 0) {
            setupList(new ArrayList<CommentViewModel>());
        } else {
            hideProgress();
        }
        adapter.add(commentViewModel);
    }

    @Override
    public void showConfirmDeleteCommentDialog(final int position, final CommentViewModel item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.confirmation);
        builder.setMessage(R.string.confirmation_delete_comment_message);

        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                presenter.deleteComment(position, item);
                dialog.dismiss();
            } });

        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            } });
        builder.show();
    }

    @Override
    public void clearInput() {
        binder.editTextComment.setText("");
    }

    @Override
    public void showProgress() {
        if(!binder.swipeRefreshLayout.isRefreshing()) {
            super.showProgress();
        }
    }
}
