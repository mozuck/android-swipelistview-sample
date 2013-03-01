/*
 * Copyright (C) 2013 47 Degrees, LLC
 *  http://47deg.com
 *  hello@47deg.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.fortysevendeg.android.swipelistview.sample.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.fortysevendeg.android.swipelistview.R;

import java.util.List;

public class PackageAdapter extends BaseAdapter {

    private List<PackageItem> data;
    private Context context;

    public PackageAdapter(Context context, List<PackageItem> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public PackageItem getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final PackageItem item = getItem(position);
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.package_row, parent, false);
            holder = new ViewHolder();
            holder.ivImage = (ImageView) convertView.findViewById(R.id.example_row_iv_image);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.example_row_tv_title);
            holder.tvDescription = (TextView) convertView.findViewById(R.id.example_row_tv_description);
            holder.bAction1 = (Button) convertView.findViewById(R.id.example_row_b_action_1);
            holder.bAction2 = (Button) convertView.findViewById(R.id.example_row_b_action_2);
            holder.bAction3 = (Button) convertView.findViewById(R.id.example_row_b_action_3);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.ivImage.setImageDrawable(item.getIcon());
        holder.tvTitle.setText(item.getName());
        holder.tvDescription.setText(item.getPackageName());


        holder.bAction1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = context.getPackageManager().getLaunchIntentForPackage(item.getPackageName());
                if (intent != null) {
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, R.string.cantOpen, Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.bAction2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + item.getPackageName())));
            }
        });

        holder.bAction3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri packageUri = Uri.parse("package:" + item.getPackageName());
                Intent uninstallIntent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE, packageUri);
                context.startActivity(uninstallIntent);
            }
        });


        return convertView;
    }

    static class ViewHolder {
        ImageView ivImage;
        TextView tvTitle;
        TextView tvDescription;
        Button bAction1;
        Button bAction2;
        Button bAction3;
    }

}
