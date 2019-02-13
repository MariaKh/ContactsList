package com.wowapp.contactslist.ui.contactslist;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wowapp.contactslist.R;
import com.wowapp.contactslist.domain.models.Contact;
import com.wowapp.contactslist.ui.widgets.expandablerecyclerview.ChildViewHolder;

import static com.wowapp.contactslist.domain.models.AnnotationStatus.AWAY;
import static com.wowapp.contactslist.domain.models.AnnotationStatus.BUSY;
import static com.wowapp.contactslist.domain.models.AnnotationStatus.CALL;
import static com.wowapp.contactslist.domain.models.AnnotationStatus.OFFLINE;
import static com.wowapp.contactslist.domain.models.AnnotationStatus.ONLINE;

class ContactViewHolder extends ChildViewHolder {

    private static final String NAME_FORMAT = "%s %s";
    private TextView name;
    private ImageView status;
    private TextView statusMessage;

    ContactViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.contact_name);
        status = itemView.findViewById(R.id.contact_status_icon);
        statusMessage = itemView.findViewById(R.id.contact_status_message);
    }

    void bind(@NonNull Contact contact) {
        name.setText(String.format(NAME_FORMAT, contact.getFirstName(), contact.getLastName()));
        int statusIconId = R.drawable.contacts_list_status_away;
        switch (contact.getStatusIcon()) {
            case ONLINE:
                statusIconId = R.drawable.contacts_list_status_online;
                break;
            case OFFLINE:
                statusIconId = R.drawable.contacts_list_status_offline;
                break;
            case BUSY:
                statusIconId = R.drawable.contacts_list_status_busy;
                break;
            case AWAY:
                statusIconId = R.drawable.contacts_list_status_away;
                break;
            case CALL:
                statusIconId = R.drawable.contacts_list_call_forward;
                break;
        }
        status.setImageResource(statusIconId);
        String message = contact.getStatusMessage();
        if (!TextUtils.isEmpty(message)) {
            statusMessage.setText(message);
        } else {
            statusMessage.setText(contact.getStatusIcon());
        }
    }
}
