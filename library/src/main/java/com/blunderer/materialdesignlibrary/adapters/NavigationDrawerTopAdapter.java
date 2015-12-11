package com.blunderer.materialdesignlibrary.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blunderer.materialdesignlibrary.R;
import com.blunderer.materialdesignlibrary.models.ListItem;
import com.blunderer.materialdesignlibrary.models.NavigationDrawerListItemCollapsibleHeader;
import com.blunderer.materialdesignlibrary.models.NavigationDrawerListItemDivider;
import com.blunderer.materialdesignlibrary.models.NavigationDrawerListItemHeader;
import com.blunderer.materialdesignlibrary.models.NavigationDrawerListItemTopFragment;
import com.blunderer.materialdesignlibrary.models.NavigationDrawerListItemTopIntent;

import java.util.List;

public class NavigationDrawerTopAdapter extends ArrayAdapter<ListItem> {

    private final int mLayoutResourceId;
    private int mNavigationDrawerSelectedItemPosition;

    public NavigationDrawerTopAdapter(Context context,
                                      int layoutResourceId,
                                      List<ListItem> objects) {
        super(context, layoutResourceId, objects);

        mLayoutResourceId = layoutResourceId;
        mNavigationDrawerSelectedItemPosition = -1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return getItem(position) instanceof NavigationDrawerListItemTopFragment ||
                getItem(position) instanceof NavigationDrawerListItemTopIntent ||
                getItem(position) instanceof NavigationDrawerListItemCollapsibleHeader;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        ListItem item = getItem(position);

        if (convertView == null || convertView.findViewById(R.id.navigation_drawer_row_title) == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(mLayoutResourceId, parent, false);

            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.navigation_drawer_row_title);
            holder.titleHeader = (TextView) convertView
                    .findViewById(R.id.navigation_drawer_row_header);
            holder.icon = (ImageView) convertView
                    .findViewById(R.id.navigation_drawer_row_icon);
            holder.headerSeparator = convertView
                    .findViewById(R.id.navigation_drawer_row_header_separator);
            holder.collapsedIcon = (ImageView) convertView
                    .findViewById(R.id.navigation_drawer_collapsed_icon);
            convertView.setTag(holder);
        } else holder = (ViewHolder) convertView.getTag();

        convertView.setBackgroundResource(item.useBackgroundStyle() ?
                item.getBackgroundStyle() : R.drawable.navigation_drawer_selector);

        // to avoid icon being shown on recycled view to another type of ListItem
        holder.collapsedIcon.setVisibility(View.GONE);

        if (item.useTitle()) {
            try {
                holder.title.setText(item.getTitle());
                holder.titleHeader.setText(item.getTitle());
            } catch (Resources.NotFoundException e) {
                holder.title.setText("");
                holder.titleHeader.setText("");
            }
            holder.title.setTextAppearance(getContext(), item.useTitleStyle() ?
                    item.getTitleStyle() :
                    R.style.MaterialDesignLibraryTheme_NavigationDrawer_ItemsTextStyle);
            holder.titleHeader.setTextAppearance(getContext(), item.useTitleStyle() ?
                    item.getTitleStyle() :
                    R.style.MaterialDesignLibraryTheme_NavigationDrawer_SectionsTextStyle);
        }

