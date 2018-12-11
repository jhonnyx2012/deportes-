package com.kaiman.sports.main.news;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Toast;

import com.core.presentation.adapter.OnItemClickListener;
import com.core.presentation.contract.BaseView;
import com.core.presentation.fragment.BaseRequestFragment;
import com.kaiman.sports.Injection;
import com.kaiman.sports.R;
import com.kaiman.sports.databinding.FragmentNewsBinding;
import com.kaiman.sports.main.news.adapter.PublicationAdapter;
import com.kaiman.sports.main.news.contract.Comments;
import com.kaiman.sports.main.news.contract.News;
import com.kaiman.sports.utils.BottomSpaceItemDecoration;
import com.kaiman.sports.utils.Utils;
import java.util.List;

public class NewsFragment extends BaseRequestFragment<FragmentNewsBinding> implements News.View {

    News.Presenter presenter;
    private PublicationAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_news;
    }

    @Override
    protected void initView() {
        presenter.initialize(this);
        binder.list.addItemDecoration(new BottomSpaceItemDecoration(Utils.dpToPx(74)));
        binder.buttonCreatePublication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(CreateNewsActivity.class);
            }
        });
        binder.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refreshContent();
            }
        });

        adapter = new PublicationAdapter();
        adapter.setLikeClickListener(new PublicationAdapter.LikeClickListener() {
            @Override
            public void onLikeClicked(int position, PublicationViewModel item, boolean liked) {
                adapter.updateItemLikes(position, liked);
                presenter.onPublicationLikeClicked(position, item, liked);
            }
        });
        adapter.setDeleteListener(new PublicationAdapter.DeleteListener() {
            @Override
            public void onDeleteItem(PublicationViewModel item, int adapterPosition) {
                presenter.deletePublication(item);
            }
        });
        binder.list.setAdapter(adapter);
        binder.list.setHasFixedSize(true);
        adapter.setOnItemClickListener(new OnItemClickListener<PublicationViewModel>() {
            @Override
            public void onItemClick(int adapterPosition, PublicationViewModel item) {
                presenter.onPublicationClicked(item);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.refreshContent();
    }

    @Override
    protected void injectDependencies() {
        presenter = Injection.provideNewsPresenter();
    }

    @Override
    public void setupList(List<PublicationViewModel> items) {
        hideProgress();
        adapter.setList(items);
    }

    @Override
    public void hideProgress() {
        super.hideProgress();
        binder.swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected BaseView.Request provideRequestViewHelper() {
        return Injection.providePlaceholderHelper(binder.contentContainer);
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
    public void showNoPublicationsFound() {
        showPlaceholder(R.drawable.ic_sentiment_dissatisfied, R.string.no_news_founds_description, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onRetryClicked();
            }
        });
    }

    @Override
    public void showProgress() {
        if(!binder.swipeRefreshLayout.isRefreshing()) {
            super.showProgress();
        }
    }

    @Override
    public void showCreatePublicationButton() {
        binder.buttonCreatePublication.setVisibility(View.VISIBLE);
    }

    @Override
    public void updateReactionId(int position, String newReactionId) {
        adapter.updateItem(position, newReactionId);
    }

    @Override
    public void showReactionErrorMessage() {
        Toast.makeText(getContext(), R.string.generic_error_message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void revertReaction(int position, boolean liked) {
        adapter.updateItemLikes(position, !liked);
    }

    @Override
    public void openPublicationComments(PublicationViewModel item) {
        Bundle bundle = new Bundle();
        bundle.putString(Comments.NEWS_ID_KEY, item.id);
        startActivity(CommentsActivity.class, bundle);
    }

    @Override
    public void removePublication(PublicationViewModel item) {
        adapter.remove(item);
    }
}
