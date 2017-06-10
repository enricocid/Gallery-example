package com.enrico.gallery.galleryapp.settings;

import android.app.Activity;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.enrico.gallery.galleryapp.R;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

class HiddenFoldersDialog {

    static void show(final Activity activity, final ArrayList<String> list) {

        final ViewGroup nullParent = null;

        AlertDialog.Builder hiddenFoldersDialog = new AlertDialog.Builder(activity);

        View view = activity.getLayoutInflater().inflate(R.layout.hidden_albums_dialog, nullParent);
        hiddenFoldersDialog.setView(view);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setHasFixedSize(true);

        HiddenAlbumsAdapter hiddenAlbumsAdapter = new HiddenAlbumsAdapter(activity, list);

        recyclerView.setAdapter(hiddenAlbumsAdapter);

        hiddenFoldersDialog
                .setTitle(R.string.hidden_title)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialogBox, int id) {

                        if (HiddenAlbumsAdapter.SimpleViewHolder.visibleFolders != null && HiddenAlbumsAdapter.SimpleViewHolder.visibleFolders.size() > 0) {
                            for (String visibleFolders : HiddenAlbumsAdapter.SimpleViewHolder.visibleFolders) {

                                SQLiteDatabase hiddenFoldersDB = activity.openOrCreateDatabase("HIDDEN", MODE_PRIVATE, null);

                                hiddenFoldersDB.delete("foldersList", "folder" + "=?", new String[]{String.valueOf(visibleFolders)});

                                SettingsActivity.restartApp(activity);
                            }
                        } else {
                            dialogBox.dismiss();
                        }
                    }
                })

                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.dismiss();
                            }
                        });

        AlertDialog alertDialogAndroid = hiddenFoldersDialog.create();
        alertDialogAndroid.show();

    }
}
