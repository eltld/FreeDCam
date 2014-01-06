package com.troop.freecam.surfaces;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceView;

import com.htc.view.DisplaySetting;
import com.lge.real3d.Real3D;
import com.lge.real3d.Real3DInfo;
import com.troop.freecam.camera.CameraManager;
import com.troop.freecam.interfaces.PreviewSizeChangedInterface;
import com.troop.freecam.manager.SettingsManager;

import java.util.List;

/**
 * Created by troop on 01.12.13.
 */
public class BasePreview extends SurfaceView implements PreviewSizeChangedInterface
{

    protected boolean hasReal3d = false;
    protected boolean hasOpenSense = false;
    protected Context context;

    protected Real3D mReal3D;
    protected CameraManager camMan;
    public SharedPreferences preferences;
    boolean is3d = false;

    public BasePreview(Context context)
    {
        super(context);
        this.context = context;
    }

    public BasePreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public BasePreview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    protected void isReald3d()
    {
        try {

            Class c = Class.forName("com.lge.real3d.Real3D");

            final String LGE_3D_DISPLAY = "lge.hardware.real3d.barrier.landscape";

            if(context.getPackageManager().hasSystemFeature(LGE_3D_DISPLAY))

                hasReal3d = true;

        } catch (ClassNotFoundException e) {

            hasReal3d = false;

        }

    }

    protected void isopensense()
    {
        try {
            Class c = Class.forName("com.htc.view.DisplaySetting");

            hasOpenSense = true;

        } catch (ClassNotFoundException e) {

            hasOpenSense = false;

        }

    }

    public  void SwitchViewMode()
    {
        if (hasReal3d)
        {
            //dont get the preferences from the SettingManager, its not init at this time
            if (preferences.getString(SettingsManager.Preferences.SwitchCamera, SettingsManager.Preferences.MODE_Front).equals(SettingsManager.Preferences.MODE_3D))
            {
                if(preferences.getBoolean("upsidedown", false) == false)
                    mReal3D.setReal3DInfo(new Real3DInfo(true, Real3D.REAL3D_TYPE_SS, Real3D.REAL3D_ORDER_LR));
                else
                    mReal3D.setReal3DInfo(new Real3DInfo(true, Real3D.REAL3D_TYPE_SS, Real3D.REAL3D_ORDER_RL));

            }
            else
            {
                mReal3D.setReal3DInfo(new Real3DInfo(true, Real3D.REAL3D_TYPE_NONE, 0));
            }
        }
        else if (hasOpenSense)
        {
            if (preferences.getString(SettingsManager.Preferences.SwitchCamera, SettingsManager.Preferences.MODE_Front).equals(SettingsManager.Preferences.MODE_3D))
                DisplaySetting.setStereoscopic3DFormat(getHolder().getSurface(), DisplaySetting.STEREOSCOPIC_3D_FORMAT_SIDE_BY_SIDE);
            else
                DisplaySetting.setStereoscopic3DFormat(getHolder().getSurface(), DisplaySetting.STEREOSCOPIC_3D_FORMAT_OFF);
        }
    }

    public void setPreviewSize(Camera.Size size)
    {
        {
            double targetRatio = (double) size.width / size.height;
        }
    }

    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.05;
        double targetRatio = (double) w / h;
        if (sizes == null)
            return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Try to find an size match aspect ratio and size
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
                continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    @Override
    public void onPreviewsizeHasChanged(int w, int h) {

    }
}
