package com.troop.menu;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.troop.freecam.CameraManager;
import com.troop.freecam.MainActivity;
import com.troop.freecam.R;

/**
 * Created by troop on 27.08.13.
 */
public class FocusMenu extends BaseMenu {


    String [] modes;
    public FocusMenu(CameraManager camMan, MainActivity activity) {
        super(camMan, activity);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onClick(View v)
    {


        View canvasView = super.GetPlaceHolder();
        PopupMenu popupMenu = new PopupMenu(activity, canvasView);

        if(camMan.Running)
            modes = camMan.parameters.get("focus-mode-values").split(",");

        //PopupMenu popupMenu = new PopupMenu(activity, super.GetPlaceHolder());
        for (int i = 0; i < modes.length; i++) {
            popupMenu.getMenu().add((CharSequence) modes[i]);
        }

        //popupMenu.getMenuInflater().inflate(R.menu.menu_popup_focus, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String tmp = item.toString();
                String camvalue = preferences.getString(CameraManager.SwitchCamera, CameraManager.SwitchCamera_MODE_3D);
                if (camvalue == CameraManager.SwitchCamera_MODE_3D)
                    preferences.edit().putString(CameraManager.Preferences_Focus3D, tmp).commit();
                if (camvalue == CameraManager.SwitchCamera_MODE_2D)
                    preferences.edit().putString(CameraManager.Preferences_Focus2D, tmp).commit();
                if (camvalue == CameraManager.SwitchCamera_MODE_Front)
                    preferences.edit().putString(CameraManager.Preferences_FocusFront, tmp).commit();
                //preferences.edit().putString("focus", tmp).commit();
                camMan.autoFocusManager.SetFocus(tmp);
                camMan.Restart(false);
                return true;
            }
        });

        popupMenu.show();
        activity.appViewGroup.removeView(canvasView);
    }
}