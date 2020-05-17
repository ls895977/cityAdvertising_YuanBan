package com.juyijia.mm.activity;

import android.content.Context;
import android.content.Intent;

import com.juyijia.mm.R;

public class CommunityAgentActivity extends AbsActivity {

    public static void startAct(Context context){
        context.startActivity(new Intent(context, CommunityAgentActivity.class));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_community_agent;
    }

    public static void main(String[] args) {

    }
}
