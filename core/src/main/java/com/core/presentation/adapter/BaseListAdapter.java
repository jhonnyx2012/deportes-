package com.core.presentation.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.core.R;
import com.core.presentation.adapter.holder.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseListAdapter<MODEL,VIEW_HOLDER extends BaseViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected static final int VIEW_TYPE_FOOTER= 34444;
    protected static final int VIEW_TYPE_ITEM = 5454;
    public static final int VIEW_TYPE_LOADING = 65656;
    private static final String RV_STATE_KEY = "rv_state";
    private static final String RV_DATA_KEY = "rv_data";
    private static final String RV_LAST_PAGE_KEY = "rv_last_page";
    protected com.core.presentation.adapter.OnItemClickListener<MODEL> onItemClickListener;
    protected List<MODEL> list;
    private boolean hasFooter;
    private boolean isItemClickable;
    private boolean isLoading;
    private int itemsPerPage;
    private int lastPage;
    private OnItemLongClickListener<MODEL> longClickListener;

    public BaseListAdapter() {
        this.list = new ArrayList<>();
        this.onItemClickListener = null;
        setDefaults(false,false);
    }

    public BaseListAdapter(List<MODEL> list) {
        this.list = list;
        this.onItemClickListener = null;
        setDefaults(false,false);
    }

    public BaseListAdapter(List<MODEL> list, com.core.presentation.adapter.OnItemClickListener<MODEL> listener) {
        this.list=list;
        this.onItemClickListener=listener;
        setDefaults(false,true);
    }

    public void setDefaults(boolean hasFooter,boolean isItemClickable) {
        setHasFooter(hasFooter);
        setItemClicklable(isItemClickable);
    }

    public void setOnItemClickListener(com.core.presentation.adapter.OnItemClickListener<MODEL> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        setItemClicklable(true);
    }

    public void setOnLoadMoreListener(final RecyclerView recyclerView, final int itemsPerPage, final OnLoadMoreListener mOnLoadMoreListener) {
        isLoading=false;
        this.itemsPerPage=itemsPerPage;
        try {
            final NestedScrollView nestedScrollView = (NestedScrollView) recyclerView.getParent().getParent();
            nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(final NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (v.getChildAt(v.getChildCount() - 1) != null) {
                        if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                                scrollY > oldScrollY) {
                            if(attempToLoadMore(recyclerView,mOnLoadMoreListener)){
                                v.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        v.fullScroll(View.FOCUS_DOWN);
                                    }
                                },100);
                            }
                        }
                    }
                }
            });
        } catch (Exception e) {
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    int lastVisiblePosition = getLastVisiblePosition(recyclerView.getLayoutManager());
                    if (lastVisiblePosition >= getItemCount()-2) {
                        if(attempToLoadMore(recyclerView,mOnLoadMoreListener)){
                            recyclerView.getLayoutManager().scrollToPosition(list.size()-1);
                        }
                    }
                }
            });
        }
    }

    public boolean isLoading() {
        return isLoading;
    }

    private int getLastVisiblePosition(RecyclerView.LayoutManager layoutManager) {
        if(layoutManager instanceof LinearLayoutManager)
            return ((LinearLayoutManager)layoutManager).findLastCompletelyVisibleItemPosition();
        else return 0;
    }

    private boolean attempToLoadMore(View view, final OnLoadMoreListener loadMoreListener) {
        if (!isLoading) {
            final int page = getPage();
            if (loadMoreListener != null&&page!=lastPage&&page>0) {
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        setLoading(true);
                        loadMoreListener.onLoadMore(page);
                        lastPage=page;
                    }
                });
                return true;
            }
        }
        return false;
    }

    private int getPage() {
        int page;
        if(getList() == null || getList().size()==0)
            page=0;
        else
            page=(getList().size()/itemsPerPage);
        return page;
    }

    public void setLoading(boolean loading) {
        if(list==null)return;
        isLoading = loading;
        if(loading){
            list.add(null);
            notifyItemInserted(list.size()-1);
        }else if(!list.isEmpty()&&list.indexOf(null)!=-1){
            int index = list.indexOf(null);
            list.remove(index);
            notifyItemRemoved(index);
        }
    }

    protected void setItemClicklable(boolean clicklable) {
        this.isItemClickable=clicklable;
    }

    @Override
    public int getItemViewType(int position) {
        if(list==null)return 0;
        int footerIndex = list.size();
        if (hasFooter&&(position ==footerIndex)) {
            return VIEW_TYPE_FOOTER;
        }
        int loadingIndex = list.size()-1;
        if (list.get(position)==null&&position == loadingIndex){
            return VIEW_TYPE_LOADING;
        }
        return VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int idLayout = viewType == VIEW_TYPE_LOADING ? R.layout.item_loading : getLayoutIdByType(viewType);
        ViewDataBinding binding= DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),idLayout,parent,false);
        View v= binding.getRoot();
        if (viewType == VIEW_TYPE_FOOTER) {
            return getNewFooterViewHolder(v);
        }else if(viewType==VIEW_TYPE_LOADING)
            return new LoadingViewHolder(v);
        return createViewHolder(viewType,v);
    }

    protected abstract RecyclerView.ViewHolder createViewHolder(int viewType, View v);

    public void remove(int adapterPosition) {
        list.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
    }

    public void addAtFirst(MODEL item) {
        list.add(0,item);
        notifyItemInserted(0);
    }

    public void add(MODEL item) {
        list.add(item);
        notifyItemInserted(list.size()-1);
    }

    public void setList(List<MODEL> items, boolean notify) {
        list=items;
        if(notify)
            notifyDataSetChanged();
    }

    protected RecyclerView.ViewHolder getNewFooterViewHolder(View v) {
        return null;
    }

    protected abstract int getLayoutIdByType(int viewType);

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if(isItemClickable)
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener!=null&&getItem(holder.getAdapterPosition())!=null)
                        onItemClickListener.onItemClick(holder.getAdapterPosition(),getItem(holder.getAdapterPosition()));
                }
            });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(longClickListener!=null)
                    longClickListener.onItemLongClick(holder,holder.getAdapterPosition(),getItem(holder.getAdapterPosition()));
                return longClickListener!=null;
            }
        });
        if (holder instanceof LoadingViewHolder)
            onBindLoadingViewHolder((LoadingViewHolder) holder);
        else if(getItem(position)!=null)
            bindViewHolder((VIEW_HOLDER) holder,position,getItem(position));
    }

    protected void bindViewHolder(VIEW_HOLDER holder, int position, MODEL item) {
        holder.bind(position,item);
    }

    private void onBindLoadingViewHolder(LoadingViewHolder holder) {
        holder.progressBar.setIndeterminate(true);
        holder.progressBar.setVisibility(View.VISIBLE);
    }

    public MODEL getItem(int position) {
        return position<0||list.size()==0||position>=list.size()?null:list.get(position);
    }

    @Override
    public int getItemCount() {
        int count = list==null?0:list.size();
        if(hasFooter)
            count++;
        return count;
    }

    public void setHasFooter(boolean hasFooter) {
        this.hasFooter = hasFooter;
    }

    public List<MODEL> getList() {
        return list;
    }

    public void setList(List<MODEL> items) {
        setList(items,true);
    }

    public boolean isItemVisible(LinearLayoutManager layoutManager, int i) {
        int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
        int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
        return i>=firstVisibleItem&&i<=lastVisibleItem;
    }

    public boolean isItemVisible(int firstVisibleItem,int lastVisibleItem, int i) {
        return i>=firstVisibleItem&&i<=lastVisibleItem;
    }

    /**
     * Return true if item was added, and return false if item was changed
     * @param item
     * @return
     */
    public boolean addOrUpdate(MODEL item,boolean notify) {
        int index = list.indexOf(item);
        if(index==-1){
            list.add(item);
            if(notify)
                notifyItemInserted(list.size()-1);
        }else{
            list.set(index,item);
            if(notify)
                notifyItemChanged(index);
        }
        return index!=-1;
    }

    public boolean addOrUpdate(MODEL item) {
        return addOrUpdate(item,true);
    }

    public void addAll(List<MODEL> newItems) {
        int itemCount = list.size();
        list.addAll(newItems);
        notifyItemRangeInserted(itemCount,newItems.size());
    }

    public void remove(MODEL item) {
        int index = list.indexOf(item);
        if(index!=-1) {
            list.remove(index);
            notifyItemRemoved(index);
        }
    }

    public MODEL getLastItem() {
        return list!=null&&!list.isEmpty()?list.get(list.size()-1):null;
    }

    public void updateItem(MODEL value) {
        int index = list.indexOf(value);
        if(index!=-1){
            updateItem(value,index);
        }
    }

    public void updateItem(MODEL item, int i) {
        list.set(i,item);
        notifyItemChanged(i);
    }

    public void updateAll(List<MODEL> items) {
        for (MODEL item : items) {
            updateItem(item);
        }
    }

    public void onSaveInstanceState(Bundle outState, RecyclerView recyclerView) {
        //TODO restaurar la lista de otro modo con fenix
        outState.putParcelable(RV_STATE_KEY,recyclerView.getLayoutManager().onSaveInstanceState());
        //outState.putParcelableArrayList(RV_DATA_KEY, (ArrayList<? extends Parcelable>) list);
        outState.putInt(RV_LAST_PAGE_KEY,lastPage);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState, RecyclerView recyclerView) {
        if(savedInstanceState==null)return;
        //TODO restaurar la lista de otro modo con fenix
        //list=savedInstanceState.getParcelableArrayList(RV_DATA_KEY);
        lastPage=savedInstanceState.getInt(RV_LAST_PAGE_KEY);
        recyclerView.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable(RV_STATE_KEY));
    }

    public void setOnLongClickListener(OnItemLongClickListener<MODEL> longClickListener) {
        this.longClickListener=longClickListener;
    }

    public static abstract class OnItemLongClickListener<T>{
        public void onItemLongClick(RecyclerView.ViewHolder holder, int adapterPosition, T item){
            onItemLongClick(adapterPosition,item);
        }

        public abstract void onItemLongClick(int adapterPosition, T item);
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;
        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar= itemView.findViewById(R.id.progressBar);
        }
    }
}