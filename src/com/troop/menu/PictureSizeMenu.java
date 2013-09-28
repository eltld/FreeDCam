package com.troop.menu;

import android.hardware.Camera;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.troop.freecam.CameraManager;
import com.troop.freecam.MainActivity;

import java.util.List;

/**
 * Created by troop on 07.09.13.
 */
public class PictureSizeMenu extends BaseMenu {
    public PictureSizeMenu(CameraManager camMan, MainActivity activity) {
        super(camMan, activity);
    }
    List<Camera.Size> sizes;
    @Override
    public void onClick(View v)
    {
        if(camMan.Running)
            sizes = camMan.parameters.getSupportedPictureSizes();
        PopupMenu popupMenu = new PopupMenu(activity, super.GetPlaceHolder());
        //popupMenu.getMenuInflater().inflate(R.menu.menu_popup_flash, popupMenu.getMenu().);
        for (int i = 0; i < sizes.size(); i++) {
            popupMenu.getMenu().add((CharSequence) (sizes.get(i).width + "x" + sizes.get(i).height));
        }
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String tmp = item.toString();
                String camvalue = preferences.getString(CameraManager.SwitchCamera, CameraManager.SwitchCamera_MODE_3D);
                if (camvalue.equals(CameraManager.SwitchCamera_MODE_3D))
                    preferences.edit().putString(CameraManager.Preferences_PictureSize3D, tmp).commit();
                if (camvalue.equals(CameraManager.SwitchCamera_MODE_2D))
                    preferences.edit().putString(CameraManager.Preferences_PictureSize2D, tmp).commit();
                if (camvalue.equals(CameraManager.SwitchCamera_MODE_Front))
                    preferences.edit().putString(CameraManager.Preferences_PictureSizeFront, tmp).commit();
                //preferences.edit().putString("whitebalance", tmp).commit();
                String[] widthHeight = tmp.split("x");
                int w = Integer.parseInt(widthHeight[0]);
                int h = Integer.parseInt(widthHeight[1]);

                camMan.parameters.setPictureSize(w, h);

                camMan.Restart(false);

                return true;
            }
        });
        popupMenu.show();
    }
}