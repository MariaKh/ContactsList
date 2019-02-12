package com.wowapp.contactslist.ui.contactslist;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.wowapp.contactslist.R;
import com.wowapp.contactslist.ui.widgets.expandablerecyclerview.ParentViewHolder;

public class GroupHeaderViewHolder extends ParentViewHolder {

    private static final float INITIAL_POSITION = 0.0f;
    private static final float ROTATED_POSITION = 180f;

    @NonNull
    private TextView groupName;

    public GroupHeaderViewHolder(@NonNull View itemView) {
        super(itemView);
        groupName = itemView.findViewById(R.id.group_name);
    }

    public void bind(@NonNull String name) {
        groupName.setText(name);
    }

    @Override
    public void onExpansionToggled(boolean expanded) {
        super.onExpansionToggled(expanded);
            RotateAnimation rotateAnimation;
            if (expanded) { // rotate clockwise
                rotateAnimation = new RotateAnimation(ROTATED_POSITION,
                        INITIAL_POSITION,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            } else { // rotate counterclockwise
                rotateAnimation = new RotateAnimation(-1 * ROTATED_POSITION,
                        INITIAL_POSITION,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            }

            rotateAnimation.setDuration(200);
            rotateAnimation.setFillAfter(true);
            //mArrowExpandImageView.startAnimation(rotateAnimation);
    }
}
