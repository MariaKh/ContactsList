package com.wowapp.contactslist.ui.contactslist;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.wowapp.contactslist.R;
import com.wowapp.contactslist.ui.widgets.expandablerecyclerview.ParentViewHolder;

class GroupHeaderViewHolder extends ParentViewHolder {

    private static final float INITIAL_POSITION = 0.0f;
    private static final float ROTATED_POSITION = 90f;

    private TextView groupName;
    private ImageView arrow;

    GroupHeaderViewHolder(@NonNull View itemView) {
        super(itemView);
        groupName = itemView.findViewById(R.id.group_name);
        arrow = itemView.findViewById(R.id.group_header_arrow);
    }

    void bind(@NonNull String name) {
        groupName.setText(name);
    }

    @Override
    public void onExpansionToggled(boolean expanded) {
        super.onExpansionToggled(expanded);
        RotateAnimation rotateAnimation;
        if (expanded) {
            rotateAnimation = new RotateAnimation(INITIAL_POSITION,
                    -1 * ROTATED_POSITION,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        } else {
            rotateAnimation = new RotateAnimation(-1 * ROTATED_POSITION,
                    INITIAL_POSITION,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        }
        rotateAnimation.setDuration(200);
        rotateAnimation.setFillAfter(true);
        arrow.startAnimation(rotateAnimation);
    }
}