        if (item instanceof NavigationDrawerListItemTopFragment) {
            if (itemVisibility(position)==View.VISIBLE) {
                NavigationDrawerListItemTopFragment itemNormal =
                        (NavigationDrawerListItemTopFragment) item;
                holder.title.setVisibility(View.VISIBLE);
                holder.titleHeader.setVisibility(View.GONE);
                holder.headerSeparator.setVisibility(View.GONE);
                if (mNavigationDrawerSelectedItemPosition == position &&
                        (itemNormal.useSelectedIconResource() || itemNormal.useSelectedIconUrl())) {
                    try {
                        if (itemNormal.useSelectedIconUrl()) {
                            itemNormal.getSelectedIconUrl().into(holder.icon);
                        } else holder.icon.setImageDrawable(itemNormal.getSelectedIconDrawable());
                        holder.icon.setVisibility(View.VISIBLE);
                    } catch (Resources.NotFoundException e) {
                        holder.icon.setVisibility(View.GONE);
                    }
                } else if (itemNormal.useIconResource() || itemNormal.useIconUrl()) {
                    try {
                        if (itemNormal.useIconUrl()) itemNormal.getIconUrl().into(holder.icon);
                        else holder.icon.setImageDrawable(itemNormal.getIconDrawable());
                        holder.icon.setVisibility(View.VISIBLE);
                    } catch (Resources.NotFoundException e) {
                        holder.icon.setVisibility(View.GONE);
                    }
                }
            } else {
                convertView = new LinearLayout(getContext());
                convertView.setTag(holder);
            }
        } else if (item instanceof NavigationDrawerListItemTopIntent) {
            if (itemVisibility(position) == View.VISIBLE) {
                NavigationDrawerListItemTopIntent itemNormal =
                        (NavigationDrawerListItemTopIntent) item;
                holder.title.setVisibility(View.VISIBLE);
                holder.titleHeader.setVisibility(View.GONE);
                holder.headerSeparator.setVisibility(View.GONE);
                if (itemNormal.useIconResource() || itemNormal.useIconUrl()) {
                    try {
                        if (itemNormal.useIconUrl()) itemNormal.getIconUrl().into(holder.icon);
                        else holder.icon.setImageDrawable(itemNormal.getIconDrawable());
                        holder.icon.setVisibility(View.VISIBLE);
                    } catch (Resources.NotFoundException e) {
                        holder.icon.setVisibility(View.GONE);
                    }
                }
            } else {
                convertView = new LinearLayout(getContext());
                convertView.setTag(holder);
            }
        } else if (item instanceof NavigationDrawerListItemCollapsibleHeader) {
            holder.title.setVisibility(View.GONE);
            holder.titleHeader.setVisibility(View.VISIBLE);
            holder.headerSeparator.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
            holder.icon.setVisibility(View.GONE);
            holder.collapsedIcon.setVisibility(View.VISIBLE);
            if (((NavigationDrawerListItemCollapsibleHeader)item).getVisibility() == View.VISIBLE) {
                holder.collapsedIcon.setImageResource(R.drawable.ic_arrow_drop_up);
            } else {
                holder.collapsedIcon.setImageResource(R.drawable.ic_arrow_drop_down);
            }
        } else if (item instanceof NavigationDrawerListItemHeader) {
            holder.title.setVisibility(View.GONE);
            holder.titleHeader.setVisibility(View.VISIBLE);
            holder.icon.setVisibility(View.GONE);
            holder.headerSeparator.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
        } else if (item instanceof NavigationDrawerListItemDivider) {
            holder.title.setVisibility(View.GONE);
            holder.titleHeader.setVisibility(View.GONE);
            holder.icon.setVisibility(View.GONE);
            holder.headerSeparator.setVisibility(View.GONE);
        }

        return convertView;
    }

    /**
     * Scan up to see if this item belongs to a collapsible header and return the header's visibility
     * returns View.VISIBLE if it does not belong to any collapsible header
     * @param position
     * @return
     */
    private int itemVisibility(int position) {
        do {
            position--;
            ListItem item = getItem(position);
            if (item instanceof NavigationDrawerListItemCollapsibleHeader) {
                return ((NavigationDrawerListItemCollapsibleHeader)item).getVisibility();
            } else if (item instanceof NavigationDrawerListItemHeader) {
                break;
            }
        }  while (position >= 0);
        return View.VISIBLE;
    }

    public void setNavigationDrawerSelectedItemPosition(int navigationDrawerSelectedItemPosition) {
        mNavigationDrawerSelectedItemPosition = navigationDrawerSelectedItemPosition;
    }

    private class ViewHolder {

        private TextView title;
        private TextView titleHeader;
        private ImageView icon;
        private View headerSeparator;
        private ImageView collapsedIcon;
    }

}
